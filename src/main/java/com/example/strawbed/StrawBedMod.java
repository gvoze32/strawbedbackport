package com.example.strawbed;

import com.example.strawbed.registry.ModBlocks;
import com.example.strawbed.registry.ModItems;
import com.example.strawbed.registry.ModSounds;
import com.example.strawbed.registry.ModStats;
import com.example.strawbed.world.StrawBedTracker;
//? if fabric {
/*import net.fabricmc.api.ModInitializer;
//? if >=26.1 {
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
//?} else if >=1.20 {
import net.minecraft.world.item.CreativeModeTabs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
//?}
*///?}
//? if forge {
//? if >=1.20 {
/*import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
*///?}
//?}
//? if fabric {
//?} else if neoforge {
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
//?} else {
/*import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
*///?}

//? if fabric {
//? if >=26.1 {
/*public class StrawBedMod implements ModInitializer {
    public static final String MOD_ID = "strawbed";
    @Override
    public void onInitialize() {
        ModItems.STRAW_BED.get();
        ModStats.SLEEP_IN_STRAW_BED.get();
        CreativeModeTabEvents.modifyOutputEvent(
                ResourceKey.create(Registries.CREATIVE_MODE_TAB,
                        Identifier.fromNamespaceAndPath("minecraft", "functional")))
                .register(output -> output.accept(ModItems.STRAW_BED.get()));
    }
*///?} else if >=1.20 {
/*public class StrawBedMod implements ModInitializer {
    public static final String MOD_ID = "strawbed";
    @Override
    public void onInitialize() {
        ModItems.STRAW_BED.get();
        ModStats.SLEEP_IN_STRAW_BED.get();
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries ->
                entries.accept(ModItems.STRAW_BED.get()));
    }
*///?} else {
/*public class StrawBedMod implements ModInitializer {
    public static final String MOD_ID = "strawbed";
    @Override
    public void onInitialize() {
        ModItems.STRAW_BED.get();
        ModStats.SLEEP_IN_STRAW_BED.get();
    }
*///?}
//?} else if neoforge {
@Mod(StrawBedMod.MOD_ID)
public class StrawBedMod {
    public static final String MOD_ID = "strawbed";
    public StrawBedMod(IEventBus modBus) {
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModSounds.SOUND_EVENTS.register(modBus);
        ModStats.CUSTOM_STATS.register(modBus);

        modBus.addListener(this::addCreativeTabItems);

        NeoForge.EVENT_BUS.addListener(this::onPlayerWakeUp);
        NeoForge.EVENT_BUS.addListener(this::onPlayerSetSpawn);
    }
    //?} else {
    /*@Mod(StrawBedMod.MOD_ID)
    public class StrawBedMod {
    public static final String MOD_ID = "strawbed";
    public StrawBedMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModSounds.SOUND_EVENTS.register(modBus);
        //? if >=1.20 {
        ModStats.CUSTOM_STATS.register(modBus);
        modBus.addListener(this::addCreativeTabItems);
        //?}
        //? if <1.20 {
        modBus.addListener(this::onCommonSetup);
        //?}
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerWakeUp);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerSetSpawn);
    }
    *///?}


    //? if forge {
    /*private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ComposterBlock.COMPOSTABLES.put(ModItems.STRAW_BED.get(), 0.65f));
    }
    *///?}

    //? if >=1.20 {
    //? if neoforge {
    private void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.STRAW_BED.get());
        }
    }
    //?} else if forge {
    /*private void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.STRAW_BED.get());
        }
    }*/
    //?}
    //?}

    //? if neoforge {
    private void onPlayerWakeUp(PlayerWakeUpEvent event) {
        StrawBedTracker.onWake((net.minecraft.world.entity.player.Player) event.getEntity());
    }

    private void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        if (StrawBedTracker.shouldCancelSpawnSet(
                (net.minecraft.world.entity.player.Player) event.getEntity(), event.getNewSpawn())) {
            event.setCanceled(true);
        }
    }
    //?} else if forge {
    /*private void onPlayerWakeUp(PlayerWakeUpEvent event) {
        StrawBedTracker.onWake((net.minecraft.world.entity.player.Player) event.getEntity());
    }

    private void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        if (StrawBedTracker.shouldCancelSpawnSet(
                (net.minecraft.world.entity.player.Player) event.getEntity(), event.getNewSpawn())) {
            event.setCanceled(true);
        }
    }*/
    //?}
}
