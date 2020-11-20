import java.io.IOException;
import java.net.*;

public class MulticastSender implements Runnable {
    private static DatagramSocket socket;
    private final String message;
    private final InetAddress group;
    Thread thread;
    MulticastSender(InetAddress group, String message) throws SocketException {
        this.socket = new DatagramSocket();
        this.group = group;
        this.message = message;
        thread = new Thread(this, "MulticastSender");
        thread.start();
    }
    @Override
    public void run() {
        DatagramPacket packetSend = new DatagramPacket(message.getBytes(), message.length(), group, 6790);//хрень
        for(;;) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            try {
                socket.send(packetSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.currentThread().sleep(TimeConstants.DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

































