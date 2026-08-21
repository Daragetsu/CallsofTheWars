package com.daragetsu.callsofthewars.entities;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.entities.soldier.BaseGeneralEntity;
import com.daragetsu.callsofthewars.entities.soldier.BaseSoldierEntity;
import com.daragetsu.callsofthewars.entities.soldier.GeneralEntityBlue;
import com.daragetsu.callsofthewars.entities.soldier.GeneralEntityGreen;
import com.daragetsu.callsofthewars.entities.soldier.GeneralEntityRed;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntityBlue;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntityGreen;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntityRed;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntityRenderer;
import com.daragetsu.callsofthewars.entities.unit_spawner.UnitSpawnerEntity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import top.ribs.scguns.common.BoundingBoxManager;
import top.ribs.scguns.common.headshot.BasicHeadshotBox;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CallsofTheWars.MOD_ID);
    //TODO: REFACTOR ALL THESE INTO BASE CLASSES INSTEAD OF 3 DIFFERENT CLASSES
    public static final RegistryObject<EntityType<SoldierEntityRed>> SOLDIER_RED = ENTITY_TYPES.register("soldier_red", () -> EntityType.Builder.of(SoldierEntityRed::new, MobCategory.CREATURE).sized(0.6F, 2F).build("soldier_red"));
    public static final RegistryObject<EntityType<GeneralEntityRed>> GENERAL_RED = ENTITY_TYPES.register("general_red", () -> EntityType.Builder.of(GeneralEntityRed::new, MobCategory.CREATURE).sized(0.6F, 2F).build("general_red"));
    public static final RegistryObject<EntityType<SoldierEntityGreen>> SOLDIER_GREEN = ENTITY_TYPES.register("soldier_green", () -> EntityType.Builder.of(SoldierEntityGreen::new, MobCategory.CREATURE).sized(0.6F, 2F).build("soldier_green"));
    public static final RegistryObject<EntityType<GeneralEntityGreen>> GENERAL_GREEN = ENTITY_TYPES.register("general_green", () -> EntityType.Builder.of(GeneralEntityGreen::new, MobCategory.CREATURE).sized(0.6F, 2F).build("general_green"));
    public static final RegistryObject<EntityType<SoldierEntityBlue>> SOLDIER_BLUE = ENTITY_TYPES.register("soldier_blue", () -> EntityType.Builder.of(SoldierEntityBlue::new, MobCategory.CREATURE).sized(0.6F, 2F).build("soldier_blue"));
    public static final RegistryObject<EntityType<GeneralEntityBlue>> GENERAL_BLUE = ENTITY_TYPES.register("general_blue", () -> EntityType.Builder.of(GeneralEntityBlue::new, MobCategory.CREATURE).sized(0.6F, 2F).build("general_blue"));
    public static final RegistryObject<EntityType<UnitSpawnerEntity>> UNIT_SPAWNER = ENTITY_TYPES.register("unit_spawner", () -> EntityType.Builder.of(UnitSpawnerEntity::new, MobCategory.CREATURE).sized(0.3F, 0.3F).build("unit_spawner"));

    public static final void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
        eventBus.addListener(ModEntities::registerAttributes);
        eventBus.addListener(ModEntities::onCommonSetup);
        eventBus.addListener(ModEntities::registerSpawnPlacements);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(ModEntities::onClientSetup);
        }
    }
    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SOLDIER_RED.get(), BaseSoldierEntity.createAttributes().build());
        event.put(ModEntities.SOLDIER_GREEN.get(), BaseSoldierEntity.createAttributes().build());
        event.put(ModEntities.SOLDIER_BLUE.get(), BaseSoldierEntity.createAttributes().build());
        event.put(ModEntities.GENERAL_RED.get(), BaseGeneralEntity.createAttributes().build());
        event.put(ModEntities.GENERAL_GREEN.get(), BaseGeneralEntity.createAttributes().build());
        event.put(ModEntities.GENERAL_BLUE.get(), BaseGeneralEntity.createAttributes().build());
        event.put(ModEntities.UNIT_SPAWNER.get(), BaseSoldierEntity.createAttributes().build());
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        BoundingBoxManager.registerHeadshotBox(ModEntities.SOLDIER_RED.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
        BoundingBoxManager.registerHeadshotBox(ModEntities.SOLDIER_GREEN.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
        BoundingBoxManager.registerHeadshotBox(ModEntities.SOLDIER_BLUE.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
        BoundingBoxManager.registerHeadshotBox(ModEntities.GENERAL_RED.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
        BoundingBoxManager.registerHeadshotBox(ModEntities.GENERAL_GREEN.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
        BoundingBoxManager.registerHeadshotBox(ModEntities.GENERAL_BLUE.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
    }

    @OnlyIn(value = Dist.CLIENT)
    private static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.SOLDIER_RED.get(), (ctx) -> new SoldierEntityRenderer<>(ctx, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "soldier"))));
        EntityRenderers.register(ModEntities.SOLDIER_GREEN.get(), (ctx) -> new SoldierEntityRenderer<>(ctx, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "soldier"))));
        EntityRenderers.register(ModEntities.SOLDIER_BLUE.get(), (ctx) -> new SoldierEntityRenderer<>(ctx, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "soldier"))));
        
        EntityRenderers.register(ModEntities.GENERAL_RED.get(), (ctx) -> new SoldierEntityRenderer<>(ctx, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "general"))));
        EntityRenderers.register(ModEntities.GENERAL_GREEN.get(), (ctx) -> new SoldierEntityRenderer<>(ctx, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "general"))));
        EntityRenderers.register(ModEntities.GENERAL_BLUE.get(), (ctx) -> new SoldierEntityRenderer<>(ctx, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "general"))));
        
        EntityRenderers.register(ModEntities.UNIT_SPAWNER.get(), (ctx)->new SoldierEntityRenderer<>(ctx, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "soldier"))));
    }
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
            ModEntities.UNIT_SPAWNER.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            (entityType, level, spawnType, pos, random) -> true,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
            ModEntities.GENERAL_RED.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            BaseGeneralEntity::checkMonsterSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
            ModEntities.GENERAL_GREEN.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            BaseGeneralEntity::checkMonsterSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
            ModEntities.GENERAL_BLUE.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            BaseGeneralEntity::checkMonsterSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
    }
}
