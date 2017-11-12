package fingerprint.mainmenus;

public class CharacterSelectionController {
    
    private int selection = 0;
    private int filesAmount;
    
    public CharacterSelectionController() {
        
    }
    public void setFilesAmount(int filesAmount) {
        this.filesAmount = filesAmount;
        if(selection >= filesAmount && filesAmount != 0){
            selection = filesAmount -1;
        }
    }

    public boolean getMoreLeft() {
        if(selection > 0){
            return true;
        }
        return false;
    }
    public boolean getMoreRight(){
        if(selection < filesAmount){
            return true;
        }
        return false;
    }
    public int getSelection() {
        return selection;
    }
    public void left(){
        if(getMoreLeft()){
            selection--;
        }else{
            //selection = filesAmount;
        }
    }
    public void right(){
        if(getMoreRight()){
            selection++;
        }else{
            //selection = 0;
        }
    }
}
