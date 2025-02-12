package it.unibo.templetower.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import org.apache.commons.io.FileUtils;

/**
 * Model class for handling modding menu operations.
 * Manages tower data and file operations.
 */
public class ModdingMenuModel {
    private static final String USER_TOWERS_DIR = System.getProperty("user.home") + File.separator + "temple-tower-mods";
    private final List<String> importedTowers;

    /**
     * Constructs a new ModdingMenuModel and initializes the user towers directory.
     */
    public ModdingMenuModel() {
        this.importedTowers = new ArrayList<>();
        initializeUserDirectory();
        loadExistingTowers();
    }

    /**
     * Imports a tower folder into the user's mods directory.
     *
     * @param sourceFolder the folder to import
     * @return true if the import was successful, false otherwise
     * @throws IOException if there's an error during file operations
     */
    public boolean importFolder(final File sourceFolder) throws IOException {
        if (!sourceFolder.isDirectory()) {
            return false;
        }

        String towerName = sourceFolder.getName();
        Path destinationPath = Paths.get(USER_TOWERS_DIR, towerName);

        // Check if tower already exists
        if (Files.exists(destinationPath)) {
            return false;
        }

        // Copy the folder recursively
        copyFolder(sourceFolder.toPath(), destinationPath);
        importedTowers.add(towerName);
        return true;
    }

    /**
     * Imports a tower from a ZIP file into the user's mods directory.
     *
     * @param zipFile the ZIP file to import
     * @return true if the import was successful, false otherwise
     * @throws IOException if there's an error during file operations
     */
    public boolean importZip(final File zipFile) throws IOException {
        if (!zipFile.exists() || !zipFile.getName().toLowerCase().endsWith(".zip")) {
            return false;
        }

        String towerName = zipFile.getName().replaceFirst("[.][^.]+$", "");
        Path destinationPath = Paths.get(USER_TOWERS_DIR, towerName);

        // Check if tower already exists
        if (Files.exists(destinationPath)) {
            return false;
        }

        // Create the destination directory
        Files.createDirectories(destinationPath);

        // Unzip the file
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entryPath = destinationPath.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zis, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }

        importedTowers.add(towerName);
        return true;
    }

    /**
     * Gets the list of imported tower names.
     *
     * @return List of tower names
     */
    public List<String> getImportedTowers() {
        return new ArrayList<>(importedTowers);
    }

    private void initializeUserDirectory() {
        File userDir = new File(USER_TOWERS_DIR);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
    }

    private void loadExistingTowers() {
        File userDir = new File(USER_TOWERS_DIR);
        File[] towers = userDir.listFiles(File::isDirectory);
        if (towers != null) {
            for (File tower : towers) {
                importedTowers.add(tower.getName());
            }
        }
    }

    private void copyFolder(Path source, Path destination) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = destination.resolve(source.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, destination.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Gets the path to the user's towers directory.
     *
     * @return String representing the path to the user's towers directory
     */
    public String getUserTowersDirectory() {
        return USER_TOWERS_DIR;
    }
}
