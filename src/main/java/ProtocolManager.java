import java.util.*;
public class ProtocolManager {
    private static ProtocolManager protocol;
    private StringBuilder sb;
    public ProtocolManager(){
    }

    public static ProtocolManager getInstance(){
        if (protocol == null){
            protocol = new ProtocolManager();
        }
        return protocol;
    }

    //PROTOCOL LISTS:
    //PROTOCOL:MOVED:1X
    //PROTOCOL:CLEARBOARD:EMPTY
    //PROTOCOL:EXIT:EMPTY

    public void sendNewMove(ClientHandler client1, ClientHandler client2, int move, char symbol){
        sb = new StringBuilder(); // reset the sb
        sb.append(move).append(symbol);
        client1.sendMessage("PROTOCOL:MOVED:"+sb); //PROTOCOL:COMMAND:DETAILS
        client2.sendMessage("PROTOCOL:MOVED:"+sb);
    }

    public void sendClearBoard(ClientHandler client1, ClientHandler client2){
        client1.sendMessage("PROTOCOL:CLEARBOARD:EMPTY");
        client2.sendMessage("PROTOCOL:CLEARBOARD:EMPTY");
    }

    public void sendClearBoard(ClientHandler client1){
        client1.sendMessage("PROTOCOL:CLEARBOARD:EMPTY");
    }

    public void sendExit(ClientHandler client){
        client.sendMessage("PROTOCOL:EXIT:EMPTY");
    }
}
