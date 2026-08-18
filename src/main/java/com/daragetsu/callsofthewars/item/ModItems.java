package com.daragetsu.callsofthewars.item;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.entities.ModEntities;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CallsofTheWars.MOD_ID);

    public static final RegistryObject<SpawnEggItem> SOLDIER_RED_SPAWN_EGG = ITEMS.register("soldier_red_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOLDIER_RED, 0xFF0000, 0xFF0000, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> SOLDIER_GREEN_SPAWN_EGG = ITEMS.register("soldier_green_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOLDIER_GREEN, 0x00FF00, 0x00FF00, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> SOLDIER_BLUE_SPAWN_EGG = ITEMS.register("soldier_blue_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOLDIER_BLUE, 0x0000FF, 0x0000FF, new Item.Properties()));

    public static final void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
