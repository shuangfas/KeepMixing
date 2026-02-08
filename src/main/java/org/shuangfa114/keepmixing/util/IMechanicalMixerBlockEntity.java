package org.shuangfa114.keepmixing.util;

import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;

public interface IMechanicalMixerBlockEntity {
    ScrollOptionBehaviour<MechanicalMixerSelectionMode> getSelectionMode();
}
