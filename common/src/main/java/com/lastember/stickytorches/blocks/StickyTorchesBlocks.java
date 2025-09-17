package com.lastember.stickytorches.blocks;

import com.lastember.stickytorches.StickyTorchesMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;



public class StickyTorchesBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(StickyTorchesMod.MOD_ID, Registries.BLOCK);

    public static RegistrySupplier<Block> STICKY_TORCH;

    public static void initBlocks() {
        STICKY_TORCH = registerBlock("sticky_torch", () -> new StickyTorchBlock(baseProperties("sticky_torch")));

        BLOCKS.register();
    }

    public static RegistrySupplier<Block> registerBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(ResourceLocation.fromNamespaceAndPath(StickyTorchesMod.MOD_ID, name), block);
    }

    public static BlockBehaviour.Properties baseProperties(String name) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(StickyTorchesMod.MOD_ID, name)));
    }
}
