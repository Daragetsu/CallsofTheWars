package com.daragetsu.callsofthewars.block;

import com.daragetsu.callsofthewars.CallsofTheWars;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CallsofTheWars.MOD_ID);

    public static final RegistryObject<BossMusicBlock> BOSS_MUSIC_BLOCK = BLOCKS.register("boss_music_block", () -> new BossMusicBlock(BlockBehaviour.Properties.copy(Blocks.AIR)));
    
    public static final RegistryObject<GeneralSummonerBlock> GENERAL_SUMMONER_BLOCK = BLOCKS.register("general_summoner_block", () -> new GeneralSummonerBlock(BlockBehaviour.Properties.copy(Blocks.AIR)));

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
