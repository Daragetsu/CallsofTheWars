package com.daragetsu.callsofthewars.entities.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.ChatFormatting;

public interface VariantEntity {
    int getVariant();

    public static enum Variants {
        Red(0),
        Green(1),
        Blue(2);
        private final int v;
        private Variants(int v) {
            this.v = v;
        }
        public int get(){return this.v;};
    }

    public static Map<ChatFormatting, Variants> VariantMap = new HashMap<>(Map.of(
        ChatFormatting.RED, Variants.Red,
        ChatFormatting.GREEN, Variants.Green,
        ChatFormatting.BLUE, Variants.Blue
    ));
}