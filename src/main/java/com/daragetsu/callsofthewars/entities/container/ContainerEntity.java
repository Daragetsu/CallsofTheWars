package com.daragetsu.callsofthewars.entities.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ContainerEntity extends AbstractChestedHorse implements GeoEntity{

    public static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(ContainerEntity.class, EntityDataSerializers.STRING);

    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    public ContainerEntity(EntityType<? extends AbstractChestedHorse> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public boolean hasChest() {
        return true;
    }

    @Override
    protected int getInventorySize() {
        return 40;
    }
    @Override
    protected void registerGoals() {
    }
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return false;
    }
    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }
    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }
    @Override
    public boolean canBeSeenAsEnemy() {
        return false;
    }
    @Override
    public boolean canBeSeenByAnyone() {
        return false;
    }
    @Override
    public boolean canBreed() {
        return false;
    }
    @Override
    public void die(DamageSource damageSource) {
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
    @Override
    public void kill() {
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER, "");
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("containerOwner", this.getContainerOwner());
    }
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(compound.contains("containerOwner")){
            this.setContainerOwner(compound.getString("containerOwner"));
        }
    }
    public String getContainerOwner(){
        return this.entityData.get(OWNER);
    }
    public void setContainerOwner(String uuid){
        this.entityData.set(OWNER, uuid);
    }
    public void serializeInventory(Player player){
        for(ItemStack item : player.getInventory().items){
            this.inventory.addItem(item);
        }
    }
    public void deserializeInventory(Player player){
        for(int i = 0; i < this.getInventorySize(); i++){
            player.getInventory().setItem(i, this.inventory.getItem(i));
        }
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }
    @Override
    public ItemStack getPickResult() {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean isPickable() {
        return false;
    }
}