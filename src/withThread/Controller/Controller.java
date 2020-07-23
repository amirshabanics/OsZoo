package withThread.Controller;

import withThread.Table.Table;
import withThread.World.Zoo;

import java.util.concurrent.atomic.AtomicInteger;

public class Controller implements  Runnable{

    AtomicInteger population;
    AtomicInteger inWorkAnimals;
    AtomicInteger inWaaitAnimals;

    public AtomicInteger getPopulation() {
        return population;
    }

    public AtomicInteger getInWorkAnimals() {
        return inWorkAnimals;
    }

    public AtomicInteger getInWaaitAnimals() {
        return inWaaitAnimals;
    }

    public void waitFor() {
    }

    @Override
    public void run() {
        while(Zoo.getZoo().isExist()){
            birth();
            life();
            death();
        }
    }

    private void death() {
        try {
            Zoo.getZoo().getMutex().acquire();
            Zoo.getZoo().getState().set(2);
            Zoo.getZoo().getNotify().signalAll();

            waitFor();
            deathing();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Zoo.getZoo().getMutex().release();
        }
    }

    private void deathing() {
        balance();
        kill();
    }

    private void kill() {

    }

    private void balance() {
        for(Table[] tables : Zoo.getZoo().getTables()){
            for(Table table : tables){
                population.addAndGet(-table.getNumbers());
                table.death();
                population.addAndGet(table.getNumbers());
            }
        }
    }

    private void life() {
        try {
            Zoo.getZoo().getMutex().acquire();
            Zoo.getZoo().getState().set(2);
            Zoo.getZoo().getNotify().signalAll();

            wait(Zoo.getZoo().getT()*1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Zoo.getZoo().getMutex().release();
        }
    }

    private void birth() {
        try {
            Zoo.getZoo().getMutex().acquire();
            Zoo.getZoo().getState().set(1);
            Zoo.getZoo().getNotify().signalAll();
            waitFor();

            birthing();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Zoo.getZoo().getMutex().release();
        }

    }

    private void birthing() {
        for(Table[] tables : Zoo.getZoo().getTables()){
            for(Table table : tables){

                population.addAndGet(-table.getNumbers());
                table.birth();
                population.addAndGet(table.getNumbers());
            }
        }

    }
}
