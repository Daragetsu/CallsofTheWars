package com.daragetsu.callsofthewars.entities.tank;

import com.daragetsu.callsofthewars.CallsofTheWars;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ProjectileRenderer extends GeoEntityRenderer<ProjectileEntity>{
    public ProjectileRenderer(Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "tank_projectile")));
    }
}