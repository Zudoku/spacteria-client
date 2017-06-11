package fingerprint.networking.events;

public class LoadNewMapEvent {
    private String mapdata;
    private int width;
    private int height;
    private int type;
    private String name;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getType() {
        return type;
    }

    public String getMapdata() {
        return mapdata;
    }

    public String getName() {
        return name;
    }
}
