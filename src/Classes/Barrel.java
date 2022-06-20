package Classes;

public class Barrel {
    private double x,y;
    private final double velocityX = 0.005;
    private double velocityY;
    private static final double radius = 0.025;
    private int floorLevel = 0;
    private int angle = 0;

    public Barrel(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void goRight() {
        x += velocityX;
    }

    public void goLeft() {
        x -= velocityX;
    }  

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(int floorLevel) {
        this.floorLevel = floorLevel;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getRadius() {
        return radius;
    }

    public void setY() {
        y += velocityY;
    }

    public void fall() {
        velocityY -= 0.001;
    }

    public void remove(MyFloor[] f) {
        velocityY = 0.0;
        y = MyFloor.getHeight() + f[floorLevel].getY() + radius;
    }

    public boolean collision(MyFloor[] f) {
        boolean floorCollide = false;

        for (MyFloor myFloor : f) {
            if (myFloor.collision(this)) {
                floorCollide = true;
            }
        }
        return floorCollide;
    }

    public void draw() {
        double neg = Math.pow(-1, floorLevel + 1);


        ScreenDrawer.picture(x, y, "Pics/barrel.png", 24, 24, angle * neg);

        angle += 5;
    }

    public static void monkeyBarrelsDraw(MyFloor[] myFloors) {
        double yHeight = myFloors[0].getY() + MyFloor.getHeight() + radius;
        ScreenDrawer.picture(0.05, yHeight, "Pics/barrel.png", 24, 24);
        ScreenDrawer.picture(0.05, yHeight + 2 * radius, "Pics/barrel.png", 24, 24);
        ScreenDrawer.picture(0.10, yHeight, "Pics/barrel.png", 24, 24);
        ScreenDrawer.picture(0.10, yHeight + 2 * radius, "Pics/barrel.png", 24, 24);
    }
}