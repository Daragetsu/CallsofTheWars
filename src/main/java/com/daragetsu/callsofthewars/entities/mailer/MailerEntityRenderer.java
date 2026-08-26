package com.daragetsu.callsofthewars.entities.mailer;

import com.daragetsu.callsofthewars.CallsofTheWars;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class MailerEntityRenderer extends GeoEntityRenderer<MailerEntity> {
    public MailerEntityRenderer(Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "mailer")));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
    
}
