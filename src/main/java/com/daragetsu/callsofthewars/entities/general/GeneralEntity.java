package com.daragetsu.callsofthewars.entities.general;

import com.daragetsu.callsofthewars.common.util.TeamHandler;
import com.daragetsu.callsofthewars.entities.common.GunnerEntity;
import com.daragetsu.callsofthewars.entities.common.VariantEntity;
import com.daragetsu.callsofthewars.entities.soldier.SoldierEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GeneralEntity extends GunnerEntity implements GeoEntity, VariantEntity{
    
    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation AIMING = RawAnimation.begin().thenPlayAndHold("idle_shoot");

    private boolean played = false;
    private int ticksSince = 0;
    public boolean isAuraFarming = false;;

    public static final Vec3 EXHAUST = new Vec3(-1D/16.0D, 12D/16.0D, -5.5D/16.0D);

    private final ServerBossEvent bossEvent =
            new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);

    public GeneralEntity(EntityType<? extends Monster> entity, Level level) {
        super(entity, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 1, true, false,
                p -> {
                    if(!this.level().isClientSide()){
                        ServerPlayer player = (ServerPlayer) p;
                        return !player.isCreative() && !player.isSpectator() && !player.isAlliedTo(this);
                    }else{
                        Player player = (Player) p;
                        return !player.isCreative() && !player.isSpectator();
                    }
                }
            )
        );
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, SoldierEntity.class, 1, true, false,
                soldier -> !(((SoldierEntity) soldier).isAlliedTo(this))));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, 1, true, false,
                entity -> !(((entity instanceof SoldierEntity)))));
        this.targetSelector.addGoal(2, new SummonReinforcementsGoal(this, 400, 5, 1, 20, 10));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.4f));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 10));
        this.goalSelector.addGoal(5, new FlyUpGoal(this));
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "walk/idle/aim", 2,
                state -> {
                    if (state.getAnimatable().isAiming()) {
                        if(!state.getAnimatable().onGround()){
                           return state.setAndContinue(RawAnimation.begin().thenLoop("fly_holding_aim")); 
                        }
                        if(((this.getX() - this.xo)*(this.getX() - this.xo))+((this.getZ() - this.zo)*(this.getZ() - this.zo))>0.0002){
                            return state.setAndContinue(RawAnimation.begin().thenLoop("walk_holding_aim"));
                        }else{
                            return state.setAndContinue(AIMING);
                        }
                    } else {
                        RawAnimation anim = RawAnimation.begin();
                        if (state.isCurrentAnimation(AIMING)) {
                            anim = anim.thenPlay("shoot_idle");
                        }
                        if (((this.getX() - this.xo)*(this.getX() - this.xo))+((this.getZ() - this.zo)*(this.getZ() - this.zo))>0.0002){
                            return state.setAndContinue(anim.thenLoop("walk"));
                        } else {
                            return state.setAndContinue(RawAnimation.begin()
                                .thenLoop("idle"));
                        }
                    }
                }
        ).setAnimationSpeed(1.3));
        controllers.add(new AnimationController<>(this, "death", 2, state -> {
            if (state.getAnimatable().isDeadOrDying()) {
                return state.setAndContinue(RawAnimation.begin().thenPlayAndHold("death"));
            } else {
                return PlayState.STOP;
            }
        }));
        controllers.add(new AnimationController<>(this, "phase", 0, state -> PlayState.CONTINUE)
        .triggerableAnim("phase_2", RawAnimation.begin().thenPlay("phase_2")));
        controllers.add(new AnimationController<>(this, "summon", 0, state -> PlayState.CONTINUE)
        .triggerableAnim("summon_reinforcements", RawAnimation.begin().thenPlay("summon_reinforcements")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }

    private boolean hasPlayed(){
        return this.played;
    }

    private void setPlayed(boolean bool){
        this.played = bool;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ARMOR, 20D)
                .add(Attributes.MAX_HEALTH, 200.0D);
    }
    public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(type, level, spawnType, pos, random) && random.nextInt(100)<3;
    }
    @Override
    public void setTarget(LivingEntity target) {
        if(target instanceof Player player){
            if(!player.isAlliedTo(this)){
                super.setTarget(target);
            }
        }else{
            super.setTarget(target);
        }
    }
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            SpawnGroupData spawnData, CompoundTag dataTag) {
        TeamHandler.AddToTeam(level.getLevel(), this);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public int getVariant() {
        return TeamHandler.getVariant(this);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide())return;
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        if(this.hasPlayed())this.ticksSince++;
        if((this.getHealth() < (this.getMaxHealth() * 0.1)) && !this.hasPlayed()){
            this.triggerAnim("phase", "phase_2");
            this.setPlayed(true);
            this.isAuraFarming = true;
        }
        if(this.ticksSince == 40){
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 60));
        }
        if(this.ticksSince == 90){
            this.isAuraFarming = false;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return this.isAuraFarming ? false : super.hurt(source, amount);
    }
    
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }   
}
