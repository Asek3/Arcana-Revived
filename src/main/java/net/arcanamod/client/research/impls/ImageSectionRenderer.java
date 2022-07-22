package net.arcanamod.client.research.impls;

import static net.arcanamod.client.gui.ResearchEntryScreen.HEIGHT_OFFSET;
import static net.arcanamod.client.gui.ResearchEntryScreen.PAGE_HEIGHT;
import static net.arcanamod.client.gui.ResearchEntryScreen.PAGE_WIDTH;
import static net.arcanamod.client.gui.ResearchEntryScreen.PAGE_X;
import static net.arcanamod.client.gui.ResearchEntryScreen.PAGE_Y;
import static net.arcanamod.client.gui.ResearchEntryScreen.RIGHT_X_OFFSET;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.systems.research.impls.ImageSection;
import net.minecraft.entity.player.PlayerEntity;

public class ImageSectionRenderer implements EntrySectionRenderer<ImageSection>{
	
	public void render(MatrixStack stack, ImageSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		mc().getTextureManager().bindTexture(section.getImage());
		ClientUiUtil.drawTexturedModalRect(stack, (right ? PAGE_X + RIGHT_X_OFFSET : PAGE_X) + (screenWidth - 256) / 2, PAGE_Y + (screenHeight - 181) / 2 + HEIGHT_OFFSET, 0, 0, PAGE_WIDTH, PAGE_HEIGHT);
	}
	
	public void renderAfter(MatrixStack stack, ImageSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		// no-op
		// maybe allow specifying tooltips in the future
	}
	
	public int span(ImageSection section, PlayerEntity player){
		return 1;
	}
}