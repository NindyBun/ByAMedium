package com.NindyBun.ByAMedium.inventories;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.blockEntities.PedestalBE;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;

public class PedestalHandler extends SingleItemStackHandler {
    private final PedestalBE blockEntity;

    public PedestalHandler(PedestalBE blockEntity) {
        super(1);
        this.blockEntity = blockEntity;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (blockEntity != null) {
            //blockEntity.getLevel().setBlockAndUpdate(blockEntity.getBlockPos(), blockEntity.getBlockState());
            blockEntity.setChanged();

            if (blockEntity.hasLevel() && !blockEntity.getLevel().isClientSide) {
                ServerLevel serverLevel = (ServerLevel) blockEntity.getLevel();
                LevelChunk chunk = serverLevel.getChunk(blockEntity.getBlockPos().getX() >> 4, blockEntity.getBlockPos().getZ() >> 4);

                serverLevel.getChunkSource().chunkMap.getPlayers(chunk.getPos(), false).forEach(e -> {
                    e.connection.send(blockEntity.getUpdatePacket());
                });
            }
        }
    }
}
