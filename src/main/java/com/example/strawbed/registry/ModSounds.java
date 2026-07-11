package com.example.strawbed.registry;

import com.example.strawbed.StrawBedMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, StrawBedMod.MOD_ID);

    public static final Supplier<SoundEvent> STRAW_BED_BREAK = register("block.straw_bed.break");
    public static final Supplier<SoundEvent> STRAW_BED_BREAK_LEAVE = register("block.straw_bed.break_leave");
    public static final Supplier<SoundEvent> STRAW_BED_STEP = register("block.straw_bed.step");
    public static final Supplier<SoundEvent> STRAW_BED_PLACE = register("block.straw_bed.place");
    public static final Supplier<SoundEvent> STRAW_BED_HIT = register("block.straw_bed.hit");
    public static final Supplier<SoundEvent> STRAW_BED_FALL = register("block.straw_bed.fall");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    private ModSounds() {
    }
}
