package com.daragetsu.callsofthewars.client.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import net.minecraft.world.item.ItemDisplayContext;

public class BeltRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource bufferSource, int light, float limbSwing,float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,float headPitch) {
        matrixStack.pushPose();

        matrixStack.translate(0D, 0.9D, 0D);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel bakedModel = itemRenderer.getModel(stack, null, null, 0);

        itemRenderer.render(stack, ItemDisplayContext.GROUND, false, matrixStack, bufferSource, light, OverlayTexture.NO_OVERLAY, bakedModel);

        matrixStack.popPose();
    }
}
