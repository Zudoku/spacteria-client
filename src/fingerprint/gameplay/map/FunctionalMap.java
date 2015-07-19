package fingerprint.gameplay.map;

import org.newdawn.slick.Color;

public class FunctionalMap {
    public static int SIZE = 1280;
    private Byte data[][] = new Byte [SIZE][SIZE];
    
    
    public FunctionalMap(byte[] input){
        for(int x = 0;x<SIZE;x++){
            for(int y = 0;y<SIZE;y++){
                if(input[x*SIZE + y] == 30){
                    System.out.println("load30 at " + x +","+ y);
                }
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
    public Color getDebugColorForID(byte id){
        if(id == 9 || id==30){
            return new Color(75,10,10);
        }
        
        return new Color(10,10,143);
    }
    
}
