package com.daragetsu.callsofthewars.sounds;

import com.daragetsu.callsofthewars.CallsofTheWars;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CallsofTheWars.MOD_ID);

    public static final RegistryObject<SoundEvent> GENERAL_BOSS_MUSIC = SOUND_EVENTS.register("general_boss_music", 
        () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "general_boss_music")));

    public static void register(IEventBus eventbus) {
        SOUND_EVENTS.register(eventbus);
    }
}