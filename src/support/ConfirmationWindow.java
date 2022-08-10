package support;

import javax.swing.*;

/**
 * Allert windows that shows a message in a confirmDialog pane (3 buttons to click: yes, no, cancel)
 * */
public class ConfirmationWindow extends JOptionPane {

    private String message;
    public ConfirmationWindow(String message){
        this.message = message;
    }

    public boolean isConfirmed(){
        JLabel label = new JLabel(message);

        int value = showConfirmDialog(null, label);
        return value == 0;
    }
}
