package com.daragetsu.callsofthewars.block;

import com.daragetsu.callsofthewars.entities.ModEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.scores.PlayerTeam;

public class GeneralSummonerBlock extends Block{

    public static final IntegerProperty VARIANT =
        IntegerProperty.create("variant", 0, 3);

    public GeneralSummonerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, 0));
    }
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(VARIANT, 0);
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int variant = state.getValue(VARIANT);
        PlayerTeam redTeam = level.getServer().getScoreboard().getPlayerTeam("red");
        PlayerTeam greenTeam = level.getServer().getScoreboard().getPlayerTeam("green");
        PlayerTeam blueTeam = level.getServer().getScoreboard().getPlayerTeam("blue");
        boolean setToRemove = false;
        switch (variant) {
            case 1:
                level.getServer().getScoreboard()
                .addPlayerToTeam(
                    ModEntities.GENERAL.get()
                    .spawn(
                        level, 
                        pos, 
                        MobSpawnType.MOB_SUMMONED)
                        .getStringUUID(), 
                        redTeam
                );
                setToRemove = true;
                break;
            case 2:
                level.getServer().getScoreboard()
                .addPlayerToTeam(
                    ModEntities.GENERAL.get()
                    .spawn(
                        level, 
                        pos, 
                        MobSpawnType.MOB_SUMMONED)
                        .getStringUUID(), 
                        greenTeam
                );
                setToRemove = true;
                break;
            case 3:
                level.getServer().getScoreboard()
                .addPlayerToTeam(
                    ModEntities.GENERAL.get()
                    .spawn(
                        level, 
                        pos, 
                        MobSpawnType.MOB_SUMMONED)
                        .getStringUUID(), 
                        blueTeam
                );
                setToRemove = true;
                break;
            default:
                break;
        }
        if(setToRemove){
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }else{
            level.scheduleTick(pos, this, 1);
        }
    }
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, 1);
    }
}