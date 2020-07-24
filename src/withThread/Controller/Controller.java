package withThread.Controller;

import withThread.Table.Table;
import withThread.World.Zoo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller implements Runnable {

    AtomicInteger population;
    AtomicInteger inWorkAnimals;
    AtomicInteger inWaaitAnimals;

    Thread controle;

    public AtomicInteger getPopulation() {
        return population;
    }

    public AtomicInteger getInWorkAnimals() {
        return inWorkAnimals;
    }

    public AtomicInteger getInWaaitAnimals() {
        return inWaaitAnimals;
    }

    public Controller(int s, int r) {
        population = new AtomicInteger();
        inWorkAnimals = new AtomicInteger();
        inWaaitAnimals = new AtomicInteger();

        population.set(s * r);
        controle = new Thread(this) ;
    }
    public void start(){
        controle.start();
    }
    public void waitFor() {
        boolean b;
        while (population.get() != inWaaitAnimals.get() || inWorkAnimals.get() > 0) {
//            System.out.println(population.get() + "   pop");
//            System.out.println(inWaaitAnimals.get() + "   wait");
//            System.out.println(inWorkAnimals.get() + "   work");
//            System.out.println("--------------------------------");
            try {
               synchronized (this) {
                   wait(10);
               }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (Zoo.getZoo().isExist()) {
            System.out.println("b print");
            print();
            System.out.println("b birth");
            birth();
            System.out.println("b life");
            life();
            System.out.println("b death");
            death();
            System.out.println("--------------------------------");
        }
    }

    private void print() {
        Zoo.getZoo().printZoo();
    }

    private void death() {
        try {
            Zoo.getZoo().getMutex().acquire();
            Zoo.getZoo().getState().set(3);
            synchronized (Zoo.getZoo().getNotify()) {
                Zoo.getZoo().getNotify().notifyAll();
            }

            waitFor();
            deathing();
            synchronized (Zoo.getZoo().getNotify()) {
                Zoo.getZoo().getNotify().notifyAll();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Zoo.getZoo().getMutex().release();
        }
    }

    private void deathing() {
        balance();
        kill();
    }

    private void kill() {
        ArrayList<Table> TablesExistAnimal = Zoo.getZoo().getTableExistAnimal();
        int[] numbers;
        for (Table table : TablesExistAnimal) {
            numbers = Zoo.getZoo().getneighbors(table);
            for (int i = 1; i < numbers.length; i++) {
                if (i != table.getType() && numbers[i] * i > table.getType() * table.getNumbers()) {
                    population.addAndGet(-table.getNumbers());
                    table.kill();

                    break;
                }
            }
        }
    }

    private void balance() {
        for (Table[] tables : Zoo.getZoo().getTables()) {
            for (Table table : tables) {
                population.addAndGet(-table.getNumbers());
                table.balance();
                population.addAndGet(table.getNumbers());
            }
        }
    }

    private void life() {
        try {
            Zoo.getZoo().getMutex().acquire();
            Zoo.getZoo().getState().set(2);
            synchronized (Zoo.getZoo().getNotify()) {
                Zoo.getZoo().getNotify().notifyAll();
            }
            synchronized (this) {
                wait(Zoo.getZoo().getT() * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Zoo.getZoo().getMutex().release();
        }
    }

    private void birth() {
        try {
            Zoo.getZoo().getMutex().acquire();
            Zoo.getZoo().getState().set(1);
            synchronized (Zoo.getZoo().getNotify()) {
                Zoo.getZoo().getNotify().notifyAll();
            }
            waitFor();

            birthing();

            waitFor();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Zoo.getZoo().getMutex().release();
        }

    }

    private void birthing() {
        for (Table[] tables : Zoo.getZoo().getTables()) {
            for (Table table : tables) {

                population.addAndGet(-table.getNumbers());
                table.birth();
                population.addAndGet(table.getNumbers());
            }
        }

    }
}
