package com.lastember.stickytorches.entities;

import com.lastember.stickytorches.blocks.StickyTorchesBlocks;
import com.lastember.stickytorches.items.StickyTorchesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.core.Direction.*;

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
    protected @NotNull Item getDefaultItem() {
        return StickyTorchesItems.STICKY_TORCH.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        // Get the position of block to spawn torch in
        BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
        // Get a list of directions the entity is most closely flying in
        Direction[] directions = orderedByNearest(this);

        // Replace the first entry in directions with the face that was collided with
        Direction direction = hitResult.getDirection().getOpposite();
        int i = 0;
        while (i < directions.length && directions[i] != direction) {
            i++;
        }
        if (i > 0) {
            System.arraycopy(directions, 0, directions, 1, i);
            directions[0] = direction;
        }

        // Get state with proper rotation based on flying direction and surrounding blocks
        BlockState state = StickyTorchesBlocks.STICKY_TORCH.get().getStateForPlacement(directions, this.level(), pos);
        // If block can be placed at location, set block with proper state
        if (state != null) {

            this.level().setBlock(pos, state, 3);
            this.level().playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.level().playSound(null, pos, SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS, 0.1F, 1.6F);

        }
        else {
            this.level().setBlock(pos, StickyTorchesBlocks.STICKY_TORCH.get().defaultBlockState(), 3);
            this.level().destroyBlock(pos, true);
        }

        //TODO: if state is null, set projectile direction to DOWN and let it fall and stick somewhere.

        // Remove the projectile from the world
        this.discard();

    }
    public static Direction[] orderedByNearest(Entity entity) {
        float x_rads = entity.getViewXRot(1.0F) * (float) (Math.PI / 180.0);
        float y_rads = -entity.getViewYRot(1.0F) * (float) (Math.PI / 180.0);

        boolean bl = Mth.sin(y_rads) > 0.0F;
        boolean bl2 = Mth.sin(x_rads) < 0.0F;
        boolean bl3 = Mth.cos(y_rads) > 0.0F;
        float l = bl ? Mth.sin(y_rads) : -Mth.sin(y_rads);
        float m = bl2 ? -Mth.sin(x_rads) : Mth.sin(x_rads);
        float n = bl3 ? Mth.cos(y_rads) : -Mth.cos(y_rads);
        float o = l * Mth.cos(x_rads);
        float p = n * Mth.cos(x_rads);
        Direction direction = bl ? WEST : EAST;
        Direction direction2 = bl2 ? DOWN : UP;
        Direction direction3 = bl3 ? SOUTH : NORTH;
        if (l > n) {
            if (m > o) {
                return makeDirectionArray(direction2, direction, direction3);
            } else {
                return p > m ? makeDirectionArray(direction, direction3, direction2) : makeDirectionArray(direction, direction2, direction3);
            }
        } else if (m > p) {
            return makeDirectionArray(direction2, direction3, direction);
        } else {
            return o > m ? makeDirectionArray(direction3, direction, direction2) : makeDirectionArray(direction3, direction2, direction);
        }
    }
    private static Direction[] makeDirectionArray(Direction first, Direction second, Direction third) {
        return new Direction[]{first, second, third, third.getOpposite(), second.getOpposite(), first.getOpposite()};
    }

}
