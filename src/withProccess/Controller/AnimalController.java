package withProccess.Controller;

import withProccess.Cage.Cage;
import withProccess.World.Zoo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class AnimalController implements Runnable {

    int n;
    int m;
    int type;
    boolean isLive;
    Thread life;

    Process process;
    PrintWriter printer;
    Scanner reader;
    Scanner error;

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public int getType() {
        return type;
    }

    public AnimalController(int n, int m, int type) {
        this.n = n;
        this.m = m;
        this.type = type;
        isLive = true;
        life = new Thread(this);

        CreateProccess();
    }

    private void CreateProccess() {
        try {
//            process = Runtime.getRuntime().exec("java C:\\Users\\npc\\IdeaProjects\\OsZoo\\src\\withProccessV2\\Animal\\Animal.java");
//            process = Runtime.getRuntime().exec("java /home/morta/IdeaProjects/OsZoo/src/withProccessV2/Animal/Animal.java");
            process = Runtime.getRuntime().exec("java  -classpath /home/morta/IdeaProjects/OsZoo/bin withProccess.Animal.Animal");
        } catch (IOException e) {
            e.printStackTrace();
        }
        printer = new PrintWriter(process.getOutputStream());
        reader = new Scanner(process.getInputStream());
        error = new Scanner(process.getErrorStream());

    }

    @Override
    public void run() {
        String[] s;
        String me = "";
        while (isLive) {
            me = reader.nextLine();
            s = me.split(" ");
            switch (s[0]) {
                case "notify":
                    if (isLive)
                        Zoo.getZoo().getController().getInWait().addAndGet(-1);
                    break;
                case "stop":
//                    if (Zoo.getZoo().b == true) {
//                        System.out.println(Zoo.getZoo().getController().getInWait().get() + "   before");
//                        System.out.println("get message ------------------");
//                    }
                    Zoo.getZoo().getController().getInWait().addAndGet(1);
//                    System.out.println(Zoo.getZoo().getController().getInWait().get() +  "   now");
//                    System.out.println("------------------------------");
                    break;
                case "move":
                    if (isLive && Zoo.getZoo().getController().getState().get() == 1 && move(Integer.parseInt(s[1]) + n, Integer.parseInt(s[2]) + m, n, m)) {
                        n += Integer.parseInt(s[1]);
                        m += Integer.parseInt(s[2]);
                    }
                    printer.write("move\n");
                    printer.flush();
                    break;
                case "kill":
                    reader.close();
                    printer.close();
                    error.close();
//                    Zoo.getZoo().getController().getInWait().addAndGet(-1);
//                    Zoo.getZoo().getController().getPopulation().addAndGet(-1);
                    break;

            }
        }

    }

    Cage[][] cages;

    private boolean move(int i, int j, int n, int m) {
        cages = Zoo.getZoo().getCages();
        if (cages[i][j].move(this)) {
            cages[n][m].exit(this);
            return true;
        }
        return false;

    }

    public void notifyClient() {

        printer.write("notify" + "\n");
        printer.flush();
    }

    public void stopClient() {
        printer.write("stop" + "\n");
        printer.flush();
    }

    public void kill() {
        isLive = false;
        printer.write("kill\n");
        printer.flush();


    }

    public void start() {
        life.start();
    }
}
