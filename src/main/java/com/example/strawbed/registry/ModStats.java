package com.example.strawbed.registry;

import com.example.strawbed.StrawBedMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
//? if <1.21.11 {
import net.minecraft.resources.ResourceLocation;
//?} else {
/*import net.minecraft.resources.Identifier;
*///?}
//? if fabric {
/*import net.minecraft.core.Registry;
*///?}
//? if >=1.20 {
import net.minecraft.core.registries.BuiltInRegistries;
//?}
//? if >=1.20 {
import net.minecraft.core.registries.Registries;
//? if fabric {
//?} else if neoforge {
import net.neoforged.neoforge.registries.DeferredRegister;
//?} else {
/*import net.minecraftforge.registries.DeferredRegister;
*///?}
//?}

import java.util.function.Supplier;

public final class ModStats {
    //? if >=1.20 {
    //? if neoforge {
    //? if <1.21.11 {
    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
            DeferredRegister.create(Registries.CUSTOM_STAT, StrawBedMod.MOD_ID);
    //?} else {
    /*public static final DeferredRegister<Identifier> CUSTOM_STATS =
            DeferredRegister.create(Registries.CUSTOM_STAT, StrawBedMod.MOD_ID);*/
    //?}
    //?} else if forge {
    /*public static final net.minecraftforge.registries.DeferredRegister<ResourceLocation> CUSTOM_STATS =
            net.minecraftforge.registries.DeferredRegister.create(Registries.CUSTOM_STAT, StrawBedMod.MOD_ID);*/
    //?}
    //?}

    //? if <1.21.11 {
    public static final Supplier<ResourceLocation> SLEEP_IN_STRAW_BED = register();
    //?} else {
    /*public static final Supplier<Identifier> SLEEP_IN_STRAW_BED = register();*/
    //?}

    //? if fabric {
    //? if >=1.21.11 {
    /*private static Supplier<Identifier> register() {
        Identifier id = Identifier.fromNamespaceAndPath(StrawBedMod.MOD_ID, "sleep_in_straw_bed");
        Identifier registered = Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        return () -> registered;
    }*/
    //?} else if >=1.21 {
    /*private static Supplier<ResourceLocation> register() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, "sleep_in_straw_bed");
        ResourceLocation registered = Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        return () -> registered;
    }*/
    //?} else if >=1.20 {
    /*private static Supplier<ResourceLocation> register() {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, "sleep_in_straw_bed");
        ResourceLocation registered = Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        return () -> registered;
    }*/
    //?} else {
    /*private static Supplier<ResourceLocation> register() {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, "sleep_in_straw_bed");
        ResourceLocation registered = Registry.register(Registry.CUSTOM_STAT, id, id);
        return () -> registered;
    }*/
    //?}
    //?} else {
    //? if >=1.20 {
    //? if <1.21.11 {
    private static Supplier<ResourceLocation> register() {
        return CUSTOM_STATS.register("sleep_in_straw_bed",
                () -> ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, "sleep_in_straw_bed"));
    }
    //?} else {
    /*private static Supplier<Identifier> register() {
        return CUSTOM_STATS.register("sleep_in_straw_bed",
                () -> Identifier.fromNamespaceAndPath(StrawBedMod.MOD_ID, "sleep_in_straw_bed"));
    }*/
    //?}
    //?} else {
    /*private static Supplier<ResourceLocation> register() {
        return () -> new ResourceLocation(StrawBedMod.MOD_ID, "sleep_in_straw_bed");
    }*/
    //?}
    //?}

    public static void award(ServerPlayer player) {
        player.awardStat(Stats.CUSTOM.get(SLEEP_IN_STRAW_BED.get(), StatFormatter.DEFAULT));
    }

    private ModStats() {
    }
}
