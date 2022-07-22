package net.arcanamod.client.model;

import static net.arcanamod.Arcana.arcLoc;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

import net.arcanamod.items.ScepterItem;
import net.arcanamod.items.StaffItem;
import net.arcanamod.items.WandItem;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelTransformComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.geometry.IModelGeometry;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class WandModelGeometry implements IModelGeometry<WandModelGeometry> {
	// hold onto data here
	ResourceLocation cap;
	ResourceLocation material;
	ResourceLocation variant;
	ResourceLocation focus;

	Logger LOGGER = LogManager.getLogger();

	public WandModelGeometry(ResourceLocation cap, ResourceLocation material, ResourceLocation variant, ResourceLocation focus) {
		this.cap = cap;
		this.material = material;
		this.variant = variant;
		this.focus = focus;
	}

	public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
		IModelTransform transformsFromModel = owner.getCombinedTransform();
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap = PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(transformsFromModel, modelTransform));

		// get core texture
		RenderMaterial coreTex = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, material);
		// get cap texture
		RenderMaterial capTex = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, cap);

		// get variant model
		if (variant == null) {
			variant = arcLoc("wand");
		}
		ResourceLocation coreLoc = new ResourceLocation(variant.getNamespace(), "item/wands/variants/" + variant.getPath());
		IUnbakedModel coreModel = bakery.getUnbakedModel(coreLoc);
		ItemCameraTransforms tfs = ItemCameraTransforms.DEFAULT;

		// they *should* be, but might as well check.
		if(coreModel instanceof BlockModel) {
			BlockModel model = (BlockModel)coreModel;
			model.textures.put("core", Either.left(coreTex));
			model.textures.put("cap", Either.left(capTex));
			tfs = model.getAllTransforms();
		} else {
			LOGGER.error("Wand model isn't a block model!");
		}

		// get focus model and texture, apply, and add
		Random rand = new Random();
		if(focus != null){
			IBakedModel focusModel = bakery.getBakedModel(new ResourceLocation(focus.getNamespace(), "item/wands/foci/" + focus.getPath()), modelTransform, spriteGetter);
			if(focusModel != null)
				builder.addAll(focusModel.getQuads(null, null, rand));
		}

		builder.addAll(coreModel.bakeModel(bakery, spriteGetter, transformsFromModel, coreLoc).getQuads(null, null, rand));

		return new WandBakedModel(builder.build(), spriteGetter.apply(coreTex), Maps.immutableEnumMap(transformMap), new AttachmentOverrideHandler(bakery), true, true, this, owner, modelTransform, tfs);
	}

	public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return Collections.emptyList();
	}

	protected static final class AttachmentOverrideHandler extends ItemOverrideList {
		private final ModelBakery bakery;

		public AttachmentOverrideHandler(ModelBakery bakery){
			this.bakery = bakery;
		}

		public IBakedModel getOverrideModel(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
			// get cap
			Cap cap = WandItem.getCap(stack);
			// get material
			Core core = WandItem.getCore(stack);
			// get variant (staff/scepter/wand)
			// TODO: improve this slightly
			ResourceLocation variant = arcLoc("wand");
			if (stack.getItem() instanceof WandItem) {
				variant = arcLoc("wand"); // yes this is redundant, just here for completeness
			} else if(stack.getItem() instanceof ScepterItem) {
				variant = arcLoc("scepter");
			} else if(stack.getItem() instanceof StaffItem) {
				variant = arcLoc("staff");
			}

			// get focus
			// nbt context comes from the focusData tag
			Focus focus = WandItem.getFocus(stack);
			CompoundNBT focusData = WandItem.getFocusData(stack);
			return new WandModelGeometry(cap.getTextureLocation(), core.getTextureLocation(), variant, focus.getModelLocation(focusData)).bake(((WandBakedModel)originalModel).owner, bakery, ModelLoader.defaultTextureGetter(), ((WandBakedModel)originalModel).modelTransform, originalModel.getOverrides(), new ResourceLocation("arcana:does_this_do_anything_lol"));
		}
	}
}