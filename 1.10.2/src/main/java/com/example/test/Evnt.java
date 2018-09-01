package com.example.test;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Evnt {
	@SubscribeEvent
	public void onAttack(final AttackEntityEvent event) {
		final ItemStack mainhand = event.getEntityPlayer().getHeldItemMainhand();
		if (mainhand==null)
			FMLLog.info("sushi");
	}

	@SubscribeEvent
	public void onHurt(final LivingHurtEvent event) {

	}
}
