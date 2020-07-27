package withThread.World;

import withThread.Animal.Animal;
import withThread.Controller.Controller;
import withThread.Table.Table;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Zoo  {
    private static Zoo world;

    boolean isExist;
    final boolean rule1;
    final boolean rule2;

    public boolean isRule1() {
        return rule1;
    }

    public boolean isRule2() {
        return rule2;
    }

    public Controller getController() {
        return controller;
    }

    public boolean isExist() {
        return isExist;
    }


    public Table[][] getTables() {
        return tables;
    }


    public AtomicInteger getState() {
        return state;
    }


    public Object getNotify() {
        return notify;
    }


    public Semaphore getMutex() {
        return mutex;
    }


    public int getT() {
        return t;
    }

    Table[][] tables;
    AtomicInteger state; // 0: print 1: birth  2: Life 3: death
    Object notify;
    Controller controller;
    Semaphore mutex;


    int r;
    int n;
    int m;
    int s;
    int k;
    int t;

    public Zoo(int r, int n, int m, int s, int k, int t, boolean rule1, boolean rule2) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.k = k;
        this.t = t;
        this.rule1 = rule1;
        this.rule2 = rule2;
        initialize();
    }

    private void initialize() {
        mutex = new Semaphore(1, true);
        notify = new Object();
        state = new AtomicInteger(1);
        controller = new Controller(s, r);
        createTables();
        createGUI();
        createAnimals();
        isExist = true;
    }

    private void createAnimals() {
        for (int i = 1; i <= r; i++) {
            for (int j = 1; j <= s; j++) {
                tables[i * n / r][j * m / s].move(new Animal(i, i * n / r, j * m / s));
            }
        }
    }

    private void createTables() {
        tables = new Table[n + 2][m + 2];
        for (int i = 0; i < tables.length; i++) {
            for (int j = 0; j < tables[i].length; j++) {
                if (i == 0 || j == 0 || i == n + 1 || j == m + 1)
                    tables[i][j] = new Table(false, k, i, j);
                else
                    tables[i][j] = new Table(true, k, i, j);
            }
        }
    }


    public static void createZoo(int r, int n, int m, int s, int k, int t, boolean rule1, boolean rule2) {
        if (world == null) {
            world = new Zoo(r, n, m, s, k, t, rule1, rule2);
        }
    }

    public static Zoo getZoo() {
        return world;
    }



    public void printZoo() {
        try {
            mutex.acquire();
             state.getAndSet(0);
            synchronized (notify) {
                notify.notifyAll();

            }
            controller.waitFor();
            printing();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            mutex.release();
            synchronized (notify) {
                notify.notifyAll();

            }

        }
    }

    JFrame frame;
    JButton[][] buttons;

    private void createGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(n+2, m+2));

        buttons = new JButton[n + 2][m + 2];

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButton();
                frame.add(buttons[i][j]);
            }
        }
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    private void printing() {
        update();
        frame.repaint();
        frame.revalidate();
    }

    private void update() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                buttons[i][j].setText("r = " + tables[i][j].getType() + " | n = " + tables[i][j].getNumbers());
                if (tables[i][j].getType() == 0)
                    buttons[i][j].setBackground(Color.WHITE);
                else
                    buttons[i][j].setBackground(new Color(f(255 * tables[i][j].getType() / r), g(255 * tables[i][j].getType() / r), h(255 * tables[i][j].getType() / r)));
            }
        }
    }

    private int f(int x) {
//        return (int) (Math.sqrt(x * (x - 256)) * 1.99);
//        return (int) (Math.sqrt((x - 128) * (x - 128)) * 1.99);
        return (int) Math.cos(x*Math.PI/180)*255;

    }

    private int g(int x) {
//        return (int) Math.cos(x*Math.PI/180)*255;
        return (int) (Math.sqrt((x - 128) * (x - 128)) * 1.99);
    }

    private int h(int x) {
        return(int) Math.sin(x*Math.PI/180)*255;
//        (Math.sqrt((x - 128) * (x - 128)) * 1.99);
    }

    public ArrayList<Table> getTableExistAnimal() {
        ArrayList<Table>[] TableOfEachType = new ArrayList[r + 1];
        ArrayList<Table> AllTable = new ArrayList<>();

        for(int i = 0 ; i<TableOfEachType.length ; i++)
            TableOfEachType[i] = new ArrayList<>();
        for (Table[] tables : this.tables) {
            for (Table table : tables) {
                TableOfEachType[table.getType()].add(table);
            }
        }

        for (ArrayList<Table> x : TableOfEachType)
            if (x.size() > 0 && x.get(0).getType() > 0)
                AllTable.addAll(x);

        if (!rule1)
            Collections.shuffle(AllTable);
        return AllTable;

    }

    public int[] getneighbors(Table table) {
        int[] numbers = new int[r + 1];
        for (int i = table.getN() - 1; i <= table.getN() + 1; i++) {
            for (int j = table.getM() - 1; j <= table.getM() + 1; j++) {
                if (canBeNeighbor(i, j, table)) {
                    numbers[tables[i][j].getType()] += tables[i][j].getNumbers();
                }
            }
        }
        return numbers;
    }

    private boolean canBeNeighbor(int i, int j, Table table) {
        if (i == table.getN() && j == table.getM())
            return false;
        if (rule2 && table.getType() > r / 2 && tables[i][j].getType() < table.getType())
            return i == table.getN() || j == table.getM();

        return true;
    }

    public void start() {
        controller.start();
        startAnimal();
    }

    private void startAnimal() {
        for (Table[] tables : this.tables)
            for(Table table : tables)
                table.start();
    }
}
