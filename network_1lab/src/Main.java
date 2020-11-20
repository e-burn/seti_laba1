import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

public class Main {
    public final static int PORT = 6790;
    private final static String IP = "228.5.6.7";
    private static String MESSAGE = "HI";
    private static MulticastSocket socket;
    public static void main(String[] args) throws SocketException {
        InetAddress group = null;
        try {
            group = InetAddress.getByName(IP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            socket = new MulticastSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TimeoutUpdateList<String> listOfUsers = new TimeoutUpdateList<String>();
        MulticastListener listener = new MulticastListener(socket, group, MESSAGE, listOfUsers);
        MulticastSender sender = new MulticastSender(group, MESSAGE);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Interrupting threads");
                Set<Thread> runningThreads = Thread.getAllStackTraces().keySet();
                for (Thread th : runningThreads) {
                    if (th != Thread.currentThread()
                            && !th.isDaemon()
                            && th.getName().startsWith("Multicast")) {
                        System.out.println("Interrupting '" + th.getName() + "' termination");
                        th.interrupt();
                    }
                }
                for (Thread th : runningThreads) {
                    try {
                        if (th != Thread.currentThread()
                                && !th.isDaemon()
                                && th.isInterrupted()) {
                            System.out.println("Waiting '" + th.getName() + "' termination");
                                th.join();
                        }
                    } catch (InterruptedException ex) {
                        System.out.println("Shutdown interrupted");
                    }
                }
                System.out.println("Shutdown finished");
                socket.close();
                System.out.println("Socket closed");
            }
        });
    }
}