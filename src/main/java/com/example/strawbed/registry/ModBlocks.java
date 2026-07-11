package com.example.strawbed.registry;

import com.example.strawbed.StrawBedMod;
import com.example.strawbed.block.StrawBedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(StrawBedMod.MOD_ID);

    public static final DeferredBlock<Block> STRAW_BED = BLOCKS.register("straw_bed",
            () -> new StrawBedBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .sound(ModSounds.STRAW_BED_SOUNDS)
                    .strength(0.2f)
                    .noOcclusion()
                    .ignitedByLava()));

    private ModBlocks() {
    }
}
