package com.lastember.stickytorches.blocks;


import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


public class StickyTorchBlock extends BaseTorchBlock {
    protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD = BuiltInRegistries.PARTICLE_TYPE
            .byNameCodec()
            .<SimpleParticleType>comapFlatMap(
                    particleType -> particleType instanceof SimpleParticleType simpleParticleType
                            ? DataResult.success(simpleParticleType)
                            : DataResult.error(() -> "Not a SimpleParticleType: " + particleType),
                    simpleParticleType -> simpleParticleType
            )
            .fieldOf("particle_options");
    public static final MapCodec<StickyTorchBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(PARTICLE_OPTIONS_FIELD.forGetter(torchBlock -> torchBlock.flameParticle), propertiesCodec()).apply(instance, StickyTorchBlock::new)
    );
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    protected final SimpleParticleType flameParticle;

    protected static final VoxelShape FLOOR_SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.box(5.0D, 3.0D, 11.0D, 11.0D, 13.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(5.0D, 3.0D, 0.0D, 11.0D, 13.0D, 5.0D);
    protected static final VoxelShape WEST_SHAPE = Block.box(11.0D, 3.0D, 5.0D, 16.0D, 13.0D, 11.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 3.0D, 5.0D, 5.0D, 13.0D, 11.0D);
    protected static final VoxelShape CEILING_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public StickyTorchBlock(SimpleParticleType flameParticle, Properties properties) {
        super(properties.lightLevel(state -> 14).noCollission());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP));
        this.flameParticle = flameParticle;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseTorchBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            case DOWN -> CEILING_SHAPE;
            default -> FLOOR_SHAPE;
        };
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess scheduledTickAccess,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        return !this.canSurvive(state, level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }


    /**
     * Checks if this block can exist at a location
     * <p>
     * A sticky torch can only exist if the block it is attached to is "sturdy"
     * on the attaching face.
     *
     * @param state The state of the sticky torch block
     * @param level The level (world) the sticky torch is in
     * @param pos   The position of the attaching block whose face is checked for "sturdiness"
     * @return      {@code true} if the block can survive, {@code false} otherwise
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
            Direction direction = state.getValue(FACING);
            BlockPos attachedPos = pos.relative(direction.getOpposite());
            BlockState attachedState = level.getBlockState(attachedPos);
            return attachedState.isFaceSturdy(level, attachedPos, direction);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for (Direction direction : context.getNearestLookingDirections()) {
            if (this.canSurvive(this.defaultBlockState().setValue(FACING, direction.getOpposite()), context.getLevel(), context.getClickedPos())) {
                return this.defaultBlockState().setValue(FACING, direction.getOpposite());
            }
        }
        return null;
    }

    public BlockState getStateForPlacement(Direction[] directions, Level level, BlockPos pos) {
        for (Direction direction : directions) {
            BlockState state = this.defaultBlockState().setValue(FACING, direction.getOpposite());
            if (this.canSurvive(state, level, pos)) {
                return state;
            }
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction direction = state.getValue(FACING);
        double x = (double)pos.getX() + 0.5D;
        double y = (double)pos.getY() + 0.7D;
        double z = (double)pos.getZ() + 0.5D;

        double xOffset = 0.0D;
        double zOffset = 0.0D;

        if (direction.getAxis() != Direction.Axis.Y) {
            Direction opposite = direction.getOpposite();
            xOffset = 0.27D * (double)opposite.getStepX();
            y = (double)pos.getY() + 0.85D;
            zOffset = 0.27D * (double)opposite.getStepZ();
        } else if (direction == Direction.DOWN) {
            y = (double)pos.getY() + 0.35D;
        }

        level.addParticle(ParticleTypes.SMOKE, x + xOffset, y, z + zOffset, 0.0D, 0.02D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, x + xOffset, y, z + zOffset, 0.0D, 0.005D, 0.0D);
    }

}
