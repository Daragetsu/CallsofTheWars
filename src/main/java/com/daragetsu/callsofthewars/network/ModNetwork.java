package com.daragetsu.callsofthewars.network;

import com.daragetsu.callsofthewars.CallsofTheWars;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(CallsofTheWars.MOD_ID, "network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(KeyPressed.class, id())
                .encoder(KeyPressed::toBytes)
                .decoder(KeyPressed::new)
                .consumerMainThread(KeyPressed::handle)
                .add();
    }

    // Helper method to send packet from client to server
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

}
