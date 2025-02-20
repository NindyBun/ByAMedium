package com.NindyBun.ByAMedium.registers;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.blocks.Alter;
import com.NindyBun.ByAMedium.blocks.Pedestal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, ByAMedium.MODID);

    public static final Supplier<Block> PEDESTAL = BLOCKS.register("pedestal",
            Pedestal::new
    );

    public static final Supplier<Block> ALTER = BLOCKS.register("alter",
            Alter::new
    );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
