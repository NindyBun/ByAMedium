package com.NindyBun.ByAMedium.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CachedRecipe<T extends Recipe<InfusionInput>> {
    private final RecipeType<T> type;
    private Optional<RecipeHolder<T>> recipe = Optional.empty();

    public CachedRecipe(RecipeType<T> type) {
        this.type = type;
    }

    public boolean check(InfusionInput input, Level level) {
        if (this.recipe.isPresent() && this.recipe.map(RecipeHolder::value)
                    .map(e -> e.matches(input, level)).get()) {
            return true;
        }

        this.recipe = level.getRecipeManager().getRecipeFor(this.type, input, level);

        return this.recipe.isPresent();
    }

    public boolean check(NonNullList<ItemStack> inputs, Level level) {
        return this.check(new InfusionInput(inputs), level);
    }

    public boolean exists() {
        return this.recipe.isPresent();
    }

    public T get() {
        return this.recipe.map(RecipeHolder::value).get();
    }

    public T checkAndGet(InfusionInput input, Level level) {
        if (this.check(input, level)) {
            return this.recipe.map(RecipeHolder::value).get();
        }
        return null;
    }

    public T checkAndGet(NonNullList<ItemStack> inputs, Level level) {
        if (this.check(new InfusionInput(inputs), level)) {
            return this.recipe.map(RecipeHolder::value).get();
        }
        return null;
    }


}
