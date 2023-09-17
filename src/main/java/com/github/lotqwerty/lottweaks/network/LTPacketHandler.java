package com.github.lotqwerty.lottweaks.network;

import java.nio.charset.StandardCharsets;

import com.github.lotqwerty.lottweaks.AdjustRangeHelper;
import com.github.lotqwerty.lottweaks.LotTweaks;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

//https://mcforge.readthedocs.io/en/1.16.x/networking/simpleimpl/

public class LTPacketHandler {

	protected static final ResourceLocation REPLACE_PACKET_ID = new ResourceLocation(LotTweaks.MODID, "replace_packet");
	protected static final ResourceLocation ADJUSTRANGE_PACKET_ID = new ResourceLocation(LotTweaks.MODID, "adjustrange_packet");
	protected static final ResourceLocation HELLO_PACKET_ID = new ResourceLocation(LotTweaks.MODID, "hello_packet");

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(REPLACE_PACKET_ID, (server, player, handler, buf, responseSender) -> {new ReplaceMessage(buf).handle(server, player);});
		ServerPlayNetworking.registerGlobalReceiver(ADJUSTRANGE_PACKET_ID, (server, player, handler, buf, responseSender) -> {new AdjustRangeMessage(buf).handle(server, player);});
	}

	public static void sendHelloMessage(ServerPlayer player) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		new HelloMessage(LotTweaks.VERSION).toBytes(buf);
		ServerPlayNetworking.send(player, HELLO_PACKET_ID, buf);
	}

	//Replace

	public static class ReplaceMessage {

		private final BlockPos pos;
		private final BlockState state;
		private final BlockState checkState;

		public ReplaceMessage(BlockPos pos, BlockState state, BlockState checkState) {
			this.pos = pos;
			this.state = state;
			this.checkState = checkState;
		}

		public ReplaceMessage(FriendlyByteBuf buf) {
			this(buf.readBlockPos(), Block.stateById(buf.readInt()), Block.stateById(buf.readInt()));
		}

		public void toBytes(FriendlyByteBuf buf) {
			buf.writeBlockPos(this.pos);
			buf.writeInt(Block.getId(state));
			buf.writeInt(Block.getId(checkState));
		}

		@SuppressWarnings("resource")
		public void handle(MinecraftServer server, ServerPlayer player) {
			/*
			ctx.get().setPacketHandled(true);
			final ServerPlayer player = ctx.get().getSender();
			*/
			if (!player.isCreative()) {
				return;
			}
			if (player.serverLevel().isClientSide) {
				// kore iru ??
				return;
			}
			if (LotTweaks.CONFIG.REQUIRE_OP_TO_USE_REPLACE && player.getServer().getPlayerList().getOps().get(player.getGameProfile())==null) {
				return;
			}
			// validation
			if (state.getBlock() == Blocks.AIR) {
				return;
			}
			double dist = player.getEyePosition(1.0F).distanceTo(new Vec3(pos.getX(), pos.getY(), pos.getZ()));
			if (dist > LotTweaks.CONFIG.MAX_RANGE) {
				return;
			}
			if (player.serverLevel().getBlockState(pos) != checkState) {
				return;
			}
			//
			server.execute(() -> {
				player.serverLevel().setBlock(pos, state, 2);
			});
			return;
		}
	}


	// AdjustRange

	public static class AdjustRangeMessage {

		private double dist;

		public AdjustRangeMessage(double dist) {
			this.dist = dist;
		}

		public AdjustRangeMessage(FriendlyByteBuf buf) {
			this(buf.readDouble());
		}

		public void toBytes(FriendlyByteBuf buf) {
			buf.writeDouble(this.dist);
		}

		public void handle(MinecraftServer server, ServerPlayer player) {
			/*
			ctx.get().setPacketHandled(true);
			final ServerPlayer player = ctx.get().getSender();
			*/
			if (!player.isCreative()) {
				return;
			}
			server.execute(() -> {
				if (dist < 0) {
					return;
				}
				dist = Math.min(LotTweaks.CONFIG.MAX_RANGE, dist);
				AdjustRangeHelper.changeRangeModifier(player, dist);
			});
			return;
		}
	}

	// Hello

	public static class HelloMessage {

		protected String version;

		public HelloMessage(String version) {
			this.version = version;
		}

		public HelloMessage(FriendlyByteBuf buf) {
			this.version = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
		}

		public void toBytes(FriendlyByteBuf buf) {
			buf.writeInt(version.length());
			buf.writeCharSequence(version, StandardCharsets.UTF_8);
		}

		public void handle() {
			/*
			ctx.get().setPacketHandled(true);
			*/
			/*
			LotTweaksClient.setServerVersion(this.version);
			*/
		}

	}

}
