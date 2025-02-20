package com.NindyBun.ByAMedium.data;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.registers.ModBlocks;
import com.NindyBun.ByAMedium.registers.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ItemModelGen extends ItemModelProvider {
    public ItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ByAMedium.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerBlockModel(ModBlocks.PEDESTAL);
        registerBlockModel(ModBlocks.ALTER);

        registerBasicItem(ModItems.SACRIFICIAL_DAGGER);
    }

    private void registerBasicItem(Supplier<?> item) {
        String path = ((DeferredHolder<?, ?>)item).getId().getPath();
        singleTexture(path, mcLoc("item/handheld"), "layer0", ResourceLocation.fromNamespaceAndPath(ByAMedium.MODID, "item/"+path));
    }

    private void registerBlockModel(Supplier<?> block) {
        String path = ((DeferredHolder<?, ?>) block).getId().getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
    }
}
