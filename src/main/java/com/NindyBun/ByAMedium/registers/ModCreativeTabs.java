package com.NindyBun.ByAMedium.registers;

import com.NindyBun.ByAMedium.ByAMedium;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ByAMedium.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("byamedium_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + ByAMedium.MODID + ".creativeTab")) //The language key for the title of your CreativeModeTab
            .icon( () -> new ItemStack(ModItems.PEDESTAL_ITEM.get()))
            .displayItems((parameters, output) -> {
                ModItems.ITEMS.getEntries().forEach(item -> {
                    output.accept(item.get());
                });
            }).build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
