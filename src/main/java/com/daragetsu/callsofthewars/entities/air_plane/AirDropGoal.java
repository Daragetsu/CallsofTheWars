package com.daragetsu.callsofthewars.entities.air_plane;

import com.daragetsu.callsofthewars.CallsofTheWars;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import top.ribs.scguns.blockentity.AmmoBoxBlockEntity;
import top.ribs.scguns.init.ModBlockEntities;
import top.ribs.scguns.init.ModBlocks;

public class AirDropGoal extends Goal{

    private final AirPlaneEntity entity;

    public AirDropGoal(AirPlaneEntity entity){
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return entity.tickCount % 20 == 0 && entity.getRandom().nextFloat() < 0.1f;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        super.start();
        if(entity.level().isClientSide())return;
        AmmoBoxBlockEntity be = new AmmoBoxBlockEntity(entity.blockPosition(), ModBlocks.AMMO_BOX.get().defaultBlockState());

        LootTable REWARDS = entity.level().getServer().getLootData().getLootTable(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "airdrop"));
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) entity.level());
        builder.withParameter(LootContextParams.ORIGIN, entity.position());
        builder.withLuck(1);
        LootParams params = builder.create(LootContextParamSets.EMPTY);
        ObjectArrayList<ItemStack> items = REWARDS.getRandomItems(params);
        for(int j = 0; j < items.size(); j++){
            be.setItem(j, items.get(j));
        }
        ItemStack is = new ItemStack(ModBlocks.AMMO_BOX.get().asItem());
        BlockItem.setBlockEntityData(is, ModBlockEntities.AMMO_BOX.get(), be.saveWithoutMetadata());
        ItemEntity en = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), is);
        entity.level().addFreshEntity(en);
    }

    @Override
    public void stop() {
        super.stop();
    }

}
