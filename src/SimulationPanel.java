import javax.swing.*;
import java.awt.*;

/**
 * This class extends JPanel and it is used to initialize and set the layout of the Simulation Panel (upper-right section of the interface).
 * This class does not contain the parameters of the Simulation, it only allows to manage the external layout of this section.
 * The logic behind the simulation and the actual animation is managed by the class "Simulation.java"
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
     * it sets top bar of the simulation panel
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

