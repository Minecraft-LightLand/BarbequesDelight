package com.mao.barbequesdelight.init.data;

import com.mao.barbequesdelight.content.recipe.CombineItemRecipeBuilder;
import com.mao.barbequesdelight.content.recipe.GrillingRecipeBuilder;
import com.mao.barbequesdelight.content.recipe.SkeweringRecipeBuilder;
import com.mao.barbequesdelight.init.food.BBQSkewers;
import com.mao.barbequesdelight.init.registrate.BBQDBlocks;
import com.mao.barbequesdelight.init.registrate.BBQDItems;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;

import java.util.function.BiFunction;

public class BBQRecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		pvd.stonecutting(DataIngredient.tag(ItemTags.LOGS), RecipeCategory.MISC, BBQDBlocks.TRAY, 4);
		pvd.stonecutting(DataIngredient.tag(ItemTags.LOGS), RecipeCategory.MISC, BBQDBlocks.BASIN, 2);

		grillSkewer(pvd, BBQSkewers.COD, 6);
		grillSkewer(pvd, BBQSkewers.SALMON, 6);
		grillSkewer(pvd, BBQSkewers.CHICKEN, 7);
		grillSkewer(pvd, BBQSkewers.RABBIT, 7);
		grillSkewer(pvd, BBQSkewers.LAMB, 8);
		grillSkewer(pvd, BBQSkewers.PORK_SAUSAGE, 9);
		grillSkewer(pvd, BBQSkewers.POTATO, 7);
		grillSkewer(pvd, BBQSkewers.BEEF, 8);
		grillSkewer(pvd, BBQSkewers.MUSHROOM, 6);
		grillSkewer(pvd, BBQSkewers.VEGETABLE, 4);

		craftSkewer(pvd, BBQSkewers.COD, Ingredient.of(CommonTags.Items.RAW_FISHES_COD), Ingredient.of(CommonTags.Items.VEGETABLES_TOMATO));
		craftSkewer(pvd, BBQSkewers.SALMON, Ingredient.of(CommonTags.Items.RAW_FISHES_SALMON), Ingredient.of(CommonTags.Items.VEGETABLES_TOMATO));
		craftSkewer(pvd, BBQSkewers.CHICKEN, Ingredient.of(CommonTags.Items.RAW_CHICKEN), Ingredient.of(CommonTags.Items.VEGETABLES_ONION));
		craftSkewer(pvd, BBQSkewers.RABBIT, Ingredient.of(Items.RABBIT), Ingredient.of(CommonTags.Items.SALAD_INGREDIENTS_CABBAGE));
		craftSkewer(pvd, BBQSkewers.LAMB, Ingredient.of(CommonTags.Items.RAW_MUTTON));
		craftSkewer(pvd, BBQSkewers.PORK_SAUSAGE, Ingredient.of(CommonTags.Items.RAW_PORK));
		craftSkewer(pvd, BBQSkewers.POTATO, Ingredient.of(Items.POTATO), Ingredient.of(CommonTags.Items.RAW_BACON));
		craftSkewer(pvd, BBQSkewers.MUSHROOM, Ingredient.of(Items.BROWN_MUSHROOM));
		craftSkewer(pvd, BBQSkewers.BEEF, Ingredient.of(CommonTags.Items.RAW_BEEF), Ingredient.of(CommonTags.Items.SALAD_INGREDIENTS_CABBAGE));
		craftSkewer(pvd, BBQSkewers.VEGETABLE, Ingredient.of(BBQTagGen.VEGETABLE), Ingredient.of(BBQTagGen.FRUITS));

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BBQDBlocks.GRILL.get())::unlockedBy, Items.IRON_INGOT)
				.pattern("ICI").pattern("I I").pattern("I I")
				.define('I', Items.IRON_INGOT)
				.define('C', Items.IRON_TRAPDOOR)
				.save(pvd);

		unlock(pvd, new CombineItemRecipeBuilder(BBQDItems.BIBIMBAP.get(), 1)::unlockedBy, ModItems.COOKED_RICE.get())
				.requires(ModItems.COOKED_RICE.get())
				.requires(CommonTags.Items.VEGETABLES_ONION)
				.requires(CommonTags.Items.VEGETABLES_CARROT)
				.requires(BBQTagGen.GRILLED_SKEWERS)
				.requires(BBQTagGen.GRILLED_SKEWERS)
				.requires(BBQTagGen.GRILLED_SKEWERS)
				.save(pvd);

		unlock(pvd, new CombineItemRecipeBuilder(BBQDItems.KEBAB_SANDWICH.get(), 1)::unlockedBy, Items.BREAD)
				.requires(CommonTags.Items.BREAD).requires(BBQTagGen.GRILLED_SKEWERS).requires(BBQTagGen.GRILLED_SKEWERS)
				.requires(CommonTags.Items.SALAD_INGREDIENTS_CABBAGE).requires(CommonTags.Items.VEGETABLES_ONION)
				.save(pvd);

		unlock(pvd, new CombineItemRecipeBuilder(BBQDItems.KEBAB_WRAP.get(), 1)::unlockedBy, Items.BREAD)
				.requires(CommonTags.Items.BREAD).requires(BBQTagGen.GRILLED_SKEWERS).requires(CommonTags.Items.SALAD_INGREDIENTS_CABBAGE)
				.save(pvd);

	}

	private static void grillSkewer(RegistrateRecipeProvider pvd, BBQSkewers skewer, int time) {
		unlock(pvd, new GrillingRecipeBuilder(Ingredient.of(skewer.item.get()), skewer.skewer.asStack(), time * 40)::unlockedBy, skewer.item.get())
				.save(pvd, skewer.skewer.getId());
	}

	private static void craftSkewer(RegistrateRecipeProvider pvd, BBQSkewers skewer, Ingredient main) {
		unlock(pvd, new SkeweringRecipeBuilder(Ingredient.of(Items.STICK), main, 4, skewer.item.asStack())::unlockedBy, Items.STICK)
				.save(pvd, skewer.item.getId());
	}

	private static void craftSkewer(RegistrateRecipeProvider pvd, BBQSkewers skewer, Ingredient main, Ingredient side) {
		unlock(pvd, new SkeweringRecipeBuilder(Ingredient.of(Items.STICK), main, 2, side, 2, skewer.item.asStack())::unlockedBy, Items.STICK)
				.save(pvd, skewer.item.getId());
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}


}
