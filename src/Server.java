import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Vector;

public class Server {

    static Vector<ClientHandler> client_list = new Vector<>();

    public static void main(String[] args) throws Exception {
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            String log_file = "log.txt";
            FileWriter writer = new FileWriter(log_file);
            String desKey = Crypto.generateRandomKey("DES");
            String aesKey = Crypto.generateRandomKey("AES");
            byte[] desIV = Crypto.generateIV(8);
            byte[] aesIV = Crypto.generateIV(16);
            String encodedAesIV = Base64.getEncoder().encodeToString(aesIV);
            String encodedDesIV = Base64.getEncoder().encodeToString(desIV);
            System.out.println("DES key: " + desKey);
            System.out.println("AES key: " + aesKey);
            System.out.println("DES IV: " + encodedDesIV);
            System.out.println("AES IV: " + encodedAesIV);
            writer.write("DES key: " + desKey + "\n");
            writer.write("AES key: " + aesKey + "\n");
            writer.write("DES IV: " + encodedDesIV + "\n");
            writer.write("AES IV: " + encodedAesIV + "\n");
            writer.close();
            while(true){
                Socket userSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(userSocket.getInputStream()); 
                DataOutputStream out = new DataOutputStream(userSocket.getOutputStream()); 
                out.writeUTF(desKey);
                out.flush();
                out.writeUTF(aesKey);
                out.flush();
                out.writeInt(desIV.length);
                out.flush();
                out.write(desIV);
                out.flush();
                out.writeInt(aesIV.length);
                out.flush();                
                out.write(aesIV);
                out.flush();
                String username = in.readUTF();
                ClientHandler clientHandler= new ClientHandler(userSocket, in, out, username, log_file);
                client_list.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch(IOException e){
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
