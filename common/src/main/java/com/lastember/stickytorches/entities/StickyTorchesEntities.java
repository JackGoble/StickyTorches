package com.lastember.stickytorches.entities;

import com.lastember.stickytorches.StickyTorchesMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class StickyTorchesEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(StickyTorchesMod.MOD_ID, Registries.ENTITY_TYPE);

    public static RegistrySupplier<EntityType<StickyTorchEntity>> STICKY_TORCH_ENTITY;

    public static void initEntityTypes() {
        STICKY_TORCH_ENTITY = registerEntityType("sticky_torch_entity", () -> EntityType.Builder.of((EntityType<StickyTorchEntity> type, Level level) -> new StickyTorchEntity(type, level), MobCategory.MISC)
                .sized(0.25F, 0.25F) // Sets the entity's hitbox size.
                .clientTrackingRange(6) // Sets the tracking range for the client.
                .updateInterval(10) // Sets how often the entity updates clients.
                .build(ResourceKey.create(Registries.ENTITY_TYPE,ResourceLocation.fromNamespaceAndPath(StickyTorchesMod.MOD_ID, "sticky_torch_entity")))); // Builds the entity type.

        ENTITIES.register();
    }

    public static <T extends Entity> RegistrySupplier<EntityType<T>> registerEntityType(String name, Supplier<EntityType<T>> entityType) {
        return ENTITIES.register(ResourceLocation.fromNamespaceAndPath(StickyTorchesMod.MOD_ID, name), entityType);
    }

}
