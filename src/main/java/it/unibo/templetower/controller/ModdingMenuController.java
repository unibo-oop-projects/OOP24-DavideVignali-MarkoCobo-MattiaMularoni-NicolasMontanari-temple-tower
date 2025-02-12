package it.unibo.templetower.controller;

import it.unibo.templetower.model.ModdingMenuModel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import it.unibo.templetower.utils.Pair;

/**
 * Controller class for the modding menu.
 * Handles business logic and coordinates between view and model.
 */
public class ModdingMenuController {
    private final ModdingMenuModel model;

    /**
     * Constructs a new ModdingMenuController with its associated model.
     */
    public ModdingMenuController() {
        this.model = new ModdingMenuModel();
    }

    /**
     * Handles the import of a tower folder.
     * Validates the tower configuration and structure before importing.
     *
     * @param folder the folder to import
     * @return Optional containing error message if import fails, empty if successful
     */
    public Optional<String> importFolder(final File folder) {
        try {
            if (!folder.exists() || !folder.isDirectory()) {
                return Optional.of("Selected path is not a valid directory");
            }

            if (!model.importFolder(folder)) {
                return Optional.of("Invalid tower configuration or tower already exists");
            }

            return Optional.empty();
        } catch (IOException e) {
            return Optional.of("Error importing tower: " + e.getMessage());
        }
    }

    /**
     * Handles the import of a tower ZIP file.
     * Validates the tower configuration and structure before importing.
     *
     * @param zipFile the ZIP file to import
     * @return Optional containing error message if import fails, empty if successful
     */
    public Optional<String> importZip(final File zipFile) {
        if (!zipFile.exists()) {
            return Optional.of("Selected file does not exist");
        }

        if (!model.importZip(zipFile)) {
            return Optional.of("Invalid tower configuration or tower already exists");
        }

        return Optional.empty();
    }

    /**
     * Gets the list of imported tower names.
     *
     * @return List of tower names
     */
    public List<String> getImportedTowers() {
        return model.getImportedTowers();
    }

    /**
     * Gets the path to the user's towers directory.
     *
     * @return String representing the path to the user's towers directory
     */
    public String getUserTowersDirectory() {
        return model.getUserTowersDirectory();
    }

    /**
     * Clears the user's towers directory.
     *
     * @return Optional containing an error message if something goes wrong, empty otherwise
     */
    public Optional<String> clearTowersDirectory() {
        try {
            this.model.deleteAllTowers();
            return Optional.empty();
        } catch (IOException e) {
            return Optional.of("Failed to delete towers directory: " + e.getMessage());
        }
    }

    /**
     * Gets the tower information (name and description) for a given tower directory name.
     *
     * @param towerDirName the name of the tower directory
     * @return Optional containing the tower info pair, or empty if not found
     */
    public Optional<Pair<String, String>> getTowerInfo(final String towerDirName) {
        return model.getTowerInfo(towerDirName);
    }

    /**
     * Gets the height of a specific tower.
     *
     * @param towerDirName the name of the tower directory
     * @return the height of the tower
     * @throws IllegalArgumentException if the tower is invalid or height is missing
     */
    public int getTowerHeight(final String towerDirName) {
        return model.getTowerHeight(towerDirName);
    }
}
