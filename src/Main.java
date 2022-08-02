import models.Pedestrian;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame homePage = new JFrame("Crowd Simulator");
        homePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homePage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        homePage.setLayout(new GridBagLayout());

        //declaring panels
        SettingPanel settingsPanel = new SettingPanel(homePage);
        SimulationPanel simulationPanel = new SimulationPanel(homePage);
        ActiveEntitiesPanel activeEntitiesPanel = ActiveEntitiesPanel.getInstance();

        //gbd for settings panel
        GridBagConstraints gbdSettings = new GridBagConstraints();
        gbdSettings.gridx = 0;
        gbdSettings.gridy = 0;
        gbdSettings.gridheight = 2;
        gbdSettings.gridwidth = 1;
        gbdSettings.weighty = 1;
        gbdSettings.weightx = 0;
        gbdSettings.anchor = GridBagConstraints.LINE_START;
        gbdSettings.fill = GridBagConstraints.VERTICAL;

        //gbd for simulation panel
        GridBagConstraints gbdSimulation = new GridBagConstraints();
        gbdSimulation.gridx = 1;
        gbdSimulation.gridy = 0;
        gbdSimulation.gridheight = 1;
        gbdSimulation.gridwidth = 1;
        gbdSimulation.weighty = 1;
        gbdSimulation.weightx = 1;
        gbdSimulation.anchor = GridBagConstraints.CENTER;
        gbdSimulation.fill = GridBagConstraints.BOTH;

        //gbd for active entities panel
        GridBagConstraints gbdActiveEntities = new GridBagConstraints();
        gbdActiveEntities.gridx = 1;
        gbdActiveEntities.gridy = 1;
        gbdActiveEntities.gridheight = 1;
        gbdActiveEntities.gridwidth = 1;
        gbdActiveEntities.weighty = 0.4;
        gbdActiveEntities.weightx = 1;
        gbdActiveEntities.anchor = GridBagConstraints.CENTER;
        gbdActiveEntities.fill = GridBagConstraints.BOTH;

        homePage.add(settingsPanel, gbdSettings);
        homePage.add(simulationPanel, gbdSimulation);
        homePage.add(activeEntitiesPanel, gbdActiveEntities);

        homePage.setVisible(true);
    }
}
