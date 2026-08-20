package com.daragetsu.callsofthewars.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class BeltItem extends Item{

    public BeltItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents,
            TooltipFlag isAdvanced) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(this);
        if (loc == null) return;
        tooltipComponents.add(Component.translatable("item." + loc.getNamespace() + "." + loc.getPath() + ".description").withStyle(ChatFormatting.GRAY));
    }
}
