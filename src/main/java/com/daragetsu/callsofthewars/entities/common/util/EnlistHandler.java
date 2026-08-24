package com.daragetsu.callsofthewars.entities.common.util;

import java.util.List;
import java.util.Optional;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.entities.ModEntities;
import com.daragetsu.callsofthewars.entities.container.ContainerEntity;
import com.daragetsu.callsofthewars.item.ModItems;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class EnlistHandler {
    public static void enlist(ServerPlayer player){
        ServerScoreboard scoreboard = player.level().getServer().getScoreboard();
        Objective x = scoreboard.getObjective("x");
        if (x == null) {
            x = scoreboard.addObjective(
                "x",
                ObjectiveCriteria.DUMMY,
                Component.literal("x"),
                ObjectiveCriteria.RenderType.INTEGER
            );
        }
        Objective y = scoreboard.getObjective("y");
        if (y == null) {
            y = scoreboard.addObjective(
                "y",
                ObjectiveCriteria.DUMMY,
                Component.literal("y"),
                ObjectiveCriteria.RenderType.INTEGER
            );
        }
        Objective z = scoreboard.getObjective("z");
        if (z == null) {
            z = scoreboard.addObjective(
                "z",
                ObjectiveCriteria.DUMMY,
                Component.literal("z"),
                ObjectiveCriteria.RenderType.INTEGER
            );
        }
        scoreboard.getOrCreatePlayerScore(player.getName().getString(), x).setScore((int)player.getX());
        scoreboard.getOrCreatePlayerScore(player.getName().getString(), y).setScore((int)player.getY());
        scoreboard.getOrCreatePlayerScore(player.getName().getString(), z).setScore((int)player.getZ());
        
        ContainerEntity conen = new ContainerEntity(ModEntities.CONTAINER_ENTITY.get(), player.level());
        conen.moveTo(player.getX(), player.getY(), player.getZ());
        conen.setContainerOwner(player.getName().getString());
        conen.serializeInventory(player);
        player.level().addFreshEntity(conen);
        
        player.getInventory().clearContent();
        LootTable STARTER_INVENTORY = player.server.getLootData().getLootTable(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "starter_inventory"));
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) player.level());
        builder.withParameter(LootContextParams.ORIGIN, player.position());
        builder.withLuck(player.getLuck());
        LootParams params = builder.create(LootContextParamSets.EMPTY);
        ObjectArrayList<ItemStack> items = STARTER_INVENTORY.getRandomItems(params);

        for(ItemStack item : items){
            player.getInventory().add(item);
        }

        player.getInventory().add(36, new ItemStack(top.ribs.scguns.init.ModItems.ANTHRALITE_BOOTS.get()));
        player.getInventory().add(37, new ItemStack(top.ribs.scguns.init.ModItems.ANTHRALITE_LEGGINGS.get()));
        player.getInventory().add(38, new ItemStack(top.ribs.scguns.init.ModItems.ANTHRALITE_CHESTPLATE.get()));
        player.getInventory().add(39, new ItemStack(top.ribs.scguns.init.ModItems.ANTHRALITE_HELMET.get()));
        
        player.teleportTo(player.serverLevel().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "warring_states"))), player.getRandom().nextInt(-10000, 10000), 100, player.getRandom().nextInt(-10000, 10000), 0, 0);

        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));
        
        int i = player.getRandom().nextInt(3);

        teleportToBase(player, i);

        switch (i) {
            case 0:
                player.getInventory().add(new ItemStack(ModItems.RED_BELT.get()));
                break;
            case 1:
                player.getInventory().add(new ItemStack(ModItems.GREEN_BELT.get()));
                break;
            case 2:
                player.getInventory().add(new ItemStack(ModItems.BLUE_BELT.get()));
                break;
            default:
                player.getInventory().add(new ItemStack(ModItems.RED_BELT.get()));
                break;
        }
        player.sendSystemMessage(Component.literal("General: MY SOLDIERS RAGE!!"));
    }
    public static void demobilize(ServerPlayer player){
        ServerScoreboard scoreboard = player.level().getServer().getScoreboard();
        Objective xObj = scoreboard.getObjective("x");
        Objective yObj = scoreboard.getObjective("y");
        Objective zObj = scoreboard.getObjective("z");
        int x = scoreboard.getOrCreatePlayerScore(player.getName().getString(), xObj).getScore();
        int y = scoreboard.getOrCreatePlayerScore(player.getName().getString(), yObj).getScore();
        int z = scoreboard.getOrCreatePlayerScore(player.getName().getString(), zObj).getScore();
        player.moveTo(x, y, z);
        List<ContainerEntity> list = player.level().getEntitiesOfClass(ContainerEntity.class, new AABB(
            player.getX()-10,
            player.getY()-10,
            player.getZ()-10,
            player.getX()+10,
            player.getY()+10,
            player.getZ()+10
        ));
        if(!list.isEmpty()){
            for(ContainerEntity conen : list){
                if(conen.getContainerOwner().equals(player.getName().getString())){
                    player.getInventory().clearContent();
                    conen.deserializeInventory(player);
                    conen.remove(RemovalReason.DISCARDED);
                    break;
                }
            }
        }
    }
    private static void teleportToBase(ServerPlayer player, int choice){
        ChunkGenerator chunkGenerator = player.serverLevel().getChunkSource().getGenerator();

        ResourceKey<Structure> structureKey;

        switch (choice) {
            case 0:
                structureKey = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "starter_base_red"));
                break;
            case 1:
                structureKey = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "starter_base_green"));
                break;
            case 2:
                structureKey = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "starter_base_blue"));
                break;
            default:
                structureKey = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "starter_base_red"));
                break;
        }
        
        Registry<Structure> registry = player.serverLevel().registryAccess().registryOrThrow(Registries.STRUCTURE);
        Optional<Reference<Structure>> holderOpt = registry.getHolder(structureKey);
        
        if(holderOpt.isPresent()){
            HolderSet<Structure> holderSet = HolderSet.direct(holderOpt.get());
            Pair<BlockPos, Holder<Structure>> pair = chunkGenerator.findNearestMapStructure(
                player.serverLevel(), 
                holderSet, 
                player.getOnPos(), 
                512, 
                false
            );
            player.teleportTo(pair.getFirst().getX(), 110, pair.getFirst().getZ());
        }
    }
}