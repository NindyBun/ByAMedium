package com.NindyBun.ByAMedium.registers;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.items.SacrificialDagger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, ByAMedium.MODID);

    public static final Supplier<BlockItem> PEDESTAL_ITEM = ITEMS.register("pedestal", () -> new BlockItem(ModBlocks.PEDESTAL.get(), new Item.Properties().stacksTo(64)));

    public static final Supplier<BlockItem> ALTER_ITEM = ITEMS.register("alter", () -> new BlockItem(ModBlocks.ALTER.get(), new Item.Properties().stacksTo(64)));

    public static final Supplier<Item> SACRIFICIAL_DAGGER = ITEMS.register("sacrificial-dagger", SacrificialDagger::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
