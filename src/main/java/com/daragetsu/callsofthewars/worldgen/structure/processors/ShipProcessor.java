package com.daragetsu.callsofthewars.worldgen.structure.processors;

import com.daragetsu.callsofthewars.worldgen.structure.ModStructureProcessors;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class ShipProcessor extends StructureProcessor{
    public static final Codec<ShipProcessor> CODEC = Codec.unit(ShipProcessor::new);
    public ShipProcessor(){
    }
    @Override
    public StructureBlockInfo process(LevelReader level, BlockPos pos, BlockPos pos2, StructureBlockInfo sbi, StructureBlockInfo sbi2, StructurePlaceSettings settings, StructureTemplate template) {
        return super.process(level, pos, pos2, sbi, sbi2, settings.setKeepLiquids(false), template);
    }
    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.SHIP_PROCESSOR.get();
    }
}