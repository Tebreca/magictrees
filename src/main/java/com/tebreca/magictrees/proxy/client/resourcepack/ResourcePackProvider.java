package com.tebreca.magictrees.proxy.client.resourcepack;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.text.StringTextComponent;

import java.util.Map;

public class ResourcePackProvider implements IPackFinder {

	@Override
	public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
		T t = packInfoFactory.create("treespack", true, ResourcePack::new, new ResourcePack(), new PackMetadataSection(new StringTextComponent("a dynamically generated resource pack for the mineral trees"), 4), ResourcePackInfo.Priority.BOTTOM);
		nameToPackMap.put("treespack", t);
	}
}
