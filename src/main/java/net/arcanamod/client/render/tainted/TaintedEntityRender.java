package net.arcanamod.client.render.tainted;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TaintedEntityRender<T extends MobEntity, M extends EntityModel<T>> extends MobRenderer<T, EntityModel<T>> {

	public TaintedEntityRender(EntityRendererManager renderManagerIn, M model) {
		super(renderManagerIn, (EntityModel<T>) model, 0.5f);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return new ResourceLocation(Arcana.MODID,
				"textures/entity/"+entity.getType().getRegistryName().getPath()+".png");
	}
}