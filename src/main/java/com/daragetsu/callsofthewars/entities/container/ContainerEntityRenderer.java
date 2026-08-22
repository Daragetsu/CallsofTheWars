package com.daragetsu.callsofthewars.entities.container;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ContainerEntityRenderer extends GeoEntityRenderer<ContainerEntity>{

    public ContainerEntityRenderer(Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "container_entity")));
    }
    @Override
    public void render(ContainerEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight) {
    }
}
