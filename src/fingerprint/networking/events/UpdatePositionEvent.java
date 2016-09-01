/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.networking.events;

/**
 *
 * @author arska
 */
public class UpdatePositionEvent {
    private int x;
    private int y;

    public UpdatePositionEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public UpdatePositionEvent() {
        
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
