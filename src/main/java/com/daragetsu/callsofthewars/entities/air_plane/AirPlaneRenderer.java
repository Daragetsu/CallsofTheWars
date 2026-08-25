package com.daragetsu.callsofthewars.entities.air_plane;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class AirPlaneRenderer extends GeoEntityRenderer<AirPlaneEntity>{
    public AirPlaneRenderer(Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<AirPlaneEntity>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "air_plane")));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
    @Override
    public void render(AirPlaneEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight) {
                poseStack.scale(10, 10, 10);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}