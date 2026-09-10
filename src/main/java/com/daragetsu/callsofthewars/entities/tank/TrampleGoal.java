package com.daragetsu.callsofthewars.entities.tank;

import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

public class TrampleGoal extends Goal{
    public final TankEntity tank;
    public TrampleGoal(TankEntity entity){
        this.tank = entity;
    }
    @Override
    public boolean canUse() {
        return true;
    }
    @Override
    public boolean canContinueToUse() {
        return true;
    }
    @Override
    public void tick() {
        super.tick();
        if(!this.tank.level().isClientSide()){
            AABB aabb = this.tank.getBoundingBox().inflate(1.5);
            List<LivingEntity> list = this.tank.level().getEntitiesOfClass(LivingEntity.class, aabb);
            for(LivingEntity le : list){
                if(le.is(this.tank))continue;
                if(this.tank.isVehicle() && le.is(this.tank.getPassengers().get(0)))continue;
                le.hurt(this.tank.damageSources().generic(), 2);
            }
        }
    }
}
