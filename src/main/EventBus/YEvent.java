package main.EventBus;


public class YEvent extends Event {
    public int getYval() {
        return yval;
    }

    int yval;
    int val;
    public YEvent(int v,int y) {
        val = v;
        yval = y;
    }

}
