import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JTextArea;

public class Client {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out; 
    private final SecretKeySpec aesKey;
    private final SecretKeySpec desKey;
    private final IvParameterSpec aesIV;
    private final IvParameterSpec desIV;

    private boolean isAES; 
    private boolean isCBC;
    private boolean connection;

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    public Client() throws Exception{
        this.socket = new Socket("localhost", 5000); //crete socket for the client
        this.in = new DataInputStream(socket.getInputStream()); //socket input stream
        this.out = new DataOutputStream(socket.getOutputStream()); //socket output stream
        String deskey = in.readUTF(); //read DES key from server 
        byte[] decodedDesKey = Base64.getDecoder().decode(deskey); //decode key
        String aeskey = in.readUTF(); //read AES key from server
        byte[] decodedAesKey = Base64.getDecoder().decode(aeskey); //decode key

        //read DES IV from server
        int desLength = in.readInt(); 
        byte[] desiv = new byte[desLength];
        in.readFully(desiv, 0, desiv.length); 

        //read AES iv from server
        int aesLength = in.readInt();   
        byte[] aesiv = new byte[aesLength];
        in.readFully(aesiv, 0, aesiv.length);

        this.aesKey = new SecretKeySpec(decodedAesKey, 0, decodedAesKey.length, "AES"); //create SecretKeyspec object for AES
        this.desKey = new SecretKeySpec(decodedDesKey, 0, decodedDesKey.length, "DES"); //create SecretKeyspec object for DES
        this.aesIV = new IvParameterSpec(aesiv); //create IVParameterSpec object for AES
        this.desIV = new IvParameterSpec(desiv); //create IVParameterSpec object for DES

        this.isAES = true;
        this.isCBC = true;
    }

    public void enterUsername(String username)throws IOException{
        out.writeUTF(username); //send username to server
        out.flush();
        return;
    }

    public String encryptMessage(String message) throws Exception{
        String ciphertext;
        if(isAES && isCBC){
            //Method:AES-Mod:CBC
            ciphertext = Crypto.encrypt(message, "AES", "CBC", aesKey, aesIV);
        }else if(isAES && !isCBC){
            //Method:AES-Mod:OFB
            ciphertext = Crypto.encrypt(message, "AES", "OFB", aesKey, aesIV);
        }else if(!isAES && isCBC){
            //Method:DES-Mod:CBC
            ciphertext = Crypto.encrypt(message, "DES", "CBC", desKey, desIV);
        }else{
            //Method:DES-Mod:OFB
            ciphertext = Crypto.encrypt(message, "DES", "OFB", desKey, desIV);
        }
        return ciphertext;
    }

    public String decryptMessage(String message, boolean isAES, boolean isCBC) throws Exception{
        String plaintext;
        if(isAES && isCBC){
            //Method:AES-Mod:CBC
            plaintext = Crypto.decrypt(message, "AES", "CBC", aesKey, aesIV);
        }else if(isAES && !isCBC){
            //Method:DES-Mod:OFB
            plaintext = Crypto.decrypt(message, "AES", "OFB", aesKey, aesIV);
        }else if(!isAES && isCBC){
            //Method:DES-Mod:CBC
            plaintext = Crypto.decrypt(message, "DES", "CBC", desKey, desIV);
        }else{
            //Method:DES-Mod:OFB
            plaintext = Crypto.decrypt(message, "DES", "OFB", desKey, desIV);
        }
        return plaintext;
    }

    public void sendMessage() {

        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = queue.take();
                        out.writeUTF(message); // send message to server
                        out.flush();
                        if(message.equals("!exit") && !connection){
                            break;
                        }
                        out.writeBoolean(isAES); // send encrption method
                        out.flush();
                        out.writeBoolean(isCBC); // send encryption mode
                        out.flush();
                    }
                } catch (Exception e) {
                    System.out.println("error: encryption");
                }
            }
        });
        sendMessage.start();
    }

    public void receiveMessage(JTextArea serverDisplayTextArea) {
        Thread receiveMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String ciphertext = in.readUTF(); // read message
                        if (ciphertext.equals("!break") && !connection) {
                            break;
                        }
                        boolean isAES = in.readBoolean(); // read decryption method
                        boolean isCBC = in.readBoolean(); // read decryption mode
                        String from = in.readUTF(); // read who sends the message
                        String plaintext = decryptMessage(ciphertext, isAES, isCBC);
                        serverDisplayTextArea.append("\n" + ciphertext);
                        serverDisplayTextArea.append("\n" + from + "> " + plaintext);
                    } catch (Exception e) {
                        System.out.println("Error: user can't receive message");
                    }
                }
            }
        });
        receiveMessage.start();
    }

    public SecretKeySpec getDesKey() {
        return desKey;
    }

    public SecretKeySpec getAesKey() {
        return aesKey;
    }

    public IvParameterSpec getDesIV() {
        return desIV;
    }

    public IvParameterSpec getAesIV() {
        return aesIV;
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public boolean getMethod(){
        return this.isAES;
    }

    public boolean getMode(){
        return this.isCBC;
    }

    public void setMethod(boolean isAES) {
        this.isAES = isAES;
    }

    public void setMode(boolean isCBC) {
        this.isCBC = isCBC;
    }

    public boolean isConnected(){
        return this.connection;
    }

    public void setConnectionInfo(boolean connection){
        this.connection = connection;
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }
}

