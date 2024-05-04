/**
 * @file
 * @authors Jakub Antonín Štigler (xstigl00)
 * @brief Base class for objects that can be placed into a room.
 */

package ija.robots.actors;

import java.util.function.Consumer;

/**
 * Represents object that can be placed into a room.
 */
public class SimObj {
    private Consumer<SimObj> onSelect;

    /**
     * Select/deselect the object.
     * @param val true if the object should be selected, otherwise false.
     */
    public void setSelected(boolean val) {
        if (onSelect == null) {
            return;
        }
        if (val) {
            onSelect.accept(this);
        } else {
            onSelect.accept(null);
        }
    }

    /**
     * Sets the onSelect event that is triggered when this object is
     * selected/deselected.
     * @param val The event handler.
     */
    public void onSelect(Consumer<SimObj> val) {
        onSelect = val;
    }

    /**
     * Gets the onSelect event handler that is triggered when this object is
     * selected/deselected.
     * @return The event handler.
     */
    public Consumer<SimObj> onSelect() {
        return onSelect;
    }
}
