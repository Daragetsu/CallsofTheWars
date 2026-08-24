package com.daragetsu.callsofthewars.entities.common;

import com.daragetsu.callsofthewars.entities.common.util.EnlistHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BaseSoldierEntity extends GunnerEntity implements GeoEntity{

    private static final EntityDataAccessor<String> BELONGS_TO = SynchedEntityData.defineId(BaseSoldierEntity.class, EntityDataSerializers.STRING);

    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    public BaseSoldierEntity(EntityType<? extends Monster> entity, Level level) {
        super(entity, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 1, true, false,
                p -> {
                    if(!this.level().isClientSide()){
                        ServerPlayer player = (ServerPlayer) p;
                        return !player.isCreative() && !player.isSpectator() && !EnlistHandler.alliedToPlayer(player, this);
                    }else{
                        Player player = (Player) p;
                        return !player.isCreative() && !player.isSpectator();
                    }
                }
            )
        );
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, BaseSoldierEntity.class, 1, true, false,
                soldier -> !(((BaseSoldierEntity) soldier).getBelongsTo() == this.getBelongsTo())));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, 1, true, false,
                entity -> !(((entity instanceof BaseSoldierEntity)))));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.4f));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 10));
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "walk/idle/aim", 2,
                state -> {
                    if(((this.getX() - this.xo)*(this.getX() - this.xo))+((this.getZ() - this.zo)*(this.getZ() - this.zo))>0.0002){
                        if(state.getAnimatable().isAiming()){
                            return state.setAndContinue(RawAnimation.begin().thenPlay("walk_aim"));
                        }else{
                            return state.setAndContinue(RawAnimation.begin().thenPlay("walk_idle"));
                        }
                    }
                    if(state.getAnimatable().isAiming()){
                        return state.setAndContinue(RawAnimation.begin().thenPlay("aim"));
                    }
                    return state.setAndContinue(RawAnimation.begin().thenPlay("idle"));
                }
        ).setAnimationSpeed(1.3));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BELONGS_TO, BelongsTo.RED.name());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ARMOR, 0.1D)
                .add(Attributes.MAX_HEALTH, 20.0D);
    }

    public enum BelongsTo{
        RED,
        GREEN,
        BLUE
    }
    public void setBelongsTo(BelongsTo belongs){
        this.entityData.set(BELONGS_TO, belongs.name());
    }
    public BelongsTo getBelongsTo(){
        return BelongsTo.valueOf(this.entityData.get(BELONGS_TO));
    }
    @Override
    public boolean save(CompoundTag compound) {
        compound.putString("belongsTo", this.getBelongsTo().name());
        return super.save(compound);
    }
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if(compound.contains("belongsTo")){
            this.setBelongsTo(BelongsTo.valueOf(compound.getString("belongsTo")));
        }else{
            this.setBelongsTo(BelongsTo.RED);
        }
    }
    public float getRed(){
        return this.getBelongsTo() == BelongsTo.RED ? 1f : 0f;
    }
    public float getGreen(){
        return this.getBelongsTo() == BelongsTo.GREEN ? 1f : 0f;
    }
    public float getBlue(){
        return this.getBelongsTo() == BelongsTo.BLUE ? 1f : 0f;
    }
    public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(type, level, spawnType, pos, random) && random.nextInt(100)<3;
    }
    @Override
    public void setTarget(LivingEntity target) {
        if(!this.level().isClientSide()){
            if(target instanceof ServerPlayer player){
                if(!EnlistHandler.alliedToPlayer(player, this)){
                    super.setTarget(target);
                }
            }else{
                super.setTarget(target);
            }
        }else{
            super.setTarget(target);
        }
    }
}