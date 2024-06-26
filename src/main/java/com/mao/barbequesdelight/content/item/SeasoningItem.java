package com.mao.barbequesdelight.content.item;

import com.mao.barbequesdelight.init.food.BBQSeasoning;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeasoningItem extends Item {

	private final BBQSeasoning seasoning;

	public SeasoningItem(Properties prop, BBQSeasoning seasoning) {
		super(prop);
		this.seasoning = seasoning;
	}

	public BBQSeasoning getSeasoning() {
		return seasoning;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(seasoning.getInfo().withStyle(ChatFormatting.YELLOW));
		super.appendHoverText(stack, level, list, flag);
	}

	public void sprinkle(ItemStack self, Vec3 pos, ItemStack skewer, Player player, InteractionHand hand) {
		if (!(player.level() instanceof ServerLevel sl)) return;
		skewer.getOrCreateTag().putString(BBQSkewerItem.KEY, getSeasoning().name());
		player.playSound(SoundEvents.SAND_BREAK, 1.0f, 1.0f);
		Integer color = seasoning.color.getColor();
		int col = color == null ? 0 : color;
		sl.sendParticles(new DustParticleOptions(Vec3.fromRGB24(col).toVector3f(), 1f),
				pos.x, pos.y, pos.z, 8, 0d, 0, 0d, 1d);
		self.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand));
	}

	public boolean canSprinkle(ItemStack storedStack) {
		if (storedStack.isEmpty())
			return false;
		if (!(storedStack.getItem() instanceof BBQSkewerItem))
			return false;
		return storedStack.getTag() == null || !storedStack.getTag().contains(BBQSkewerItem.KEY);
	}
}
