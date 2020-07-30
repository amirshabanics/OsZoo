package withProccess.Animal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


public class Animal extends Thread {


    AtomicBoolean stop;
    Object move;

    //    static FileWriter log;
    static AtomicBoolean giveBack;


    public static void main(String[] args) {
        giveBack = new AtomicBoolean(false);
//        try {
////            log.write("send stop");
////            log = new FileWriter(new File("log/" + Math.random() + ".txt"));
//            log = new FileWriter(new File("/home/morta/IdeaProjects/OsZoo/src/withProccess/Animal/log/" + Math.random() + ".txt"));
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Animal animal = new Animal();
        Scanner reader = new Scanner(System.in);
        animal.start();

        new Thread(() -> {
            while (true) {
                if (giveBack.get() == true) {
                    synchronized (animal.move) {
                        animal.move.notifyAll();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        String s;
        a:
        while (true) {
            s = reader.nextLine();
//            synchronized (log) {
//                try {
//                    log.write(s + "\n");
//                    log.flush();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            switch (s) {
                case "move":

//                    synchronized (log) {
//                        try {
//                            log.write("set Free From move wait\n");
//                            log.flush();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    synchronized (animal.move) {

                        animal.move.notifyAll();
                    }
                    giveBack.set(true);
                    break;
                case "notify":
//                    synchronized (log) {
//                        try {
//                            log.write("server say  run \n");
//                            log.flush();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    animal.stop.set(false);
                    synchronized (animal) {
                        animal.notifyAll();
                    }
                    break;
                case "stop":
//                    synchronized (log) {
//                        try {
//                            log.write("server say stop\n");
//                            log.flush();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    animal.stop.set(true);
                    synchronized (animal.move) {
                        animal.move.notifyAll();
                    }
                    break;
                case "kill":
                    animal.interrupt();
                    synchronized (animal) {
                        animal.notifyAll();
                    }
                    break a;


            }
        }
    }

    private Animal() {
        stop = new AtomicBoolean(true);
        move = new Object();
    }

    @Override
    public void run() {
        while (true) {

            if (stop.get()) {
                synchronized (this) {
                    try {
//                        synchronized (log) {
//                            try {
//                                log.write("animal stop\n");
//                                log.flush();
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
                        System.out.println("stop");
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                synchronized (log) {
//                    try {
//                        log.write("animal resume AND SEND NOTIFY\n");
//                        log.flush();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                System.out.println("notify");
            }
            if (isInterrupted()) {
                System.out.println("kill\n");
                break;
            }

            if (Math.random() < 0.5)
                move((int) (Math.random() * 3 - 1), (int) (Math.random() * 3 - 1));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void move(int n, int m) {
        if (stop.get())
            return;
//        synchronized (log) {
//            try {
//                log.write("animal wana move" + "  " + n + "    " + m + "\n");
//                log.flush();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println("move" + " " + n + " " + m);
        synchronized (move) {
            try {
                move.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        giveBack.set(false);
//        synchronized (log) {
//            try {
//                log.write("animal free from move" + "  " + n + "  " + m + "\n");
//                log.flush();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
