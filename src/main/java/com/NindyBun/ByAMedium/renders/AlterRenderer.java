package com.NindyBun.ByAMedium.renders;

import com.NindyBun.ByAMedium.blockEntities.AlterBE;
import com.NindyBun.ByAMedium.blockEntities.PedestalBE;
import com.NindyBun.ByAMedium.registers.ModBlocks;
import com.NindyBun.ByAMedium.registers.ModRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.items.IItemHandler;

public class AlterRenderer implements BlockEntityRenderer<AlterBE> {
    public AlterRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(AlterBE alterBE, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        IItemHandler handler = alterBE.handler;//.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, alterBE.getBlockPos(), alterBE.getBlockState(), alterBE, null);
        if (handler == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        ItemStack stack = !handler.getStackInSlot(1).isEmpty() ? handler.getStackInSlot(1) : handler.getStackInSlot(0);

        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.2D, 0.5D);
            float scale = stack.getItem() instanceof BlockItem ? 0.95F : 0.75F;
            poseStack.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            poseStack.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, i, i1, poseStack, multiBufferSource, mc.level, 0);
            poseStack.popPose();
        }


        BlockPos pos = alterBE.getBlockPos();
        var builder = multiBufferSource.getBuffer(ModRenderTypes.GHOST);

        poseStack.pushPose();
        poseStack.translate(-pos.getX(), -pos.getY(), -pos.getZ());

        alterBE.getPedestalLocations().forEach(aoePos -> {
            if (level.isEmptyBlock(aoePos)) {
                poseStack.pushPose();
                poseStack.translate(aoePos.getX(), aoePos.getY(), aoePos.getZ());
                mc.getBlockRenderer().renderBatched(ModBlocks.PEDESTAL.get().defaultBlockState(), aoePos, level, poseStack, builder, false, level.getRandom(), ModelData.EMPTY, null);
                poseStack.popPose();
            }
        });

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(AlterBE alterBE) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(AlterBE alterBE) {
        return AABB.INFINITE;

    }
}
