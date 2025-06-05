public class ClientProtocolManager {
    private SwingInterface swingInterface;
    private InterfaceClient client;

    public ClientProtocolManager(SwingInterface swingInterface, InterfaceClient client) {
            this.swingInterface = swingInterface;
            this.client = client;
    }

    //PROTOCOL LISTS:
    //PROTOCOL:MOVED:1X
    //PROTOCOL:CLEARBOARD:EMPTY
    //PROTOCOL:EXIT:EMPTY

    public void process(String fullCommand){
        String[] parts = fullCommand.split(":"); // PROTOCOL:COMMAND:DETAILS
        String command = parts[1];
        String details = parts[2];
        switch (command){
            case "MOVED": // PROTOCOL:MOVED:1X
                updateBoard(details);
                break;
            case "CLEARBOARD":
                swingInterface.clearBoard();
                break;
            case "EXIT":
                client.close();
                break;
        }
    }

    public void updateBoard(String details){
        int cell = Integer.parseInt(String.valueOf(details.charAt(0)));
        char symbol = details.charAt(1);
        swingInterface.setCell(cell, symbol);
    }
}
