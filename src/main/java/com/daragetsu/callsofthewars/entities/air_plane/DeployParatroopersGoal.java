package com.daragetsu.callsofthewars.entities.air_plane;

import com.daragetsu.callsofthewars.entities.ModEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.scores.PlayerTeam;

public class DeployParatroopersGoal extends Goal{

    public final AirPlaneEntity plane;
    private int cooldown = 0;

    public DeployParatroopersGoal(AirPlaneEntity entity){
        this.plane = entity;
    }

    @Override
    public boolean canUse() {
        if(this.cooldown>0)this.cooldown--;
        return this.cooldown <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
    
    @Override
    public void start() {
        super.start();
        this.cooldown = 200;
        if(!this.plane.level().isClientSide()){
            for(int i = 0; i < 5; i++){
                this.plane.level().getServer().getScoreboard()
                .addPlayerToTeam(
                    ModEntities.PARATROOPER.get()
                    .spawn(
                        (ServerLevel)this.plane.level(), 
                        this.plane.blockPosition().below(), 
                        MobSpawnType.MOB_SUMMONED)
                        .getStringUUID(), 
                        (PlayerTeam)this.plane.getTeam()
                );
            }
        }
    }
}
