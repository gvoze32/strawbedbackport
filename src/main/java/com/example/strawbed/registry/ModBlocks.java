package com.example.strawbed.registry;

import com.example.strawbed.StrawBedMod;
import com.example.strawbed.block.StrawBedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
//? if <1.20 {
/*import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
*///?} else {
import net.minecraft.world.level.material.MapColor;
//?}
//? if fabric {
/*import net.minecraft.core.Registry;
//? if <1.21.11 {
import net.minecraft.resources.ResourceLocation;
//?} else {
import net.minecraft.resources.Identifier;
//? if >=1.21.11 {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?}
//?}
*///?}
//? if >=1.20 {
import net.minecraft.core.registries.BuiltInRegistries;
//?}
//? if fabric {
//?} else if neoforge {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
//?} else {
/*import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
*///?}

import java.util.function.Supplier;

public final class ModBlocks {
    //? if fabric {
    //? if >=1.21.11 {
    /*public static final Supplier<Block> STRAW_BED = register("straw_bed",
            new StrawBedBlock(createProperties().setId(ResourceKey.create(Registries.BLOCK,
                    Identifier.fromNamespaceAndPath(StrawBedMod.MOD_ID, "straw_bed")))));
    *///?} else {
    /*public static final Supplier<Block> STRAW_BED = register("straw_bed",
            new StrawBedBlock(createProperties()));*/
    //?}
    //?} else if neoforge {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, StrawBedMod.MOD_ID);

    //? if <1.21.11 {
    public static final Supplier<Block> STRAW_BED = BLOCKS.register("straw_bed",
            () -> new StrawBedBlock(createProperties()));
    //?} else {
    /*public static final Supplier<Block> STRAW_BED = BLOCKS.register("straw_bed",
            id -> new StrawBedBlock(createProperties().setId(ResourceKey.create(Registries.BLOCK, id))));
    *///?}
    //?} else {
    /*public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, StrawBedMod.MOD_ID);

    public static final Supplier<Block> STRAW_BED = BLOCKS.register("straw_bed",
            () -> new StrawBedBlock(createProperties()));*/
    //?}

    //? if <1.20 {
    /*private static BlockBehaviour.Properties createProperties() {
        return BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_YELLOW)
                .sound(ModSounds.STRAW_BED_SOUNDS)
                .strength(0.2f)
                .noOcclusion();
    }*/
    //?} else {
    private static BlockBehaviour.Properties createProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_YELLOW)
                .sound(ModSounds.STRAW_BED_SOUNDS)
                .strength(0.2f)
                .noOcclusion()
                .ignitedByLava();
    }
    //?}

    //? if fabric {
    //? if >=1.21.11 {
    /*private static Supplier<Block> register(String name, Block block) {
        Identifier id = Identifier.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        Block registered = Registry.register(BuiltInRegistries.BLOCK, id, block);
        return () -> registered;
    }*/
    //?} else if >=1.21 {
    /*private static Supplier<Block> register(String name, Block block) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        Block registered = Registry.register(BuiltInRegistries.BLOCK, id, block);
        return () -> registered;
    }*/
    //?} else if >=1.20 {
    /*private static Supplier<Block> register(String name, Block block) {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, name);
        Block registered = Registry.register(BuiltInRegistries.BLOCK, id, block);
        return () -> registered;
    }*/
    //?} else {
    /*private static Supplier<Block> register(String name, Block block) {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, name);
        Block registered = Registry.register(Registry.BLOCK, id, block);
        return () -> registered;
    }*/
    //?}
    //?}

    private ModBlocks() {
    }
}
