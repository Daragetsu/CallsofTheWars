package com.daragetsu.callsofthewars.entities.air_plane;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class AirPlaneMoveToPlayerGoal extends Goal {
   private final PathfinderMob mob;
   @Nullable
   private LivingEntity target;
   private double wantedX;
   private double wantedY;
   private double wantedZ;
   private final double speedModifier;
   private final float within;

   public AirPlaneMoveToPlayerGoal(PathfinderMob mob, double speedModifier, float within) {
      this.mob = mob;
      this.speedModifier = speedModifier;
      this.within = within;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public boolean canUse() {
      this.target = this.mob.getTarget();
      if (this.target == null) {
         return false;
      } else if (this.target.distanceToSqr(this.mob) > (double)(this.within * this.within)) {
         return false;
      } else {
         Vec3 vec3 = DefaultRandomPos.getPosTowards(this.mob, 16, 7, this.target.position().add(0, 30, 0), (double)((float)Math.PI / 2F));
         if (vec3 == null) {
            return false;
         } else {
            this.wantedX = vec3.x;
            this.wantedY = vec3.y;
            this.wantedZ = vec3.z;
            return true;
         }
      }
   }

   public boolean canContinueToUse() {
      return !this.mob.getNavigation().isDone() && this.target.isAlive() && this.target.distanceToSqr(this.mob) < (double)(this.within * this.within);
   }

   public void stop() {
      this.target = null;
   }

   public void start() {
      this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
   }
}