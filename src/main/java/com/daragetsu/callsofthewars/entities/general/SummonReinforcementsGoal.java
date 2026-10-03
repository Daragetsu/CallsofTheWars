package com.daragetsu.callsofthewars.entities.general;

import com.daragetsu.callsofthewars.entities.ModEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.scores.PlayerTeam;

public class SummonReinforcementsGoal extends Goal{

    private final GeneralEntity ge;
    private final int cooldown;
    private final int infantry;
    private final int elite;
    private final int runFor;
    private final int summonAt;
    private long startAgainAt = 0;
    private long startedAt = 0;

    public SummonReinforcementsGoal(GeneralEntity ge, int cooldown, int soliders, int heighteneds, int runFor, int summonAt){
        this.ge = ge;
        this.cooldown = cooldown;
        this.infantry = soliders;
        this.elite = heighteneds;
        this.runFor = runFor;
        this.summonAt = summonAt;
    }

    @Override
    public boolean canUse() {
        return this.ge.level().getGameTime() > this.startAgainAt;
    }

    @Override
    public boolean canContinueToUse() {
        return this.ge.level().getGameTime()-this.startedAt < this.runFor;
    }

    @Override
    public void start() {
        if(this.ge.level().isClientSide())return;
        this.startedAt = this.ge.level().getGameTime();
        this.ge.triggerAnim("summon", "summon_reinforcements");
    }

    @Override
    public void tick() {
        if(this.ge.level().isClientSide())return;
        long ticks = this.ge.level().getGameTime()-this.startedAt;
        if(ticks == this.summonAt){
            for(int i = 0; i < this.infantry; i++)this.ge.level().getServer().getScoreboard().addPlayerToTeam(ModEntities.SOLDIER.get().spawn((ServerLevel)this.ge.level(), this.ge.getOnPos().above().above(), MobSpawnType.NATURAL).getStringUUID(), (PlayerTeam)this.ge.getTeam());
            for(int i = 0; i < this.elite; i++)this.ge.level().getServer().getScoreboard().addPlayerToTeam(ModEntities.HEIGHTENED.get().spawn((ServerLevel)this.ge.level(), this.ge.getOnPos().above().above(), MobSpawnType.NATURAL).getStringUUID(), (PlayerTeam)this.ge.getTeam());
        }
    }

    @Override
    public void stop() {
        this.startAgainAt = this.ge.level().getGameTime()+this.cooldown;
    }
    
}
