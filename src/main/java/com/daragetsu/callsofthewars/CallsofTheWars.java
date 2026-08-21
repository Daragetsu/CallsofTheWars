package com.daragetsu.callsofthewars;

import com.daragetsu.callsofthewars.entities.ModEntities;
import com.daragetsu.callsofthewars.entities.common.BaseSoldierEntity.BelongsTo;
import com.daragetsu.callsofthewars.item.ModItems;
import com.mojang.logging.LogUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import top.ribs.scguns.event.ModCommonEventBus;

import java.util.Map;

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

        modEventBus.addListener(CallsofTheWars::addCreative);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS){
            event.accept(ModItems.SOLDIER_RED_SPAWN_EGG);
            event.accept(ModItems.SOLDIER_GREEN_SPAWN_EGG);
            event.accept(ModItems.SOLDIER_BLUE_SPAWN_EGG);
            event.accept(ModItems.HEIGHTENED_RED_SPAWN_EGG);
            event.accept(ModItems.HEIGHTENED_GREEN_SPAWN_EGG);
            event.accept(ModItems.HEIGHTENED_BLUE_SPAWN_EGG);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.RED_BELT);
            event.accept(ModItems.GREEN_BELT);
            event.accept(ModItems.BLUE_BELT);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @Mod.EventBusSubscriber(modid = CallsofTheWars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class ModEvents{
        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            final ServerPlayer player = (ServerPlayer) event.getEntity();
            if(event.getTo().location().compareTo(ResourceLocation.fromNamespaceAndPath("callsofthewars", "warring_states")) == 0){
                int i = player.getRandom().nextInt(3);
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
            }
        }
    }
}