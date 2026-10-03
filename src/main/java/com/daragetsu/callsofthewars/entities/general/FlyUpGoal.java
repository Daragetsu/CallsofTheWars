package com.daragetsu.callsofthewars.entities.general;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class FlyUpGoal extends Goal{

    private final GeneralEntity ge;
    private int ticks;
    private final int cooldown = 60;
    private long endTime = 0;

    public FlyUpGoal(GeneralEntity ge){
        this.ge = ge;
    }

    @Override
    public void start() {
        if(this.ge.level().isClientSide())return;
        this.ticks = 0;
        this.ge.setNoGravity(true);
    }

    @Override
    public void tick() {
        if(this.ge.level().isClientSide())return;
        this.ticks++;
        if(!ge.level().getBlockState(ge.blockPosition().below().below()).isAir()){
            ge.addDeltaMovement(new Vec3(0,0.01,0));
        }
        Vec3 ex_pos = this.ge.position().add(GeneralEntity.EXHAUST.yRot(-this.ge.yBodyRot * Mth.DEG_TO_RAD));
        for(double i = 0; i < 2; i+=0.1){
            ((ServerLevel)this.ge.level()).sendParticles(
                ParticleTypes.FLAME, 
                ex_pos.x, 
                ex_pos.y-i, 
                ex_pos.z, 
                1, 
                0,
                0, 
                0,
                0
            );
        }
    }

    @Override
    public boolean canUse() {
        return this.ge.level().getGameTime() > this.endTime && this.ge.getRandom().nextFloat() < 0.3f;
    }

    @Override
    public boolean canContinueToUse() {
        return this.ticks<70 && !this.ge.isAuraFarming;
    }

    @Override
    public void stop() {
        if(this.ge.level().isClientSide())return;
        this.endTime = this.ge.level().getGameTime()+this.cooldown;
        this.ge.setNoGravity(false);
    }
    
}
