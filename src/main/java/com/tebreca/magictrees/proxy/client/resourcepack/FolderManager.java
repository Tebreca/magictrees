package com.tebreca.magictrees.proxy.client.resourcepack;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class FolderManager {

	private static final Map<ResourceLocation, File> entries = new HashMap<>();
	private static File folder = new File("./generated/");
	private static File static_ = new File(folder, "static/");
	private static File dynamic = new File(folder, "dynamic/");
	private static Gson gson = new Gson();

	private FolderManager() {
	}

	public static boolean setup() {
		try {
			folder.mkdir();
			static_.mkdir();
			dynamic.mkdir();
			for (File file : Objects.requireNonNull(dynamic.listFiles())) {
				if (file.isDirectory()) {
					FileUtils.deleteDirectory(file);
				} else {
					FileUtils.forceDelete(file);
				}
			}
			if (!static_.exists() || !dynamic.exists()) {
				throw new IllegalStateException("The folders couldn't be generated");
			}
			if (!checkStatic()) {
				setupStatic();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't generate the required folders! and/or clear the dynamic folder");
			return false;
		}
		return true;
	}

	private static void setupStatic() throws URISyntaxException, IOException {
		InputStream inputStream = FolderManager.class.getResourceAsStream("/files.json");
		String[] files = gson.fromJson(new InputStreamReader(inputStream), String[].class);
		for (String filename : files) {
			File sourceFile = new File(FolderManager.class.getResource("/" + filename).toURI());
			File destination = new File(static_, filename);
			FileUtils.copyFile(sourceFile, destination);
		}
	}

	private static boolean checkStatic() {
		InputStream inputStream = FolderManager.class.getResourceAsStream("/files.json");
		String[] files = gson.fromJson(new InputStreamReader(inputStream), String[].class);
		List<String> fileNames = Arrays.asList(files);
		try {
			return Arrays.stream(Objects.requireNonNull(static_.listFiles())).map(File::getName).collect(Collectors.toList()).containsAll(fileNames);
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Nullable
	public static File forResource(ResourceLocation location, String extension) {
		String name = String.format("%s/%s.%s", location.getNamespace(), location.getPath(), extension);
		File file = new File(dynamic, name);
		try {
			FileUtils.touch(file);
		} catch (IOException e) {
			return null;
		}
		entries.put(location, file);
		return file;
	}

	public static InputStream streamEntry(ResourceLocation location) throws FileNotFoundException {
		return new FileInputStream(entries.get(location));
	}

	public static File getRootFolder() {
		return folder;
	}

	public static File getStaticFolder() {
		return static_;
	}

	public static File getDynamicFolder() {
		return dynamic;
	}

	public static ResourceLocation[] getEntries() {
		return entries.keySet().toArray(new ResourceLocation[0]);
	}

	public static boolean containsEntry(ResourceLocation location) {
		return entries.containsKey(location);
	}

	public static String[] getNameSpaces() {
		List<String> namespaces = new ArrayList<>();
		for (Map.Entry<ResourceLocation, File> entry : entries.entrySet()) {
			if (!namespaces.contains(entry.getKey().getNamespace())) {
				namespaces.add(entry.getKey().getNamespace());
			}
		}
		return namespaces.toArray(new String[0]);
	}

	public static JsonObject getMetaDataObject() {
		return gson.fromJson("{\"pack\": {\"description\":\"treemagic's dynamically generated resources\",\"pack_format\": 4}}", JsonObject.class);
	}
}