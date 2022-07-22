package net.arcanamod.client.render.particles;

import static net.arcanamod.client.gui.UiUtil.blend;
import static net.arcanamod.client.gui.UiUtil.blue;
import static net.arcanamod.client.gui.UiUtil.green;
import static net.arcanamod.client.gui.UiUtil.red;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.world.ClientAuraView;
import net.arcanamod.world.Node;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NodeParticle extends SpriteTexturedParticle{
	
	private Node node;
	protected static final int time = 40;
	
	protected NodeParticle(ClientWorld world, double x, double y, double z, TextureAtlasSprite sprite, @Nullable Node node){
		super(world, x, y, z);
		this.node = node;
		particleGravity = 0;
		maxAge = 0;
		particleScale = .7f;
		canCollide = false;
		setSprite(sprite);
	}
	
	public IParticleRenderType getRenderType(){
		return PASSTHROUGH_TERRAIN_SHEET;
	}
	
	public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks){
		if(node != null && time > 0 && node.getAspects().countHolders() > 0){
			// get current and next aspect
			try{
				Aspect current = node.getAspects().getHolder(((int)(world.getGameTime() + partialTicks) / time) % node.getAspects().countHolders()).getStack().getAspect();
				Aspect next = node.getAspects().getHolder(((int)(world.getGameTime() + partialTicks) / time + 1) % node.getAspects().countHolders()).getStack().getAspect();
				// get progress between them
				float progress = (((int)world.getGameTime() + partialTicks) / (float)time) % 1;
				// set colour to blended
				int blended = blend(0xffffff, blend(next.getColorRange().get(3), current.getColorRange().get(3), progress), .3f);
				particleRed = red(blended) / 255f;
				particleGreen = green(blended) / 255f;
				particleBlue = blue(blended) / 255f;
			}catch(ArithmeticException arithmeticException){
				Arcana.LOGGER.error(arithmeticException + " at: [" + posX + "@" + posY + "@" + posZ + "]");
			}
		}
		super.renderParticle(buffer, renderInfo, partialTicks);
	}
	
	protected int getBrightnessForRender(float partialTick){
		// fullbright
		return 0xf000f0;
	}
	
	public static final IParticleRenderType PASSTHROUGH_TERRAIN_SHEET = new IParticleRenderType() {
		public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.depthMask(false);
			RenderSystem.disableDepthTest();
			textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			bufferBuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		}
		
		public void finishRender(Tessellator tesselator) {
			tesselator.draw();
			RenderSystem.enableDepthTest();
			RenderSystem.depthMask(true);
		}
		
		public String toString() {
			return "arcana:PASSTHROUGH_TERRAIN_SHEET";
		}
	};
	
	@OnlyIn(Dist.CLIENT)
	@ParametersAreNonnullByDefault
	public static class Factory implements IParticleFactory<NodeParticleData>{
		
		@SuppressWarnings("deprecation")
		public Particle makeParticle(NodeParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			return new NodeParticle(world, x, y, z, Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(data.nodeTexture), new ClientAuraView((ClientWorld)world).getNodeByUuid(data.node).orElse(null));
		}
	}
}