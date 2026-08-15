package com.example.strawbed.registry;

import com.example.strawbed.StrawBedMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
//? if <1.20 {
/*import net.minecraft.world.item.CreativeModeTab;
*///?}
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
*///?}

import java.util.function.Supplier;

public final class ModItems {
    //? if fabric {
    //? if >=1.21.11 {
    /*public static final Supplier<BlockItem> STRAW_BED = register("straw_bed",
            new BlockItem(ModBlocks.STRAW_BED.get(),
                    createProperties().setId(ResourceKey.create(Registries.ITEM,
                            Identifier.fromNamespaceAndPath(StrawBedMod.MOD_ID, "straw_bed")))));
    *///?} else {
    /*public static final Supplier<BlockItem> STRAW_BED = register("straw_bed",
            new BlockItem(ModBlocks.STRAW_BED.get(), createProperties()));*/
    //?}
    //?} else if neoforge {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, StrawBedMod.MOD_ID);

    //? if <1.21.11 {
    public static final Supplier<BlockItem> STRAW_BED = ITEMS.register("straw_bed",
            () -> new BlockItem(ModBlocks.STRAW_BED.get(), createProperties()));
    //?} else {
    /*public static final Supplier<BlockItem> STRAW_BED = ITEMS.register("straw_bed",
            id -> new BlockItem(ModBlocks.STRAW_BED.get(),
                    createProperties().setId(ResourceKey.create(Registries.ITEM, id))));
    *///?}
    //?} else {
    /*public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.ITEMS, StrawBedMod.MOD_ID);

    public static final Supplier<BlockItem> STRAW_BED = ITEMS.register("straw_bed",
            () -> new BlockItem(ModBlocks.STRAW_BED.get(), createProperties()));*/
    //?}

    private static Item.Properties createProperties() {
        //? if <1.20 {
        /*return new Item.Properties()
                .tab(CreativeModeTab.TAB_DECORATIONS)
                .stacksTo(16);
        *///?} else {
        return new Item.Properties().stacksTo(16);
        //?}
    }

    //? if fabric {
    //? if >=1.21.11 {
    /*private static Supplier<BlockItem> register(String name, BlockItem item) {
        Identifier id = Identifier.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        BlockItem registered = Registry.register(BuiltInRegistries.ITEM, id, item);
        return () -> registered;
    }*/
    //?} else if >=1.21 {
    /*private static Supplier<BlockItem> register(String name, BlockItem item) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(StrawBedMod.MOD_ID, name);
        BlockItem registered = Registry.register(BuiltInRegistries.ITEM, id, item);
        return () -> registered;
    }*/
    //?} else if >=1.20 {
    /*private static Supplier<BlockItem> register(String name, BlockItem item) {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, name);
        BlockItem registered = Registry.register(BuiltInRegistries.ITEM, id, item);
        return () -> registered;
    }*/
    //?} else {
    /*private static Supplier<BlockItem> register(String name, BlockItem item) {
        ResourceLocation id = new ResourceLocation(StrawBedMod.MOD_ID, name);
        BlockItem registered = Registry.register(Registry.ITEM, id, item);
        return () -> registered;
    }*/
    //?}
    //?}

    private ModItems() {
    }
}
