package com.daragetsu.callsofthewars.common.util;

import com.daragetsu.callsofthewars.entities.common.VariantEntity;
import com.daragetsu.callsofthewars.entities.common.VariantEntity.Variants;

import net.minecraft.ChatFormatting;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

public class TeamHandler {
    public static <T extends LivingEntity & VariantEntity> void AddToTeam(ServerLevel level, T entity){
        ServerScoreboard scoreboard = level.getServer().getScoreboard();
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
        PlayerTeam[] teams = {
            redTeam,
            greenTeam,
            blueTeam
        };
        level.getServer().getScoreboard().addPlayerToTeam(entity.getStringUUID(), teams[entity.getRandom().nextInt(teams.length)]);
    }
    public static <T extends LivingEntity & VariantEntity> void AddToTeam(ServerLevel level, T entity, Team team){
        level.getServer().getScoreboard().addPlayerToTeam(entity.getStringUUID(), (PlayerTeam)team);
    }
    public static <T extends LivingEntity & VariantEntity> int getVariant(T entity){
        Variants v;
        if((v = VariantEntity.VariantMap.get(entity.getTeam().getColor()))!=null){
            return v.get();
        }
        return Variants.Red.get();
    }
}
