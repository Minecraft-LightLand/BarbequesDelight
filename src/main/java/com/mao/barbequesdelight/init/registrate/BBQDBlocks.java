package com.mao.barbequesdelight.init.registrate;

import com.mao.barbequesdelight.content.block.*;
import com.mao.barbequesdelight.init.BarbequesDelight;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ModelFile;

public class BBQDBlocks {

	public static final BlockEntry<DelegateBlock> GRILL = BarbequesDelight.REGISTRATE.block("grill", p -> DelegateBlock.newBaseBlock(
			BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN),
			BlockProxy.HORIZONTAL,
			new GrillBlock(),
			GrillBlock.TE
	)).blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/grill")
			.parent(new ModelFile.UncheckedModelFile(BarbequesDelight.loc("custom/grill")))
			.texture("side", pvd.modLoc("block/grill_side"))
			.texture("top", pvd.modLoc("block/grill_top"))
			.renderType("cutout")
	)).simpleItem().defaultLoot().tag(BlockTags.MINEABLE_WITH_PICKAXE).register();

	public static final BlockEntityEntry<GrillBlockEntity> TE_GRILL = BarbequesDelight.REGISTRATE
			.blockEntity("grill", GrillBlockEntity::new).validBlock(GRILL)
			.renderer(() -> GrillBlockEntityRenderer::new).register();


	public static final BlockEntry<DelegateBlock> BASIN = BarbequesDelight.REGISTRATE.block("basin", p -> DelegateBlock.newBaseBlock(
			BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS),
			BlockProxy.HORIZONTAL,
			new BasinBlock(),
			BasinBlock.TE
	)).blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/basin")
			.parent(new ModelFile.UncheckedModelFile(BarbequesDelight.loc("custom/basin")))
			.renderType("cutout")
	)).simpleItem().defaultLoot().tag(BlockTags.MINEABLE_WITH_AXE)
			.lang("Ingredients Basin").register();

	public static final BlockEntityEntry<BasinBlockEntity> TE_BASIN = BarbequesDelight.REGISTRATE
			.blockEntity("basin", BasinBlockEntity::new).validBlock(BASIN)
			.renderer(() -> BasinBlockEntityRenderer::new).register();

	public static void register() {
	}

}
