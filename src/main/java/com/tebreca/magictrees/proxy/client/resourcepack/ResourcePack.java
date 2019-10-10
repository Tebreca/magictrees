package com.tebreca.magictrees.proxy.client.resourcepack;

import com.tebreca.magictrees.Constants;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.input.ReaderInputStream;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ResourcePack implements IResourcePack {

	ResourceLocation location = new ResourceLocation(Constants.MODID, "iron_dust");

	@MethodsReturnNonnullByDefault
	@Override
	public InputStream getRootResourceStream(String fileName) throws IOException {
		return new FileInputStream(new File(FolderManager.getRootFolder(), fileName));
	}

	@MethodsReturnNonnullByDefault
	@Override
	public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {
		if (type == ResourcePackType.CLIENT_RESOURCES) {
			if (FolderManager.containsEntry(location)) {
				return FolderManager.streamEntry(location);
			}
		}
		return new ReaderInputStream(new StringReader(""), Charset.defaultCharset());
	}

	@MethodsReturnNonnullByDefault
	@Override
	public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String pathIn, int maxDepth, Predicate<String> filter) {
		return type == ResourcePackType.CLIENT_RESOURCES ? Arrays.asList(FolderManager.getEntries()) : Collections.emptyList();
	}

	@MethodsReturnNonnullByDefault
	@Override
	public boolean resourceExists(ResourcePackType type, ResourceLocation location) {
		return type == ResourcePackType.CLIENT_RESOURCES && FolderManager.containsEntry(location);
	}

	@MethodsReturnNonnullByDefault
	@Override
	public Set<String> getResourceNamespaces(ResourcePackType type) {
		return type == ResourcePackType.CLIENT_RESOURCES ? new HashSet<>(Arrays.asList(FolderManager.getNameSpaces())) : Collections.emptySet();
	}

	@Nullable
	@Override
	public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) throws IOException {
		return deserializer.deserialize(FolderManager.getMetaDataObject());
	}

	@Override
	public String getName() {
		return "Magictrees' Dynamic Resources";
	}

	@Override
	public void close() throws IOException {

	}
}
