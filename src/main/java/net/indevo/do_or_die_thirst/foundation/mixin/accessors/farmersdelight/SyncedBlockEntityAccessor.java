package net.indevo.do_or_die_thirst.foundation.mixin.accessors.farmersdelight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

@Mixin(SyncedBlockEntity.class)
public interface SyncedBlockEntityAccessor
{
    @Invoker(remap = false)
    void invokeInventoryChanged();
}
