package com.daragetsu.callsofthewars.entities.tank;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TankEntityRenderer extends GeoEntityRenderer<TankEntity>{

    public TankEntityRenderer(Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "tank")));
    }

    @Override
    public void render(TankEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight) {
            poseStack.scale(2, 2, 2);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    
}
