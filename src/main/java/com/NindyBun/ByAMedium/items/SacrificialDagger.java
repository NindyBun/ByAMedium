package com.NindyBun.ByAMedium.items;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.blockEntities.AlterBE;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

public class SacrificialDagger extends Item {

    public SacrificialDagger() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player && !level.isClientSide) {
            if ((this.getUseDuration(stack, livingEntity) - timeCharged) < 15) {
                return;
            }

            BlockPos bottomOffset = player.blockPosition().offset(-1, -1, -1);

            for (int x = bottomOffset.getX(); x < bottomOffset.getX() + 3; x++) {
                for (int y = bottomOffset.getY(); y < bottomOffset.getY() + 3; y++) {
                    for (int z = bottomOffset.getZ(); z < bottomOffset.getZ() + 3; z++) {
                        BlockEntity blockEntity = level.getBlockEntity(new BlockPos(x, y, z));
                        if (blockEntity instanceof AlterBE alterBE) {
                            if (!alterBE.isActive() && alterBE.activateAlter(level, player)) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
