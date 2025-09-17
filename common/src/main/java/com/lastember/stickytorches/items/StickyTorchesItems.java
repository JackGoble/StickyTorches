package com.lastember.stickytorches.items;

import com.lastember.stickytorches.StickyTorchesMod;
import com.lastember.stickytorches.blocks.StickyTorchesBlocks;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public class StickyTorchesItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(StickyTorchesMod.MOD_ID, Registries.ITEM);

    public static RegistrySupplier<Item> STICKY_TORCH;

    public static void initItems() {
        STICKY_TORCH = registerItem("sticky_torch",
                () -> new StickyTorchItem(
                        StickyTorchesBlocks.STICKY_TORCH.get(),
                        baseProperties("sticky_torch").arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
                )
        );

        ITEMS.register();
    }

    public static RegistrySupplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(ResourceLocation.fromNamespaceAndPath(StickyTorchesMod.MOD_ID, name), item);

    }

    public static Item.Properties baseProperties(String name) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(StickyTorchesMod.MOD_ID, name)));
    }
}
