package com.daragetsu.callsofthewars.entities.air_plane;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AirPlaneEntity extends Monster implements FlyingAnimal, GeoEntity{
    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    public AirPlaneEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 5, true);
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 30.0F : 0.0F;
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 2,
                state -> state.setAndContinue(RawAnimation.begin().thenLoop("idle"))
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ARMOR, 0.1D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 0.1D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1D)
                .add(Attributes.ATTACK_SPEED, 0.1D)
                ;
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new WanderGoal());
        this.goalSelector.addGoal(4, new AirDropGoal(this));
    }
    protected PathNavigation createNavigation(Level p_level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, p_level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }
    class WanderGoal extends Goal {
        Vec3 goTo = null;
        WanderGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return AirPlaneEntity.this.navigation.isDone();
        }

        public boolean canContinueToUse() {
            return AirPlaneEntity.this.navigation.isInProgress();
        }

        public void start() {
            this.goTo = this.findPos();
            if (this.goTo != null) {
                AirPlaneEntity.this.navigation.moveTo(AirPlaneEntity.this.navigation.createPath(BlockPos.containing(this.goTo), 1), (double)1.0F);
            }
        }

        @Override
        public void stop() {
            super.stop();
        }

        @Override
        public void tick() {
            super.tick();
            AirPlaneEntity.this.lookAt(Anchor.EYES, this.goTo);
        }

        @Nullable
        private Vec3 findPos() {
            AABB range = new AABB(
                AirPlaneEntity.this.position().x()-400,
                AirPlaneEntity.this.position().y(),
                AirPlaneEntity.this.position().z()-400,
                AirPlaneEntity.this.position().x()+400,
                AirPlaneEntity.this.position().y(),
                AirPlaneEntity.this.position().z()+400
            );
            AABB banned = new AABB(
                AirPlaneEntity.this.position().x()-300,
                AirPlaneEntity.this.position().y(),
                AirPlaneEntity.this.position().z()-300,
                AirPlaneEntity.this.position().x()+300,
                AirPlaneEntity.this.position().y(),
                AirPlaneEntity.this.position().z()+300
            );
            Vec3 target = new Vec3(AirPlaneEntity.this.random.nextInt((int)range.minX, (int)range.maxX), range.maxY, AirPlaneEntity.this.random.nextInt((int)range.minZ, (int)range.maxZ));
            while(banned.contains(target)){
                target = new Vec3(AirPlaneEntity.this.random.nextInt((int)range.minX, (int)range.maxX), range.maxY, AirPlaneEntity.this.random.nextInt((int)range.minZ, (int)range.maxZ));
            }
            return target;
        }
    }
}
