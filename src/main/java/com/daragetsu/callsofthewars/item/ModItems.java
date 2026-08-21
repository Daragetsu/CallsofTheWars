package com.daragetsu.callsofthewars.item;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.client.curios.BeltRenderer;
import com.daragetsu.callsofthewars.entities.ModEntities;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CallsofTheWars.MOD_ID);

    public static final RegistryObject<SpawnEggItem> SOLDIER_RED_SPAWN_EGG = ITEMS.register("soldier_red_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOLDIER_RED, 0x990000, 0x990000, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> SOLDIER_GREEN_SPAWN_EGG = ITEMS.register("soldier_green_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOLDIER_GREEN, 0x009900, 0x009900, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> SOLDIER_BLUE_SPAWN_EGG = ITEMS.register("soldier_blue_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOLDIER_BLUE, 0x000099, 0x000099, new Item.Properties()));
    
    public static final RegistryObject<SpawnEggItem> HEIGHTENED_RED_SPAWN_EGG = ITEMS.register("heightened_red_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.HEIGHTENED_RED, 0xFF0000, 0xFF0000, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> HEIGHTENED_GREEN_SPAWN_EGG = ITEMS.register("heightened_green_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.HEIGHTENED_GREEN, 0x00FF00, 0x00FF00, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> HEIGHTENED_BLUE_SPAWN_EGG = ITEMS.register("heightened_blue_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.HEIGHTENED_BLUE, 0x0000FF, 0x0000FF, new Item.Properties()));
    
    public static final RegistryObject<Item> RED_BELT = ITEMS.register("red_belt", ()->new BeltItem(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_BELT = ITEMS.register("green_belt", ()->new BeltItem(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_BELT = ITEMS.register("blue_belt", ()->new BeltItem(new Item.Properties()));

    public static final void register(IEventBus eventBus){
        ITEMS.register(eventBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(ModItems::onClientSetup);
        }
    }
    @OnlyIn(value = Dist.CLIENT)
    private static void onClientSetup(FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(ModItems.RED_BELT.get(), BeltRenderer::new);
        CuriosRendererRegistry.register(ModItems.GREEN_BELT.get(), BeltRenderer::new);
        CuriosRendererRegistry.register(ModItems.BLUE_BELT.get(), BeltRenderer::new);
    }
}
