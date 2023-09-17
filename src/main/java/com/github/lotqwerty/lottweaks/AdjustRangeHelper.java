package com.github.lotqwerty.lottweaks;

import java.util.UUID;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public class AdjustRangeHelper implements ServerTickEvents.StartTick {

	private static final UUID _UUID = new UUID(2457550121339451521L, 1595282694073824061L);
	private static final String NAME = LotTweaks.MODID + "v2";

	/*
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent event) {
		removeOldRangeModifiers(event.getEntity());
	}
	*/

	@Override
	public void onStartTick(MinecraftServer server) {
		for (Player player: server.getPlayerList().getPlayers()) {
			if (!player.isCreative()) {
				clearRangeModifier(player);
			}
		}
	}

	/*
	//Just remove all of them!
	public static void removeOldRangeModifiers(Player player) {
		AttributeInstance instance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		for (AttributeModifier modifier: instance.getModifiers()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
	}
	*/

	private static void clearRangeModifier(Player player) {
		player.getAttribute(ReachEntityAttributes.REACH).removeModifier(_UUID);
	}

	public static void changeRangeModifier(Player player, double dist) {
		clearRangeModifier(player);
		AttributeInstance instance = player.getAttribute(ReachEntityAttributes.REACH);
		instance.addPermanentModifier(new AttributeModifier(_UUID, NAME, dist - 5 + 0.5, AttributeModifier.Operation.ADDITION));
	}

}
