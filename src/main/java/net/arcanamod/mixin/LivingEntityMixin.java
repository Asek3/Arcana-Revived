package net.arcanamod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.arcanamod.entities.TaintedGooWrapper;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TaintedGooWrapper {
    @Unique
    int gooTicks;

    @Override
    public int getGooTicks() {
        return gooTicks;
    }

    @Override
    public void setGooTicks(int value) {
        gooTicks = value;
    }
}
