package com.NindyBun.ByAMedium.data;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.registers.ModBlocks;
import com.NindyBun.ByAMedium.registers.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LangGen extends LanguageProvider {
    public LangGen(PackOutput output, String locale) {
        super(output, ByAMedium.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup."+ByAMedium.MODID+".creativeTab", "By a Medium");

        add(ModBlocks.PEDESTAL.get(), "Pedestal");
        add(ModBlocks.ALTER.get(), "Alter");

        add(ModItems.SACRIFICIAL_DAGGER.get(), "Sacrificial Dagger");
    }
}
