package com.daragetsu.callsofthewars.data;

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

public class ConflictZonesDataManager extends SimpleJsonResourceReloadListener{
    private static final Gson GSON = new Gson();
    public static ArrayList<ResourceLocation> CONFLICT_ZONES = new ArrayList<>();
    public ConflictZonesDataManager() {
        super(GSON, "conflict_zones");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
        CONFLICT_ZONES.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            JsonObject json = GsonHelper.convertToJsonObject(entry.getValue(), entry.getKey().toString());

            JsonArray zonesArray = GsonHelper.getAsJsonArray(json, "zones");

            for (JsonElement element : zonesArray) {
                CONFLICT_ZONES.add(
                        ResourceLocation.bySeparator(element.getAsString(), ':')
                );
            }
        }
    }
}