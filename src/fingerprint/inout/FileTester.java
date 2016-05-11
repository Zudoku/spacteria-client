package fingerprint.inout;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import fingerprint.gameplay.map.generation.AreaGenerator;
import fingerprint.gameplay.map.generation.AreaShapeGenerator;

public class FileTester {
    public static void main(String[] args) {
        
        
        /*TileFileHandler s = new TileFileHandler();
        s.init("123123");
        s.writeTestMap(); */
        
        int size = 1280;
        float [][] mask = new float[1280][1280];
        AreaShapeGenerator asd = new AreaShapeGenerator();
        int seed = new Random().nextInt(100000);
        for(int y = 0 ; y < size ; y++){
            for(int x = 0 ; x < size ; x++){
                mask[x][y] = (float)asd.getNoiseValue(x, y, size, size, seed,100);
            }
        }
       
        //float[][] imageData = asd.intArrayToFloatArray(mask, size, size);
        float[][] imageData = asd.toUnitArray(mask, size, size);
        
        BufferedImage image = asd.imageDataFromFloatArray(imageData, size, size);
        
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

}
