package Classes;



import java.util.LinkedList;

public class Player {
    private double x,y;
    private final double velocityX = 0.02;
    private double velocityY;
    private static final double halfHeight = 0.025;
    private boolean isAlive = true;
    private int bonus;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getHalfHeight() {
        return halfHeight;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getBonus(){
        return bonus;
    }

    public void goRight() {
        x += velocityX;
    }

    public void goLeft() {
        x -= velocityX;
    }

    public void lookLeft(int dir) {
        if (dir % 3 == 0) {
            ScreenDrawer.picture(x, y + 0.01, "Pics/playerRun2.png", 35, 35);
        } else if (dir % 3 == 1) {
            ScreenDrawer.picture(x, y + 0.01, "Pics/playerRun2.png", 35, 35);
        } else if (dir % 3 == 2) {
            ScreenDrawer.picture(x, y + 0.01, "Pics/playerRun2.png", 35, 35);
        }
    }
    public void lookRight(int dir) {
        if (dir % 3 == 0) {
            ScreenDrawer.picture(x, y + 0.01, "Pics/playerRun1.png", 35, 35);
        } else if (dir % 3 == 1) {
            ScreenDrawer.picture(x, y + 0.01, "Pics/playerRun1.png", 35, 35);
        } else if (dir % 3 == 2) {
            ScreenDrawer.picture(x, y + 0.01, "Pics/playerRun1.png", 35, 35);
        }
    }

    public void draw(boolean facing) {
        if (facing) {
            ScreenDrawer.picture(x, y, "Pics/playerRun1.png", 35, 35);
        } else {
            ScreenDrawer.picture(x, y, "Pics/playerRun2.png", 35, 35);
        }
    }

    public void climb(int dir) {
        if (dir % 2 == 0) {
            ScreenDrawer.picture(x, y, "Pics/playerClimb.png", 35, 35);
        } else if (dir % 2 == 1) {
            ScreenDrawer.picture(x, y, "Pics/playerClimb.png", -35, 35);
        }
    }

    public void jump(boolean facing) {
        if (facing) {
            ScreenDrawer.picture(x, y, "Pics/playerRun2.png", -35, 35);
        } else {
            ScreenDrawer.picture(x, y, "Pics/playerRun2.png", 35, 35);
        }
    }

    public void updateY() {
        y += velocityY;
    }

    public void jump() {
        velocityY = 0.012;
    }

    public void fall() {


        velocityY -= 0.001;
    }

    public void die(MyFloor[] f) {
        double min = Double.POSITIVE_INFINITY;
        double closest = 0;

        for (MyFloor myFloor : f) {
            double temp = Math.abs(y - myFloor.getY());
            if (temp < min) {
                closest = myFloor.getY();
                min = temp;
            }
        }
        y = closest + halfHeight + MyFloor.getHeight();
        velocityY = 0.0;
    }

    public boolean touchFloor(MyFloor[] f) {
        boolean floorCollide = false;

        for (MyFloor myFloor : f) {
            if (myFloor.collision(this)) {
                floorCollide = true;
            }
        }
        return floorCollide;
    }

    public boolean touchLadder(MyLadders[] l) {
        for (MyLadders myLadders : l) {
            if(myLadders == null)
                continue;
            if (myLadders.getX() - 0.015 < x && x < myLadders.getX() + 0.015) {
                if (myLadders.getY() - 0.075 < y && y < myLadders.getY() + 0.1) {
                    return true;
                }
            }
        }
        return false;
    }

    public void goUp() {
        y += 0.015;
    }

    public void goDown() {
        y -= 0.015;
    }

    public boolean won(Queen queen) {
        if (queen.getX() < x + 0.01 && x - 0.01 < queen.getX() && bonus>=3) {
            return queen.getY() < y + 0.015 && y - 0.015 < queen.getY();
        }
        return false;
    }

    public void contactBarrel(LinkedList<Barrel> barrels) {
        int counter = 0;
        
        while (counter < barrels.size()) {
            if (barrels.get(counter).getX() < x + 0.02 &&  x - 0.02 < barrels.get(counter).getX()) {

                if (barrels.get(counter).getY() < y + 0.03 &&  y - 0.03 < barrels.get(counter).getY())
                    isAlive = false;
            }
            counter++;
        }
    }

    public void contactBarrier(LinkedList<Barrier> barriers) {
        int counter = 0;

        while (counter < barriers.size()) {
            if (barriers.get(counter).getX() < x + 0.07 &&
                    x - 0.07 < barriers.get(counter).getX()) {
                if (barriers.get(counter).getY() < y + 0.05 &&
                        y - 0.05 < barriers.get(counter).getY())
                    isAlive = false;
            }
            counter++;
        }
    }

    public void bonusCollect(LinkedList<Bonus> bonuses) {
        int counter = 0;

        while (counter < bonuses.size()) {
            if (bonuses.get(counter).getX() < x + 0.02 &&  x - 0.02 < bonuses.get(counter).getX()) {

                if (bonuses.get(counter).getY() < y + 0.03 &&  y - 0.03 < bonuses.get(counter).getY()) {
                    bonuses.remove(counter);
                    ++bonus;
                }
            }
            counter++;
        }
    }

    public void firstPosition() {
        if (x > 0.97) {
            x = 0.97;
        }
        else if (x < 0.03) {
            x = 0.03;
        }
        
        if (y < -0.05) {
            isAlive = false;
        }
    }
}