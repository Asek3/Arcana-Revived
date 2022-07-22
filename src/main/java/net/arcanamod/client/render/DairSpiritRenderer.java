package net.arcanamod.client.render;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.client.model.DairEntityModel;
import net.arcanamod.entities.SpiritEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DairSpiritRenderer extends MobRenderer<SpiritEntity, DairEntityModel<SpiritEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Arcana.MODID,
            "textures/entity/dair_spirit.png");

    public DairSpiritRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DairEntityModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(SpiritEntity entity) {
        return TEXTURE;
    }
}

