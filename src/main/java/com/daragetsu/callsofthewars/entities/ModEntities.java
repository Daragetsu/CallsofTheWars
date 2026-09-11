package com.daragetsu.callsofthewars.entities;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.daragetsu.callsofthewars.entities.soldier.ParatrooperEntity;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntity;
import com.daragetsu.callsofthewars.entities.air_plane.AirPlaneEntity;
import com.daragetsu.callsofthewars.entities.common.client.VariantEntityRenderer;
import com.daragetsu.callsofthewars.entities.container.ContainerEntity;
import com.daragetsu.callsofthewars.entities.container.ContainerEntityRenderer;
import com.daragetsu.callsofthewars.entities.heightened.HeightenedEntity;
import com.daragetsu.callsofthewars.entities.mailer.MailerEntity;
import com.daragetsu.callsofthewars.entities.mailer.MailerEntityRenderer;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntityRenderer;
import com.daragetsu.callsofthewars.entities.tank.ProjectileEntity;
import com.daragetsu.callsofthewars.entities.tank.ProjectileRenderer;
import com.daragetsu.callsofthewars.entities.tank.TankEntity;
import com.daragetsu.callsofthewars.entities.tank.TankEntityRenderer;
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
    public static final RegistryObject<EntityType<SoldierEntity>> SOLDIER = ENTITY_TYPES.register("soldier", () -> EntityType.Builder.of(SoldierEntity::new, MobCategory.CREATURE).sized(0.6F, 2F).build("soldier"));
    public static final RegistryObject<EntityType<ParatrooperEntity>> PARATROOPER = ENTITY_TYPES.register("paratrooper", () -> EntityType.Builder.of(ParatrooperEntity::new, MobCategory.CREATURE).sized(0.6F, 2F).build("paratrooper"));
    public static final RegistryObject<EntityType<HeightenedEntity>> HEIGHTENED = ENTITY_TYPES.register("heightened", () -> EntityType.Builder.of(HeightenedEntity::new, MobCategory.CREATURE).sized(0.6F, 2F).build("heightened"));
    
    public static final RegistryObject<EntityType<UnitSpawnerEntity>> UNIT_SPAWNER = ENTITY_TYPES.register("unit_spawner", () -> EntityType.Builder.of(UnitSpawnerEntity::new, MobCategory.CREATURE).sized(0.3F, 0.3F).build("unit_spawner"));
    public static final RegistryObject<EntityType<ContainerEntity>> CONTAINER_ENTITY = ENTITY_TYPES.register("container_entity", () -> EntityType.Builder.of(ContainerEntity::new, MobCategory.CREATURE).sized(0.1F, 0.1F).build("container_entity"));
    
    public static final RegistryObject<EntityType<AirPlaneEntity>> AIR_PLANE = ENTITY_TYPES.register("air_plane", () -> EntityType.Builder.of(AirPlaneEntity::new, MobCategory.CREATURE).sized(3F, 3F).build("air_plane"));
    
    public static final RegistryObject<EntityType<MailerEntity>> MAILER = ENTITY_TYPES.register("mailer", () -> EntityType.Builder.of(MailerEntity::new, MobCategory.MONSTER).sized(0.6F, 2F).build("mailer"));
    
    public static final RegistryObject<EntityType<TankEntity>> TANK = ENTITY_TYPES.register("tank", () -> EntityType.Builder.of(TankEntity::new, MobCategory.MONSTER).sized(2F, 2F).build("tank"));
    
    public static final RegistryObject<EntityType<ProjectileEntity>> TANK_PROJECTILE = ENTITY_TYPES.register("tank_projectile", () -> EntityType.Builder.of(ProjectileEntity::new, MobCategory.MONSTER).sized(0.2F, 0.2F).build("tank_projectile"));

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
        event.put(ModEntities.SOLDIER.get(), SoldierEntity.createAttributes().build());
        event.put(ModEntities.PARATROOPER.get(), SoldierEntity.createAttributes().build());
        event.put(ModEntities.HEIGHTENED.get(), HeightenedEntity.createAttributes().build());
        event.put(ModEntities.UNIT_SPAWNER.get(), SoldierEntity.createAttributes().build());
        event.put(ModEntities.CONTAINER_ENTITY.get(), SoldierEntity.createAttributes().build());
        event.put(ModEntities.AIR_PLANE.get(), AirPlaneEntity.createAttributes().build());
        event.put(ModEntities.MAILER.get(), SoldierEntity.createAttributes().build());
        event.put(ModEntities.TANK.get(), TankEntity.createAttributes().build());
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        BoundingBoxManager.registerHeadshotBox(ModEntities.SOLDIER.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
        BoundingBoxManager.registerHeadshotBox(ModEntities.PARATROOPER.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
        BoundingBoxManager.registerHeadshotBox(ModEntities.HEIGHTENED.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
        BoundingBoxManager.registerHeadshotBox(ModEntities.MAILER.get(), new BasicHeadshotBox<>((double)8.0F, (double)24.0F));
    }

    @OnlyIn(value = Dist.CLIENT)
    private static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.SOLDIER.get(), (ctx) -> new VariantEntityRenderer<>(ctx, "soldier"));

        EntityRenderers.register(ModEntities.PARATROOPER.get(), (ctx) -> new VariantEntityRenderer<>(ctx, "paratrooper"));
        
        EntityRenderers.register(ModEntities.HEIGHTENED.get(), (ctx) -> new VariantEntityRenderer<>(ctx, "soldier"));
        
        EntityRenderers.register(ModEntities.UNIT_SPAWNER.get(), (ctx)->new SoldierEntityRenderer<>(ctx, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "soldier"))));
        EntityRenderers.register(ModEntities.CONTAINER_ENTITY.get(), (ctx)->new ContainerEntityRenderer(ctx));
        EntityRenderers.register(ModEntities.AIR_PLANE.get(), (ctx)->new VariantEntityRenderer<>(ctx, "air_plane"));
        EntityRenderers.register(ModEntities.MAILER.get(), (ctx)->new MailerEntityRenderer(ctx));
        EntityRenderers.register(ModEntities.TANK.get(), (ctx)->new TankEntityRenderer(ctx));
        EntityRenderers.register(ModEntities.TANK_PROJECTILE.get(), (ctx)->new ProjectileRenderer(ctx));
    }
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
            ModEntities.UNIT_SPAWNER.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            SoldierEntity::checkMonsterSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
            ModEntities.HEIGHTENED.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            SoldierEntity::checkMonsterSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
            ModEntities.MAILER.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            (type, level, spawnType, pos, random) -> true,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
            ModEntities.TANK.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            (type, level, spawnType, pos, random) -> true,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
    }
}
