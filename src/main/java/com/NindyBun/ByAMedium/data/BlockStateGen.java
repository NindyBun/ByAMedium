package com.NindyBun.ByAMedium.data;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.registers.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStateGen extends BlockStateProvider {
    public BlockStateGen(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ByAMedium.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(ModBlocks.PEDESTAL.get(), new ModelFile.UncheckedModelFile(modLoc("block/pedestal")));
        horizontalBlock(ModBlocks.ALTER.get(), new ModelFile.UncheckedModelFile(modLoc("block/alter")));
    }
}
