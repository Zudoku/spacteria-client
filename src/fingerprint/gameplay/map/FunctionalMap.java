package fingerprint.gameplay.map;

import org.newdawn.slick.Color;

import fingerprint.gameplay.map.blocks.BlockManager;

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
    public Color getDebugColorForID(byte id){
        if(id == BlockManager.Dirt){
            return new Color(102, 51, 0);
        }
        if(id == BlockManager.Bush || id == BlockManager.Tree){
            return new Color(102, 255, 51);
        }
        if(id == BlockManager.Rock_Smooth || id == BlockManager.Rock_Sharp){
            return new Color(153, 102, 51);
        }
        
        if(id == BlockManager.Water){
            return new Color(0, 153, 255);
        }
        
        return new Color(10,10,143);
    }
    
}
