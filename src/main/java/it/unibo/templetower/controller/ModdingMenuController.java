package it.unibo.templetower.controller;

import it.unibo.templetower.model.ModdingMenuModel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
                return Optional.of("Tower already exists or invalid tower folder");
            }

            return Optional.empty();
        } catch (IOException e) {
            return Optional.of("Error importing tower: " + e.getMessage());
        }
    }

    /**
     * Handles the import of a tower ZIP file.
     *
     * @param zipFile the ZIP file to import
     * @return Optional containing error message if import fails, empty if successful
     */
    public Optional<String> importZip(final File zipFile) {
        try {
            if (!zipFile.exists()) {
                return Optional.of("Selected file does not exist");
            }

            if (!model.importZip(zipFile)) {
                return Optional.of("Tower already exists or invalid ZIP file");
            }

            return Optional.empty();
        } catch (IOException e) {
            return Optional.of("Error importing ZIP: " + e.getMessage());
        }
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
}
