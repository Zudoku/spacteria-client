package fingerprint.rendering.gui;

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
