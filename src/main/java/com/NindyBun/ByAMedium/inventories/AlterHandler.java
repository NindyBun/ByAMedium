package com.NindyBun.ByAMedium.inventories;

import com.NindyBun.ByAMedium.blockEntities.AlterBE;
import com.NindyBun.ByAMedium.blockEntities.PedestalBE;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;

public class AlterHandler extends SingleItemStackHandler {
    private final AlterBE blockEntity;

    public AlterHandler(AlterBE blockEntity) {
        super(2);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 0 && this.getStackInSlot(1).isEmpty()) {
            return true;
        } else if (slot == 1 && this.getStackInSlot(0).isEmpty()) {
            return true;
        }
        return false;
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
