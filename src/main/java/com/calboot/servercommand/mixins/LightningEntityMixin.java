package com.calboot.servercommand.mixins;

import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LightningEntity.class)
public interface LightningEntityMixin {

    @Invoker("getAffectedBlockPos")
    BlockPos getStuckBlockPos();

}
