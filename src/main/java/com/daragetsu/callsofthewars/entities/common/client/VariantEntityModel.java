package com.daragetsu.callsofthewars.entities.common.client;

import com.daragetsu.callsofthewars.entities.common.VariantEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class VariantEntityModel <T extends Mob & GeoAnimatable & VariantEntity> extends DefaultedEntityGeoModel<T> {

    public VariantEntityModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return super.getTextureResource(animatable)
                .withPath(s -> s.substring(0, s.length() - 4) +  "_" + animatable.getVariant() + ".png");
    }   
}