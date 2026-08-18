package com.daragetsu.callsofthewars.entities.common.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import top.ribs.scguns.entity.ai.GunAttackGoal;

@Mixin(value = GunAttackGoal.class, remap = false)
public interface GunAttackGoalAccessor {

    @Accessor("aimingStabilityTimer")
    int getAimingStabilityTimer();

    @Accessor("isReloading")
    boolean isReloading();

    @Accessor("remainingBursts")
    int getRemainingBursts();

    @Accessor("attackTime")
    int getAttackTime();

}
