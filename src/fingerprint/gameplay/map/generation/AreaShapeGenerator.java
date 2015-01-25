package fingerprint.gameplay.map.generation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;



public class AreaShapeGenerator {
    private static final Logger logger = Logger.getLogger(AreaShapeGenerator.class.getName());
    public AreaShapeGenerator() {
        // TODO Auto-generated constructor stub
    }

    public float[][] createGradient(int GRADIENT_SIZE) {
        float[][] valueArray = new float[GRADIENT_SIZE][GRADIENT_SIZE];
        int centerX = GRADIENT_SIZE/2 -1;
        int centerY = GRADIENT_SIZE/2 -1;
        
        float[][] noise = getNoise(GRADIENT_SIZE);
        float[][] noise2 = getNoise(GRADIENT_SIZE);
        
        for (int x = 0; x < GRADIENT_SIZE; x++) {
            for (int y = 0; y < GRADIENT_SIZE; y++) {

                //Simple squaring, you can use whatever math libraries are available to you to make this more readable
                //The cool thing about squaring is that it will always give you a positive distance! (-10 * -10 = 100)
                float distanceX = (centerX - x) * (centerX - x);
                float distanceY = (centerY - y) * (centerY - y);

                float distanceToCenter = (float) Math.sqrt(distanceX
                        + distanceY);
                
                    // Make sure this value ends up as a float and not an
                    // integer
                    // If you're not outputting this to an image, get the
                    // correct 1.0 white on the furthest edges by dividing by
                    // half the map size, in this case 64. You will get higher
                    // than 1.0 values, so clamp them!
                    float h = noise[x][y];
                    float h2 = noise2[x][y];
                    distanceToCenter = h
                            - (distanceToCenter / GRADIENT_SIZE * 1.5f);
                    distanceToCenter = h2 - distanceToCenter;
                    if (distanceToCenter < 0.2)
                        distanceToCenter = 0;
                    if(distanceToCenter >0.2f){
                        valueArray[x][y] = 1;
                    }else{
                        valueArray[x][y] = 0;
                    }
                    
                
                
            }
        }
        return valueArray;
    }
    public float[][] getNoise(int GRADIENT_SIZE){
        float[][] result = new float[GRADIENT_SIZE][GRADIENT_SIZE];
        Random r = new Random();
        int freq = r.nextInt(10000);
        for(int x=0;x<GRADIENT_SIZE;x++) {
           for(int y=0;y<GRADIENT_SIZE;y++) {
               
               double h = getNoiseValue(x,y,GRADIENT_SIZE,freq);
               
               result[x][y] = (float)h;

           }
        }
        double minValue = result[0][0], maxValue = result[0][0];
        for (int x = 0; x < GRADIENT_SIZE; x++) {
            for (int y = 0; y < GRADIENT_SIZE; y++) {
                minValue = Math.min(result[x][y], minValue);
                maxValue = Math.max(result[x][y], maxValue);
            }
        }
        
        for(int x=0;x<GRADIENT_SIZE;x++) {
            for(int y=0;y<GRADIENT_SIZE;y++) {
                result[x][y]=(float) ( (result[x][y] - minValue) / (maxValue - minValue));
                if(result[x][y]>1) result[x][y] = 1;
            }
        }
        return result;
    }
    
    public float[][] maskGeneration(int size){
        float result[][] = new float[size][size];
        float[][] noise = getNoise(size);
        for( int i = 0; i < size; i++ ) {
            for( int j = 0; j < size; j++ ) {
                result[ i ][ j ] = makeMask( size, size, i, j, noise[ i ][ j ] );
            }
        }
        double minValue = result[0][0], maxValue = result[0][0];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                minValue = Math.min(result[x][y], minValue);
                maxValue = Math.max(result[x][y], maxValue);
            }
        }
        
        for(int x=0;x<size;x++) {
            for(int y=0;y<size;y++) {
                result[x][y]=(float) ( (result[x][y] - minValue) / (maxValue - minValue));
                if(result[x][y]>1) result[x][y] = 1;
            }
        }
        for(int x=0;x<size;x++) {
            for(int y=0;y<size;y++) {
                if(result[x][y] > 0.2){
                    result[x][y] = 1f;
                }else{
                    result[x][y] = 0f;
                }
            }
        }
        
        return result;
    }
    
    private float getFactor( int val, int min, int max ) {
        int full = max - min;
        int part = val - min;
        float factor = (float)part / (float)full;
        return factor;
    }

    public int getDistanceToEdge( int x, int y, int width, int height ) {
        int[] distances = new int[]{ y, x, ( width - x ), ( height - y ) };
        int min = distances[ 0 ];
        for(int val: distances) {
            if( val < min ) {
                min = val;
            }
        }
        return min;
    }
    public float makeMask( int width, int height, int posX, int posY, float oldValue ) {
        int minVal = ( ( ( height + width ) / 2 ) / 100 * 2 );
        int maxVal = ( ( ( height + width ) / 2 ) / 100 * 20 );
        if( getDistanceToEdge( posX, posY, width, height ) <= minVal ) {
            return 0;
        } else if( getDistanceToEdge( posX, posY, width, height ) >= maxVal ) {
            return 1;
        } else {
            float factor = getFactor( getDistanceToEdge( posX, posY, width, height ), minVal, maxVal );
            return ( oldValue + oldValue ) * factor;
        }
    }
    
    
    public double getNoiseValue(int x, int y, int GRADIENT_SIZE, int freq){
        double  NOISE_SIZE = 20d;

        return SimplexNoise.noise(NOISE_SIZE * x / GRADIENT_SIZE, NOISE_SIZE * y / GRADIENT_SIZE,freq);
    }
    
    public BufferedImage imageDataFromFloatArray(float[][]data,int size){
        BufferedImage bI =new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        for(int x=0;x<size;x++) {
            for(int y=0;y<size;y++){
                int value = (int)(data[x][y] * 255);
                Color newColor = new Color(value, value , value);
                bI.setRGB(x, y, newColor.getRGB());
                
            }
         }
        return bI;
    }
    
}
