package fingerprint.mainmenus;

import java.util.List;

public class GenericGridController {
    private int selectedColumn = 0;
    private int selectedRow = 0;
    
    private int maxRows = 0;
    private boolean locked = false;
    
    private List<Integer> rows;
    private List<Integer> lockedRows;
    
    public GenericGridController(List<Integer> rows, List<Integer> lockedRows) {
        this.rows = rows;
        this.maxRows = this.rows.size() - 1;
        this.lockedRows = lockedRows;
        
    }
    public void up(){
        if(isLocked()){
            return;
        }
        if(selectedRow > 0){
            selectedRow--;
        }else{
            selectedRow = maxRows;
        }
        selectedColumn = 0;
        checkLockState();
    }
    public void down(){
        if(isLocked()){
            return;
        }
        if(selectedRow == maxRows){
            selectedRow = 0;
        }else{
            selectedRow++;
        }
        selectedColumn = 0;
        checkLockState();
    }
    public void left() {
        if (selectedColumn > 0) {
            selectedColumn--;
        } else {
            selectedColumn = rows.get(selectedRow);
        }
    }
    public void right() {
        if (selectedColumn < rows.get(selectedRow)) {
            selectedColumn++;
        } else {
            selectedColumn = 0;
        }
    }
    
    private void checkLockState() {
        if(lockedRows.contains(selectedRow)){
            this.locked = true;
        }
    }
    public int getSelectedColumn() {
        return selectedColumn;
    }
    public int getSelectedRow() {
        return selectedRow;
    }
    
    public void unlock(){
        this.locked = false;
    }

    public boolean isLocked() {
        return locked;
    }
    
    
}
