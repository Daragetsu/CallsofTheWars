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

    public static final RegistryObject<SpawnEggItem> SOLDIER_SPAWN_EGG = ITEMS.register("soldier_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOLDIER, 0x990000, 0x990000, new Item.Properties()));
    
    public static final RegistryObject<SpawnEggItem> HEIGHTENED_SPAWN_EGG = ITEMS.register("heightened_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.HEIGHTENED, 0xFF0000, 0xFF0000, new Item.Properties()));
    
    public static final void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
