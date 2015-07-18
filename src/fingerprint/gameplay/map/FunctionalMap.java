package fingerprint.gameplay.map;

public class FunctionalMap {
    public static int SIZE = 1280;
    private Byte data[][] = new Byte [SIZE][SIZE];
    
    
    public FunctionalMap(byte[] input){
        for(int x = 0;x<SIZE;x++){
            for(int y = 0;y<SIZE;y++){
                data[x][y] = input[x*SIZE + y];
            }
        }
    }
    
    
    public Byte[][] getData() {
        return data;
    }
    public void setData(Byte[][] data) {
        this.data = data;
    }
}
