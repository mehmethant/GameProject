package Classes;

public class Monkey {
    private final double x,y;

    public Monkey(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void lookLeft() {
        ScreenDrawer.picture(x, y, "Pics/donkeyLeft.png", 60, 47);
    }

    public void lookFront() {
        ScreenDrawer.picture(x, y, "Pics/donkey.png", 60, 47);
    }

    public void lookRight() {
        ScreenDrawer.picture(x, y, "Pics/donkeyRight.png", 60, 47);
    }

    public void lookOrigin() {
        ScreenDrawer.picture(x, y, "Pics/donkeyCenter.png", 60, 47);
    }
}