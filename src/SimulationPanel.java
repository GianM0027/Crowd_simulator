import javax.swing.*;
import java.awt.*;

public class SimulationPanel{
    private JPanel panel;
    private GridBagConstraints gbd = new GridBagConstraints();
    private JFrame frame;

    public SimulationPanel(JFrame frame){
        this.frame = frame;
        this.panel = new JPanel();
        gbd.gridx = 1;
        gbd.gridy = 0;
        gbd.fill = GridBagConstraints.BOTH;
        gbd.weightx = 0.7;
        gbd.weighty = 0.6;
        panel.setBorder(BorderFactory.createEtchedBorder());
        this.panel.setBackground(Color.WHITE);
    }

    public GridBagConstraints getGbd() {
        return gbd;
    }

    public JPanel getPanel(){
        return panel;
    }

    public JFrame getFrame() {
        return frame;
    }
}

