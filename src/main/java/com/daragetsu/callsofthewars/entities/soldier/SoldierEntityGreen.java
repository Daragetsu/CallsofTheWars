package com.daragetsu.callsofthewars.entities.soldier;

import com.daragetsu.callsofthewars.entities.common.BaseSoldierEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class SoldierEntityGreen extends BaseSoldierEntity{

    public SoldierEntityGreen(EntityType<? extends Monster> entity, Level level) {
        super(entity, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            SpawnGroupData spawnData, CompoundTag dataTag) {
        if (!level.isClientSide()) {
            this.setBelongsTo(BelongsTo.GREEN);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
}
