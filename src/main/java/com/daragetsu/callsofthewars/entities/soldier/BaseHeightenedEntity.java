package com.daragetsu.callsofthewars.entities.soldier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class BaseHeightenedEntity extends BaseSoldierEntity{
    public BaseHeightenedEntity(EntityType<? extends Monster> entity, Level level) {
        super(entity, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 56.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 0.6D)
                .add(Attributes.MAX_HEALTH, 400.0D);
    }
}
