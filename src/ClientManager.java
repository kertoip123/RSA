import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientManager extends CommunicationManager{

    public ClientManager(String name, int port) throws IOException
    {
        super("Client");
        mClientSocket = new Socket(name, port);
        System.out.println(mLogName + ": Connecting to " + name +
                " on port " + port);
        System.out.println(mLogName + ": Just connected to "
                + mClientSocket.getRemoteSocketAddress());

        this.start();
    }

    public void close() throws IOException{
        super.close();
        mClientSocket.close();
    }
}
