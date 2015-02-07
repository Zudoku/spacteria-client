package fingerprint.mainmenus;

public class WorldCreationController {
    private int selectedColumn = 0;
    private int selectedRow = 0;
    
    private int maxRows = 2;
    private int maxColumn_0 = 3;
    private int maxColumn_1 = 0;
    private int maxColumn_2 = 1;
    public WorldCreationController() {
        
    }
    public void up(){
        if(selectedRow > 0){
            selectedRow--;
        }else{
            selectedRow = maxRows;
        }
        selectedColumn = 0;
    }
    public void down(){
        if(selectedRow == maxRows){
            selectedRow = 0;
        }else{
            selectedRow++;
        }
        selectedColumn = 0;
    }
    public void left(){
        switch(selectedRow){
        case 0:
            if(selectedColumn > 0){
                selectedColumn--;
            }else{
                selectedColumn = maxColumn_0;
            }
            break;
            
        case 1:
            if(selectedColumn > 0){
                selectedColumn--;
            }else{
                selectedColumn = maxColumn_1;
            }
            break;
            
        case 2:
            if(selectedColumn > 0){
                selectedColumn--;
            }else{
                selectedColumn = maxColumn_2;
            }
            break;
        }
    }
    public void right(){
        switch(selectedRow){
        case 0:
            if(selectedColumn < maxColumn_0){
                selectedColumn++;
            }else{
                selectedColumn = 0;
            }
            break;
            
        case 1:
            if(selectedColumn < maxColumn_1){
                selectedColumn++;
            }else{
                selectedColumn = 0;
            }
            break;
            
        case 2:
            if(selectedColumn < maxColumn_2){
                selectedColumn++;
            }else{
                selectedColumn = 0;
            }
            break;
        }
    }
    public int getSelectedColumn() {
        return selectedColumn;
    }
    public int getSelectedRow() {
        return selectedRow;
    }
}
