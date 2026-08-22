package com.daragetsu.callsofthewars;

import com.daragetsu.callsofthewars.entities.ModEntities;
import com.daragetsu.callsofthewars.entities.common.BaseSoldierEntity.BelongsTo;
import com.daragetsu.callsofthewars.entities.container.ContainerEntity;
import com.daragetsu.callsofthewars.item.ModItems;
import com.mojang.logging.LogUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
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
        // WILL BE MOVED TO IT'S OWN PROPER ENLIST METHOD
        // @SubscribeEvent
        // public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        //     final ServerPlayer player = (ServerPlayer) event.getEntity();
        //     if(event.getTo().location().compareTo(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "warring_states")) == 0){
        //         int i = player.getRandom().nextInt(3);
        //         switch (i) {
        //             case 0:
        //                 player.getInventory().add(new ItemStack(ModItems.RED_BELT.get()));
        //                 break;
        //             case 1:
        //                 player.getInventory().add(new ItemStack(ModItems.GREEN_BELT.get()));
        //                 break;
        //             case 2:
        //                 player.getInventory().add(new ItemStack(ModItems.BLUE_BELT.get()));
        //                 break;
        //             default:
        //                 player.getInventory().add(new ItemStack(ModItems.RED_BELT.get()));
        //                 break;
        //         }
        //         ServerScoreboard scoreboard = player.level().getServer().getScoreboard();
        //         Objective x = scoreboard.getObjective("x");
        //         if (x == null) {
        //             x = scoreboard.addObjective(
        //                 "x",
        //                 ObjectiveCriteria.DUMMY,
        //                 Component.literal("x"),
        //                 ObjectiveCriteria.RenderType.INTEGER
        //             );
        //         }
        //         Objective y = scoreboard.getObjective("y");
        //         if (y == null) {
        //             y = scoreboard.addObjective(
        //                 "y",
        //                 ObjectiveCriteria.DUMMY,
        //                 Component.literal("y"),
        //                 ObjectiveCriteria.RenderType.INTEGER
        //             );
        //         }
        //         Objective z = scoreboard.getObjective("z");
        //         if (z == null) {
        //             z = scoreboard.addObjective(
        //                 "z",
        //                 ObjectiveCriteria.DUMMY,
        //                 Component.literal("z"),
        //                 ObjectiveCriteria.RenderType.INTEGER
        //             );
        //         }
        //         scoreboard.getOrCreatePlayerScore(player.getStringUUID(), x).setScore((int)player.getX());
        //         scoreboard.getOrCreatePlayerScore(player.getStringUUID(), y).setScore((int)player.getY());
        //         scoreboard.getOrCreatePlayerScore(player.getStringUUID(), z).setScore((int)player.getZ());
        //         ContainerEntity conen = new ContainerEntity(ModEntities.CONTAINER_ENTITY.get(), player.level());
        //         conen.moveTo(player.getX(), player.getY(), player.getZ());
        //         conen.serializeInventory(player);
        //         player.level().addFreshEntity(conen);
        //         player.getInventory().clearContent();
        //         player.getInventory().add(0, new ItemStack(top.ribs.scguns.init.ModItems.BIRDFEEDER.get()));
        //         player.getInventory().add(1, new ItemStack(top.ribs.scguns.init.ModItems.STANDARD_COPPER_ROUND.get(), 64));
        //         player.getInventory().add(2, new ItemStack(top.ribs.scguns.init.ModItems.STANDARD_COPPER_ROUND.get(), 64));
        //         player.getInventory().add(3, new ItemStack(top.ribs.scguns.init.ModItems.STANDARD_COPPER_ROUND.get(), 64));
        //     }
        // }
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
                ServerScoreboard scoreboard = player.level().getServer().getScoreboard();
                Objective xObj = scoreboard.getObjective("x");
                Objective yObj = scoreboard.getObjective("y");
                Objective zObj = scoreboard.getObjective("z");
                int x = scoreboard.getOrCreatePlayerScore(player.getStringUUID(), xObj).getScore();
                int y = scoreboard.getOrCreatePlayerScore(player.getStringUUID(), yObj).getScore();
                int z = scoreboard.getOrCreatePlayerScore(player.getStringUUID(), zObj).getScore();
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
                    ContainerEntity container = null;
                    for(ContainerEntity conen : list){
                        if(conen.getContainerOwner() == ogPlayer.getStringUUID()){
                            container = conen;
                            break;
                        }
                    }
                    if(container != null){
                        container.deserializeInventory(player);
                    }
                }
            }
        }
    }
}