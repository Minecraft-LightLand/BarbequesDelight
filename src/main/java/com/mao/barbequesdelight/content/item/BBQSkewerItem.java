package com.mao.barbequesdelight.content.item;

import com.mao.barbequesdelight.init.food.BBQSeasoning;
import com.mao.barbequesdelight.init.registrate.BBQDItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BBQSkewerItem extends FoodItem {

	@Nullable
	public static BBQSeasoning getSeasoning(ItemStack stack) {
		return BBQDItems.SEASONING.get(stack);
	}

	public BBQSkewerItem(Properties prop) {
		super(prop);
	}

	@Override
	public Component getName(ItemStack stack) {
		BBQSeasoning seasoning = getSeasoning(stack);
		if (seasoning != null)
			return seasoning.getTitle().append(super.getName(stack).copy().withStyle(seasoning.color));
		return super.getName(stack);
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var ans = super.getFoodProperties(stack, entity);
		if (ans == null || stack.isComponentsPatchEmpty()) return ans;
		BBQSeasoning seasoning = getSeasoning(stack);
		if (seasoning == null) return ans;
		FoodProperties.Builder builder = new FoodProperties.Builder()
				.nutrition(ans.nutrition())
				.saturationModifier(ans.saturation() / ans.nutrition() / 2);
		for (var e : ans.effects()) {
			seasoning.appendEffect(builder, e.effect(), e.probability());
		}
		if (ans.eatDurationTicks() < 20) builder.fast();
		if (ans.canAlwaysEat()) builder.alwaysEdible();
		seasoning.modify(builder);
		return builder.build();
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
		applyEffects(stack, consumer);
		ItemStack remain = stack.getCraftingRemainingItem();
		ItemStack ans = super.finishUsingItem(stack, level, consumer);
		if (remain.isEmpty()) {
			return ans;
		}
		if (ans.isEmpty()) {
			return remain;
		}
		if (consumer instanceof Player player && !player.isCreative()) {
			player.getInventory().placeItemBackInInventory(remain);
		}
		return ans;
	}

	protected void applyEffects(ItemStack stack, LivingEntity consumer) {
		BBQSeasoning seasoning = getSeasoning(stack);
		if (seasoning != null && consumer instanceof Player player) {
			seasoning.onFinish(player);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		BBQSeasoning seasoning = getSeasoning(stack);
		if (seasoning != null)
			list.add(seasoning.getTitle());
		super.appendHoverText(stack, level, list, flag);
	}

}
