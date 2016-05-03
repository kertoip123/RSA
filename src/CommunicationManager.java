import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

public class CommunicationManager extends Thread{
    protected String  mLogName;
    protected Socket  mClientSocket;
    private Boolean mSyncMutex;
    private LinkedList<String> mReceivedMessages;

    public CommunicationManager(String logName) throws IOException
    {
        mLogName = logName;
        mClientSocket = null;

        mReceivedMessages = new LinkedList<String>();
        mSyncMutex = true;
    }

    public void run(){
        while(mSyncMutex){
            try
            {
                manageRunIteration();
            }
            catch(SocketTimeoutException s){
                System.out.println(mLogName + ": My socket was timed out!");
            }
            catch(IOException e) {
                mClientSocket = null;
                e.printStackTrace();
            }
        }
    }


    protected void manageRunIteration() throws IOException{
        read();
    }

    protected void read() throws  IOException{
        DataInputStream in = new DataInputStream(mClientSocket.getInputStream());
        if(in.available() > 0){
            String message = in.readUTF();
            System.out.println(mLogName + ": Received message: " + message);
            mReceivedMessages.add(message);
        }
    }

    public void send(String message) throws IOException{
        DataOutputStream out = new DataOutputStream(mClientSocket.getOutputStream());
        out.writeUTF(message);
        System.out.println("Client: Sent message: " + message);
    }

    public String getMessage(){
        if(mReceivedMessages.isEmpty()){
            return null;
        }
        else{
            return mReceivedMessages.removeFirst();
        }
    }

    public void close() throws IOException{
        mSyncMutex = false;
    }
}
