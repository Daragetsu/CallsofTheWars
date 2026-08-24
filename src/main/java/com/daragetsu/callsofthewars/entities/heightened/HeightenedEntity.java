package com.daragetsu.callsofthewars.entities.heightened;

import com.daragetsu.callsofthewars.entities.soldier.SoldierEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class HeightenedEntity extends SoldierEntity{
    public HeightenedEntity(EntityType<? extends Monster> entity, Level level) {
        super(entity, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 56.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 0.6D)
                .add(Attributes.MAX_HEALTH, 80.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new MagDumpGoal(this, 200, 200));
    }
}