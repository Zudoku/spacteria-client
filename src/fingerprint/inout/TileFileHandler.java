package fingerprint.inout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.Closeables;

import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.gameworld.GameWorldContainer;

public class TileFileHandler {
    private static final Logger logger = Logger.getLogger(TileFileHandler.class.getName());
    private RandomAccessFile tileFile;
    
    
    public static final int BYTETOSHORT = 2;
    public static final int LAYERS = 2;
    
    public TileFileHandler(){
        
    }
    
    public void init(String filename){
        //Initialize the render map.
        File file = new File( FileUtil.SAVES_FOLDER + "/" + filename + "/world/" + FileUtil.RENDER_MAP_FILE_NAME + FileUtil.RENDER_MAP_FILE_EXTENSION);
        if(!file.exists()){
            throw new RuntimeException("Missing renderingmap");
        }
        try {
            tileFile = new RandomAccessFile(file, "rw");
            if(tileFile.length() != (FunctionalMap.SIZE * BYTETOSHORT * LAYERS) * (FunctionalMap.SIZE * BYTETOSHORT)){
                tileFile.setLength((FunctionalMap.SIZE * BYTETOSHORT * LAYERS) * (FunctionalMap.SIZE * BYTETOSHORT));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeTestMap(){
        short slice[][] = new short[FunctionalMap.SIZE][1];
        for (int i = 0; i < FunctionalMap.SIZE; i++) {
            slice[i][0] = (short) (40);
        }
        for (int i = 0; i < FunctionalMap.SIZE; i++) {
            writeMap(slice,0,i,FunctionalMap.SIZE,1,true);
            
        }
        System.out.println("written");
        short mapdata[][] = getPartOfMap(20, 0, 10, 2);
        System.out.println("read");
    }
    
    /**
     * 
     * @param content
     * @param x
     * @param y
     * @param width
     * @param height
     * @param writeFloorZeros
     * @param layer
     */
    public void writeMap(short[][] content, int x, int y, int width, int height,boolean writeFloorZeros){
        final int MAPSIZE = FunctionalMap.SIZE;
        
        if(x < 0 || x > MAPSIZE || (x + width) > MAPSIZE){
            logger.log(Level.SEVERE,"Can't write: X coord is out of bounds");
            return;
        }
        if(y < 0 || y > MAPSIZE || (y + height) > MAPSIZE){
            logger.log(Level.SEVERE,"Can't write: Y coord is out of bounds");
            return;
        }
        //We write width shorts loop
        for(int u = 0; u < height; u++){
            //We multiply by two because short == 2 byte
            //We also need to transfer 2d array position to 1d array position
            //We write it in slices, u = height
            
            
            int bufferPositionStarts = (x  * BYTETOSHORT * LAYERS) + ((y + u) * MAPSIZE * BYTETOSHORT * LAYERS);
            MappedByteBuffer buffer = null;
            try {
                int BUFFERLENGTH = width * BYTETOSHORT * LAYERS;
                buffer = map(tileFile, FileChannel.MapMode.READ_WRITE, bufferPositionStarts , BUFFERLENGTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer = buffer.load();
            for(int i = 0 ; i < width*BYTETOSHORT ; i++){ //i = short index
                if(content[i][u] == 0 && !writeFloorZeros){
                    continue;
                }
                buffer.putShort(i*BYTETOSHORT, content[i][u]);
            }
            buffer = buffer.force();
        }
        
    }
    /**
     * Returns a part of map for rendering purposes. Does not return the direct bytebuffer.
     * @param x top left corner x pos
     * @param y top left corner y pos
     * @param width 
     * @param height
     * @return
     */
    public short[][] getPartOfMap(int x, int y, int width,int height){
        short data[][] = new short[width*2][height];
        int mapSize = FunctionalMap.SIZE;
        
        if(x < 0 || x > mapSize || (x + width) > mapSize){
            logger.log(Level.SEVERE,"Can't read: X coord is out of bounds");
            return new short[0][0];
        }
        if(y < 0 || y > mapSize || (y + height) > mapSize){
            logger.log(Level.SEVERE,"Can't read: Y coord is out of bounds");
            return new short[0][0];
        }
        
        
        for (int i = 0; i < height; i++) {
            //We multiply by two because short == 2 byte
            //We also need to transfer 2d array position to 1d array position
            
            long position = ((y + i) * (mapSize * BYTETOSHORT * LAYERS)) + (x * BYTETOSHORT * LAYERS);
            try {
                MappedByteBuffer buffer = map(tileFile, FileChannel.MapMode.READ_WRITE, position, width * BYTETOSHORT * LAYERS);
                buffer = buffer.load();
                ShortBuffer asd  = buffer.asShortBuffer();
                for(int z=0;z<width*BYTETOSHORT;z++){
                    data[z][i] = asd.get(z);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE,e.getMessage());
            }
            
        }
        return data;
    }
    
    
    
    private static MappedByteBuffer map(RandomAccessFile raf,MapMode mode,long position,long size) throws IOException {
        FileChannel channel=raf.getChannel();
        boolean threw=true;
          MappedByteBuffer mbb=channel.map(mode,position,size);
          threw=false;
          return mbb;
      }
}
