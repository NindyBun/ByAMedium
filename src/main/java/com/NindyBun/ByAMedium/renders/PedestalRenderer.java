package com.NindyBun.ByAMedium.renders;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.blockEntities.PedestalBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class PedestalRenderer implements BlockEntityRenderer<PedestalBE> {
    public PedestalRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(PedestalBE pedestalBE, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        IItemHandler handler = pedestalBE.handler;//.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, pedestalBE.getBlockPos(), pedestalBE.getBlockState(), pedestalBE, null);
        if (handler == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = handler.getStackInSlot(0);

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
    }
}
