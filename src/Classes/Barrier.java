package Classes;

public class Barrier {
    private final double x,y;

    public Barrier(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void drawBarrier() {
        ScreenDrawer.picture(x, y, "Pics/barrier.png", 60, 25);
    }

    public double getY() { 
        return y; 
    }

    public double getX() { 
        return x; 
    }
}