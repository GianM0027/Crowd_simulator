import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame homePage = new JFrame("Crowd Simulation");
        homePage.setSize(720, 480);
        //homePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homePage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        homePage.setVisible(true);
        homePage.setLayout(new GridBagLayout());

        //declaring panels
        JPanel settingsPanel = new JPanel();
        JPanel simulationPanel = new JPanel();
        JPanel activeEntitiesPanel = new JPanel();

        //setting panels
        GridBagConstraints gbdSettings = new GridBagConstraints();
        gbdSettings.gridx = 0;
        gbdSettings.gridy = 0;
        gbdSettings.gridheight = 2;
        gbdSettings.fill = GridBagConstraints.BOTH;
        gbdSettings.weightx = 0.3;
        gbdSettings.weighty = 1;
        homePage.add(settingsPanel,gbdSettings);

        GridBagConstraints gbdSimulation = new GridBagConstraints();
        gbdSimulation.gridx = 1;
        gbdSimulation.gridy = 0;
        gbdSimulation.fill = GridBagConstraints.BOTH;
        gbdSimulation.weightx = 0.7;
        gbdSimulation.weighty = 0.5;
        homePage.add(simulationPanel,gbdSimulation);

        GridBagConstraints gbdActiveEntities = new GridBagConstraints();
        gbdActiveEntities.gridx = 1;
        gbdActiveEntities.gridy = 1;
        gbdActiveEntities.fill = GridBagConstraints.BOTH;
        gbdActiveEntities.weightx = 0.7;
        gbdActiveEntities.weighty = 0.5;
        homePage.add(activeEntitiesPanel,gbdActiveEntities);

        //remove them when they are useless
        settingsPanel.setBackground(Color.RED);
        simulationPanel.setBackground(Color.GREEN);
        activeEntitiesPanel.setBackground(Color.BLUE);
    }
}
