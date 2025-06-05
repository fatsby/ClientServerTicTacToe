import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SwingInterface extends JFrame {
    private JTextArea textArea;
    private JPanel board;
    private ArrayList<JButton> buttons; // this will be used to get button id
    private InterfaceClient client;

    public SwingInterface(InterfaceClient client) {
        buttons = new ArrayList<>();
        this.client = client;
        init();
    }

    private void init(){
        setTitle("TicTacToe Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setFocusable(true);

        textArea = new JTextArea();
        textArea.setBounds(50, 450, 700, 100);
        textArea.setEditable(false);
        getContentPane().add(textArea);

        board = new JPanel();
        board.setLayout(new GridLayout(3, 3, 20, 20));
        board.setBounds(250, 100, 300, 300);
        getContentPane().add(board);

        //main board when the game starts
        for (int i = 0; i < 9; i++) {
            JButton boardBtn = new JButton();
            boardBtn.setFont(new Font("Arial", Font.BOLD, 40));
            boardBtn.setBackground(Color.LIGHT_GRAY);
            buttons.add(boardBtn);

            //remember the boardBtn id needs to be +1 because it is an index of array
            boardBtn.addActionListener(e -> {
                String btnID = String.valueOf(buttons.indexOf(boardBtn)+1);
                client.write(btnID); //tells the client to send the pressed button to server
            });

            board.add(boardBtn);
        }
    }

    public void setCell(int cellIndex, char symbol){
        JButton button = buttons.get(cellIndex-1);
        button.setText(String.valueOf(symbol));
    }

    public void clearBoard(){
        buttons.forEach(button -> button.setText(""));
    }

    public void updateLabelMessage(String message){
        textArea.setText(message);
        this.revalidate();
        this.repaint();
    }
}