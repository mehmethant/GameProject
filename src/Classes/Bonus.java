package Classes;

public class Bonus {
    private final double x,y;

    public Bonus(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void drawBonus() {
        ScreenDrawer.picture(x, y, "Pics/bonus.png", 30, 30);
    }


    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }




}
