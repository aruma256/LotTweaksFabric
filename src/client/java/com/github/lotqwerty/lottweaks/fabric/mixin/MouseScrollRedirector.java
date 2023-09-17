package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.lotqwerty.lottweaks.fabric.ScrollEvent;

import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;

@Mixin(MouseHandler.class)
public abstract class MouseScrollRedirector {

	@Redirect(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V"))
	private void lottweaks_redirectedOnScroll(Inventory playerInventory, double scrollAmount) {
		if (ScrollEvent.post(new ScrollEvent(scrollAmount))) {
			return;
		}
		playerInventory.swapPaint(scrollAmount);
	}

}
