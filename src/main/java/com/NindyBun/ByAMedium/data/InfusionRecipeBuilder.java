package com.NindyBun.ByAMedium.data;

import com.NindyBun.ByAMedium.crafting.InfusionInput;
import com.NindyBun.ByAMedium.crafting.InfusionRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;

public class InfusionRecipeBuilder {
    private final NonNullList<Ingredient> inputs;
    private Ingredient catalyst;
    private int health;
    private final ItemStack result;
    private final List<ICondition> conditions;

    public InfusionRecipeBuilder(ItemStack result) {
        this.inputs = NonNullList.withSize(InfusionRecipe.MAX_RECIPE_SIZE-1, Ingredient.EMPTY);
        this.result = result;
        this.catalyst = Ingredient.EMPTY;
        this.conditions = new ArrayList<>();
        this.health = 0;
    }

    public InfusionRecipeBuilder addIngredient(int index, Ingredient ingredient) {
        this.inputs.set(index, ingredient);
        return this;
    }

    public InfusionRecipeBuilder addIngredientList(List<Ingredient> ingredients) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (i < InfusionRecipe.MAX_RECIPE_SIZE-1) {
                this.inputs.set(i, ingredients.get(i));
            }
        }
        return this;
    }

    public InfusionRecipeBuilder addCondition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public InfusionRecipeBuilder setHealth(int health) {
        this.health = health;
        return this;
    }

    public InfusionRecipeBuilder setCatalyst(Ingredient catalyst) {
        this.catalyst = catalyst;
        return this;
    }

    public void build(RecipeOutput consumer, ResourceLocation id) {
        consumer.accept(id, new InfusionRecipe(this.health, this.catalyst, this.inputs, this.result), null, this.conditions.toArray(new ICondition[0]));
    }


}
