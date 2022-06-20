package Classes;

import java.util.LinkedList;


public class World {
    public static void main(String[] args) {

        ScreenDrawer.setPenColor(ScreenDrawer.RED);
        ScreenDrawer.clear(ScreenDrawer.BLACK);
        ScreenDrawer.setFontSize(50);
        ScreenDrawer.text(0.5, 0.8, "INSTRUCTIONS");
        ScreenDrawer.setPenColor(ScreenDrawer.WHITE);
        ScreenDrawer.setFontSize(20);
        ScreenDrawer.text(0.5, 0.7, "Use 'a' to move left, 'd' for right, w' to jump");
        ScreenDrawer.text(0.5, 0.6, "In order to win collect at least 3 golden key and ");
        ScreenDrawer.text(0.5, 0.5, "get to the princess!");
        ScreenDrawer.setPenColor(ScreenDrawer.GREEN);
        ScreenDrawer.text(0.5, 0.4, "Press 'y' to start the game");
        ScreenDrawer.setFontSize(50);
        ScreenDrawer.text(0.5, 0.2, "GOOD LUCK!");
        ScreenDrawer.setFontSize(20);



        char myChar = 0;
        while (myChar != 'y') {
            if (ScreenDrawer.hasNextKeyTyped())
                myChar = ScreenDrawer.nextKeyTyped();
        }


        while (true) {
            int frameCount = 0;
            int direction_right = 0;
            int direction_left = 0;
            int currentDirection = 0;
            boolean isClimbing = false;
            int climbing = 0;
            boolean standing = false;
            boolean isWon = false;


            MyFloor[] myFloors = new MyFloor[6];

            for (int i = 0; i < myFloors.length; i++) {
                if (i % 2 == 0) {
                    myFloors[i] = new MyFloor(0.35, 0.8 - i * 0.15);
                } else {
                    myFloors[i] = new MyFloor(0.65, 0.65 - (i - 1) * 0.15);
                }
            }

            MyLadders[] myLadders = new MyLadders[5];

            myLadders[0] = new MyLadders(0.4, 0.125);
            myLadders[1] = new MyLadders(0.7, 0.275);
            myLadders[2] = new MyLadders(0.3, 0.425);
            myLadders[3] = new MyLadders(0.6, 0.575);
            myLadders[4] = new MyLadders(0.45, 0.725);

            LinkedList<Barrier> barriers = new LinkedList<>();

            barriers.add(new Barrier(0.60, 0.767));
            barriers.add(new Barrier(0.40, 0.617));
            barriers.add(new Barrier(0.60, 0.467));
            barriers.add(new Barrier(0.40, 0.317));
            barriers.add(new Barrier(0.60, 0.167));

            LinkedList<Bonus> bonuses = new LinkedList<>();

            bonuses.add(new Bonus(0.90, 0.167));
            bonuses.add(new Bonus(0.10, 0.317));
            bonuses.add(new Bonus(0.90, 0.467));
            bonuses.add(new Bonus(0.10, 0.617));
            bonuses.add(new Bonus(0.90, 0.767));

            LinkedList<Barrel> barrels = new LinkedList<>();

            Player player = new Player(0.5, myFloors[5].getY() + MyFloor.getHeight() + 0.025);
            Queen queen = new Queen(0.70, myFloors[0].getY() + MyFloor.getHeight() + 0.035);
            Monkey monkey = new Monkey(0.15, myFloors[0].getY() + MyFloor.getHeight() + 0.04);

            ScreenDrawer.FPS(30);


            while (player.isAlive() && !isWon) {
                ScreenDrawer.clear(ScreenDrawer.BLACK);
                ScreenDrawer.text(0.5, 0.95, "Keys : " + player.getBonus());
                Barrel.monkeyBarrelsDraw(myFloors);

                for (int i = 0; i < myFloors.length; i++) {
                    myFloors[i].draw();
                    if (i < myLadders.length) {
                        myLadders[i].drawLadder();
                    }
                }
                for (Barrier barrier : barriers)
                    barrier.drawBarrier();

                for (Bonus bonus : bonuses) {
                    bonus.drawBonus();

                }


                if (0 <= frameCount && frameCount < 145) {
                    monkey.lookOrigin();
                } else if (145 <= frameCount && frameCount < 155) {
                    monkey.lookLeft();
                } else if (155 <= frameCount && frameCount < 165) {
                    monkey.lookFront();
                } else if (165 <= frameCount) {
                    monkey.lookRight();
                } else monkey.lookOrigin();

                queen.draw();
                player.firstPosition();

                if (player.touchLadder(myLadders) && isClimbing) {
                    player.climb(climbing);
                } else if (currentDirection == 1) {
                    player.lookRight(direction_right);
                } else if (currentDirection == 2) {
                    player.lookLeft(direction_left);
                } else if (!(player.touchFloor(myFloors)) &&
                        !(player.touchLadder(myLadders))) {
                    player.jump(standing);
                } else {
                    player.draw(standing);
                }

                currentDirection = 0;

                if (ScreenDrawer.hasNextKeyTyped()) {
                    char dir = ScreenDrawer.nextKeyTyped();
                    if (dir == 'a') {

                        if (!(player.touchLadder(myLadders) && !player.touchFloor(myFloors))) {
                            player.goLeft();
                            direction_left++;
                            direction_right = 0;
                            isClimbing = false;
                            standing = false;
                            currentDirection = 2;
                        }
                    } else if (dir == 'd') {
                        if (!(player.touchLadder(myLadders) && !player.touchFloor(myFloors))) {
                            player.goRight();
                            direction_right++;
                            direction_left = 0;
                            isClimbing = false;
                            standing = true;
                            currentDirection = 1;
                        }
                    }
                    if (dir == 'w') {
                        if (player.touchLadder(myLadders)) {
                            isClimbing = true;
                            climbing++;
                            player.goUp();
                        } else if (player.touchFloor(myFloors)) {
                            isClimbing = false;
                            player.jump();
                        }
                    } else if (dir == 's') {
                        if (player.touchLadder(myLadders) && player.touchFloor(myFloors)) {
                            isClimbing = true;
                            climbing--;
                            player.goDown();
                        }
                    }

                }
                player.updateY();

                int counter = 0;
                for (MyFloor myFloor : myFloors) {
                    if ((myFloor.collision(player))) {
                        counter++;
                    }
                }

                if (counter <= 0.0 && !(player.touchLadder(myLadders))) {
                    player.fall();
                } else if (player.getVelocityY() < 0.0) {
                    player.die(myFloors);
                }

                if (frameCount % 180 == 0) {
                    barrels.add(new Barrel(0.2, myFloors[0].getY() + MyFloor.getHeight() + 0.025));
                } else if (barrels.size() > 10) {
                    barrels.remove(0);
                }


                int counter1 = 0;
                while (counter1 < barrels.size()) {
                    if (barrels.get(counter1).collision(myFloors)) {
                        if (barrels.get(counter1).getFloorLevel() % 2 == 0) {
                            barrels.get(counter1).goRight();
                        } else {
                            barrels.get(counter1).goLeft();
                        }
                    }


                    if (!(barrels.get(counter1).collision(myFloors))) {
                        barrels.get(counter1).fall();
                    } else if (barrels.get(counter1).getVelocityY() < 0.0) {
                        int temp = barrels.get(counter1).getFloorLevel();
                        barrels.get(counter1).setFloorLevel(temp + 1);
                        barrels.get(counter1).remove(myFloors);
                    }

                    barrels.get(counter1).setY();
                    counter1++;
                }


                int counter2 = 0;
                while (counter2 < barrels.size()) {
                    barrels.get(counter2).draw();
                    counter2++;
                }


                player.contactBarrel(barrels);
                player.contactBarrier(barriers);
                player.bonusCollect(bonuses);


                frameCount++;
                if (frameCount >= 180) {
                    frameCount = 0;
                }

                ScreenDrawer.advance();
                isWon = player.won(queen);
            }


            ScreenDrawer.disableAnimation();

            if (isWon) {
                ScreenDrawer.clear(ScreenDrawer.BLACK);
                ScreenDrawer.setPenColor(ScreenDrawer.GREEN);
                ScreenDrawer.setFontSize(75);
                ScreenDrawer.text(0.5, 0.5, "You Won!");
            } else if (!player.isAlive()) {
                ScreenDrawer.clear(ScreenDrawer.BLACK);
                ScreenDrawer.setPenColor(ScreenDrawer.RED);
                ScreenDrawer.setFontSize(75);
                ScreenDrawer.text(0.5, 0.5, "YOU LOST!");
            }

            ScreenDrawer.setPenColor(ScreenDrawer.WHITE);
            ScreenDrawer.setFontSize(25);
            ScreenDrawer.text(0.5, 0.4, "Press 'y' to play again");
            ScreenDrawer.text(0.5, 0.3, "Press 'n' to close the game");

            char d = 0;
            while (d != 'y') {
                if (ScreenDrawer.hasNextKeyTyped())
                    d = ScreenDrawer.nextKeyTyped();
                if (d == 'n') System.exit(0);
            }
        }
    }
}


