package com.daragetsu.callsofthewars.entities.mailer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MailerEntity extends Monster implements GeoEntity{

    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    public MailerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2,
                state -> {
                    if(((this.getX() - this.xo)*(this.getX() - this.xo))+((this.getZ() - this.zo)*(this.getZ() - this.zo))>0.0002){
                        return state.setAndContinue(RawAnimation.begin().thenLoop("walk"));
                    }
                    return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
                }
        ));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.4, 50));
        this.goalSelector.addGoal(3, new Goal() {//can't bother making a new class
            @Override
            public boolean canUse() {
                return MailerEntity.this.getTarget()!=null && MailerEntity.this.getRandom().nextFloat() < 0.4 && MailerEntity.this.distanceToSqr(MailerEntity.this.getTarget()) < 4;
            }
            @Override
            public boolean canContinueToUse() {
                return false;
            }
            @Override
            public void start() {
                super.start();
                if(MailerEntity.this.level().isClientSide())return;
                ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
                CompoundTag tag = new CompoundTag();
                tag.putString(WrittenBookItem.TAG_TITLE, "Invitation");
                tag.putString(WrittenBookItem.TAG_AUTHOR, "Universal Peacekeepers Organization");
                ListTag listtag = new ListTag();
                String s = Component.Serializer.toJson(
                    Component.literal(String.format("""
                        Hello %s,\n
                        we hope this letter finds you well, 
                        we are the Universal Peacekeepers Organization or UPO for short, 
                        we are an multi-world Intergovernmental Organization who works""", 
                        MailerEntity.this.getTarget().getName().getString()
                    ))
                );
                String s2 = Component.Serializer.toJson(
                    Component.literal("""
                        to protect the peace of this universe,\n
                        but in the recent years,\n
                        an war has broken out in the world %d, we, in behalf of this world,\n
                        ask you  to join us in our mission
                        """
                    )
                );
                String s3 = Component.Serializer.toJson(
                    Component.literal("""
                        to keep the peace of this world, if you are interested, please run /enlist,\n
                        thank you very much,\n
                        Universal Peacekeepers Organization
                        """
                    )
                );
                listtag.add(StringTag.valueOf(s));
                listtag.add(StringTag.valueOf(s2));
                listtag.add(StringTag.valueOf(s3));
                tag.put("pages", listtag);
                stack.save(tag);
                stack.setTag(tag);
                ItemEntity en = new ItemEntity(MailerEntity.this.level(), MailerEntity.this.getX(), MailerEntity.this.getY(), MailerEntity.this.getZ(), stack);
                MailerEntity.this.level().addFreshEntity(en);
                MailerEntity.this.remove(RemovalReason.DISCARDED);
            }
        });
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.4));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }
}
