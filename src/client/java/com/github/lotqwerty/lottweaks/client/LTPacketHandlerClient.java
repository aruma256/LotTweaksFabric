package com.github.lotqwerty.lottweaks.client;

import com.github.lotqwerty.lottweaks.network.LTPacketHandler;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler.HelloMessage;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class LTPacketHandlerClient extends LTPacketHandler {

	protected static void initClient() {
		ClientPlayNetworking.registerGlobalReceiver(HELLO_PACKET_ID, (client, handler, buf, responseSender) -> {new HelloMessageClient(buf).handle();});
	}

	public static void sendReplaceMessage(BlockPos pos, BlockState state, BlockState checkState) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		new ReplaceMessage(pos, state, checkState).toBytes(buf);
		ClientPlayNetworking.send(REPLACE_PACKET_ID, buf);
	}

	public static void sendReachRangeMessage(double dist) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		new AdjustRangeMessage(dist).toBytes(buf);
		ClientPlayNetworking.send(ADJUSTRANGE_PACKET_ID, buf);
	}

	public static class HelloMessageClient extends HelloMessage {

		public HelloMessageClient(FriendlyByteBuf buf) {
			super(buf);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handle() {
			LotTweaksClient.setServerVersion(this.version);
		}
	}
}
