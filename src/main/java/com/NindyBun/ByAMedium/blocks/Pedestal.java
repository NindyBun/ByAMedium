package com.NindyBun.ByAMedium.blocks;

import com.NindyBun.ByAMedium.blockEntities.PedestalBE;
import com.NindyBun.ByAMedium.helpers.StackHelpers;
import com.NindyBun.ByAMedium.inventories.PedestalHandler;
import com.NindyBun.ByAMedium.inventories.SingleItemStackHandler;
import com.NindyBun.ByAMedium.registers.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class Pedestal extends Block implements EntityBlock {
    public static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public Pedestal() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.PEDESTAL_BE.get().create(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shape.getFromFacing(state.getValue(HorizontalDirectionalBlock.FACING));
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof PedestalBE ped) {
            PedestalHandler handler = ped.handler;//.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, ped.getBlockPos(), ped.getBlockState(), ped, null);
            if (handler == null) {
                return ItemInteractionResult.FAIL;
            }
            ItemStack set = handler.getStackInSlot(0);
            ItemStack held = player.getItemInHand(hand);

            if (set.isEmpty() && !held.isEmpty()) {
                handler.setStackInSlot(0, StackHelpers.withSize(held, 1, false));
                player.setItemInHand(hand, StackHelpers.shrink(held, 1, false));
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
            } else if (!set.isEmpty()) {
                handler.setStackInSlot(0, ItemStack.EMPTY);
                ItemEntity item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), set);
                item.setNoPickUpDelay();
                level.addFreshEntity(item);
            }
            ped.setChanged();
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PedestalBE ped) {
                IItemHandler handler = ped.handler;//.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, ped.getBlockPos(), ped.getBlockState(), ped, null);
                if (handler == null) {
                    return;
                }
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(0));
                super.onRemove(state, level, pos, newState, movedByPiston);
            }
        }
    }

    private enum Shape {
        NORTH(Stream.of(
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(4, 4, 4, 12, 12, 12),
                Block.box(2, 12, 2, 14, 16, 14)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()),
        EAST(Stream.of(
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(4, 4, 4, 12, 12, 12),
                Block.box(2, 12, 2, 14, 16, 14)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()),
        SOUTH(Stream.of(
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(4, 4, 4, 12, 12, 12),
                Block.box(2, 12, 2, 14, 16, 14)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()),
        WEST(Stream.of(
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(4, 4, 4, 12, 12, 12),
                Block.box(2, 12, 2, 14, 16, 14)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());

        private final VoxelShape shape;

        Shape(VoxelShape shape) {
            this.shape = shape;
        }

        public static VoxelShape getFromFacing(Direction facing) {
            return switch (facing) {
                case NORTH -> NORTH.shape;
                case EAST -> EAST.shape;
                case SOUTH -> SOUTH.shape;
                case WEST -> WEST.shape;
                default -> throw new IllegalStateException("Invalid facing");
            };
        }
    }

}
