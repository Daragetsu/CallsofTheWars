package com.daragetsu.callsofthewars.entities.soldier;

import com.daragetsu.callsofthewars.entities.common.client.GunGeoLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

/***
 * @author Krei, Daragetsu
 * ***/
public class SoldierEntityRenderer <T extends SoldierEntity> extends GeoEntityRenderer<T> {

    protected boolean noDeathTilt = false;
    protected boolean noDeathRedTint = false;
    protected boolean hasCustomShadowRadius = false;

    public SoldierEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        this.shadowRadius = 0;

        addRenderLayer(new GunGeoLayer<>(this) {
            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.pushPose();
                poseStack.translate(0,0.2,0);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
                poseStack.popPose();
            }
        });
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this){
            @Override
            public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                RenderType emissiveRenderType = getRenderType(animatable);
		        getRenderer().reRender(
                    bakedModel,
                    poseStack,
                    bufferSource,
                    animatable,
                    emissiveRenderType,
					bufferSource.getBuffer(emissiveRenderType),
                    partialTick,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    animatable.getRed(),
                    animatable.getGreen(),
                    animatable.getBlue(),
                    1
                );
            }
        });
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (!this.hasCustomShadowRadius && this.shadowRadius == 0) {
            this.shadowRadius = entity.getBbWidth()/2;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        if (this.noDeathTilt) return 0;
        return 90f;
    }


    // Factory methods

    public SoldierEntityRenderer<T> noDeathTilt() {
        this.noDeathTilt = true;
        return this;
    }

    public SoldierEntityRenderer<T> noDeathRedTint() {
        this.noDeathRedTint = true;
        return this;
    }

    public SoldierEntityRenderer<T> customShadowRadius(float shadowRadius) {
        this.hasCustomShadowRadius = true;
        this.shadowRadius = shadowRadius;
        return this;
    }
}