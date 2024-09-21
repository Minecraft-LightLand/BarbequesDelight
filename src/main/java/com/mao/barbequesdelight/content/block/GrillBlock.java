package com.mao.barbequesdelight.content.block;

import com.mao.barbequesdelight.content.item.SeasoningItem;
import com.mao.barbequesdelight.init.registrate.BBQDBlocks;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.AnimateTickBlockMethod;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class GrillBlock implements ShapeBlockMethod, AnimateTickBlockMethod, UseWithoutItemBlockMethod, UseItemOnBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(BBQDBlocks.TE_GRILL, GrillBlockEntity.class);

	public static final VoxelShape OUTER = Block.box(1, 12, 1, 15, 15, 15);
	public static final VoxelShape SHAPE = Shapes.or(
			Block.box(15, 0, 0, 16, 10, 1),
			Block.box(15, 0, 15, 16, 10, 16),
			Block.box(0, 0, 15, 1, 10, 16),
			Block.box(0, 0, 0, 1, 10, 1),
			Block.box(0, 10, 15, 16, 16, 16),
			Block.box(0, 10, 0, 16, 16, 1),
			Block.box(0, 10, 1, 1, 16, 15),
			Block.box(15, 10, 1, 16, 16, 15),
			Block.box(1, 12, 1, 15, 15, 15)
	);

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (level.getBlockEntity(pos) instanceof GrillBlockEntity grill) {
			if (grill.isBarbecuing()) {
				double x = (double) pos.getX() + 0.5;
				double y = pos.getY();
				double z = (double) pos.getZ() + 0.5;
				if (random.nextInt(8) == 0) {
					level.playLocalSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundSource.BLOCKS,
							0.4F, random.nextFloat() * 0.2F + 0.9F, false);
				}
			}
		}
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof GrillBlockEntity grill)) {
			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		}
		int i = grill.getSlotForHitting(hit, level);
		if (i < 0 || i >= grill.size()) return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		ItemStack item = grill.getStack(i);
		if (stack.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (item.isEmpty()) {
			return grill.addItem(i, stack) ? ItemInteractionResult.SUCCESS : ItemInteractionResult.FAIL;
		} else if (stack.getItem() instanceof SeasoningItem seasoning && seasoning.canSprinkle(item)) {
			seasoning.sprinkle(stack, hit.getLocation(), item, player, hand);
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof GrillBlockEntity grill)) {
			return InteractionResult.PASS;
		}
		int i = grill.getSlotForHitting(hit, level);
		if (i < 0 || i >= grill.size()) return InteractionResult.PASS;
		ItemStack item = grill.getStack(i);
		if (player.isShiftKeyDown()) {
			return grill.flip(i) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		} else {
			if (!level.isClientSide()) {
				ItemStack ret = item.split(1);
				if (player.getMainHandItem().isEmpty()) {
					player.setItemInHand(InteractionHand.MAIN_HAND, ret);
				} else player.getInventory().placeItemBackInInventory(ret);
				grill.inventoryChanged();
			}
			return InteractionResult.SUCCESS;
		}
	}

}
