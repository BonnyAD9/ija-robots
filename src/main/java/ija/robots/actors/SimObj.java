package ija.robots.actors;

import ija.robots.SimHandler;

/**
 * Represents object that can be placed into a room.
 */
public class SimObj {
    private SimHandler<SimObj> onSelect;

    /**
     * Select/deselect the object.
     * @param val true if the object should be selected, otherwise false.
     */
    public void setSelected(boolean val) {
        if (onSelect == null) {
            return;
        }
        if (val) {
            onSelect.invoke(this);
        } else {
            onSelect.invoke(null);
        }
    }

    /**
     * Sets the onSelect event that is triggered when this object is
     * selected/deselected.
     * @param val The event handler.
     */
    public void onSelect(SimHandler<SimObj> val) {
        onSelect = val;
    }

    /**
     * Gets the onSelect event handler that is triggered when this object is
     * selected/deselected.
     * @return The event handler.
     */
    public SimHandler<SimObj> onSelect() {
        return onSelect;
    }
}
