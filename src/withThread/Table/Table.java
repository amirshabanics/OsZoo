package withThread.Table;

import withThread.Animals.Animal;

import java.util.ArrayList;

public class Table {
    boolean possible;
    int n;
    int m;
    int type;
    int numbers;
    int limit;
    ArrayList<Animal> animals;

    public Table(boolean possible, int limit) {
        this.possible = possible;
        this.limit = limit;
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
            if(type == this.type){
                numbers--;
                animals.remove(animal);
                if(numbers == 0){
                    type = 0;
                }
                return true;
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

    public synchronized void birth() {
        ArrayList<Animal> baby = new ArrayList<Animal>();
        for(int i = 0 ; i< numbers ; i++){
            baby.add(new Animal(type , n , m));
        }
        numbers+= baby.size();
        animals.addAll(baby);
    }

    public void death() {
        ArrayList<Animal> balance = new ArrayList<Animal>();
        for(int i = 0 ; (i+1)*type < limit ; i++){
            balance.add(animals.get(i));
        }
        numbers = balance.size();
        for(int i = numbers ; i<animals.size() ; i++){
            animals.get(i).kill();
        }
        animals = balance;

    }
}
