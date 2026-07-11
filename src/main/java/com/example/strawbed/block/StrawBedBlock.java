package com.example.strawbed.block;

import com.example.strawbed.registry.ModSounds;
import com.example.strawbed.world.StrawBedTracker;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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

import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class StrawBedBlock extends BedBlock {

    public StrawBedBlock(Properties properties) {
        super(DyeColor.BROWN, properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
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

        // In dimensions where beds don't work: destroy the bed (no explosion)
        if (!level.dimensionType().bedWorks()) {
            removeBothHalves(level, pos, state, ModSounds.STRAW_BED_BREAK.get());
            return InteractionResult.SUCCESS;
        }

        // Check if bed is occupied
        if (state.getValue(OCCUPIED)) {
            player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
            return InteractionResult.SUCCESS;
        }

        // Attempt to sleep
        ServerPlayer serverPlayer = (ServerPlayer) player;
        StrawBedTracker.beginSleepAttempt(serverPlayer, pos);
        Either<Player.BedSleepingProblem, net.minecraft.util.Unit> result = serverPlayer.startSleepInBed(pos);

        if (result.left().isPresent()) {
            StrawBedTracker.finishSleepAttempt(serverPlayer, false);
            Player.BedSleepingProblem problem = result.left().get();
            if (problem.getMessage() != null) {
                player.displayClientMessage(problem.getMessage(), true);
            }
            return InteractionResult.SUCCESS;
        }

        // Sleep succeeded — mark bed as occupied
        level.setBlock(pos, state.setValue(OCCUPIED, true), Block.UPDATE_ALL);
        serverPlayer.awardStat(Stats.SLEEP_IN_BED);
        StrawBedTracker.finishSleepAttempt(serverPlayer, true);
        return InteractionResult.SUCCESS;
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
