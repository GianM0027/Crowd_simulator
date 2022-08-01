package support;

import javax.swing.*;

public class AllertWindow extends JOptionPane {

    private String message;
    public AllertWindow(String message){
        this.message = message;
    }

    public boolean isConfirmed(){
        JLabel label = new JLabel(message);

        int value = showConfirmDialog(null, label);
        return value == 0;
    }
}
