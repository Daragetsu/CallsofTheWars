package com.daragetsu.callsofthewars.entities.common.util;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.entities.common.BaseSoldierEntity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import com.daragetsu.callsofthewars.entities.heightened.BaseHeightenedEntity;

public class RewardHandler {
    public static void giveRewards(ServerPlayer player){
        ShulkerBoxBlockEntity be = new ShulkerBoxBlockEntity(player.blockPosition(), Blocks.SHULKER_BOX.defaultBlockState());

        LootTable REWARDS = player.server.getLootData().getLootTable(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "rewards"));
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) player.level());
        builder.withParameter(LootContextParams.ORIGIN, player.position());
        builder.withLuck(player.getLuck());
        LootParams params = builder.create(LootContextParamSets.EMPTY);
        int score = RewardHandler.getScore(player);
        if(score / 100 >= 1){
            for(int i = 0; i < score/100; i++){
                ObjectArrayList<ItemStack> items = REWARDS.getRandomItems(params);
                for(int j = 0; j < items.size(); j++){
                    be.setItem(j, items.get(j));
                }
                ItemStack is = new ItemStack(Items.SHULKER_BOX);
                BlockItem.setBlockEntityData(is, BlockEntityType.SHULKER_BOX, be.saveWithoutMetadata());
                player.getInventory().add(is);
            }
        }
        ServerScoreboard scoreboard = player.level().getServer().getScoreboard();
        Objective xObj = scoreboard.getObjective("x");
        Objective yObj = scoreboard.getObjective("y");
        Objective zObj = scoreboard.getObjective("z");
        Objective pointsObj = scoreboard.getObjective("points");
        scoreboard.resetPlayerScore(player.getName().getString(), xObj);
        scoreboard.resetPlayerScore(player.getName().getString(), yObj);
        scoreboard.resetPlayerScore(player.getName().getString(), zObj);
        scoreboard.resetPlayerScore(player.getName().getString(), pointsObj);
    }

    public static void startScore(ServerPlayer player){
        ServerScoreboard scoreboard = player.level().getServer().getScoreboard();
        Objective points = scoreboard.getObjective("points");
        if (points == null) {
            points = scoreboard.addObjective(
                "points",
                ObjectiveCriteria.DUMMY,
                Component.literal("points"),
                ObjectiveCriteria.RenderType.INTEGER
            );
        }
        scoreboard.getOrCreatePlayerScore(player.getName().getString(), points).setScore(0);
        
        scoreboard.setDisplayObjective(ServerScoreboard.DISPLAY_SLOT_SIDEBAR, points);
    }

    public static void checkKill(ServerPlayer player, BaseSoldierEntity killed){
        if(EnlistHandler.alliedToPlayer(player, killed)){
            RewardHandler.setScore(player, RewardHandler.getScore(player)-10);
        }else{
            RewardHandler.setScore(player, RewardHandler.getScore(player)+10);
        }
        if(killed instanceof BaseHeightenedEntity){
            if(EnlistHandler.alliedToPlayer(player, killed)){
                RewardHandler.setScore(player, RewardHandler.getScore(player)-10);
            }else{
                RewardHandler.setScore(player, RewardHandler.getScore(player)+10);
            }
        }
    }

    private static int getScore(ServerPlayer player){
        ServerScoreboard scoreboard = player.level().getServer().getScoreboard();
        Objective points = scoreboard.getObjective("points");
        if (points == null) {
            points = scoreboard.addObjective(
                "points",
                ObjectiveCriteria.DUMMY,
                Component.literal("points"),
                ObjectiveCriteria.RenderType.INTEGER
            );
        }
        return scoreboard.getOrCreatePlayerScore(player.getName().getString(), points).getScore();
    }

    private static void setScore(ServerPlayer player, int score){
        ServerScoreboard scoreboard = player.level().getServer().getScoreboard();
        Objective points = scoreboard.getObjective("points");
        if (points == null) {
            points = scoreboard.addObjective(
                "points",
                ObjectiveCriteria.DUMMY,
                Component.literal("points"),
                ObjectiveCriteria.RenderType.INTEGER
            );
        }
        scoreboard.getOrCreatePlayerScore(player.getName().getString(), points).setScore(score);
    }

}
