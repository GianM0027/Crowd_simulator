import models.Group;

import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel{
    private JFrame frame;

    public SimulationPanel(JFrame frame){
        this.frame = frame;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        //setting top bar
        setTopBar();

        //Setting simulation area
        this.add(Simulation.getInstance(), BorderLayout.CENTER);
    }

    private void setTopBar(){
        JToolBar topBar = new JToolBar();
        topBar.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Simulation Panel");
        topBar.setFloatable(false);
        topBar.add(label);

        this.add(topBar,BorderLayout.NORTH);
    }

    public JFrame getFrame() {
        return frame;
    }
}

