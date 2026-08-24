package com.daragetsu.callsofthewars.entities.unit_spawner;

import com.daragetsu.callsofthewars.entities.ModEntities;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;

public class UnitSpawnerEntity extends SoldierEntity{

    public UnitSpawnerEntity(EntityType<UnitSpawnerEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide())return;
        int i = this.random.nextInt(3);
        ServerScoreboard scoreboard = this.level().getServer().getScoreboard();
        PlayerTeam redTeam = scoreboard.getPlayerTeam("red");
        PlayerTeam greenTeam = scoreboard.getPlayerTeam("green");
        PlayerTeam blueTeam = scoreboard.getPlayerTeam("blue");
        if(redTeam==null){
            redTeam = scoreboard.addPlayerTeam("red");
            redTeam.setColor(ChatFormatting.RED);
        }
        if(greenTeam==null){
            greenTeam = scoreboard.addPlayerTeam("green");
            greenTeam.setColor(ChatFormatting.GREEN);
        }
        if(blueTeam==null){
            blueTeam = scoreboard.addPlayerTeam("blue");
            blueTeam.setColor(ChatFormatting.BLUE);
        }
        int l = this.random.nextInt(1, 11);
        for(int n = 0; n < l; n++){
            switch (i) {
                case 0:
                    this.level().getServer().getScoreboard().addPlayerToTeam(ModEntities.SOLDIER.get().spawn((ServerLevel)this.level(), this.getOnPos().above().above(), MobSpawnType.NATURAL).getStringUUID(), redTeam);
                    break;
                case 1:
                    this.level().getServer().getScoreboard().addPlayerToTeam(ModEntities.SOLDIER.get().spawn((ServerLevel)this.level(), this.getOnPos().above().above(), MobSpawnType.NATURAL).getStringUUID(), greenTeam);
                    break;
                case 2:
                    this.level().getServer().getScoreboard().addPlayerToTeam(ModEntities.SOLDIER.get().spawn((ServerLevel)this.level(), this.getOnPos().above().above(), MobSpawnType.NATURAL).getStringUUID(), blueTeam);
                    break;
                default:
                    this.level().getServer().getScoreboard().addPlayerToTeam(ModEntities.SOLDIER.get().spawn((ServerLevel)this.level(), this.getOnPos().above().above(), MobSpawnType.NATURAL).getStringUUID(), redTeam);
                    break;
            }
        }
        this.remove(RemovalReason.DISCARDED);
    }
}
