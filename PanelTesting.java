import javax.swing.*;
import java.awt.*;

public class PanelTesting {
    public static void main(String[] args) {
        // create the JFrame
        //We will probably need different JFrame for seller and customer
        JFrame frame = new JFrame("Seller options");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // create the JPanel to see options
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        // create the option buttons
        JButton option1 = new JButton("Option 1");
        JButton option2 = new JButton("Option 2");
        JButton option3 = new JButton("Option 3");
        JButton option4 = new JButton("Option 4");
        JButton option5 = new JButton("Option 5");

        // add the option buttons to the panel
        optionsPanel.add(option1);
        optionsPanel.add(option2);
        optionsPanel.add(option3);
        optionsPanel.add(option4);
        optionsPanel.add(option5);

        // create the JPanel for the text block
        //Need also to add the reading and displaying the file with messages
        JPanel textPanel = new JPanel();
        JTextArea textArea = new JTextArea(20, 30);
        textArea.setEditable(false);
        textPanel.add(textArea);

        // add the option panel and text panel to the JFrame
        frame.add(optionsPanel, BorderLayout.WEST);
        frame.add(textPanel, BorderLayout.CENTER);

        // show the Frame
        frame.setVisible(true);
    }
}
