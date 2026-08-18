package com.daragetsu.callsofthewars.entities.unit_spawner;

import com.daragetsu.callsofthewars.entities.ModEntities;
import com.daragetsu.callsofthewars.entities.soldier.BaseSoldierEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;

public class UnitSpawnerEntity extends BaseSoldierEntity{

    public UnitSpawnerEntity(EntityType<UnitSpawnerEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    public void tick() {
        super.tick();
        int i = this.random.nextInt(3);
        int l = this.random.nextInt(1, 11);
        if(this.level().isClientSide())return;
        for(int n = 0; n < l; n++){
            switch (i) {
                case 0:
                    ModEntities.SOLDIER_RED.get().spawn((ServerLevel)this.level(), this.getOnPos().above().above(), MobSpawnType.NATURAL);
                    break;
                case 1:
                    ModEntities.SOLDIER_GREEN.get().spawn((ServerLevel)this.level(), this.getOnPos().above().above(), MobSpawnType.NATURAL);
                    break;
                case 2:
                    ModEntities.SOLDIER_BLUE.get().spawn((ServerLevel)this.level(), this.getOnPos().above().above(), MobSpawnType.NATURAL);
                    break;
                default:
                    ModEntities.SOLDIER_RED.get().spawn((ServerLevel)this.level(), this.getOnPos().above().above(), MobSpawnType.NATURAL);
                    break;
            }
        }
        this.remove(RemovalReason.DISCARDED);
    }
}
