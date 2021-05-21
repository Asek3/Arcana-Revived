package net.arcanamod.client.research;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.mixin.FontRendererAccessor;
import net.arcanamod.mixin.ModContainerAccessor;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.arcanamod.systems.research.impls.StringSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.EmptyGlyph;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextFormatter{
	
	private TextFormatter(){}
	
	private static final float TEXT_WIDTH = ResearchEntryScreen.PAGE_WIDTH / ResearchEntryScreen.TEXT_SCALING;
	
	public interface Span{
		
		void render(MatrixStack stack, int x, int y);
		
		float getWidth();
		
		float getHeight();
	}
	
	// TODO: text size, shadow
	public static class TextSpan implements Span{
		
		private final CustomTextStyle renderStyle;
		String text;
		
		public TextSpan(String text, CustomTextStyle style){
			this.text = text;
			this.renderStyle = style;
		}
		
		public void render(MatrixStack stack, int x, int y){
			renderStringWithCustomFormatting(stack, text, renderStyle, x, y);
		}
		
		public float getWidth(){
			return width(text, renderStyle);
		}
		
		public float getHeight(){
			return 9 + (renderStyle.isWavy() ? 1 : 0);
		}
	}
	
	public static class AspectSpan implements Span{
		
		Aspect aspect;
		
		public AspectSpan(Aspect aspect){
			this.aspect = aspect;
		}
		
		public void render(MatrixStack stack, int x, int y){
			ClientUiUtil.renderAspect(stack, aspect, x, y);
		}
		
		public float getWidth(){
			return 16;
		}
		
		public float getHeight(){
			return 17;
		}
	}
	
	public interface Paragraph{
		
		void render(MatrixStack stack, int x, int y, float scale);
		
		float getHeight();
	}
	
	public static class SpanParagraph implements Paragraph{
		
		List<Span> spans;
		boolean centred;
		
		List<List<Span>> lines = new ArrayList<>();
		float height;
		
		public SpanParagraph(List<Span> spans, boolean centred){
			this.spans = spans;
			this.centred = centred;
			
			// put the spans into different lines and keep track of height
			lines.add(new ArrayList<>());
			int curLine = 0;
			float curLineWidth = 0;
			float curLineHeight = 0;
			for(int i = 0; i < spans.size(); i++){
				Span span = spans.get(i);
				if((curLineWidth + span.getWidth()) < TEXT_WIDTH){
					lines.get(curLine).add(span);
					curLineWidth += span.getWidth() + 5;
					curLineHeight = Math.max(curLineHeight, (span.getHeight() + 1));
				}else{
					curLine++;
					lines.add(new ArrayList<>());
					curLineWidth = 0;
					height += curLineHeight;
					curLineHeight = 0;
					if(span.getWidth() < TEXT_WIDTH)
						// make sure this span gets added to the next line instead
						i--;
					else
						lines.get(curLine).add(span);
				}
			}
			height += curLineHeight;
		}
		
		public SpanParagraph(List<Span> spans){
			this(spans, false);
		}
		
		public void render(MatrixStack stack, int x, int y, float scale){
			float curY = 0;
			for(List<Span> line : lines){
				float curX = 0;
				// recaulculate width/height
				// maybe cache these in the future?
				float lineWidth = (float)line.stream().mapToDouble(value -> value.getWidth() + 2).sum();
				float lineHeight = (float)line.stream().mapToDouble(Span::getHeight).max().orElse(1);
				if(centred)
					curX = (TEXT_WIDTH - lineWidth) / 2;
				for(Span span : line){
					span.render(stack, (int)(x + curX), (int)(y + curY + (lineHeight - span.getHeight()) / 2));
					curX += span.getWidth() + 5;
				}
				curY += lineHeight;
			}
		}
		
		public float getHeight(){
			return height;
		}
	}
	
	public static float width(String str, Style style){
		return width(str, style, Minecraft.getInstance().fontRenderer);
	}
	
	public static float width(String str, Style style, FontRenderer fr){
		float ret = 0;
		Font font = ((FontRendererAccessor)fr).callGetFont(style.getFontId());
		boolean formatting = false;
		for(char c : str.toCharArray())
			if(c == '\u00a7')
				formatting = true;
			else if(!formatting)
				ret += font.func_238557_a_(c).getAdvance(style.getBold());
			else
				formatting = false;
		return ret;
	}
	
	public static float width(String str, CustomTextStyle style){
		float ret = 0;
		Font font = ((FontRendererAccessor)Minecraft.getInstance().fontRenderer).callGetFont(Style.DEFAULT_FONT);
		boolean formatting = false;
		for(char c : str.toCharArray())
			if(c == '\u00a7')
				formatting = true;
			else if(!formatting)
				ret += font.func_238557_a_(c).getAdvance(style.isBold());
			else
				formatting = false;
		return ret;
	}
	
	public static List<Paragraph> compile(String in, @Nullable StringSection section){
		// split up by (\n\n)s
		String[] paragraphs = in.split("\n+");
		List<Paragraph> ret = new ArrayList<>(paragraphs.length);
		for(String paragraph : paragraphs){
			CustomTextStyle curStyle = CustomTextStyle.EMPTY;
			boolean styleNeedsCopy = true;
			List<Span> list = new ArrayList<>();
			// splits before { and after } and at spaces
			for(String s : paragraph.split("([ ]+)|(?=\\{)|(?<=})")){
				// if it begins with { and ends with }, its a formatting fragment
				if(s.startsWith("{") && s.endsWith("}")){
					s = s.substring(1, s.length() - 1);
					if(s.startsWith("aspect:"))
						list.add(new AspectSpan(Aspects.ASPECTS.get(new ResourceLocation(s.substring(7)))));
					else if(s.equals("r")){
						curStyle = CustomTextStyle.EMPTY;
						styleNeedsCopy = true;
					}else{
						if(styleNeedsCopy){
							curStyle = curStyle.copy();
							styleNeedsCopy = false;
						}
						// it takes up 100% more space on java 8 >_>
						//noinspection IfCanBeSwitch
						if(s.equals("b"))
							curStyle.setBold(!curStyle.isBold());
						else if(s.equals("i"))
							curStyle.setItalics(!curStyle.isItalics());
						else if(s.equals("s"))
							curStyle.setStrikethrough(!curStyle.isStrikethrough());
						else if(s.equals("u"))
							curStyle.setUnderline(!curStyle.isUnderline());
						else if(s.equals("o"))
							curStyle.setObfuscated(!curStyle.isObfuscated());
						else if(s.equals("w"))
							curStyle.setWavy(!curStyle.isWavy());
						else if(s.equals("sh"))
							curStyle.setShadow(!curStyle.isShadow());
					}
					
				}else if(!s.isEmpty()){
					list.add(new TextSpan(s, curStyle));
					styleNeedsCopy = true;
				}
			}
			ret.add(new SpanParagraph(list));
		}
		return ret;
	}
	
	public static String process(String in, @Nullable StringSection section){
		// Formatted sections appear as such:
		//    {$config:arcana:General.MaxAlembicAir}
		// An open brace, a dollar sign, a formatting type, colon separated parameters, and a closing brace.
		// There's currently only config-formatted sections, but hey, might wanna extend that later.
		if(section != null && ArcanaConfig.ENTRY_TITLES.get()){
			ResearchEntry entry = ResearchBooks.getEntry(section.getEntry());
			if(entry.sections().get(0) == section)
				in = "{~c}" + TextFormatting.ITALIC + I18n.format(entry.name()) + TextFormatting.RESET + "{~sep}" + in;
		}
		if(in.contains("{$")){
			Pattern findBraces = Pattern.compile("(\\{\\$.*?})");
			Matcher braces = findBraces.matcher(in);
			while(braces.find()){
				String inlineSection = braces.group().substring(2, braces.group().length() - 1);
				String[] parts = inlineSection.split(":");
				String replaceWith = I18n.format("researchEntry.invalidInline", inlineSection);
				String name = parts[0];
				if(name.equals("config") && parts.length == 3)
					replaceWith = inlineConfig(parts[1], parts[2]);
				else if(name.equals("numOfAspects"))
					replaceWith = String.valueOf(Aspects.getWithoutEmpty().size());
				in = in.replace(braces.group(), replaceWith);
			}
		}
		return in;
	}
	
	public static String inlineConfig(String modid, String configName){
		// iterate through mod containers
		AtomicReference<String> ret = new AtomicReference<>(I18n.format("researchEntry.invalidConfig", modid, configName));
		ModList.get().forEachModContainer((s, container) -> {
			if(s.equals(modid))
				((ModContainerAccessor)container).getConfigs().forEach((type, config) -> {
					if(config.getConfigData().contains(configName))
						// Have to cast to integer
						// get() returns a T, as in "whatever you ask for"
						// Java assumes the char[] version of valueOf and dies trying to cast it
						ret.set(String.valueOf((Object)config.getConfigData().get(configName)));
				});
		});
		
		return ret.get();
	}
	
	// vanilla copy: FontRenderer, line 288
	public static void renderStringWithCustomFormatting(MatrixStack stack, String text, CustomTextStyle style, float x, float y, Font font){
		IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		int colour = style.getColour();
		float red = (float)(colour >> 16 & 255) / 255.0F;
		float green = (float)(colour >> 8 & 255) / 255.0F;
		float blue = (float)(colour & 255) / 255.0F;
		List<TexturedGlyph.Effect> effects = new ArrayList<>();
		for(char c : text.toCharArray()){
			IGlyph glyph = font.func_238557_a_(c);
			TexturedGlyph texturedglyph = style.isObfuscated() && c != 32 ? font.obfuscate(glyph) : font.func_238559_b_(c);
			IVertexBuilder ivertexbuilder = buffer.getBuffer(texturedglyph.getRenderType(false));
			if(!(texturedglyph instanceof EmptyGlyph)){
				float boldOffset = style.isBold() ? glyph.getBoldOffset() : 0;
				float shadowOffset = style.isShadow() ? glyph.getShadowOffset() : 0;
				float wavyOffset = style.isWavy() ? MathHelper.sin(x * 2 + (Minecraft.getInstance().getRenderPartialTicks() + Minecraft.getInstance().world.getGameTime()) / 2f) * 1.1f : 0;
				if(style.isShadow()){
					texturedglyph.render(style.isItalics(), x + shadowOffset, y + shadowOffset + wavyOffset, stack.getLast().getMatrix(), ivertexbuilder, red * .25f, green * .25f, blue * .25f, .25f, 0xf000f0);
					if(style.isBold())
						texturedglyph.render(style.isItalics(), x + shadowOffset + boldOffset, y + shadowOffset + wavyOffset, stack.getLast().getMatrix(), ivertexbuilder, red * .25f, green * .25f, blue * .25f, .25f, 0xf000f0);
				}
				texturedglyph.render(style.isItalics(), x, y + wavyOffset, stack.getLast().getMatrix(), ivertexbuilder, red, green, blue, 1, 0xf000f0);
				if(style.isBold())
					texturedglyph.render(style.isItalics(), x + boldOffset, y + wavyOffset, stack.getLast().getMatrix(), ivertexbuilder, red, green, blue, 1, 0xf000f0);
			}
			buffer.finish();
			
			float advance = glyph.getAdvance(style.isBold());
			float shadowed = style.isShadow() ? 1 : 0;
			if (style.isStrikethrough())
				effects.add(new TexturedGlyph.Effect(x + shadowed - 1, y + shadowed + 4, x + shadowed + advance, y + shadowed + 4.5F - 1, 0.01F, red, green, blue, 1));
			
			if (style.isUnderline())
				effects.add(new TexturedGlyph.Effect(x + shadowed - 1, y + shadowed + 9, x + shadowed + advance, y + shadowed + 9.0F - 1, 0.01F, red, green, blue, 1));
			x += advance;
		}
		TexturedGlyph texturedglyph = font.getWhiteGlyph();
		IVertexBuilder ivertexbuilder = buffer.getBuffer(texturedglyph.getRenderType(false));
		for(TexturedGlyph.Effect effect : effects)
			texturedglyph.renderEffect(effect, stack.getLast().getMatrix(), ivertexbuilder, 0xf000f0);
		buffer.finish();
	}
	
	public static void renderStringWithCustomFormatting(MatrixStack stack, String text, CustomTextStyle style, float x, float y){
		Font font = ((FontRendererAccessor)Minecraft.getInstance().fontRenderer).callGetFont(Style.DEFAULT_FONT);
		renderStringWithCustomFormatting(stack, text, style, x, y, font);
	}
}