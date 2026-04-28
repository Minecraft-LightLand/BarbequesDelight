package com.mao.barbequesdelight.init.registrate;

import com.mao.barbequesdelight.content.item.BBQSandwichItem;
import com.mao.barbequesdelight.content.item.DCListStack;
import com.mao.barbequesdelight.content.item.FoodItem;
import com.mao.barbequesdelight.init.BarbequesDelight;
import com.mao.barbequesdelight.init.food.BBQSeasoning;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2core.init.reg.simple.EnumCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;

public class BBQDItems {

	public static final ItemEntry<FoodItem> BURNT_FOOD = reg("burnt_skewer",
			p -> new FoodItem(p.craftRemainder(Items.STICK).food(
					new FoodProperties.Builder().alwaysEdible().nutrition(4).saturationModifier(.2f)
							.effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 1), 1.0f)
							.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 1), 1.0f)
							.build()))).register();

	public static final ItemEntry<BBQSandwichItem> KEBAB_WRAP = reg("kebab_wrap",
			p -> new BBQSandwichItem(p.food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f)
					.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1200, 0), 0.5f)
					.build()))).register();

	public static final ItemEntry<BBQSandwichItem> KEBAB_SANDWICH = reg("kebab_sandwich",
			p -> new BBQSandwichItem(p.food(new FoodProperties.Builder().nutrition(14).saturationModifier(0.7f)
					.build()))).register();

	public static final ItemEntry<BBQSandwichItem> BIBIMBAP = reg("bibimbap",
			p -> new BBQSandwichItem(p.food(new FoodProperties.Builder().nutrition(16).saturationModifier(0.7f)
					.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 2400), 1)
					.build()))).register();

	private static final DCReg DC = DCReg.of(BarbequesDelight.REG);
	public static final DCVal<BBQSeasoning> SEASONING = DC.enumVal("seasoning", EnumCodec.of(BBQSeasoning.class, BBQSeasoning.values()));
	public static final DCVal<DCListStack> CONTENTS = DC.reg("content",
			ItemStack.OPTIONAL_CODEC.listOf().xmap(DCListStack::new, DCListStack::stack),
			ItemStack.OPTIONAL_STREAM_CODEC.<List<ItemStack>>apply(Wrappers.cast(ByteBufCodecs.list()))
					.map(DCListStack::new, DCListStack::stack), true);

	private static <T extends Item> ItemBuilder<T, ?> reg(String id, NonNullFunction<Item.Properties, T> item) {
		return BarbequesDelight.REGISTRATE.item(id, item);
	}

	public static void register() {
	}
}
