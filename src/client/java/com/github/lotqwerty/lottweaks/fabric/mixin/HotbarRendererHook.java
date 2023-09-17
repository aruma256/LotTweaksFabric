package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(Gui.class)
public abstract class HotbarRendererHook {

	@Inject(at = @At("TAIL"), method = "renderHotbar(FLnet/minecraft/client/gui/GuiGraphics;)V")
	private void lottweaks_renderHotbar(float tickDelta, GuiGraphics guiGraphics, CallbackInfo info) {
		RenderHotbarEvent.post(guiGraphics, tickDelta);
	}

}
