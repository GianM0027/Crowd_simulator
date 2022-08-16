import javax.swing.*;
import java.awt.*;

/**
 * General setting of the simulation panel, division of spaces and basic layout
 * */
public class SimulationPanel extends JPanel{

    public SimulationPanel(){
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setLayout(new GridBagLayout());

        //setting top bar
        setTopBar();

        //Setting simulation area
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.gridy = 1;
        gbd.weightx = 1;
        gbd.weighty = 1;
        gbd.fill = GridBagConstraints.BOTH;
        this.add(Simulation.getInstance(), gbd);
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

        GridBagConstraints gbd = new GridBagConstraints();
        gbd.fill = GridBagConstraints.HORIZONTAL;
        gbd.gridy = 0;
        gbd.weightx = 1;
        gbd.weighty = 0;
        this.add(topBar, gbd);
    }

}

