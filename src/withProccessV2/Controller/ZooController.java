package withProccessV2.Controller;

import withProccessV2.Cage.Cage;
import withProccessV2.World.Zoo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static withProccess.Controller.Comand.kill;


public class ZooController implements Runnable {

    AtomicInteger population;
    AtomicInteger inWait;

    Thread controller;

    AtomicInteger state;

    public AtomicInteger getInWait() {
        return inWait;
    }

    public AtomicInteger getState() {
        return state;
    }

    public ZooController(int s, int r) {
        population = new AtomicInteger(s * r);
        inWait = new AtomicInteger(0);
        state = new AtomicInteger(0);
        controller = new Thread(this);
    }

    @Override
    public void run() {
        while (Zoo.getZoo().isLive()) {
            System.out.println("wait");
            waitfor();
            System.out.println("death");
            death();
            System.out.println("birth");
            birth();
            System.out.println("wait again");
            waitfor();
            System.out.println("print");
            print();
            System.out.println("life");
            life();
        }
    }

    private void waitfor() {
        while (population.get() != inWait.get()) {
            System.out.println(population.get() + "    " + inWait.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void death() {
        balance();
        kill();
    }

    ArrayList<Cage> tableMustCheck;
    int[] numbers;

    private void kill() {
        tableMustCheck = Zoo.getZoo().getTableMustCheck();
        for (Cage cage : tableMustCheck) {
            numbers = Zoo.getZoo().getNeighbors(cage);
            for (int i = 1; i < numbers.length; i++)
                if (i != cage.getType() && i * numbers[i] > cage.getType() * cage.getNumbers()) {
                    population.addAndGet(-cage.getNumbers());
                    inWait.addAndGet(-cage.getNumbers());
                    cage.kill();
                    population.addAndGet(+cage.getNumbers());
                    inWait.addAndGet(+cage.getNumbers());
                    break;
                }


        }
    }

    private void balance() {
        cages = Zoo.getZoo().getCages();
        for (Cage[] cage : cages)
            for (Cage c : cage) {
                population.addAndGet(-c.getNumbers());
                inWait.addAndGet(-c.getNumbers());
                c.balance();
                population.addAndGet(+c.getNumbers());
                inWait.addAndGet(+c.getNumbers());
            }
    }

    private void print() {
        Zoo.getZoo().printing();
    }

    private void life() {
        state.set(1);
        notifyAnimals();
        waitEmpty();
        System.out.println("emptyWait");
        try {
            Thread.sleep(1000 * Zoo.getZoo().getT());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        state.set(0);
//        Zoo.getZoo().b = true;
        stopAnimals();
    }

    private void waitEmpty() {
        while(inWait.get() > 0){

        }
    }

    private void stopAnimals() {
        cages = Zoo.getZoo().getCages();
        for (Cage[] cage : cages)
            for (Cage c : cage)
                c.stopAnimals();
    }

    Cage[][] cages;

    private void notifyAnimals() {
        cages = Zoo.getZoo().getCages();
        for (Cage[] cage : cages)
            for (Cage c : cage)
                c.notifyAnimal();
    }

    private void birth() {
        cages = Zoo.getZoo().getCages();
        for (Cage[] cage : cages)
            for (Cage c : cage) {
                population.addAndGet(-c.getNumbers());
                c.birth();
                population.addAndGet(c.getNumbers());
            }
    }

    public void start() {
        controller.start();
    }


}
