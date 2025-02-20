package com.NindyBun.ByAMedium.crafting;

import com.NindyBun.ByAMedium.registers.ModRecipes;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

public class InfusionRecipe implements Recipe<InfusionInput> {
    public static final int MAX_RECIPE_SIZE = 9;
    private final Ingredient catalyst;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack result;
    private final int health;

    public InfusionRecipe(int health, Ingredient catalyst, NonNullList<Ingredient> inputs, ItemStack result) {
        this.catalyst = catalyst;
        this.inputs = inputs;
        this.result = result;
        this.health = health;
    }

    @Override
    public boolean matches(InfusionInput craftingInput, Level level) {
        if (this.inputs.size() != craftingInput.size() - 1)
            return false;

        var input = craftingInput.getItem(0);
        if (!this.catalyst.test(input))
            return false;

        NonNullList<ItemStack> inputs = NonNullList.create();

        for (int i = 1; i < craftingInput.size(); i++) {
            ItemStack item = craftingInput.getItem(i);
            if (!item.isEmpty()) {
                inputs.add(item);
            }
        }

        return RecipeMatcher.findMatches(inputs, this.inputs) != null;
    }

    @Override
    public ItemStack assemble(InfusionInput craftingInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.INFUSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.INFUSION_RECIPE.get();
    }

    public Ingredient getCatalyst() {
        return this.catalyst;
    }

    public NonNullList<Ingredient> getInputs() {
        return this.inputs;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getHealth() {
        return this.health;
    }

    public static class Serializer implements RecipeSerializer<InfusionRecipe> {
        public static final MapCodec<InfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        ExtraCodecs.POSITIVE_INT.fieldOf("health").forGetter(InfusionRecipe::getHealth),
                        Ingredient.CODEC_NONEMPTY.fieldOf("catalyst").forGetter(InfusionRecipe::getCatalyst),
                        Ingredient.CODEC_NONEMPTY
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            var max = 8;
                                            var ingredients = field.toArray(Ingredient[]::new);
                                            if (ingredients.length == 0) {
                                                return DataResult.error(() -> "No ingredients for infusion recipe");
                                            } else {
                                                if (ingredients.length > max) {
                                                    return DataResult.error(() -> "Too many ingredients for infusion recipe. The maximum is: %s".formatted(max));
                                                }

                                                var result = NonNullList.withSize(max, Ingredient.EMPTY);

                                                for (int i = 0; i < ingredients.length; i++) {
                                                    result.set(i, ingredients[i]);
                                                }

                                                return DataResult.success(result);
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(InfusionRecipe::getInputs),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(InfusionRecipe::getResult)
                ).apply(builder, InfusionRecipe::new)
        );


        public static final StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> STREAM_CODEC = StreamCodec.of(
                InfusionRecipe.Serializer::toNetwork, InfusionRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<InfusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static InfusionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient catalyst = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int size = buffer.readVarInt();
            int health = buffer.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);

            for (int i = 0; i < size; i++) {
                inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            }

            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);

            return new InfusionRecipe(health, catalyst, inputs, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, InfusionRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.catalyst);
            buffer.writeVarInt(recipe.inputs.size());
            buffer.writeVarInt(recipe.health);

            for (Ingredient ingredient : recipe.inputs) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }
}
