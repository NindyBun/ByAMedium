package com.NindyBun.ByAMedium.data;

import com.NindyBun.ByAMedium.ByAMedium;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeGen extends RecipeProvider {
    public RecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        new InfusionRecipeBuilder(Items.DIAMOND_BLOCK.getDefaultInstance())
                .setCatalyst(Ingredient.of(Items.COAL_BLOCK))
                .setHealth(10)
                .addIngredientList(List.<Ingredient>of(
                        Ingredient.of(Items.OBSIDIAN),
                        Ingredient.of(Items.OBSIDIAN),
                        Ingredient.of(Items.OBSIDIAN),
                        Ingredient.of(Items.OBSIDIAN),
                        Ingredient.of(Items.MAGMA_BLOCK),
                        Ingredient.of(Items.MAGMA_BLOCK),
                        Ingredient.of(Items.MAGMA_BLOCK),
                        Ingredient.of(Items.MAGMA_BLOCK)
                ))
                .build(recipeOutput, ResourceLocation.fromNamespaceAndPath("diamond_block", ByAMedium.MODID));


    }
}
