package com.daragetsu.callsofthewars.entities.common.client;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.entities.common.VariantEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

public class VariantEntityRenderer <T extends Mob & GeoAnimatable & VariantEntity> extends GeoEntityRenderer<T> {
    public VariantEntityRenderer(Context renderManager, String path) {
        super(renderManager, new VariantEntityModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, path)));
        this.addRenderLayer(new GunGeoLayer<>(this) {
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
                    1,
                    1,
                    1,
                    1
                );
            }
        });
        this.addRenderLayer(new ItemArmorGeoLayer<>(this){
            @Override
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, T animatable) {
                if(bone.getName().equals("head"))return EquipmentSlot.HEAD;
                if(bone.getName().equals("chest"))return EquipmentSlot.CHEST;
                if(bone.getName().equals("left_leg"))return EquipmentSlot.LEGS;
                if(bone.getName().equals("right_leg"))return EquipmentSlot.LEGS;
                if(bone.getName().equals("left_foot"))return EquipmentSlot.FEET;
                if(bone.getName().equals("right_foot"))return EquipmentSlot.FEET;
                return super.getEquipmentSlotForBone(bone, stack, animatable);
            }
            @Override
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable,
                    HumanoidModel<?> baseModel) {
                if(bone.getName().equals("head"))return baseModel.head;
                if(bone.getName().equals("chest"))return baseModel.body;
                if(bone.getName().equals("left_arm"))return baseModel.leftArm;
                if(bone.getName().equals("right_arm"))return baseModel.rightArm;
                if(bone.getName().equals("left_leg"))return baseModel.leftLeg;
                if(bone.getName().equals("right_leg"))return baseModel.rightLeg;
                return super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
            }
            @Override
            protected ItemStack getArmorItemForBone(GeoBone bone, T animatable) {
                if(bone.getName().equals("head"))return this.helmetStack;
                if(bone.getName().equals("chest"))return this.chestplateStack;
                if(bone.getName().equals("left_leg"))return this.leggingsStack;
                if(bone.getName().equals("right_leg"))return this.leggingsStack;
                if(bone.getName().equals("left_foot"))return this.bootsStack;
                if(bone.getName().equals("right_foot"))return this.bootsStack;
                return super.getArmorItemForBone(bone, animatable);
            }
        });
    }
}