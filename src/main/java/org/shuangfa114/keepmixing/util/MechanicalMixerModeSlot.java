package org.shuangfa114.keepmixing.util;

import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import net.minecraft.core.Direction;

public class MechanicalMixerModeSlot extends CenteredSideValueBoxTransform {
    public MechanicalMixerModeSlot() {
        super(((blockState, direction) -> (direction == Direction.EAST || direction == Direction.NORTH || direction == Direction.SOUTH || direction == Direction.WEST)));
    }

    @Override
    public int getOverrideColor() {
        return 0x592424;
    }
}
