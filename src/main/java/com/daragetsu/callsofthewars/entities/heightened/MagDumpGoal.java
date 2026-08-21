package com.daragetsu.callsofthewars.entities.heightened;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class MagDumpGoal extends Goal{

    private final BaseHeightenedEntity shooter;
    private final int maxRunFor;
    private final int maxCooldown;
    private int ticks = 0;
    private int cooldown = 0;

    public MagDumpGoal(BaseHeightenedEntity shooter, int cooldown, int runFor){
        this.shooter = shooter;
        this.maxCooldown = cooldown;
        this.maxRunFor = runFor;
    }

    @Override
    public void start() {
        super.start();
        this.ticks = 0;
        this.cooldown = this.maxCooldown;
        ItemStack mainStack = this.shooter.getMainHandItem();
        ItemStack offStack = this.shooter.getOffhandItem();
        this.shooter.setItemInHand(InteractionHand.MAIN_HAND, offStack);
        this.shooter.setItemInHand(InteractionHand.OFF_HAND, mainStack);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticks++;
    }

    @Override
    public void stop() {
        super.stop();
        ItemStack mainStack = this.shooter.getMainHandItem();
        ItemStack offStack = this.shooter.getOffhandItem();
        this.shooter.setItemInHand(InteractionHand.MAIN_HAND, offStack);
        this.shooter.setItemInHand(InteractionHand.OFF_HAND, mainStack);
    }

    @Override
    public boolean canUse() {
        if(this.cooldown>0)this.cooldown--;
        return this.shooter.getTarget()!=null && this.cooldown <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.shooter.getTarget()!=null && this.ticks <=this.maxRunFor;
    }   
}
