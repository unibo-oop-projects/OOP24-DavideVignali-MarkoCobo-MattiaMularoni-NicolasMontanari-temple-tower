package it.unibo.templetower.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import it.unibo.templetower.controller.GameDataManagerImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import it.unibo.templetower.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model class for handling modding menu operations.
 * Manages tower data and file operations.
 */
public class ModdingMenuModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModdingMenuModel.class);
    private static final String USER_TOWERS_DIR = System.getProperty("user.home") + File.separator + "temple-tower-mods";
    private static final int DEFAULT_MAX_SEARCH_DEPTH = 3;
    private static final String TOWER_CONFIG_FILENAME = "tower.json";
    private final List<String> importedTowers;
    private final Map<String, Pair<String, String>> towerInfo = new HashMap<>();
    private final GameDataManagerImpl gameDataManager = new GameDataManagerImpl();

    /**
     * Constructs a new ModdingMenuModel and initializes the user towers directory.
     */
    public ModdingMenuModel() {
        this.importedTowers = new ArrayList<>();
        if (!initializeUserDirectory()) {
            throw new IllegalStateException("Failed to create user directory");
        }
        loadExistingTowers();
    }

    /**
     * Imports a tower folder into the user's mods directory.
     * Validates the tower configuration before importing.
     *
     * @param sourceFolder the folder to import
     * @return true if the import was successful, false otherwise
     * @throws IOException if there's an error during file operations
     */
    public boolean importFolder(final File sourceFolder) throws IOException {
        if (!sourceFolder.isDirectory()) {
            return false;
        }

        final String towerName = sourceFolder.getName();
        final Path destinationPath = Paths.get(USER_TOWERS_DIR, towerName);

        // Check if tower already exists
        if (Files.exists(destinationPath)) {
            return false;
        }

        // Copy the folder recursively
        copyFolder(sourceFolder.toPath(), destinationPath);

        // Validate the tower after copying
        if (!validateTower(destinationPath)) {
            FileUtils.deleteDirectory(destinationPath.toFile());
            return false;
        }

        // Load tower info after successful import
        try {
            final File towerJson = new File(destinationPath.toFile(), TOWER_CONFIG_FILENAME);
            if (towerJson.exists()) {
                final Pair<String, String> info = gameDataManager.loadTowerInfo(towerJson.getPath());
                towerInfo.put(towerName, info);
                importedTowers.add(towerName);
                return true;
            }
        } catch (IOException e) {
            FileUtils.deleteDirectory(destinationPath.toFile());
            return false;
        }
        return false;
    }

    /**
     * Imports a tower from a ZIP file into the user's mods directory.
     * Validates the tower configuration before finalizing the import.
     *
     * @param zipFile the ZIP file to import
     * @return true if the import was successful, false otherwise
     * @throws IOException if there's an error during file operations
     */
    public boolean importZip(final File zipFile) {
        if (!zipFile.exists() || !zipFile.getName().toLowerCase(Locale.getDefault()).endsWith(".zip")) {
            return false;
        }
        final String towerName = zipFile.getName().replaceFirst("[.][^.]+$", "");
        final Path destinationPath = Paths.get(USER_TOWERS_DIR, towerName);
        if (Files.exists(destinationPath)) {
            return false;
        }
        // Create temporary directory for extraction
        final Path tempDestPath = Paths.get(USER_TOWERS_DIR, towerName + "_temp");
        try {
            Files.createDirectories(tempDestPath);
        } catch (IOException e) {
            LOGGER.error("Failed to create temporary directory", e);
            return false;
        }

        if (!extractZipFile(zipFile, tempDestPath) || !validateTower(tempDestPath)) {
            try {
                FileUtils.deleteDirectory(tempDestPath.toFile());
            } catch (IOException e) {
                LOGGER.error("Failed to clean up temporary directory", e);
            }
            return false;
        }

        // Try to load tower info before moving to final location
        try {
            final File towerJson = new File(tempDestPath.toFile(), TOWER_CONFIG_FILENAME);
            if (!towerJson.exists()) {
                FileUtils.deleteDirectory(tempDestPath.toFile());
                return false;
            }
            final Pair<String, String> info = gameDataManager.loadTowerInfo(towerJson.getPath());
            Files.move(tempDestPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            towerInfo.put(towerName, info);
            importedTowers.add(towerName);
            return true;
        } catch (IOException e) {
            // Clean up if anything goes wrong
            try {
                FileUtils.deleteDirectory(tempDestPath.toFile());
            } catch (IOException cleanupError) {
                LOGGER.error("Failed to clean up after error", cleanupError);
            }
            return false;
        }
    }

    private boolean extractZipFile(final File zipFile, final Path tempDestPath) {
        if (zipFile == null || tempDestPath == null) {
            return false;
        }
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                final String entryName = entry.getName();
                if (entryName == null) {
                    zis.closeEntry();
                    entry = zis.getNextEntry();
                    continue;
                }
                final Path entryPath = tempDestPath.resolve(entryName);
                // Add null check for entryPath
                if (entryPath == null || !entryPath.normalize().startsWith(tempDestPath.normalize())) {
                    LOGGER.error("Zip entry is outside of the target directory or path is null");
                    return false;
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    final Path parent = entryPath.getParent();
                    if (parent != null) {
                        Files.createDirectories(parent);
                    }
                    Files.copy(zis, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
                entry = zis.getNextEntry();
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Failed to extract zip file", e);
            return false;
        }
    }

    /**
     * Gets the list of imported tower names.
     *
     * @return List of tower names
     */
    public List<String> getImportedTowers() {
        return new ArrayList<>(importedTowers);
    }

    private boolean initializeUserDirectory() {
        final File userDir = new File(USER_TOWERS_DIR);
        return userDir.exists() || userDir.mkdirs();
    }

    private void loadExistingTowers() {
        final File userDir = new File(USER_TOWERS_DIR);
        final File[] towers = userDir.listFiles(File::isDirectory);
        if (towers != null) {
            for (final File tower : towers) {
                try {
                    final File towerJson = new File(tower, TOWER_CONFIG_FILENAME);
                    if (towerJson.exists()) {
                        final Pair<String, String> info = gameDataManager.loadTowerInfo(towerJson.getPath());
                        towerInfo.put(tower.getName(), info);
                        importedTowers.add(tower.getName());
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to load tower info", e);
                }
            }
        }
    }

    private void copyFolder(final Path source, final Path destination) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                final Path targetDir = destination.resolve(source.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                Files.copy(file, destination.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Validates a tower directory by checking for a valid tower.json configuration.
     *
     * @param towerDir the directory containing the tower files
     * @return true if the tower is valid, false otherwise
     */
    private boolean validateTower(final Path towerDir) {
        if (!Files.exists(towerDir)) {
            LOGGER.error("Tower directory does not exist: {}", towerDir);
            return false;
        }
        final Path towerJsonPath = findTowerJson(towerDir, DEFAULT_MAX_SEARCH_DEPTH);
        if (towerJsonPath == null) {
            return false;
        }
        try {
            gameDataManager.loadGameDataFromTower(towerJsonPath.toString());
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Tower validation failed", e);
            return false;
        }
    }

    private Path findTowerJson(final Path baseDir, final int maxDepth) {
        if (!Files.exists(baseDir)) {
            LOGGER.error("Base directory does not exist: {}", baseDir);
            return null;
        }
        try (Stream<Path> stream = Files.walk(baseDir, maxDepth)) {
            return stream
                .filter(path -> path != null)
                .filter(path -> {
                    try {
                        return Files.exists(path) && Files.isRegularFile(path);
                    } catch (SecurityException e) {
                        return false;
                    }
                })
                .filter(path -> {
                    try {
                        final Path fileName = path.getFileName();
                        return fileName != null && TOWER_CONFIG_FILENAME.equals(fileName.toString());
                    } catch (SecurityException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
        } catch (IOException e) {
            LOGGER.error("Failed to search for tower.json", e);
            return null;
        }
    }

    /**
     * Gets the path to the user's towers directory.
     *
     * @return String representing the path to the user's towers directory
     */
    public String getUserTowersDirectory() {
        return USER_TOWERS_DIR;
    }

    /**
     * Deletes all towers in the user's directory and reinitializes it.
     *
     * @throws IOException if there's an error during directory deletion or creation
     */
    public void deleteAllTowers() throws IOException {
        final File userDir = new File(USER_TOWERS_DIR);
        if (userDir.exists()) {
            FileUtils.deleteDirectory(userDir);
        }
        this.importedTowers.clear();
        this.towerInfo.clear(); // Clear the tower info map too
        if (!initializeUserDirectory()) {
            throw new IOException("Failed to reinitialize user directory after deletion");
        }
    }

    /**
     * Gets the tower information (name and description) for a given tower directory name.
     *
     * @param towerDirName the name of the tower directory
     * @return Optional containing the tower info pair, or empty if not found
     */
    public Optional<Pair<String, String>> getTowerInfo(final String towerDirName) {
        return Optional.ofNullable(towerInfo.get(towerDirName));
    }

    /**
     * Gets the height of a specific tower from its tower.json configuration.
     *
     * @param towerDirName the name of the tower directory
     * @return the tower height, throws IllegalArgumentException if tower is invalid
     */
    public int getTowerHeight(final String towerDirName) {
        final Path towerPath = Paths.get(USER_TOWERS_DIR, towerDirName, TOWER_CONFIG_FILENAME);
        final GameDataManagerImpl gameDataManager = new GameDataManagerImpl();
        try {
            gameDataManager.loadGameDataFromTower(towerPath.toString());
            return gameDataManager.getTower().height();
        } catch (IllegalArgumentException e) {
            final String msg = "Invalid tower configuration: " + e.getMessage();
            LOGGER.error(msg, e);
            throw new IllegalArgumentException(msg, e);
        }
    }
}
