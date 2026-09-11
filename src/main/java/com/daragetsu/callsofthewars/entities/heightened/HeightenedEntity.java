package com.daragetsu.callsofthewars.entities.heightened;

import com.daragetsu.callsofthewars.entities.ModEntities;
import com.daragetsu.callsofthewars.entities.air_plane.AirPlaneEntity;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.scores.PlayerTeam;

public class HeightenedEntity extends SoldierEntity{
    public HeightenedEntity(EntityType<? extends Monster> entity, Level level) {
        super(entity, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 56.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 0.6D)
                .add(Attributes.MAX_HEALTH, 80.0D);
    }
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            SpawnGroupData spawnData, CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        AirPlaneEntity plane = new AirPlaneEntity(ModEntities.AIR_PLANE.get(), level.getLevel());
        plane.moveTo(this.getX(), this.getY()+40, this.getZ());
        level.addFreshEntity(plane);
        level.getLevel().getScoreboard().addPlayerToTeam(plane.getStringUUID(), (PlayerTeam)this.getTeam());
        ItemStack helmetStack = this.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplateStack = this.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggingStack = this.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack bootStack = this.getItemBySlot(EquipmentSlot.FEET);
        helmetStack.addTagElement("ExoSuitUpgrades", whyDoesThisThrow("{ Upgrades: [ { Item: { id: \"scguns:target_tracker_module\", Count: 1b, tag: { Damage: 0 } }, Slot: 1 }, { Item: { id: \"scguns:gas_mask_module\", Count: 1b, tag: { Damage: 0 } }, Slot: 2 } ], ExoSuitPowerStates: { hud: 1b } }"));
        chestplateStack.addTagElement("ExoSuitUpgrades", whyDoesThisThrow("{ Upgrades: [ { Item: { id: \"scguns:pauldron\", Count: 1b, tag: { Damage: 0 } }, Slot: 1 }, { Item: { id: \"scguns:advanced_exo_suit_core\", Count: 1b, tag: { Energy: 59675 } }, Slot: 2 }, { Item: { id: \"scguns:tension_spring\", Count: 1b, tag: { Damage: 0 } }, Slot: 3 } ] }"));
        leggingStack.addTagElement("ExoSuitUpgrades", whyDoesThisThrow("{ Upgrades: [ { Item: { id: \"scguns:armor_plate\", Count: 1b, tag: { Damage: 0 } }, Slot: 1 }, { Item: { id: \"scguns:tension_spring\", Count: 1b, tag: { Damage: 0 } }, Slot: 2 } ] }"));
        bootStack.addTagElement("ExoSuitUpgrades", whyDoesThisThrow("{ Upgrades: [ { Item: { id: \\\"scguns:armor_plate\\\", Count: 1b, tag: { Damage: 0 } }, Slot: 0 }, { Item: { id: \\\"scguns:rabbit_module\\\", Count: 1b, tag: { Damage: 0 } }, Slot: 1 }, { Item: { id: \\\"scguns:tension_spring\\\", Count: 1b, tag: { Damage: 0 } }, Slot: 2 } ], ExoSuitPowerStates: { mobility: 1b }"));
        return data;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new MagDumpGoal(this, 200, 200));
        this.goalSelector.addGoal(4, new CallAirStrikeGoal(this));
    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSource) {
        super.dropAllDeathLoot(damageSource);
    }

    private CompoundTag whyDoesThisThrow(String tag){
        try {
            return TagParser.parseTag(tag);
        } catch (Exception e) {
            return new CompoundTag();
        }
    }
}