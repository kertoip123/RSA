import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommunicationManager extends Thread{

    protected static final Logger LOGGER = Logger.getLogger(CommunicationManager.class.getName());

    protected String  mLogName;
    protected Socket  mClientSocket;
    private Boolean mSyncMutex;
    private LinkedList<byte []> mReceivedMessages;

    public CommunicationManager(String logName) throws IOException
    {
        mLogName = logName;
        mClientSocket = null;

        mReceivedMessages = new LinkedList<byte []>();
        mSyncMutex = true;
    }

    public void run(){
        while(mSyncMutex){
            try
            {
                manageRunIteration();
                Thread.sleep(100);
            }
            catch(SocketTimeoutException s){
                //System.out.println(mLogName + ": My socket was timed out!");
                LOGGER.fine(mLogName + ": My socket was timed out!");
            }
            catch(IOException e) {
                mClientSocket = null;
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }


    protected void manageRunIteration() throws IOException{
        read();
    }

    protected void read() throws  IOException{
        DataInputStream in = new DataInputStream(mClientSocket.getInputStream());
        int count = in.available();
        if(count > 0){
            byte [] message = new byte [count];
            count = in.read(message);
            //System.out.println(mLogName + ": Received message: " + new String(message));
            LOGGER.fine(mLogName + ": Received message: " + count + " bytes");
            mReceivedMessages.add(message);
        }
    }

    public void send(byte [] message) throws IOException{
        DataOutputStream out = new DataOutputStream(mClientSocket.getOutputStream());
        out.write(message);
        //System.out.println(mLogName + ": Sent message: " + new String(message));
        LOGGER.fine(mLogName + ": Sent message: " + message.length + " bytes");
    }

    public byte[] getMessage(){
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

    public static void addLogHandler(Handler handler){
        LOGGER.setLevel(Level.FINE);
        LOGGER.addHandler(handler);
    }
}
