package fingerprint.gameplay.map.generation;

public class StructureContainer {
    
    
    private byte[][] functional;
    private short[][] rendering;
    private int height;
    private int width;
    
    public StructureContainer(byte[][] functional,short[][] rendering,int width,int height) {
        this.functional = functional;
        this.rendering = rendering;
        this.width = width;
        this.height = height;
    }
    public byte[][] getFunctional() {
        return functional;
    }
    public int getHeight() {
        return height;
    }
    public short[][] getRendering() {
        return rendering;
    }
    public int getWidth() {
        return width;
    }
    public void flipHorizontal(){
        byte[][] replaceFunctional = new byte[width][height];
        short[][] replaceRendering = new short[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                replaceFunctional[x][y] = functional[(width-1)-x][y];
                replaceRendering[x][y] = rendering[(width-1)-x][y];
            }
        }
        functional = replaceFunctional;
        rendering = replaceRendering;
        
    }
    public void flipVertical(){
        byte[][] replaceFunctional = new byte[width][height];
        short[][] replaceRendering = new short[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                replaceFunctional[x][y] = functional[width][(height-1)-y];
                replaceRendering[x][y] = rendering[width][(height-1)-y];
            }
        }
        functional = replaceFunctional;
        rendering = replaceRendering;
    }
}
