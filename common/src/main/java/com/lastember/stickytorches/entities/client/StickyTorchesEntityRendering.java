package com.lastember.stickytorches.entities.client;

import com.lastember.stickytorches.entities.StickyTorchesEntities;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class StickyTorchesEntityRendering {
    public static void initEntityRendering() {
        EntityRendererRegistry.register(StickyTorchesEntities.STICKY_TORCH_ENTITY, ThrownItemRenderer::new);

    }
}
