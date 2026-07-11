package com.example.strawbed.registry;

import com.example.strawbed.StrawBedMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StrawBedMod.MOD_ID);

    public static final DeferredItem<BlockItem> STRAW_BED = ITEMS.register("straw_bed",
            () -> new BlockItem(ModBlocks.STRAW_BED.get(), new Item.Properties().stacksTo(16)) {
                @Override
                protected boolean placeBlock(net.minecraft.world.item.context.BlockPlaceContext context, net.minecraft.world.level.block.state.BlockState state) {
                    return context.getLevel().setBlock(context.getClickedPos(), state, 26);
                }
            });

    private ModItems() {
    }
}
