import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel{
    private JFrame frame;

    public SimulationPanel(JFrame frame){
        this.frame = frame;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);
    }


    public JFrame getFrame() {
        return frame;
    }
}

