package com.daragetsu.callsofthewars.entities.tank;

import javax.annotation.Nullable;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class TankEntity extends Mob implements GeoEntity {

    public static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(TankEntity.class, EntityDataSerializers.BOOLEAN);

    public static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenPlay("open");
    public static final RawAnimation CLOSE_ANIM = RawAnimation.begin().thenPlay("close");
    public static final RawAnimation FIRE_ANIM = RawAnimation.begin().thenPlay("fire");

    private int COOLDOWN_TIME = 200;

    public static final EntityDataAccessor<Long> CAN_FIRE_AFTER = SynchedEntityData.defineId(TankEntity.class, EntityDataSerializers.LONG);

    public TankEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }
    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ARMOR, 10D)
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 999.0D)
                ;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OPEN, false);
        this.entityData.define(CAN_FIRE_AFTER, this.level().getGameTime());
    }

    public boolean isOpen(){
        return this.entityData.get(OPEN);
    }

    public void setOpen(boolean op){
        this.entityData.set(OPEN, op);
    }

    public boolean canFire(){
        return this.level().getGameTime() > this.entityData.get(CAN_FIRE_AFTER);
    }

    public void addCooldown(){
        this.entityData.set(CAN_FIRE_AFTER, this.level().getGameTime()+COOLDOWN_TIME);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2,
                state -> {
                    if(state.getAnimatable().isVehicle() && state.getAnimatable().getPassengers().get(0).isSprinting()){
                        return state.setAndContinue(OPEN_ANIM);
                    }else{
                        return state.setAndContinue(CLOSE_ANIM);
                    }
                }
        ).triggerableAnim("fire", FIRE_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    public double getPassengersRidingOffset() {
        return 0.3;
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        Vec2 vec2 = this.getRiddenRotation(player);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
    }
    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.getXRot() * 0.5F, entity.getYRot());
    }
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float f = player.xxa * 0.5F;
        float f1 = player.zza;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        return new Vec3((double)f, (double)0.0F, (double)f1);
    }

    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    protected int getMaxPassengers() {
        return 1;
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

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND){
            if(this.isVehicle() && this.getPassengers().get(0).is(player)){
                fire();
            }
            if(this.canAddPassenger(player)){
                player.startRiding(this);
                return InteractionResult.SUCCESS;
            }
        }
        return super.interactAt(player, vec, hand);
    }
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isAlive()) {
            if (this.isVehicle() &&
            this.getPassengers().get(0) instanceof LivingEntity passenger) {
                this.setYRot(passenger.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(passenger.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;

                float strafe = passenger.xxa * 0.5F;
                float straight = passenger.zza;
                if (straight <= 0.0F) {
                    straight *= 0.25F; 
                }
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                super.travel(new Vec3(strafe, travelVector.y, straight));
                return;
            }
        }
        super.travel(travelVector);
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(this.isVehicle() && source.getEntity().is(this.getPassengers().get(0))){
            return false;
        }
        return super.hurt(source, amount);
    }

    public void fire(){
        if(!this.canFire())return;
        this.triggerAnim("controller", "fire");
    }
}
