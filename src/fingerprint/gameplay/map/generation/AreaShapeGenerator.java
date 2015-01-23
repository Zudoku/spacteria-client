package fingerprint.gameplay.map.generation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;



public class AreaShapeGenerator {
    public AreaShapeGenerator() {
        // TODO Auto-generated constructor stub
    }

    public float[][] createGradient(int GRADIENT_SIZE) {
        float[][] valueArray = new float[GRADIENT_SIZE][GRADIENT_SIZE];
        int centerX = GRADIENT_SIZE/2 -1;
        int centerY = GRADIENT_SIZE/2 -1;

        for (int x = 0; x < GRADIENT_SIZE; x++) {
            for (int y = 0; y < GRADIENT_SIZE; y++) {

                //Simple squaring, you can use whatever math libraries are available to you to make this more readable
                //The cool thing about squaring is that it will always give you a positive distance! (-10 * -10 = 100)
                float distanceX = (centerX - x) * (centerX - x);
                float distanceY = (centerY - y) * (centerY - y);

                float distanceToCenter = (float) Math.sqrt(distanceX + distanceY);
                if(distanceToCenter >0.1) {
                    Random random = new Random();
                    distanceToCenter= distanceToCenter - 0.1f +(random.nextFloat()/10);
                    
                }
                
                //Make sure this value ends up as a float and not an integer
                //If you're not outputting this to an image, get the correct 1.0 white on the furthest edges by dividing by half the map size, in this case 64. You will get higher than 1.0 values, so clamp them!
                SimplexNoise noise = new SimplexNoise();
                int freq = 10;
                float h = (noise.noise((float) x / freq, 0) + 1) / 2;
                distanceToCenter = h -(distanceToCenter / GRADIENT_SIZE*2);
                if(distanceToCenter < 0) distanceToCenter = 0;
                
                valueArray[x][y] = distanceToCenter;

            }
        }
        return valueArray;
    }
    public float[][] applyNoise(float[][] applied, int GRADIENT_SIZE, int freq){
        float[][] result = applied;
        SimplexNoise noise = new SimplexNoise();
        
        for(int x=0;x<GRADIENT_SIZE;x++) {
           
           for(int y=0;y<GRADIENT_SIZE;y++) {
               float h = (noise.noise((float) x / freq, 0) + 1) / 2;
               result[x][y] = h - result[x][y];
               if(result[x][y] >1 ) result[x][y] = 1;
               if(result[x][y] <0 ) result[x][y] = 0;

           }
        }
        
        return result;
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
