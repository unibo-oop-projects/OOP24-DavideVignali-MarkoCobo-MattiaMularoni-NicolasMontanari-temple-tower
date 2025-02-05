package it.unibo.templetower;

import it.unibo.templetower.view.GameViewImpl;
import javafx.application.Application;

/** Main application entry-point's class. */

public final class App {
    private App() { }

    /**
     * Main application entry-point.
     * @param args
     */
    public static void main(final String[] args) {
        Application.launch(GameViewImpl.class, args);
        
    }
}  