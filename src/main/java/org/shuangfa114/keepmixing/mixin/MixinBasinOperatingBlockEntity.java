package org.shuangfa114.keepmixing.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import org.shuangfa114.keepmixing.util.IMechanicalMixerBlockEntity;
import org.shuangfa114.keepmixing.util.MechanicalMixerSelectionMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public abstract class MixinBasinOperatingBlockEntity {
    @ModifyExpressionValue(method = "updateBasin", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/basin/BasinOperatingBlockEntity;isRunning()Z"))
    private boolean isRunning(boolean value) {
        if ((Object) this instanceof MechanicalMixerBlockEntity) {
            return ((IMechanicalMixerBlockEntity) this).getSelectionMode().get() != MechanicalMixerSelectionMode.NON_STOP && value;
        }
        return value;
    }
}
