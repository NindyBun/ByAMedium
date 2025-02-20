package com.NindyBun.ByAMedium.blockEntities;

import com.NindyBun.ByAMedium.ByAMedium;
import com.NindyBun.ByAMedium.crafting.CachedRecipe;
import com.NindyBun.ByAMedium.crafting.InfusionInput;
import com.NindyBun.ByAMedium.crafting.InfusionRecipe;
import com.NindyBun.ByAMedium.helpers.PedestalPositions;
import com.NindyBun.ByAMedium.helpers.SoundTicker;
import com.NindyBun.ByAMedium.inventories.AlterHandler;
import com.NindyBun.ByAMedium.inventories.SingleItemStackHandler;
import com.NindyBun.ByAMedium.registers.ModBlockEntities;
import com.NindyBun.ByAMedium.registers.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlterBE extends BlockEntity {
    public final AlterHandler handler = new AlterHandler(this);
    private final PedestalPositions standardLocations = new PedestalPositions.Builder()
            .pos(3, 0, 1).pos(3, 0, -1)
            .pos(-3, 0, 1).pos(-3, 0, -1)
            .pos(1, 0, 3).pos(1, 0, -3)
            .pos(-1, 0, 3).pos(-1, 0, -3)
            .build();
    private int progress = 0;
    private boolean active = false;
    private CachedRecipe<InfusionRecipe> recipe;
    private final NonNullList<ItemStack> inputs = NonNullList.withSize(InfusionRecipe.MAX_RECIPE_SIZE, ItemStack.EMPTY);
    private SoundTicker ambientSound;

    public AlterBE(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ALTER_BE.get(), pos, blockState);
        this.recipe = new CachedRecipe<>(ModRecipes.INFUSION_RECIPE.get());
    }

    public boolean verifyPedestalPlacements() {
        if (this.level == null) {
            return false;
        }

        for (BlockPos pos : this.getPedestalLocations()) {
            if (!(this.level.getBlockEntity(pos) instanceof PedestalBE)) {
                return false;
            }
        }

        return true;
    }

    public boolean activateAlter(Level level, Player player) {
        InfusionRecipe infusionRecipe = this.getActiveRecipe();
        if (infusionRecipe == null || !this.verifyPedestalPlacements()) {
            player.hurt(level.damageSources().generic(), 2);
            return false;
        }
        if (infusionRecipe.getHealth() <= player.getHealth()) {
            if (this.recipe.exists()) {
                player.hurt(level.damageSources().generic(), infusionRecipe.getHealth());
                this.setActive(true);
                level.playSound((Player)null, this.getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }

        return false;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlterBE alterBE) {
        ItemStack catalyst = alterBE.handler.getStackInSlot(0);
        if (catalyst.isEmpty()) {
            if (alterBE.isActive()) {
                level.playSound((Player)null, alterBE.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            alterBE.reset();
            return;
        }

        if (alterBE.isActive()) {
            InfusionRecipe infusionRecipe = alterBE.getActiveRecipe();
            if (infusionRecipe != null) {
                alterBE.progress++;

                List<PedestalBE> pedestalBES = alterBE.getPedestals();
                if (alterBE.progress >= 100) {
                    InfusionInput infusionInput = alterBE.toInfusionInput();
                    NonNullList<ItemStack> remainings = infusionRecipe.getRemainingItems(infusionInput);

                    for (int i = 0; i < pedestalBES.size(); i++) {
                        PedestalBE pedestal = pedestalBES.get(i);
                        pedestal.handler.setStackInSlot(0, remainings.get(i + 1));
                        alterBE.spawnParticles(ParticleTypes.SMOKE, pedestal.getBlockPos(), 1.2D, 20);
                    }

                    ItemStack result = infusionRecipe.assemble(infusionInput, level.registryAccess());

                    alterBE.setOutput(result, remainings.getFirst());

                    level.playSound((Player)null, alterBE.getBlockPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                    lightningBolt.setPos(alterBE.getBlockPos().getX()+0.5d, alterBE.getBlockPos().getY(), alterBE.getBlockPos().getZ()+0.5d);
                    level.addFreshEntity(lightningBolt);
                    alterBE.reset();
                    alterBE.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos, 1.0D, 10);
                } else {
                    for (PedestalBE pedestalBE : pedestalBES) {
                        BlockPos pedPos = pedestalBE.getBlockPos();
                        ItemStack pedStack = pedestalBE.handler.getStackInSlot(0);
                        alterBE.spawnItemParticles(pedPos, pedStack);
                    }

                    if (alterBE.ambientSound == null) {
                        alterBE.ambientSound = new SoundTicker(alterBE.getBlockPos(), 1, 1, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, level.random);
                        Minecraft.getInstance().getSoundManager().play(alterBE.ambientSound);
                    }

                }
            } else {
                level.playSound((Player)null, alterBE.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                alterBE.reset();
            }
        } else {
            alterBE.progress = 0;
        }
    }

    public void setOutput(ItemStack result, ItemStack remaining) {
        this.handler.setStackInSlot(0, remaining);
        this.handler.setStackInSlot(1, result);
    }

    private <T extends ParticleOptions> void spawnParticles(T particle, BlockPos pos, double yOffset, int count) {
        if (this.level == null || this.level.isClientSide())
            return;

        var level = (ServerLevel) this.level;

        double x = pos.getX() + 0.5D;
        double y = pos.getY() + yOffset;
        double z = pos.getZ() + 0.5D;

        level.sendParticles(particle, x, y, z, count, 0, 0, 0, 0.1D);
    }

    private void spawnItemParticles(BlockPos pedestalPos, ItemStack stack) {
        if (this.level == null || this.level.isClientSide() || stack.isEmpty())
            return;

        ServerLevel level = (ServerLevel) this.level;
        BlockPos pos = this.getBlockPos();

        double x = pedestalPos.getX() + (level.getRandom().nextDouble() * 0.2D) + 0.4D;
        double y = pedestalPos.getY() + (level.getRandom().nextDouble() * 0.2D) + 1.2D;
        double z = pedestalPos.getZ() + (level.getRandom().nextDouble() * 0.2D) + 0.4D;

        double velX = pos.getX() - pedestalPos.getX();
        double velY = 0.25D;
        double velZ = pos.getZ() - pedestalPos.getZ();

        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
    }

    public List<BlockPos> getPedestalLocations() {
        return this.standardLocations.get(this.getBlockPos());
    }

    public InfusionRecipe getActiveRecipe() {
        if (this.level == null) {
            return null;
        }

        this.updateRecipe(this.getPedestals());

        return this.recipe.checkAndGet(this.toInfusionInput(), this.level);
    }

    private InfusionInput toInfusionInput() {
        return new InfusionInput(this.inputs);
    }

    private List<PedestalBE> getPedestals() {
        if (this.level == null) {
            return Collections.emptyList();
        }

        List<PedestalBE> pedestalBES = new ArrayList<>();

        for (BlockPos pos : this.getPedestalLocations()) {
            BlockEntity blockEntity = this.level.getBlockEntity(pos);
            if (blockEntity instanceof PedestalBE pedestalBE) {
                pedestalBES.add(pedestalBE);
            }
        }
        return pedestalBES;
    }

    private void updateRecipe(List<PedestalBE> pedestals) {
        this.inputs.clear();
        this.inputs.set(0, this.handler.getStackInSlot(0));

        for (int i = 0; i < pedestals.size(); i++) {
            ItemStack stack = pedestals.get(i).handler.getStackInSlot(0);
            this.inputs.set(i+1, stack);
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void reset() {
        this.progress = 0;
        this.active = false;
        if (this.ambientSound != null) {
            this.ambientSound.stopPlaying();
            this.ambientSound = null;
        }
    }


    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        if (tag.contains("Inventory"))
            handler.deserializeNBT(provider, tag.getCompound("Inventory"));
        if (tag.contains("progress"))
            this.progress = tag.getInt("progress");
        if (tag.contains("active"))
            this.active = tag.getBoolean("active");
        super.loadAdditional(tag, provider);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inventory", handler.serializeNBT(provider));
        tag.putInt("progress", this.progress);
        tag.putBoolean("active", this.active);

    }

}
