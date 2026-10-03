package com.daragetsu.callsofthewars.entities.general;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import top.ribs.scguns.entity.throwable.ThrowableGrenadeEntity;

public class ICastFiiiireeebaaaalll extends Goal{

    private final GeneralEntity ge;
    private final int cooldown;
    private final int runFor;
    private final int runFrom;
    private final int runUntil;
    private long canStartAgain = 0;
    private long startedAt = 0;

    public ICastFiiiireeebaaaalll(GeneralEntity ge, int cooldown, int runFor, int runFrom, int runUntil){
        this.ge = ge;
        this.cooldown = cooldown;
        this.runFor = runFor;
        this.runFrom = runFrom;
        this.runUntil = runUntil;
    }

    @Override
    public void start() {
        if(this.ge.level().isClientSide())return;
        this.startedAt = this.ge.level().getGameTime();
        this.ge.triggerAnim("flame", "flame");
    }

    @Override
    public void tick() {
        if(this.ge.level().isClientSide())return;
        if((this.ge.level().getGameTime()-this.startedAt >= this.runFrom) && this.ge.level().getGameTime()-this.startedAt <= this.runUntil){
            if(this.ge.level().getGameTime()-this.startedAt % 5 == 0){
                LivingEntity target = this.ge.getTarget();
                ThrowableGrenadeEntity grenade = new ThrowableGrenadeEntity(this.ge.level(), this.ge, 20);
                grenade.shoot(target.getX(), target.getY()+3, target.getZ(), 0.6f, 0f);
                double d0 = target.getX() - this.ge.getX();
                double d1 = target.getY(0.88888) - ge.getY();
                double d2 = target.getZ() - this.ge.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                grenade.shoot(
                    d0, 
                    d1 + d3 * (double)0.2F, 
                    d2, 
                    0.3F, 
                    0.1F
                );
                this.ge.level().addFreshEntity(grenade);
            }
        }
    }

    @Override
    public boolean canUse() {
        return this.ge.level().getGameTime() > this.canStartAgain && this.ge.getTarget()!=null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.ge.level().getGameTime()-this.startedAt < this.runFor;
    }

    @Override
    public void stop() {
        if(this.ge.level().isClientSide())return;
        this.canStartAgain = this.ge.level().getGameTime()+this.cooldown;
    }
    
}
