package com.NindyBun.ByAMedium.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Generator {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new LangGen(output, "en_us"));
        generator.addProvider(event.includeClient(), new ItemModelGen(output, helper));
        generator.addProvider(event.includeClient(), new BlockModelGen(output, helper));

        generator.addProvider(event.includeServer(), new RecipeGen(output, provider));
        generator.addProvider(event.includeServer(), new BlockStateGen(output, helper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(BlockLootGen::new, LootContextParamSets.BLOCK)), provider));


        BlockTagGen blockTagGen = new BlockTagGen(output, provider, helper);
        generator.addProvider(event.includeServer(), blockTagGen);

    }
}
