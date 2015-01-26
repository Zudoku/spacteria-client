package fingerprint.gameplay.map.generation;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AreaStructureGenerator {
    private static final Logger logger = Logger.getLogger(AreaStructureGenerator.class.getName());
    public AreaStructureGenerator() {
        // TODO Auto-generated constructor stub
    }

    public void generatePassagesToNewArea(int[][] mask, int width, int height,
            int amount) {
        Random r = new Random();
        for (int count = 0; count < amount; count++) {
            int minVal = (((height + width) / 2) / 100 * 2);
            int maxVal = (((height + width) / 2) / 100 * 20);
            int counter = 0;
            while (true) {
                counter++;
                int x = r.nextInt(width - minVal) + minVal;
                int y = r.nextInt(height - minVal) + minVal;
                int edgeDistance = AreaShapeGenerator.getDistanceToEdge(x, y,
                        width, height);
                if (edgeDistance > maxVal)continue;
                if (edgeDistance < minVal)continue;

                boolean vertical = r.nextBoolean();
                if (vertical) {
                    if (!passageValidation(x, y, mask, vertical))continue;
                    if (!passageValidation(x, y + 1, mask, vertical))continue;
                    if (!passageValidation(x, y - 1, mask, vertical))continue;
                    // TEMP
                    mask[x][y] = 50;
                    mask[x][y + 1] = 50;
                    mask[x][y - 1] = 50;
                } else {
                    if (!passageValidation(x, y, mask, vertical))continue;
                    if (!passageValidation(x + 1, y, mask, vertical))continue;
                    if (!passageValidation(x - 1, y, mask, vertical))continue;
                    // TEMP
                    mask[x][y] = 50;
                    mask[x + 1][y] = 50;
                    mask[x - 1][y] = 50;
                }
                break;

            }
            logger.log(Level.INFO,"Placing passage took {0} tries",counter);
        }
    }

    private boolean passageValidation(int x , int y , int[][] mask , boolean vertical){
        if (vertical) {
            if (mask[x+1][y] == 0 && mask[x-1][y] == 1 || mask[x+1][y] == 1 && mask[x-1][y] == 0) {
                return true;
            }
            return false;
        }else{
            if (mask[x][y + 1] == 0 && mask[x][y-1] == 1 || mask[x][y + 1] == 1 && mask[x][y-1] == 0) {
                return true;
            }
            return false;
        }
    }
}
