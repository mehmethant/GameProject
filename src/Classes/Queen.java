package Classes;

public class Queen {
    private final double x,y;

    public Queen(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void draw() {
        ScreenDrawer.picture(x, y, "Pics/queen.png", -42, 38);
    }
}