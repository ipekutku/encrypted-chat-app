import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final Writer writer;
    private String username;
    private boolean loggedIn;

    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out, String username, String logFile) throws UnknownHostException, IOException {
        this.socket = socket;
        this.loggedIn = true;
        this.input = in;
        this.output = out;
        this.username = username;
        this.writer = new FileWriter(logFile, true);
    }

    @Override
    public void run() {
        try {
            System.out.println(username + " connected.");
            writer.write(username + " connected.\n");
            writer.flush();
            while(true){
                String receivedMessage = this.input.readUTF();
                if (receivedMessage.equals("!exit")) {
                    this.output.writeUTF("!break");
                    this.output.flush();
                    break;
                }
                    boolean isAES = this.input.readBoolean();
                    boolean isCBC = this.input.readBoolean();
                    System.out.println(this.username + "> " + receivedMessage);
                    writer.write(this.username + "> " + receivedMessage +"\n");
                    writer.flush();
                    for (ClientHandler clientHandler : Server.client_list) {
                        if (clientHandler.getLogInfo() == true) {
                            clientHandler.getOutput().writeUTF(receivedMessage);
                            clientHandler.getOutput().flush();
                            clientHandler.getOutput().writeBoolean(isAES);
                            clientHandler.getOutput().flush();
                            clientHandler.getOutput().writeBoolean(isCBC);
                            clientHandler.getOutput().flush();
                            clientHandler.getOutput().writeUTF(this.username);
                            clientHandler.getOutput().flush();
                        }
                    }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            this.loggedIn = false;
            try {
                this.socket.close();
                this.input.close();
                this.output.close();
                System.out.println(this.username + " disconnected.");
                writer.write(this.username + " disconnected.\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error: user can't disconnet -> " + e.getMessage());
            }
        }
    }

    public DataInputStream getInput() {
        return input;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public String getUsername() {
        return username;
    }

    public boolean getLogInfo() {
        return loggedIn;
    }


}
