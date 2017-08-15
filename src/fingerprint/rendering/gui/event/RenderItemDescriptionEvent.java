package fingerprint.rendering.gui.event;

import fingerprint.rendering.gui.MOAType;

public class RenderItemDescriptionEvent {
    private int index;
    private MOAType from;

    public RenderItemDescriptionEvent(int index, MOAType from) {
        this.index = index;
        this.from = from;
    }

    public MOAType getFrom() {
        return from;
    }

    public int getIndex() {
        return index;
    }
}
