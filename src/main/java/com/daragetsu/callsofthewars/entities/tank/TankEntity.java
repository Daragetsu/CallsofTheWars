package com.daragetsu.callsofthewars.entities.tank;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TankEntity extends Entity implements GeoEntity {

    private float deltaRotation;
    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;
    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    public TankEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag arg0) {
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag arg0) {
    }

    public static boolean canVehicleCollide(Entity vehicle, Entity entity) {
        return (entity.canBeCollidedWith() || entity.isPushable()) && !vehicle.isPassengerOfSameVehicle(entity);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    public double getPassengersRidingOffset() {
        return -0.1;
    }

    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    private void controlBoat() {
        if (this.isVehicle()) {
            float f = 0.0F;
            if (this.inputLeft) {
                --this.deltaRotation;
            }

            if (this.inputRight) {
                ++this.deltaRotation;
            }

            if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                f += 0.005F;
            }

            this.setYRot(this.getYRot() + this.deltaRotation);
            if (this.inputUp) {
                f += 0.04F;
            }

            if (this.inputDown) {
                f -= 0.005F;
            }

            this.setDeltaMovement(
                    this.getDeltaMovement().add((double) (Mth.sin(-this.getYRot() * ((float) Math.PI / 180F)) * f),
                            (double) 0.0F, (double) (Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * f)));
        }

    }

    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    protected int getMaxPassengers() {
        return 2;
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        LivingEntity livingentity1;
        if (entity instanceof LivingEntity livingentity) {
            livingentity1 = livingentity;
        } else {
            livingentity1 = null;
        }

        return livingentity1;
    }

    public void setInput(boolean inputLeft, boolean inputRight, boolean inputUp, boolean inputDown) {
        this.inputLeft = inputLeft;
        this.inputRight = inputRight;
        this.inputUp = inputUp;
        this.inputDown = inputDown;
    }

    public void tick() {
        super.tick();
        if (this.isControlledByLocalInstance()) {
            if (this.level().isClientSide) {
                this.controlBoat();
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        this.checkInsideBlocks();
        List<Entity> list = this.level().getEntities(this,
                this.getBoundingBox().inflate((double) 0.2F, (double) -0.01F, (double) 0.2F),
                EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.level().isClientSide && !(this.getControllingPassenger() instanceof Player);

            for (int j = 0; j < list.size(); ++j) {
                Entity entity = (Entity) list.get(j);
                if (!entity.hasPassenger(this)) {
                    if (flag && this.getPassengers().size() < this.getMaxPassengers() && !entity.isPassenger()
                            && entity instanceof LivingEntity && !(entity instanceof WaterAnimal)
                            && !(entity instanceof Player)) {
                        entity.startRiding(this);
                    } else {
                        this.push(entity);
                    }
                }
            }
        }
    }
}
