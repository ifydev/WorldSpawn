package me.ifydev.worldspawn.api.library;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.ifydev.worldspawn.api.util.MiscUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Innectic
 * @since 08/07/2018
 */
public final class LibraryHandler {

    public boolean loadLibraries(Logger logger) {
        // Loop through list from json
        try {
            InputStream stream = getClass().getResourceAsStream("/libraries.json");
            if (stream == null) return false;

            JsonArray libraryArray = new Gson().fromJson(new InputStreamReader(stream), JsonArray.class);
            MiscUtil.convertIteratorToList(libraryArray.iterator()).stream().map(entry -> (JsonObject) entry)
                    .map(entry -> new MavenObject(entry.get("groupId").getAsString(), entry.get("artifactId").getAsString(),
                            entry.get("version").getAsString(), entry.get("repo").getAsString())).forEach(lib -> this.handle(lib, logger));
        } catch (Exception e) {
            logger.severe("Could not load libraries.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void handle(MavenObject library, Logger logger) {
        Set<File> jars = new HashSet<>();
        try {
            File location = getWriteLocation(library);
            if (!location.exists()) {
                logger.info("Downloading: " + library.getArtifactId());
                try (InputStream inputStream = new URL(library.getRepo() + "/" + getPath(library) + getFileName(library)).openStream()) {
                    Files.copy(inputStream, location.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            jars.add(location);
        } catch (Exception e) {
            logger.severe("Could not load library: " + library.getArtifactId());
            e.printStackTrace();
        }
        jars.forEach(jar -> {
            try {
                addURL(jar.toURI().toURL());
            } catch (IOException e) {
                logger.severe("Could not load library: " + jar.getName());
                return;
            }
            logger.info("Library loaded: " + jar.getName());
        });
    }

    private String getPath(MavenObject library) {
        return library.getGroupId().replaceAll("\\.", "/") + "/" + library.getArtifactId() + "/" + library.getVersion() + "/";
    }

    private String getFileName(MavenObject library) {
        return library.getArtifactId() + "-" + library.getVersion() + ".jar";
    }

    private File getWriteLocation(MavenObject library) throws IOException {
        File rootDir = new File(".libs");
        if ((!rootDir.exists() || !rootDir.isDirectory()) && !rootDir.mkdir()) {
            throw new IOException("Could not create root directory .libs");
        }
        File path = new File(rootDir, getPath(library));
        path.mkdirs();
        return new File(path, getFileName(library));
    }

    private void addURL(URL url) throws IOException {
        // TODO: We should probably move this to loading on the plugin class loader
        URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> sysClass = URLClassLoader.class;
        try {
            Method method = sysClass.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(sysLoader, url);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }
}
