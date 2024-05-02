package ija.robots.actors;

import ija.robots.SimHandler;

public class SimObj {
    private SimHandler<SimObj> onSelect;

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

    public void onSelect(SimHandler<SimObj> val) {
        onSelect = val;
    }

    public SimHandler<SimObj> onSelect() {
        return onSelect;
    }
}
