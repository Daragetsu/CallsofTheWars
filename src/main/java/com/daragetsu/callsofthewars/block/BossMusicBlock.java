package com.daragetsu.callsofthewars.block;

import com.daragetsu.callsofthewars.sounds.ModSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BossMusicBlock extends Block{
    public static final IntegerProperty TICKS =
        IntegerProperty.create("ticks", 0, 559);
    public BossMusicBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TICKS, 0));
    }
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(TICKS);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(TICKS, 0);
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int ticks = state.getValue(TICKS);
        if (ticks == 0) {
            level.playSound(null, pos, ModSounds.GENERAL_BOSS_MUSIC.get(), SoundSource.BLOCKS);
        }
        level.setBlock(pos, state.setValue(TICKS, (ticks + 1) % 560), Block.UPDATE_ALL);
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, 1);
    }
}
