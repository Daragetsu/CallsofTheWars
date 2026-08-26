package com.daragetsu.callsofthewars;

import com.daragetsu.callsofthewars.common.util.EnlistHandler;
import com.daragetsu.callsofthewars.common.util.RewardHandler;
import com.daragetsu.callsofthewars.entities.ModEntities;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntity;
import com.daragetsu.callsofthewars.entities.container.ContainerEntity;
import com.daragetsu.callsofthewars.item.ModItems;
import com.daragetsu.callsofthewars.worldgen.structure.ModStructureProcessors;
import com.daragetsu.callsofthewars.worldgen.structure.ModStructures;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import top.ribs.scguns.entity.monster.CogMinionEntity;
import top.ribs.scguns.event.ModCommonEventBus;

import java.util.Map;
import java.util.List;

import org.slf4j.Logger;

@SuppressWarnings("unused")
@Mod(CallsofTheWars.MOD_ID)
public class CallsofTheWars
{
    public static final String MOD_ID = "callsofthewars";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CallsofTheWars(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModStructures.register(modEventBus);
        ModStructureProcessors.register(modEventBus);

        modEventBus.addListener(CallsofTheWars::addCreative);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS){
            event.accept(ModItems.SOLDIER_SPAWN_EGG);
            event.accept(ModItems.HEIGHTENED_SPAWN_EGG);
            event.accept(ModItems.AIR_PLANE_SPAWN_EGG);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @Mod.EventBusSubscriber(modid = CallsofTheWars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class ModEvents{
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            event.getDispatcher().register(
                Commands.literal("enlist").executes(context -> {
                    final ServerPlayer player = (ServerPlayer) context.getSource().getPlayer();
                    EnlistHandler.enlist(player);
                    RewardHandler.startScore(player);
                    return 1;
                })
            );
        }
        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if(!event.isWasDeath())return;
            if (!(event.getEntity() instanceof ServerPlayer player)) {
                return;
            }
            if (!(event.getOriginal() instanceof ServerPlayer ogPlayer)) {
                return;
            }
            if(ogPlayer.level().dimension().location().compareTo(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "warring_states")) == 0){
                EnlistHandler.demobilize(player);
                player.sendSystemMessage(Component.literal("General: if we fall, we just try again, and again, and again, and at the end, WE WILL WIN!"));
                RewardHandler.giveRewards(player);
            }
        }
        @SubscribeEvent
        public static void onEntityDeath(LivingDeathEvent event){
            if(event.getEntity() instanceof SoldierEntity sol && event.getSource().getEntity() instanceof ServerPlayer player){
                RewardHandler.checkSoldierKill(player, sol);
            }
            if(event.getSource().getEntity() instanceof ServerPlayer player){
                if(player.level().dimension().location().compareTo(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "warring_states")) == 0){
                    RewardHandler.checkGeneralKill(player, event.getEntity());
                }
            }
            if(event.getEntity() instanceof ServerPlayer player){
                if(player.level().dimension().location().compareTo(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "warring_states")) == 0){
                    player.getInventory().clearContent();
                }
            }
        }
    }
}