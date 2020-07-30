package withProccess;


import withProccess.World.Zoo;

public class Main {
    public static void main(String[] args) {
        int r=3;
        int n=15;
        int m=7;
        int s=3;
        int k=20;
        int t=1;
        boolean r1 = true;
        boolean r2 = true;

        Zoo.createZoo(r , n , m , s  , k , t, r1 , r2);
        Zoo.start();
    }
}
