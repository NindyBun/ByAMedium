package com.NindyBun.ByAMedium.registers;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.blockEntities.AlterBE;
import com.NindyBun.ByAMedium.blockEntities.PedestalBE;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ByAMedium.MODID);

    public static final Supplier<BlockEntityType<PedestalBE>> PEDESTAL_BE = BLOCK_ENTITY.register("pedestal",
            () -> BlockEntityType.Builder.of(PedestalBE::new, ModBlocks.PEDESTAL.get()).build(null));

    public static final Supplier<BlockEntityType<AlterBE>> ALTER_BE = BLOCK_ENTITY.register("alter",
            () -> BlockEntityType.Builder.of(AlterBE::new, ModBlocks.ALTER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY.register(eventBus);
    }
}
