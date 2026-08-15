package com.example.strawbed.registry;

import com.example.strawbed.StrawBedMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
//? if <1.21.11 {
import net.minecraft.resources.ResourceLocation;
//?} else {
/*import net.minecraft.resources.Identifier;
*///?}
//? if >=1.20 {
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
//?}
//? if fabric {
/*import net.minecraft.core.Registry;
*///?} else if neoforge {
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredRegister;
//?} else {
/*import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.DeferredRegister;
*///?}

import java.util.function.Supplier;

public final class ModSounds {
    //? if fabric {
    /*// Fabric registers sound events directly in register(String).
    *///?} else if neoforge {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, StrawBedMod.MOD_ID);
    //?} else {
    //? if <1.20 {
    /*public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS, StrawBedMod.MOD_ID);
    *///?} else {
    /*public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, StrawBedMod.MOD_ID);
    *///?}
    //?}
    public static final Supplier<SoundEvent> STRAW_BED_BREAK = register("block.straw_bed.break");
    public static final Supplier<SoundEvent> STRAW_BED_BREAK_LEAVE = register("block.straw_bed.break_leave");
    public static final Supplier<SoundEvent> STRAW_BED_STEP = register("block.straw_bed.step");
    public static final Supplier<SoundEvent> STRAW_BED_PLACE = register("block.straw_bed.place");
    public static final Supplier<SoundEvent> STRAW_BED_HIT = register("block.straw_bed.hit");
    public static final Supplier<SoundEvent> STRAW_BED_FALL = register("block.straw_bed.fall");

    //? if fabric {
    /*public static final SoundType STRAW_BED_SOUNDS = new SoundType(1.0F, 1.0F,
            STRAW_BED_BREAK.get(), STRAW_BED_STEP.get(), STRAW_BED_PLACE.get(),
            STRAW_BED_HIT.get(), STRAW_BED_FALL.get());
    *///?} else if neoforge {
    public static final SoundType STRAW_BED_SOUNDS = new DeferredSoundType(1.0F, 1.0F,
            STRAW_BED_BREAK, STRAW_BED_STEP, STRAW_BED_PLACE, STRAW_BED_HIT, STRAW_BED_FALL);
    //?} else {
    /*public static final SoundType STRAW_BED_SOUNDS = new ForgeSoundType(1.0F, 1.0F,
            STRAW_BED_BREAK, STRAW_BED_STEP, STRAW_BED_PLACE, STRAW_BED_HIT, STRAW_BED_FALL);
    *///?}

    //? if fabric {
    //? if >=1.21.11 {
    /*private static Supplier<SoundEvent> register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        SoundEvent sound = SoundEvent.createVariableRangeEvent(id);
        SoundEvent registered = Registry.register(BuiltInRegistries.SOUND_EVENT, id, sound);
        return () -> registered;
    }*/
    //?} else if >=1.21 {
    /*private static Supplier<SoundEvent> register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        SoundEvent sound = SoundEvent.createVariableRangeEvent(id);
        SoundEvent registered = Registry.register(BuiltInRegistries.SOUND_EVENT, id, sound);
        return () -> registered;
    }*/
    //?} else if >=1.20 {
    /*private static Supplier<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, name);
        SoundEvent sound = SoundEvent.createVariableRangeEvent(id);
        SoundEvent registered = Registry.register(BuiltInRegistries.SOUND_EVENT, id, sound);
        return () -> registered;
    }*/
    //?} else {
    /*private static Supplier<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, name);
        SoundEvent sound = new SoundEvent(id);
        SoundEvent registered = Registry.register(Registry.SOUND_EVENT, id, sound);
        return () -> registered;
    }*/
    //?}
    //?} else if neoforge {
    private static Supplier<SoundEvent> register(String name) {
        //? if >=1.21.11 {
        /*Identifier id = Identifier.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);*/
        //?} else {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        //?}
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    //?} else {
    //? if <1.20 {
    /*private static Supplier<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> new SoundEvent(id));
    }*/
    //?} else {
    /*private static Supplier<SoundEvent> register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }*/
    //?}
    //?}


    private ModSounds() {
    }
}
