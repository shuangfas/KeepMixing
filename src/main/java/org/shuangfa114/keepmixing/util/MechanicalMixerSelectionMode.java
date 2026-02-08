package org.shuangfa114.keepmixing.util;

import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.gui.AllIcons;
import net.createmod.catnip.lang.Lang;

public enum MechanicalMixerSelectionMode implements INamedIconOptions {
    VANILLA(AllIcons.I_TUNNEL_SPLIT),
    NON_STOP(AllIcons.I_TUNNEL_FORCED_SPLIT);
    private final String translationKey;
    private final AllIcons icon;

    MechanicalMixerSelectionMode(AllIcons icon) {
        this.icon = icon;
        this.translationKey = "keepmixing.mixer.selection_mode." + Lang.asId(name());
    }

    @Override
    public AllIcons getIcon() {
        return icon;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }
}
