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
        short[][] replaceRendering = new short[width*2][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width*2; x+=2) {
                replaceFunctional[x/2][y] = functional[(width-1)-(x/2)][y];
                replaceRendering[x][y] = rendering[(width*2-1)-x][y];
                replaceRendering[x+1][y] = rendering[(width*2)-x][y];
            }
        }
        functional = replaceFunctional;
        rendering = replaceRendering;
        
    }
    public void flipVertical(){
        byte[][] replaceFunctional = new byte[width][height];
        short[][] replaceRendering = new short[width*2][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width*2; x+=2) {
                replaceFunctional[x][y] = functional[x/2][(height-1)-y];
                replaceRendering[x][y] = rendering[x][(height-1)-y];
                replaceRendering[x+1][y] = rendering[x+1][(height-1)-y];
            }
        }
        functional = replaceFunctional;
        rendering = replaceRendering;
    }
}
