package com.daragetsu.callsofthewars.entities.soldier;

import com.daragetsu.callsofthewars.entities.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.scores.PlayerTeam;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;

public class ParatrooperEntity extends SoldierEntity{

    public ParatrooperEntity(EntityType<? extends Monster> entity, Level level) {
        super(entity, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            SpawnGroupData spawnData, CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);

        this.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, MobEffectInstance.INFINITE_DURATION));

        return data;
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
    }

    @Override
    public void tick() {
        super.tick();
        if(this.onGround() && !this.level().isClientSide()){
            this.level().getServer().getScoreboard()
                .addPlayerToTeam(
                    ModEntities.SOLDIER.get()
                    .spawn(
                        (ServerLevel)this.level(), 
                        this.blockPosition(), 
                        MobSpawnType.MOB_SUMMONED)
                        .getStringUUID(), 
                        (PlayerTeam)this.getTeam()
                );
            this.discard();
        }
    }
    
}
