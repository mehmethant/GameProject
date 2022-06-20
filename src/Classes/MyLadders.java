package Classes;

public class MyLadders {
    private final double x,y;

    public MyLadders(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void drawLadder() {
        ScreenDrawer.picture(x, y, "Pics/ladder.png", 0.09 * 256, 70);
    }

    public double getY() { 
        return y; 
    }

    public double getX() { 
        return x; 
    }
}