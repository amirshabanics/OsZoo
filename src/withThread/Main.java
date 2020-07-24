package withThread;

import withThread.World.Zoo;

public class Main {
    public static void main(String[] args) {
       int r=10;
       int n=15;
       int m=7;
       int s=3;
       int k=20;
       int t=1;
       boolean r1 = false;
       boolean r2 = false;

        Zoo.createZoo(r , n , m , s , k , t , r1  , r2);
        Zoo.getZoo().start();
    }
}
