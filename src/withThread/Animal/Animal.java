package withThread.Animal;

import withThread.World.Zoo;

public class Animal implements Runnable {
    boolean isLive;
    int type;
    int n;
    int m;
    Thread life;

    public int getType() {
        return type;
    }

    public Animal(int type, int n, int m) {
        this.type = type;
        this.n = n;
        this.m = m;
        isLive = true;
        life = new Thread(this);
    }

    public void kill() {
        isLive = false;
    }

    @Override
    public void run() {
        while (isLive) {
            switch (Zoo.getZoo().getState().get()) {
                case 0:
                case 1:
                case 3:
                    await();
                    break;
                case 2:
                    move();
                    break;

            }


        }
    }

    private void move() {
        Zoo.getZoo().getController().getInWorkAnimals().addAndGet(1);
        if (Math.random() < 0.75) {
            int r = (int) (Math.random() * 3) - 1 + n;
            int c = (int) (Math.random() * 3) - 1 + m;

            if (r != n || c != m) {
                if (Zoo.getZoo().getTables()[r][c].move(this)) {
                    Zoo.getZoo().getTables()[n][m].exit(this);
                    n = r;
                    m = c;
                }
            }


        }
        Zoo.getZoo().getController().getInWorkAnimals().addAndGet(-1);

    }

    private void await() {
        try {
            Zoo.getZoo().getController().getInWaaitAnimals().addAndGet(1);
           synchronized (Zoo.getZoo().getNotify()) {
               Zoo.getZoo().getNotify().wait();
           }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Zoo.getZoo().getController().getInWaaitAnimals().addAndGet(-1);
        }
    }

    public void start() {
        life.start();
    }
}
