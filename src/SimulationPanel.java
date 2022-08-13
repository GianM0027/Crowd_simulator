import javax.swing.*;
import java.awt.*;

/**
 * General setting of the simulation panel, division of spaces and basic layout
 * */
public class SimulationPanel extends JPanel{

    public SimulationPanel(){
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        //setting top bar
        setTopBar();

        //Setting simulation area
        this.add(Simulation.getInstance(), BorderLayout.CENTER);
    }

    /**
     * Set top bar of the simulation panel
     * */
    private void setTopBar(){
        JToolBar topBar = new JToolBar();
        topBar.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Simulation Panel");
        topBar.setFloatable(false);
        topBar.add(label);

        this.add(topBar,BorderLayout.NORTH);
    }

}

