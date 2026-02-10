package org.shuangfa114.keepmixing.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.shuangfa114.keepmixing.util.IMechanicalMixerBlockEntity;
import org.shuangfa114.keepmixing.util.MechanicalMixerModeSlot;
import org.shuangfa114.keepmixing.util.MechanicalMixerSelectionMode;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Debug(export = true)
@Mixin(value = MechanicalMixerBlockEntity.class, remap = false)
public abstract class MixinMechanicalMixerBlockEntity extends BasinOperatingBlockEntity implements IMechanicalMixerBlockEntity {
    @Shadow
    public boolean running;
    @Shadow
    public int runningTicks;
    @Unique
    public ScrollOptionBehaviour<MechanicalMixerSelectionMode> keepmixing$selectionMode;
    @Unique
    public boolean keepmixing$canProcess;

    public MixinMechanicalMixerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Inject(method = "addBehaviours", at = @At("RETURN"))
    private void addSelector(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        keepmixing$selectionMode = new ScrollOptionBehaviour<>(MechanicalMixerSelectionMode.class,
                Component.translatable("keepmixing.mixer.modes"), (MechanicalMixerBlockEntity) (Object) this, new MechanicalMixerModeSlot());
        behaviours.add(keepmixing$selectionMode);
    }

    @Inject(method = "read", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"), cancellable = true)
    private void test2(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        getBasin().ifPresent(bte -> bte.setAreFluidsMoving((running && runningTicks <= 20) && canProcess()));
        ci.cancel();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/basin/BasinOperatingBlockEntity;tick()V"))
    private void tick(CallbackInfo ci) {
        if (this.level != null && !this.level.isClientSide) {
            keepmixing$canProcess = true;
            if (isNonStop()) {
                running = true;
                try {
                    BasinBlockEntity basinBlockEntity = getBasin().get();
                    keepmixing$canProcess = BasinRecipe.match(basinBlockEntity, currentRecipe);
                } catch (Exception ignored) {
                    keepmixing$canProcess = false;
                }
            }
        }
        this.getPersistentData().putBoolean("canProcess", keepmixing$canProcess);
        notifyUpdate();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/mixer/MechanicalMixerBlockEntity;applyBasinRecipe()V", shift = At.Shift.AFTER))
    private void applyBasinRecipe(CallbackInfo ci) {
        if (runningTicks != 20 && isNonStop()) {
            runningTicks = 20;
        }
    }

    @Inject(method = "getRenderedHeadRotationSpeed", at = @At("HEAD"), cancellable = true)
    private void test(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (this.level != null) {
            if (isNonStop() && !canProcess()) {
                cir.setReturnValue(0f);
            }
        }
    }

    @Definition(id = "runningTicks", field = "Lcom/simibubi/create/content/kinetics/mixer/MechanicalMixerBlockEntity;runningTicks:I")
    @Expression("this.runningTicks == 20")
    @ModifyExpressionValue(method = "tick", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean renderParticles(boolean original) {
        return canProcess() && original;
    }

    @Inject(method = "onBasinRemoved", at = @At("HEAD"), cancellable = true)
    private void test3(CallbackInfo ci) {
        if (isNonStop()) {
            ci.cancel();
        }
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/mixer/MechanicalMixerBlockEntity;renderParticles()V"))
    private boolean test3(MechanicalMixerBlockEntity instance) {
        return canProcess();
    }

    @Definition(id = "runningTicks", field = "Lcom/simibubi/create/content/kinetics/mixer/MechanicalMixerBlockEntity;runningTicks:I")
    @Expression("this.runningTicks == 20")
    @ModifyExpressionValue(method = "tickAudio", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean playAudio(boolean original) {
        return canProcess() && original;
    }

    @Unique
    private boolean isNonStop() {
        return keepmixing$selectionMode.get() == MechanicalMixerSelectionMode.NON_STOP;
    }

    @Unique
    private boolean canProcess() {
        return this.getPersistentData().getBoolean("canProcess");
    }

    @Override
    public ScrollOptionBehaviour<MechanicalMixerSelectionMode> getSelectionMode() {
        return keepmixing$selectionMode;
    }
}
