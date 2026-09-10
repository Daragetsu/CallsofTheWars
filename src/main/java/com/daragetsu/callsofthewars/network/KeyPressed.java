package com.daragetsu.callsofthewars.network;

import java.util.function.Supplier;

import com.daragetsu.callsofthewars.entities.tank.TankEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class KeyPressed {
    public KeyPressed() {}

    public KeyPressed(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if(player.getVehicle()!=null && player.getVehicle() instanceof TankEntity te){
                    te.setOpen(true);
                }
            }
        });
        return true;
    }
}
