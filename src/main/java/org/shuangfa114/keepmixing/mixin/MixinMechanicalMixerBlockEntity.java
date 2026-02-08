package org.shuangfa114.keepmixing.mixin;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import net.minecraft.network.chat.Component;
import org.shuangfa114.keepmixing.util.IMechanicalMixerBlockEntity;
import org.shuangfa114.keepmixing.util.MechanicalMixerModeSlot;
import org.shuangfa114.keepmixing.util.MechanicalMixerSelectionMode;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = MechanicalMixerBlockEntity.class,remap = false)
public abstract class MixinMechanicalMixerBlockEntity implements IMechanicalMixerBlockEntity {
    @Unique
    public ScrollOptionBehaviour<MechanicalMixerSelectionMode> keepmixing$selectionMode;

    @Inject(method = "addBehaviours", at = @At("RETURN"))
    private void addSelector(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        keepmixing$selectionMode = new ScrollOptionBehaviour<>(MechanicalMixerSelectionMode.class,
                Component.translatable("keepmixing.mixer.modes"), (MechanicalMixerBlockEntity) (Object) this, new MechanicalMixerModeSlot());
        behaviours.add(keepmixing$selectionMode);
    }

    @Override
    public ScrollOptionBehaviour<MechanicalMixerSelectionMode> getSelectionMode() {
        return keepmixing$selectionMode;
    }
}
