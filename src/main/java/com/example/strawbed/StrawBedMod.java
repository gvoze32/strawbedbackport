package com.example.strawbed;

import com.example.strawbed.registry.ModBlocks;
import com.example.strawbed.registry.ModItems;
import com.example.strawbed.registry.ModSounds;
import com.example.strawbed.registry.ModStats;
import com.example.strawbed.world.StrawBedTracker;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;

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
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.world.level.block.ComposterBlock.COMPOSTABLES.put(ModItems.STRAW_BED.get(), 0.65f);
        });
    }

    private void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.STRAW_BED);
        }
    }

    private void onPlayerWakeUp(PlayerWakeUpEvent event) {
        StrawBedTracker.onWake(event.getEntity());
    }

    private void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        if (StrawBedTracker.shouldCancelSpawnSet(event.getEntity(), event.getNewSpawn())) {
            event.setCanceled(true);
        }
    }
}
