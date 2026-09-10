package com.daragetsu.callsofthewars.entities.tank;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class TankFireGoal extends Goal{

    public final TankEntity tank;
    public int cooldown = 0;

    public TankFireGoal(TankEntity tank){
        this.tank = tank;
    }

    @Override
    public boolean canUse() {
        if(this.cooldown>0)this.cooldown--;
        if(tank.isVehicle() && !(tank.getPassengers().get(0) instanceof Player)) return true && this.cooldown <= 0;
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        super.start();
        this.cooldown = 60;
        this.tank.fire();
    }
}
