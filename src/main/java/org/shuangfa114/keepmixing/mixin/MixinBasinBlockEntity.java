package org.shuangfa114.keepmixing.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//the fucking bug make me unable to play ccvm!!!!
@Mixin(value = BasinBlockEntity.class, remap = false)
public abstract class MixinBasinBlockEntity {
    @ModifyArg(method = "lazyTick",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;setAreFluidsMoving(Z)Z",ordinal = 1))
    public boolean lazyTick(boolean value, @Local MechanicalMixerBlockEntity mixer) {
        return (mixer.running && mixer.runningTicks <= 20)&&mixer.getPersistentData().getBoolean("canProcess");
    }
}
