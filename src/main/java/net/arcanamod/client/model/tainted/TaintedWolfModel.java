package net.arcanamod.client.model.tainted;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.arcanamod.entities.tainted.TaintedEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class TaintedWolfModel<T extends TaintedEntity> extends EntityModel {
	private final ModelRenderer head;
	private final ModelRenderer field_228298_b_;
	private final ModelRenderer body;
	private final ModelRenderer legBackRight;
	private final ModelRenderer legBackLeft;
	private final ModelRenderer legFrontRight;
	private final ModelRenderer legFrontLeft;
	private final ModelRenderer tail;
	private final ModelRenderer field_228299_l_;
	private final ModelRenderer mane;

	public TaintedWolfModel() {
		float f = 0.0F;
		float f1 = 13.5F;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
		this.field_228298_b_ = new ModelRenderer(this, 0, 0);
		this.field_228298_b_.addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, 0.0F);
		this.head.addChild(this.field_228298_b_);
		this.body = new ModelRenderer(this, 18, 14);
		this.body.addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F);
		this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
		this.mane = new ModelRenderer(this, 21, 0);
		this.mane.addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, 0.0F);
		this.mane.setRotationPoint(-1.0F, 14.0F, 2.0F);
		this.legBackRight = new ModelRenderer(this, 0, 18);
		this.legBackRight.addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F);
		this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
		this.legBackLeft = new ModelRenderer(this, 0, 18);
		this.legBackLeft.addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F);
		this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
		this.legFrontRight = new ModelRenderer(this, 0, 18);
		this.legFrontRight.addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F);
		this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
		this.legFrontLeft = new ModelRenderer(this, 0, 18);
		this.legFrontLeft.addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F);
		this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
		this.tail = new ModelRenderer(this, 9, 18);
		this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
		this.field_228299_l_ = new ModelRenderer(this, 9, 18);
		this.field_228299_l_.addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F);
		this.tail.addChild(this.field_228299_l_);
		this.field_228298_b_.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F);
		this.field_228298_b_.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F);
		this.field_228298_b_.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3.0F, 3.0F, 4.0F, 0.0F);
	}

	/**
	 * Sets this entity's model rotation angles
	 *
	 * @param entityIn
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param ageInTicks
	 * @param netHeadYaw
	 * @param headPitch
	 */
	@Override
	public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		this.tail.rotateAngleX = ageInTicks;
	}

	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of(this.head);
	}

	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of(this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.tail, this.mane);
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
			this.tail.rotateAngleY = 0.0F;

			this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
			this.body.rotateAngleX = ((float)Math.PI / 2F);
			this.mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
			this.mane.rotateAngleX = this.body.rotateAngleX;
			this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
			this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
			this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
			this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
			this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
			this.legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
			this.legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
			this.legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

		/*this.field_228298_b_.rotateAngleZ = entityIn.getInterestedAngle(partialTick) + entityIn.getShakeAngle(partialTick, 0.0F);
		this.mane.rotateAngleZ = entityIn.getShakeAngle(partialTick, -0.08F);
		this.body.rotateAngleZ = entityIn.getShakeAngle(partialTick, -0.16F);
		this.field_228299_l_.rotateAngleZ = entityIn.getShakeAngle(partialTick, -0.2F);*/
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.getHeadParts().forEach((p_228228_8_) -> {
			p_228228_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
		this.getBodyParts().forEach((p_228227_8_) -> {
			p_228227_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}
}