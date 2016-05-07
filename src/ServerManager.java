import java.io.IOException;
import java.net.ServerSocket;


public class ServerManager extends CommunicationManager{

    private ServerSocket        mServerSocket;

    public ServerManager(int port) throws IOException
    {
        super("Server");
        mServerSocket = new ServerSocket(port);
        mServerSocket.setSoTimeout(1000);

        this.start();
    }

    protected void manageRunIteration() throws IOException{
        if(mClientSocket == null){
            LOGGER.fine(mLogName + ": Waiting for client on port " +
                    mServerSocket.getLocalPort() + "...");
            mClientSocket = mServerSocket.accept();
            LOGGER.fine(mLogName + "Just connected to "
                    + mClientSocket.getRemoteSocketAddress());

        }
        else {
            read();
        }
    }

    public void close() throws IOException{
        super.close();
        mServerSocket.close();
    }

}
