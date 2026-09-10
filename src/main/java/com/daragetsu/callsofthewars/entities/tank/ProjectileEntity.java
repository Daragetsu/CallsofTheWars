package com.daragetsu.callsofthewars.entities.tank;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ProjectileEntity extends AbstractArrow implements GeoEntity{

    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    public ProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(!this.level().isClientSide()){
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5, ExplosionInteraction.TNT);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if(!this.level().isClientSide()){
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5, ExplosionInteraction.TNT);
            this.discard();
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }


}
