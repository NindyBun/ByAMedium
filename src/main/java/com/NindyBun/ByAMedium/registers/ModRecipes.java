package com.NindyBun.ByAMedium.registers;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.crafting.InfusionRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ByAMedium.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ByAMedium.MODID);

    public static final Supplier<RecipeType<InfusionRecipe>> INFUSION_RECIPE = RECIPE_TYPES.register("infusion",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(ByAMedium.MODID, "infusion")));

    public static final Supplier<RecipeSerializer<?>> INFUSION_SERIALIZER = RECIPE_SERIALIZERS.register("infusion",
            InfusionRecipe.Serializer::new);

    public static void register(IEventBus event) {
        RECIPE_TYPES.register(event);
        RECIPE_SERIALIZERS.register(event);
    }


}
