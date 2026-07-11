package com.example.strawbed.world;

import com.example.strawbed.block.StrawBedBlock;
import com.example.strawbed.registry.ModBlocks;
import com.example.strawbed.registry.ModSounds;
import com.example.strawbed.registry.ModStats;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StrawBedTracker {
    private static final Map<UUID, BlockPos> PENDING_BEDS = new HashMap<>();
    private static final Map<UUID, BlockPos> ACTIVE_BEDS = new HashMap<>();

    public static void beginSleepAttempt(ServerPlayer player, BlockPos headPos) {
        PENDING_BEDS.put(player.getUUID(), headPos.immutable());
    }

    public static void finishSleepAttempt(ServerPlayer player, boolean success) {
        BlockPos pos = PENDING_BEDS.remove(player.getUUID());
        if (success && pos != null) {
            ACTIVE_BEDS.put(player.getUUID(), pos);
        }
    }

    public static boolean shouldCancelSpawnSet(Player player, BlockPos newSpawn) {
        if (newSpawn == null) {
            return false;
        }
        BlockPos pending = PENDING_BEDS.get(player.getUUID());
        return pending != null && pending.equals(newSpawn);
    }

    public static void onWake(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        UUID playerId = serverPlayer.getUUID();
        BlockPos pos = ACTIVE_BEDS.remove(playerId);
        if (pos == null) {
            return;
        }
        Level level = serverPlayer.level();
        BlockState state = level.getBlockState(pos);
        if (!state.is(ModBlocks.STRAW_BED.get())) {
            return;
        }
        // Navigate to head if we're at foot
        if (state.getValue(StrawBedBlock.PART) == BedPart.FOOT) {
            pos = pos.relative(state.getValue(StrawBedBlock.FACING));
            state = level.getBlockState(pos);
        }
        if (state.is(ModBlocks.STRAW_BED.get())) {
            StrawBedBlock.removeBothHalves(level, pos, state, ModSounds.STRAW_BED_BREAK_LEAVE.get());
            ModStats.award(serverPlayer);
        }
    }

    private StrawBedTracker() {
    }
}
