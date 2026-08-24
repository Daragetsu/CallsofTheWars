package com.daragetsu.callsofthewars.worldgen.structure;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.worldgen.structure.structures.StarterBaseStructure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister
            .create(Registries.STRUCTURE_TYPE, CallsofTheWars.MOD_ID);

    public static final RegistryObject<StructureType<StarterBaseStructure>> STARTER_BASE_STRUCTURE =
        STRUCTURE_TYPES.register(
            "starter_base",
            () -> () -> StarterBaseStructure.CODEC
        );
    public static void register(IEventBus modEventBus) {
        STRUCTURE_TYPES.register(modEventBus);
    }
}
