package net.arcanamod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.util.ResourceLocation;

@Mixin(FontRenderer.class)
public interface FontRendererAccessor{
	
	@Invoker
	Font callGetFont(ResourceLocation fontLocation);
}