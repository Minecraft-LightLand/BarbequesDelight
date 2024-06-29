package com.mao.barbequesdelight.compat.jade;

import com.mao.barbequesdelight.content.block.GrillBlockEntity;
import com.mao.barbequesdelight.init.BarbequesDelight;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.ui.ItemStackElement;

public class GrillInfo implements IBlockComponentProvider {

	public static final ResourceLocation ID = new ResourceLocation(BarbequesDelight.MODID, "grill");

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor access, IPluginConfig config) {
		if (access.getBlockEntity() instanceof GrillBlockEntity be) {
			for (int i = 0; i <= 1; i++) {
				var s = be.entries[i];
				if (s.stack.isEmpty()) continue;
				tooltip.add(ItemStackElement.of(s.stack,0.5f));
				tooltip.append(s.getTooltip());
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return ID;
	}
}
