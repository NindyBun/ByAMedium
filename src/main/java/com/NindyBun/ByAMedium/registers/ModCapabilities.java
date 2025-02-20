package com.NindyBun.ByAMedium.registers;

import com.NindyBun.ByAMedium.blockEntities.AlterBE;
import com.NindyBun.ByAMedium.blockEntities.PedestalBE;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ModCapabilities {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, blockEntity, context) -> ((PedestalBE) blockEntity).handler,
                ModBlocks.PEDESTAL.get()
        );

        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, blockEntity, context) -> ((AlterBE) blockEntity).handler,
                ModBlocks.ALTER.get()
        );
    }
}
