package com.mao.barbequesdelight.init.data;

import com.mao.barbequesdelight.init.BarbequesDelight;
import com.mao.barbequesdelight.init.registrate.BBQDBlocks;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.tag.ForgeTags;

public class BBQTagGen {

	public static final TagKey<Item> GRILLED_SKEWERS = ItemTags.create(BarbequesDelight.loc("grilled_skewers"));
	public static final TagKey<Item> RAW_SKEWERS = ItemTags.create(BarbequesDelight.loc("raw_skewers"));

	public static final TagKey<Item> VEGETABLE = ItemTags.create(BarbequesDelight.loc("skewer_vegetables"));
	public static final TagKey<Item> FRUITS = ItemTags.create(BarbequesDelight.loc("skewer_fruits"));

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(BlockTags.create(new ResourceLocation("carryon", "block_blacklist")))
				.add(BBQDBlocks.GRILL.get(), BBQDBlocks.BASIN.get());
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(ForgeTags.VEGETABLES_POTATO);
		pvd.addTag(ForgeTags.VEGETABLES_TOMATO);
		pvd.addTag(ForgeTags.VEGETABLES_CARROT);
		pvd.addTag(ForgeTags.VEGETABLES_ONION);
		pvd.addTag(VEGETABLE).addTags(
				ForgeTags.VEGETABLES_POTATO,
				ForgeTags.VEGETABLES_TOMATO,
				ForgeTags.VEGETABLES_CARROT,
				ForgeTags.VEGETABLES_ONION,
				Tags.Items.MUSHROOMS
		);
		var fruits = ItemTags.create(new ResourceLocation("forge", "fruits"));
		pvd.addTag(fruits);
		pvd.addTag(ForgeTags.BERRIES);
		pvd.addTag(FRUITS).addTags(
				fruits,
				ForgeTags.BERRIES
		).add(Items.APPLE, Items.MELON_SLICE);
	}

}
