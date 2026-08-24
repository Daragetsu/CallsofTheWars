package com.daragetsu.callsofthewars.worldgen.structure;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.worldgen.structure.processors.ShipProcessor;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructureProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS = DeferredRegister
            .create(Registries.STRUCTURE_PROCESSOR, CallsofTheWars.MOD_ID);

    public static final RegistryObject<StructureProcessorType<ShipProcessor>> SHIP_PROCESSOR = STRUCTURE_PROCESSORS.register("ship_processor", ()->()->ShipProcessor.CODEC);

    public static void register(IEventBus modEventBus) {
        STRUCTURE_PROCESSORS.register(modEventBus);
    }
}
