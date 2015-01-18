package fingerprint.rendering;

public enum RenderingResolutions {
    
    RES_640_480(640,480),
    RES_800_600(800,600),
    RES_1024_768(1024,768),
    RES_1280_800(1024,768),
    RES_1280_1024(1280,1024),
    RES_1366_768(1366,768),
    RES_1440_900(1440,900),
    RES_1600_900(1600,900),
    RES_1680_1050(1680,1050),
    RES_1920_1080(1920,1080),
    ;
    
    private int width;
    private int height;
    private RenderingResolutions(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    @Override
    public String toString() {
        Integer widthI = new Integer(width);
        Integer heightI = new Integer(height);
        return widthI.toString() + "x" + heightI.toString();
    }
    
}
