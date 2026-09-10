package com.daragetsu.callsofthewars.client;

import org.lwjgl.glfw.GLFW;

import com.daragetsu.callsofthewars.CallsofTheWars;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBinds {
    public static final String KEY_CATEGORY = "key.categories."+CallsofTheWars.MOD_ID;
    public static final String KEY_SAMPLE = "key."+CallsofTheWars.MOD_ID+".open_tank_window";

    public static final KeyMapping OPEN_TANK_WINDOW_KEY = new KeyMapping(
        "key."+CallsofTheWars.MOD_ID+".open_tank_window",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_O,
        KEY_CATEGORY
    );
}