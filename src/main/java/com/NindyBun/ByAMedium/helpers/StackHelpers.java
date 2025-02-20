package com.NindyBun.ByAMedium.helpers;

import net.minecraft.world.item.ItemStack;

public class StackHelpers {

    public static ItemStack withSize(ItemStack stack, int size, boolean container) {
        if (size <= 0) {
            if (container && stack.hasCraftingRemainingItem()) {
                return stack.getCraftingRemainingItem();
            } else {
                return ItemStack.EMPTY;
            }
        }

        stack = stack.copy();
        stack.setCount(size);

        return stack;
    }

    public static ItemStack grow(ItemStack stack, int amount) {
        return withSize(stack, stack.getCount() + amount, false);
    }

    public static ItemStack shrink(ItemStack stack, int amount, boolean container) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        return withSize(stack, stack.getCount() - amount, container);
    }
}
