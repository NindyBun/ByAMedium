package com.NindyBun.ByAMedium.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record InfusionInput(NonNullList<ItemStack> inputs) implements RecipeInput {

    @Override
    public ItemStack getItem(int i) {
        return this.inputs.get(i);
    }

    @Override
    public int size() {
        return this.inputs.size();
    }

}
