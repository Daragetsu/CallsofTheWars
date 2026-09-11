package com.daragetsu.callsofthewars.entities.air_plane;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import top.ribs.scguns.entity.throwable.ThrowableGrenadeEntity;
import top.ribs.scguns.init.ModEntities;

public class AirStrikeGoal extends Goal{
    public final AirPlaneEntity plane;
    private boolean striked = false;
    public AirStrikeGoal(AirPlaneEntity entity){
        this.plane = entity;
    }

    @Override
    public boolean canUse() {
        return this.plane.getTarget()!=null;
    }
    @Override
    public boolean canContinueToUse() {
        return this.plane.getTarget()!=null && !this.striked;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.plane.getTarget()==null)return;
        Vec3 pos = this.plane.getTarget().position();
        if(this.plane.getNavigation().getTargetPos().distToCenterSqr(pos.x, this.plane.getY(), pos.z)>20){
            this.plane.getNavigation().moveTo(pos.x, this.plane.getY(), pos.z, 1);
        }
        if(this.plane.distanceToSqr(new Vec3(pos.x, this.plane.getY(), pos.z))<=20){
            if(!this.plane.level().isClientSide()){
                ThrowableGrenadeEntity en = new ThrowableGrenadeEntity(ModEntities.THROWABLE_GRENADE.get(), this.plane.level());
                en.setPos(this.plane.getEyePosition().add(0, -2, 0));
                en.shoot(0, 0, 0, 1, 1);
                this.plane.level().addFreshEntity(en);
            }
            this.striked = true;
            this.plane.setTarget(null);
        }
    }
}
