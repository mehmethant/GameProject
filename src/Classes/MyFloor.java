package Classes;

public class MyFloor {

    private final double x,y;
    private static final double halfWidth = 0.4;
    private static final double halfHeight = 0.01;

    public MyFloor(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y; 
    }

    public static double getHeight() { 
        return halfHeight;
    }

    public void draw() {
        ScreenDrawer.picture(x, y, "Pics/floor.png", 2 * halfWidth * 500,
                         2 * halfHeight * 512);
    }

    public boolean collision(Player player) {
        return (player.getY() - player.getHalfHeight() <= y + halfHeight &&
                player.getY() >= y && player.getX() <= x + halfWidth &&
                player.getX() >= x - halfWidth);
    }

    public boolean collision(Barrel barrel) {
        return (barrel.getY() - barrel.getRadius() <= y + halfHeight &&
                barrel.getY() >= y && barrel.getX() <= x + halfWidth &&
                barrel.getX() >= x - halfWidth);
    }
}