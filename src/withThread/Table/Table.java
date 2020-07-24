package withThread.Table;

import withThread.Animals.Animal;

import java.util.ArrayList;

public class Table {
    final boolean possible;
    final int n;
    final int m;
    final int limit;

    int type;
    int numbers;
    ArrayList<Animal> animals;

    public Table(boolean possible, int limit , int n , int m) {
        this.possible = possible;
        this.limit = limit;
        this.n = n;
        this.m = m;
        animals = new ArrayList<>();
    }

    public synchronized boolean move(Animal animal){
      if(possible){
          if(animal.getType() == type || type == 0){
              this.type = animal.getType();
              if((numbers+1)*type < limit){
                  numbers++;
                  animals.add(animal);
                  return true;
              }
          }
      }
       return false;
    }

    public  synchronized boolean exit(Animal animal){
        if(possible){
            if(animal.getType() == this.type){
                if(animals.remove(animal)){
                    numbers--;
                    if(numbers == 0){
                        type = 0;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized int getType(){
        return type;
    }
    public synchronized int getNumbers(){
        return numbers;
    }

    public synchronized int getN() {
        return n;
    }

    public synchronized int getM() {
        return m;
    }

    public synchronized void birth() {
        Animal a;
        for(int i = 0 ; i< numbers ; i++){
            a = new Animal(type , n , m);
            a.start();
            animals.add(a);
        }
        numbers+= numbers;
    }

    public void balance() {
        ArrayList<Animal> balance = new ArrayList<>();
        for(int i = 0 ; (i+1)*type < limit && i<numbers; i++){
            balance.add(animals.get(i));
        }
        numbers = balance.size();
        for(int i = numbers ; i<animals.size() ; i++){
            animals.get(i).kill();
        }
        animals = balance;

    }

    public void kill() {
        type = 0;
        numbers = 0;
        for (Animal animal : animals)
            animal.kill();
        animals.clear();
    }

    public void start() {
        for(Animal animal : animals)
            animal.start();
    }
}
