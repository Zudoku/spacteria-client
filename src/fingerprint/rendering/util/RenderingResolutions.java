package fingerprint.rendering.util;

public enum RenderingResolutions {
    
    RES_640_480(640,480),
    RES_800_600(800,600),
    RES_1024_768(1024,768),
    RES_1280_800(1280,800),
    RES_1024_1024(1024,1024),
    RES_1280_1024(1280,1024),
    RES_1366_768(1366,768),
    RES_1440_900(1440,900),
    RES_1600_900(1600,900),
    RES_1664_1024(1664,1024),
    RES_1680_1050(1680,1050),
    RES_1920_1080(1920,1080),
    RES_1920_1200(1920,1200),
    IDENTIFY_SCREEN(0,0)
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
        if(this == IDENTIFY_SCREEN) {
            return "IDENTIFY";
        }
        if(this == RES_1280_1024) {
            return widthI.toString() + "x" + heightI.toString() + " (BEST)";
        }
        return widthI.toString() + "x" + heightI.toString();
    }
    
}
