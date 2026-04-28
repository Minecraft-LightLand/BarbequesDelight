package com.mao.barbequesdelight.content.item;

import com.mao.barbequesdelight.init.registrate.BBQDItems;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BBQSandwichItem extends FoodItem {

	public static final String KEY = "Skewers";

	public static DCListStack getSkewers(ItemStack stack) {
		return BBQDItems.CONTENTS.getOrDefault(stack, DCListStack.EMPTY);
	}

	public BBQSandwichItem(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var skewers = getSkewers(stack);
		FoodProperties old = super.getFoodProperties(stack, entity);
		if (old == null || skewers.stack().isEmpty()) return old;
		FoodProperties.Builder builder = new FoodProperties.Builder();
		builder.nutrition(old.nutrition());
		builder.saturationModifier(old.saturation() / old.nutrition() / 2);
		if (old.eatDurationTicks() < 20) builder.fast();
		if (old.canAlwaysEat()) builder.alwaysEdible();
		Set<Holder<MobEffect>> set = new HashSet<>();
		var list = new ArrayList<ItemStack>();
		list.add(getDefaultInstance());
		list.addAll(skewers.stack());
		for (var e : list) {
			FoodProperties x = e.getFoodProperties(entity);
			if (x == null) continue;
			for (var eff : x.effects()) {
				var ins = eff.effect();
				if (set.contains(ins.getEffect()))
					continue;
				set.add(eff.effect().getEffect());
				builder.effect(eff::effect, eff.probability());
			}
			if (x.eatDurationTicks() < 20) builder.fast();
			if (x.canAlwaysEat()) builder.alwaysEdible();
		}
		return builder.build();
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		var skewers = getSkewers(stack);
		for (var e : skewers.stack()) {
			if (e.getItem() instanceof BBQSkewerItem item) {
				item.applyEffects(e, entity);
			}
		}
		return super.finishUsingItem(stack, level, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var skewers = getSkewers(stack);
		for (var e : skewers.stack()) {
			list.add(e.getHoverName());
		}
		super.appendHoverText(stack, level, list, flag);
	}

}
