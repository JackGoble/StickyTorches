package com.lastember.stickytorches;

import com.lastember.stickytorches.blocks.StickyTorchesBlocks;
import com.lastember.stickytorches.entities.StickyTorchesEntities;
import com.lastember.stickytorches.entities.client.StickyTorchesEntityRendering;
import com.lastember.stickytorches.items.StickyTorchesItems;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StickyTorchesMod {
    public static final String MOD_ID = "sticky_torches";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        // Write common init code here.
        LOGGER.info("Initializing Sticky Torches");


        StickyTorchesBlocks.initBlocks();
        StickyTorchesItems.initItems();
        StickyTorchesEntities.initEntityTypes();

        ClientLifecycleEvent.CLIENT_STARTED.register(listener -> {
            StickyTorchesEntityRendering.initEntityRendering();
        });
    }
}
