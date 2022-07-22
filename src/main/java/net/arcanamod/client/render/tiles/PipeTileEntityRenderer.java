package net.arcanamod.client.render.tiles;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.matrix.MatrixStack;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.pipes.AspectSpeck;
import net.arcanamod.blocks.pipes.TubeTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PipeTileEntityRenderer extends TileEntityRenderer<TubeTileEntity>{
	
	private static final Random shakeRng = new Random();
	
	public PipeTileEntityRenderer(TileEntityRendererDispatcher dispatcher){
		super(dispatcher);
	}
	
	public void render(TubeTileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay){
		for(AspectSpeck speck : te.getSpecks()){
			stack.push();
			// so
			// x ->
			//   <- x
			// both have pos 0
			// just opposite directions
			// start at centre
			stack.translate(.5, .5, .5);
			// move by -dir*0.5
			stack.translate(-speck.direction.getXOffset() * 0.5, -speck.direction.getYOffset() * 0.5, -speck.direction.getZOffset() * 0.5);
			// move the speck by its progress
			float pos = speck.pos + partialTicks * speck.speed / 20f;
			stack.translate(pos * speck.direction.getXOffset(), pos * speck.direction.getYOffset(), pos * speck.direction.getZOffset());
			// debugging
			stack.translate(0, .25, 0);
			// shake specks that are stuck
			if(speck.stuck)
				stack.translate(shakeRng.nextFloat() / 16f, shakeRng.nextFloat() / 16f, shakeRng.nextFloat() / 16f);
			float v = /*speck.payload.getAmount() / 3f*/1;
			stack.scale(0.5f * v, 0.5f * v, 0.5f * v);
			// render
			Minecraft.getInstance().getItemRenderer().renderItem(AspectUtils.getItemStackForAspect(speck.payload.getAspect()), ItemCameraTransforms.TransformType.GROUND, light, overlay, stack, buffer);
			stack.pop();
		}
	}
}