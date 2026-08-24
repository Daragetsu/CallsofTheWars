package com.daragetsu.callsofthewars.worldgen.structure.structures;

import java.util.Optional;

import com.daragetsu.callsofthewars.worldgen.structure.ModStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class StarterBaseStructure extends Structure {
    public static final int MAX_TOTAL_STRUCTURE_RANGE = 128;
    public static final Codec<StarterBaseStructure> CODEC = ExtraCodecs
            .validate(RecordCodecBuilder.mapCodec((p_227640_) -> p_227640_.group(settingsCodec(p_227640_),
                    StructureTemplatePool.CODEC.fieldOf("camp_start_pool").forGetter((p_227656_) -> p_227656_.campStartPool),
                    StructureTemplatePool.CODEC.fieldOf("ship_start_pool").forGetter((p_227656_) -> p_227656_.shipStartPool),
                    Codec.intRange(0, 7).fieldOf("size").forGetter((p_227652_) -> p_227652_.maxDepth),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter((p_227649_) -> p_227649_.startHeight),
                    Codec.BOOL.fieldOf("use_expansion_hack").forGetter((p_227646_) -> p_227646_.useExpansionHack),
                    Types.CODEC.optionalFieldOf("project_start_to_heightmap")
                            .forGetter((p_227644_) -> p_227644_.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center")
                            .forGetter((p_227642_) -> p_227642_.maxDistanceFromCenter))
                    .apply(p_227640_, StarterBaseStructure::new)), StarterBaseStructure::verifyRange)
            .codec();
    private final Holder<StructureTemplatePool> campStartPool;
    private final Holder<StructureTemplatePool> shipStartPool;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    private static DataResult<StarterBaseStructure> verifyRange(StarterBaseStructure structure) {
        return DataResult.success(structure);
    }

    public StarterBaseStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> campStartPool, Holder<StructureTemplatePool> shipStartPool, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceToCenter) {
        super(settings);
        this.campStartPool = campStartPool;
        this.shipStartPool = shipStartPool;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceToCenter;
    }

    public StarterBaseStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> campStartPool,Holder<StructureTemplatePool> shipStartPool, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, Heightmap.Types projectStartToHeightmap) {
        this(settings, campStartPool, shipStartPool, maxDepth, startHeight, useExpansionHack, Optional.of(projectStartToHeightmap), 80);
    }

    public StarterBaseStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> campStartPool, Holder<StructureTemplatePool> shipStartPool,
            int maxDepth, HeightProvider startHeight, boolean useExpansionHack) {
        this(settings, campStartPool, shipStartPool, maxDepth, startHeight, useExpansionHack, Optional.empty(), 80);
    }

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkpos = context.chunkPos();
        int i = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());
        BlockState state = context.chunkGenerator().getBaseColumn(context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ(), context.heightAccessor(), context.randomState()).getBlock(62);
        if (state.is(Blocks.WATER) || state.is(Blocks.KELP_PLANT)) {
            return JigsawPlacement.addPieces(context, this.shipStartPool, Optional.empty(), this.maxDepth, blockpos, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter);
        }
        return JigsawPlacement.addPieces(context, this.campStartPool, Optional.empty(), this.maxDepth, blockpos, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.STARTER_BASE_STRUCTURE.get();
    }
}