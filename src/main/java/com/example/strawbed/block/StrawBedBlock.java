package com.example.strawbed.block;

import com.example.strawbed.registry.ModSounds;
import com.example.strawbed.world.StrawBedTracker;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
//? if <1.20.5 {
/*import net.minecraft.world.InteractionHand;
*///?}
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
//? if >=1.21.11 {
/*import net.minecraft.world.attribute.EnvironmentAttributes;
*///?}

public class StrawBedBlock extends BedBlock {

    // Foot: flat slab, 4 pixels tall (same shape regardless of direction)
    private static final VoxelShape FOOT_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    // Head shapes per direction: base (4px) + pillow (5px, on the side away from foot)
    // The pillow is on the far side from the foot half
    private static final VoxelShape HEAD_SOUTH = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 8.0),    // base
            Block.box(0.0, 0.0, 8.0, 16.0, 5.0, 16.0));   // pillow (south side)
    private static final VoxelShape HEAD_NORTH = Shapes.or(
            Block.box(0.0, 0.0, 8.0, 16.0, 4.0, 16.0),    // base
            Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 8.0));     // pillow (north side)
    private static final VoxelShape HEAD_EAST = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 8.0, 4.0, 16.0),     // base
            Block.box(8.0, 0.0, 0.0, 16.0, 5.0, 16.0));    // pillow (east side)
    private static final VoxelShape HEAD_WEST = Shapes.or(
            Block.box(8.0, 0.0, 0.0, 16.0, 4.0, 16.0),    // base
            Block.box(0.0, 0.0, 0.0, 8.0, 5.0, 16.0));     // pillow (west side)

    public StrawBedBlock(Properties properties) {
        super(DyeColor.BROWN, properties);
    }

    //? if <26.2 {
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
    //?}

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    //? if >=1.20.5 {
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    //?} else {
    /*public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    *///?}
        if (state.getValue(PART) == BedPart.FOOT) {
            return FOOT_SHAPE;
        }
        // Head part: pillow position depends on facing direction
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case SOUTH -> HEAD_SOUTH;
            case NORTH -> HEAD_NORTH;
            case EAST -> HEAD_EAST;
            case WEST -> HEAD_WEST;
            default -> HEAD_SOUTH;
        };
    }

    //? if >=1.20.5 {
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return attemptSleep(state, level, pos, player);
    }
    //?} else {
    /*@Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return attemptSleep(state, level, pos, player);
    }
    *///?}

    private InteractionResult attemptSleep(BlockState state, Level level, BlockPos pos, Player player) {
        if (isClientSide(level)) {
            return InteractionResult.CONSUME;
        }

        // Navigate from foot to head
        if (state.getValue(PART) != BedPart.HEAD) {
            pos = pos.relative(state.getValue(FACING));
            state = level.getBlockState(pos);
            if (!state.is(this)) {
                return InteractionResult.CONSUME;
            }
        }

        if (!bedWorks(level, pos)) {
            removeBothHalves(level, pos, state, ModSounds.STRAW_BED_BREAK.get());
            return InteractionResult.SUCCESS;
        }

        // Check if bed is occupied
        if (state.getValue(OCCUPIED)) {
            //? if <1.19 {
            /*displayOverlay(player, new net.minecraft.network.chat.TranslatableComponent(
                    "block.minecraft.bed.occupied"));*/
            //?} else {
            displayOverlay(player, Component.translatable("block.minecraft.bed.occupied"));
            //?}
            return InteractionResult.SUCCESS;
        }

        // Attempt to sleep
        ServerPlayer serverPlayer = (ServerPlayer) player;
        StrawBedTracker.beginSleepAttempt(serverPlayer, pos);
        Either<Player.BedSleepingProblem, net.minecraft.util.Unit> result = serverPlayer.startSleepInBed(pos);

        if (result.left().isPresent()) {
            StrawBedTracker.finishSleepAttempt(serverPlayer, false);
            Player.BedSleepingProblem problem = result.left().get();
            Component message = sleepingProblemMessage(problem);
            if (message != null) {
                displayOverlay(player, message);
            }
            return InteractionResult.SUCCESS;
        }

        // Sleep succeeded — mark bed as occupied
        level.setBlock(pos, state.setValue(OCCUPIED, true), Block.UPDATE_ALL);
        serverPlayer.awardStat(Stats.SLEEP_IN_BED);
        StrawBedTracker.finishSleepAttempt(serverPlayer, true);
        return InteractionResult.SUCCESS;
    }
    private static boolean isClientSide(Level level) {
        //? if >=1.21.11 {
        /*return level.isClientSide();*/
        //?} else {
        return level.isClientSide;
        //?}
    }

    private static boolean bedWorks(Level level, BlockPos pos) {
        //? if >=1.21.11 {
        /*return level.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos).canSleep(level);*/
        //?} else {
        return level.dimensionType().bedWorks();
        //?}
    }

    private static Component sleepingProblemMessage(Player.BedSleepingProblem problem) {
        //? if >=1.21.11 {
        /*return problem.message();*/
        //?} else {
        return problem.getMessage();
        //?}
    }
    private static void displayOverlay(Player player, Component message) {
        //? if >=26.1 {
        /*player.sendOverlayMessage(message);*/
        //?} else {
        player.displayClientMessage(message, true);
        //?}
    }


    public static void removeBothHalves(Level level, BlockPos headPos, BlockState headState, SoundEvent sound) {
        BlockPos footPos = headPos.relative(headState.getValue(FACING).getOpposite());
        level.destroyBlock(headPos, false);
        BlockState footState = level.getBlockState(footPos);
        if (footState.is(headState.getBlock()) && footState.hasProperty(BlockStateProperties.BED_PART)) {
            level.destroyBlock(footPos, false);
        }
        level.playSound(null, headPos, sound, SoundSource.BLOCKS, 0.9f, 1.0f);
    }
}
