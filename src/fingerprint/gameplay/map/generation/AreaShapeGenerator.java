package fingerprint.gameplay.map.generation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;



public class AreaShapeGenerator {
    private static final Logger logger = Logger.getLogger(AreaShapeGenerator.class.getName());
    
    public static final double SMOOTH_AREA_SMOOTHNESS = 5d;
    public static final double GENERIC_AREA_SMOOTHNESS = 20d;
    public static final double GRAINY_AREA_SMOOTHNESS = 40d;
    
    public static final double SHORT_MASK_CUTOFF = 0.4d;
    public static final double GENERIC_MASK_CUTOFF = 0.2d;
    public static final double LONG_MASK_CUTOFF = 0.1d;
    
    public AreaShapeGenerator() {
        
    }

    public float[][] createGradient(int GRADIENT_SIZE) {
        float[][] valueArray = new float[GRADIENT_SIZE][GRADIENT_SIZE];
        int centerX = GRADIENT_SIZE/2 -1;
        int centerY = GRADIENT_SIZE/2 -1;

        
        for (int x = 0; x < GRADIENT_SIZE; x++) {
            for (int y = 0; y < GRADIENT_SIZE; y++) {
                float distanceX = (centerX - x) * (centerX - x);
                float distanceY = (centerY - y) * (centerY - y);

                float distanceToCenter = (float) Math.sqrt(distanceX
                        + distanceY);
                valueArray[x][y] = distanceToCenter;
            }
        }
        return valueArray;
    }
    /**
     * returns a perlin noise float array with values between 0-1
     * @param SIZE
     * @return
     */
    public float[][] getNoise(int width,int height,double noiseSmoothness){
        float[][] result = new float[width][height];
        Random r = new Random();
        int freq = r.nextInt(10000);
        for(int x=0;x<width;x++) {
           for(int y=0;y<height;y++) {
               double h = getNoiseValue(x,y,width,height,freq,noiseSmoothness);
               result[x][y] = (float)h;
           }
        }
        result = toUnitArray(result, width, height);
        return result;
    }
    /**
     * Transforms 2d float array with values between 0-FLOAT.MAX_VALUE to values between 0-1.
     * In the returned array 0 will be the min value found in the original array 
     * and 1 will be the max value found in the original array.
     * @param original
     * @param width
     * @param height
     * @return
     */
    public float[][] toUnitArray(float[][] original,int width,int height){
        float[][] result = original;
        double minValue = result[0][0], maxValue = result[0][0];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                minValue = Math.min(result[x][y], minValue);
                maxValue = Math.max(result[x][y], maxValue);
            }
        }
        
        for(int x=0;x<height;x++) {
            for(int y=0;y<width;y++) {
                result[x][y]=(float) ( (result[x][y] - minValue) / (maxValue - minValue));
            }
        }        
        return result;
    }
    /**
     * Generates a mask for the shape of a new Area . Use the static variables found in this class for areaSmoothness and maskCutOff
     * @param width
     * @param height
     * @param areaSmoothness
     * @param maskCutOff
     * @return
     */
    public int[][] maskGeneration(int width,int height,double areaSmoothness, double maskCutOff){
        float temporaryResult[][] = new float[width][height];
        float[][] noise = getNoise(width,height,areaSmoothness);
        for( int x = 0; x < width; x++ ) {
            for( int y = 0; y < height; y++ ) {
                temporaryResult[ x ][ y ] = makeMask( width, height, x, y, noise[ x ][ y ] );
            }
        }
        temporaryResult = toUnitArray(temporaryResult, width, height);
        
        int[][] result = new int[width][height];
        
        for(int x=0;x<width;x++) {
            for(int y=0;y<height;y++) {
                if(temporaryResult[x][y] > maskCutOff){
                    result[x][y] = 1;
                }else{
                    result[x][y] = 0;
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
    
    /**
     * Returns the noise value for a specific x.y coordinate
     * @param x - x coordinate
     * @param y - y coordinate
     * @param width - image width that you are generating
     * @param height - image height that you are generating
     * @param freq - Random seed for the noise
     * @param noiseSmoothness - How rough the perlin noise is
     * @return
     */
    public double getNoiseValue(int x, int y, int width,int height, int freq, double noiseSmoothness){

        return PerlinNoise.noise( (noiseSmoothness * x / width), (noiseSmoothness * y / height),freq);
    }
    
    public float[][] intArrayToFloatArray(int[][] original,int width,int height){
        float[][] result = new float[width][height];
        
        for(int x=0;x<width;x++) {
            for(int y=0;y<height;y++){
                result[x][y] = original[x][y];
            }
        }
        
        return result;
    }
    /**
     * Builds grayscale bufferedImage from float arrays containing values between 0-1
     * @param data
     * @param width
     * @param height
     * @return
     */
    public BufferedImage imageDataFromFloatArray(float[][]data,int width,int height){
        BufferedImage bI =new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for(int x=0;x<width;x++) {
            for(int y=0;y<height;y++){
                int value = (int)(data[x][y] * 255);
                Color newColor = new Color(value, value , value);
                bI.setRGB(x, y, newColor.getRGB());
                
            }
         }
        return bI;
    }
    
}
