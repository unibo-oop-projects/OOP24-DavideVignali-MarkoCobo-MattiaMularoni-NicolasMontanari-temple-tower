package it.unibo.templetower.view;

/**
 * Interface for views that need to be notified when their scene becomes active.
 * This follows the Observer pattern, allowing views to react when they are displayed.
 */
public interface SceneActivationListener {
    /**
     * Called when the scene containing this view becomes active.
     */
    void onSceneActivated();
}
