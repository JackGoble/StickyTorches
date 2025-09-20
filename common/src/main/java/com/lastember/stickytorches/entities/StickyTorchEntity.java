package com.lastember.stickytorches.entities;

import com.lastember.stickytorches.StickyTorchesMod;
import com.lastember.stickytorches.blocks.StickyTorchesBlocks;
import com.lastember.stickytorches.items.StickyTorchesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class StickyTorchEntity extends ThrowableItemProjectile {

    public StickyTorchEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public StickyTorchEntity(Level level, double x, double y, double z, ItemStack item) {
        super(StickyTorchesEntities.STICKY_TORCH_ENTITY.get(), x, y, z, level, item);
    }

    public StickyTorchEntity(Level level, LivingEntity owner, ItemStack item) {
        super(StickyTorchesEntities.STICKY_TORCH_ENTITY.get(), owner, level, item);
    }

    @Override
    protected Item getDefaultItem() {
        return StickyTorchesItems.STICKY_TORCH.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        StickyTorchesMod.LOGGER.info(hitResult.getDirection().toString());

        // Get the position and face of the block hit
        BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
        // Set the block at that location
        this.level().setBlock(pos, StickyTorchesBlocks.STICKY_TORCH.get().defaultBlockState(), 3);

        // Remove the projectile from the world
        this.discard();
    }

}
