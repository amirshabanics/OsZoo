package withProccessV2.World;

import withProccessV2.Cage.Cage;
import withProccessV2.Controller.AnimalController;
import withProccessV2.Controller.ZooController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;

public class Zoo {
    private static Zoo zoo;

    int r;
    int n;
    int m;
    int s;
    int k;
    int t;
    boolean rule1;
    boolean rule2;

    public boolean b = false;

    ZooController controller;
    Cage[][] cages;
    boolean isLive;

    public static void start() {
        zoo.startWorld();
    }

    private void startWorld() {
        controller.start();
        for (Cage[] cage : cages) {
            for (Cage c : cage)
                c.startAnimal();
        }
    }

    public boolean isLive() {
        return isLive;
    }

    public Cage[][] getCages() {
        return cages;
    }

    public int getT() {
        return t;
    }

    public ZooController getController() {
        return controller;
    }

    public Zoo(int r, int n, int m, int s, int k, int t, boolean rule1, boolean rule2) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.k = k;
        this.t = t;
        this.rule1 = rule1;
        this.rule2 = rule2;
        isLive = true;
        initialize();
    }

    private void initialize() {
        createCages();
        createAnimals();
        controller = new ZooController(s, r);
        createGUI();
    }

    private void createAnimals() {
        for (int i = 1; i <= r; i++)
            for (int j = 1; j <= s; j++)
                cages[i * n / r][j * m / s].move(new AnimalController(i * n / r, j * m / s, i));
    }

    private void createCages() {
        cages = new Cage[n + 2][m + 2];
        for (int i = 0; i < cages.length; i++)
            for (int j = 0; j < cages[i].length; j++)
                if (i * j == 0 || i == n + 1 || j == m + 1)
                    cages[i][j] = new Cage(false, i, j, k);
                else
                    cages[i][j] = new Cage(true, i, j, k);
    }

    public static void createZoo(int r, int n, int m, int s, int k, int t, boolean r1, boolean r2) {
        if (zoo == null)
            zoo = new Zoo(r, n, m, s, k, t, r1, r2);
    }

    public static Zoo getZoo() {
        return zoo;
    }

    ArrayList<Cage>[] allTypes;
    ArrayList<Cage> allTables;

    public ArrayList<Cage> getTableMustCheck() {
        allTypes = new ArrayList[r + 1];
        allTables = new ArrayList<>();
        for (int i = 0; i < allTypes.length; i++)
            allTypes[i] = new ArrayList<>();

        for (Cage[] cage : cages)
            for (Cage c : cage)
                if (c.getType() > 0 && c.getNumbers() > 0)
                    allTypes[c.getType()].add(c);

        for (int i = 1; i < allTypes.length; i++)
            if (allTypes[i].size() > 0)
                allTables.addAll(allTypes[i]);

        if (!rule1)
            Collections.shuffle(allTables);

        return allTables;
    }

    int[] numbers;

    public int[] getNeighbors(Cage cage) {
        int n = cage.getN();
        int m = cage.getM();
        numbers = new int[r + 1];

        for (int i = n - 1; i <= n + 1; i++)
            for (int j = m - 1; j <= m + 1; j++)
                if (canBeNeighbors(i, j, cage))
                    numbers[cage.getType()] += cage.getNumbers();
        return numbers;
    }

    private boolean canBeNeighbors(int i, int j, Cage cage) {
        if (i == cage.getN() && j == cage.getM())
            return false;
        if (rule2 && r / 2 < cage.getType() && cages[i][j].getType() < cage.getType())
            return i == cage.getN() || j == cage.getM();
        return true;
    }

    JFrame frame;
    JButton[][] buttons;

    private void createGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(n + 2, m + 2));

        buttons = new JButton[n + 2][m + 2];

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButton();
                frame.add(buttons[i][j]);
            }
        }
        buttons[0][0].addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Cage[] cage : cages)
                    for (Cage c : cage)
                        c.kill();

                frame.setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.setSize(500, 500);
        frame.setVisible(true);

    }

    public void printing() {
        update();
        frame.repaint();
        frame.revalidate();
    }

    private void update() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                buttons[i][j].setText("r = " + cages[i][j].getType() + " | n = " + cages[i][j].getNumbers());
                if (cages[i][j].getType() == 0)
                    buttons[i][j].setBackground(Color.WHITE);
                else
                    buttons[i][j].setBackground(new Color(f(255 * cages[i][j].getType() / r), g(255 * cages[i][j].getType() / r), h(255 * cages[i][j].getType() / r)));
            }
        }

    }

    private int f(int x) {
//        return (int) (Math.sqrt(x * (x - 256)) * 1.99);
//        return (int) (Math.sqrt((x - 128) * (x - 128)) * 1.99);
        return (int) Math.cos(x * Math.PI / 180) * 255;

    }

    private int g(int x) {
//        return (int) Math.cos(x*Math.PI/180)*255;
        return (int) (Math.sqrt((x - 128) * (x - 128)) * 1.99);
    }

    private int h(int x) {
        return (int) Math.sin(x * Math.PI / 180) * 255;
//        (Math.sqrt((x - 128) * (x - 128)) * 1.99);
    }
}
