import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.*;

public class ClientManager extends CommunicationManager{

    public ClientManager(String name, int port) throws IOException
    {
        super("Client");
        mClientSocket = new Socket(name, port);
        LOGGER.fine(mLogName + ": Connecting to " + name +
                " on port " + port);
        LOGGER.fine(mLogName + ": Just connected to "
                + mClientSocket.getRemoteSocketAddress());
        
        this.start();
    }

    public void close() throws IOException{
        super.close();
        mClientSocket.close();
    }


}
