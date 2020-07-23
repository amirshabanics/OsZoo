package withThread.World;

import withThread.Controller.Controller;
import withThread.Table.Table;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;

public class Zoo implements Runnable {
    private static Zoo world;

    boolean isExist;

    public Controller getController() {
        return controller;
    }

    public boolean isExist() {
        return isExist;
    }


    public Table[][] getTables() {
        return tables;
    }


    public ArrayList<Table>[] getAnimals() {
        return animals;
    }

    public void setAnimals(ArrayList<Table>[] animals) {
        this.animals = animals;
    }

    public AtomicInteger getState() {
        return state;
    }


    public Condition getNotify() {
        return notify;
    }


    public Semaphore getMutex() {
        return mutex;
    }


    public int getT() {
        return t;
    }

    Table[][] tables;
    ArrayList<Table>[] animals;
    AtomicInteger state; // 0: print 1: birth  2: Life 3: death
    Condition notify;
    Controller controller;
    Semaphore mutex;


    int r;
    int n;
    int m;
    int s;
    int k;
    int t;

    public Zoo(int r, int n, int m, int s, int k, int t) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.k = k;
        this.t = t;
        initialize();
    }

    private void initialize() {
    }


    public static void createZoo(int r, int n, int m, int s, int k, int t) {
        if (world == null) {
            world = new Zoo(r, n, m, s, k, t);
        }
    }

    public static Zoo getZoo() {
        return world;
    }

    @Override
    public void run() {
        long time = 0;
        long current;

        while (isExist) {
            current = System.currentTimeMillis();
            if (current > time + t * 1000) {
                printZoo();
                time = current;
            }
        }
    }

    private void printZoo() {
        try {
            mutex.acquire();
            int s = state.getAndSet(0);
            controller.waitFor();
            printing();
            state.set(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            mutex.release();
            notify.signalAll();

        }
    }

    private void printing() {

    }
}
