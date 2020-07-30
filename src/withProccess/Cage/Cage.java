package withProccess.Cage;

import withProccess.Controller.AnimalController;

import java.util.concurrent.CopyOnWriteArrayList;

public class Cage {

    boolean possible;
    int n;
    int m;
    CopyOnWriteArrayList<AnimalController> animals;
    int numbers;
    int type;
    int limit;

    public int getNumbers() {
        return numbers;
    }

    public int getType() {
        return type;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public Cage(boolean possible, int n, int m, int limit) {
        this.possible = possible;
        this.n = n;
        this.m = m;
        this.limit = limit;
        animals = new CopyOnWriteArrayList<>();
    }

    public synchronized boolean move(AnimalController animal) {
        if (!possible)
            return false;

        if (type == 0 || type == animal.getType()) {
            if ((numbers + 1) * type <= limit) {
                type = animal.getType();
                numbers++;
                animals.add(animal);
            }
        }
        return false;
    }

    public synchronized boolean exit(AnimalController animal) {
        if (!possible)
            return false;
        if (animals.remove(animal)) {
            numbers--;
            if (numbers == 0)
                type = 0;
            return true;
        }
        return false;


    }


    public void notifyAnimal() {
        for (AnimalController animal : animals)
            animal.notifyClient();
    }

    public void stopAnimals() {
        for (AnimalController animal : animals)
            animal.stopClient();
    }

    public synchronized void birth() {
        AnimalController a ;
        for (AnimalController animal : animals) {
            a = new AnimalController(n, m, type);
            animals.add(a);
            a.start();
        }
        numbers *= 2;
    }

    public synchronized  void balance() {
        CopyOnWriteArrayList<AnimalController> balance = new CopyOnWriteArrayList<>();
//        for (int i = 0; i < numbers && (i + 1) * type <= limit; i++)
//            balance.add(animals.get(i));
//        numbers = balance.size();
//        System.out.println("balance: " + numbers + "    all: " + animals.size());
//        for (int i = numbers; i < animals.size(); i++) {
//            System.out.println("wana kill number " + (i + 1));
//            animals.get(i).kill();
//        }
        int i = 1;
        for(AnimalController animal : animals){
            if(i*type <= limit)
                balance.add(animal);
            else
                animal.kill();
            i++;
        }

//        System.out.println("----------------------------");
        animals.clear();
        animals = balance;
    }

    public void kill() {
        numbers = 0;
        type = 0;
        for (AnimalController animal : animals)
            animal.kill();
        animals.clear();
    }

    public void startAnimal() {
        for (AnimalController animal : animals) {
            animal.start();
        }
    }
}
