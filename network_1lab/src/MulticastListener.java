import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class MulticastListener implements Runnable {
    private TimeoutUpdateList listOfUsers;
    private MulticastSocket socket;
    private String message;
    private InetAddress group;
    Thread thread;

    MulticastListener(MulticastSocket socket, InetAddress group, String message,TimeoutUpdateList listOfUsers) {
        this.socket = socket;
        this.group = group;
        this.message = message;
        this.listOfUsers = listOfUsers;
        thread = new Thread(this, "MulticastListener");
        thread.start();
    }
    @Override
    public void run() {
        byte[] buf = new byte[1000];
        DatagramPacket packetReceive = new DatagramPacket(buf, buf.length);
        for(;;) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            try {
                socket.receive(packetReceive);
                listOfUsers.push(packetReceive.getAddress() + ":" + packetReceive.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


