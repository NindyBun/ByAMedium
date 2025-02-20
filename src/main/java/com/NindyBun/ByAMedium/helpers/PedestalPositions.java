package com.NindyBun.ByAMedium.helpers;

import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.ArrayList;

public class PedestalPositions {
    private final java.util.List<BlockPos> positions;

    private PedestalPositions (List<BlockPos> positions) {
        this.positions = positions;
    }

    public List<BlockPos> get(BlockPos pos) {
        return this.positions.stream().map(pos::offset).toList();
    }

    public static PedestalPositions of(List<BlockPos> positions) {
        return new PedestalPositions(positions);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<BlockPos> positions = new ArrayList<>();

        public Builder pos(int x, int y, int z) {
            this.positions.add(new BlockPos(x, y, z));
            return this;
        }

        public PedestalPositions build() {
            return new PedestalPositions(this.positions);
        }
    }
}
