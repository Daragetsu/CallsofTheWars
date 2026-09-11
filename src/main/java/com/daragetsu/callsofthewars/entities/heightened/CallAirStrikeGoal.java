package com.daragetsu.callsofthewars.entities.heightened;

import java.util.List;

import com.daragetsu.callsofthewars.entities.air_plane.AirPlaneEntity;

import net.minecraft.world.entity.ai.goal.Goal;

public class CallAirStrikeGoal extends Goal{

    public final HeightenedEntity mob;
    private int cooldown = 0;

    public CallAirStrikeGoal(HeightenedEntity entity){
        this.mob = entity;
    }

    @Override
    public boolean canUse() {
        if(this.cooldown>0)this.cooldown--;
        return this.cooldown <= 0 && this.mob.getTarget()!=null;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        super.start();
        this.cooldown = 100;
        List<AirPlaneEntity> airPlanes = this.mob.level().getEntitiesOfClass(AirPlaneEntity.class,this.mob.getBoundingBox().inflate(100));
        AirPlaneEntity found = null;
        for(AirPlaneEntity airPlane : airPlanes){
            if(airPlane.isAlliedTo(this.mob)){
                found = airPlane;
                break;
            }
        }
        if(found==null)return;
        found.setTarget(this.mob.getTarget());
    }
}
