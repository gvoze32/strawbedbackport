package com.example.strawbed.registry;

import com.example.strawbed.StrawBedMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModStats {
    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, StrawBedMod.MOD_ID);

    public static final DeferredHolder<ResourceLocation, ResourceLocation> SLEEP_IN_STRAW_BED =
            CUSTOM_STATS.register("sleep_in_straw_bed",
                    () -> ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, "sleep_in_straw_bed"));

    public static void award(ServerPlayer player) {
        player.awardStat(Stats.CUSTOM.get(SLEEP_IN_STRAW_BED.get(), StatFormatter.DEFAULT));
    }

    private ModStats() {
    }
}
