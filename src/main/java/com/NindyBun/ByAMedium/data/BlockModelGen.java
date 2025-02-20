package com.NindyBun.ByAMedium.data;

import com.NindyBun.ByAMedium.ByAMedium;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockModelGen extends BlockModelProvider {
    public BlockModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ByAMedium.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    }
}
