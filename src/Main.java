/**
 * Main class of the code. It initializes the layout and position of the main frame and the three panel that
 * constitute the GUI.
 * The interface is composed as follows:
 * - One JFrame that contains every other element
 * - Inside the JFrame there are two panels, "settingPanels" on the left part of the screen and "rightPanel" on the right part.
 * - "settingsPanel" contains buttons and forms that allow the user to set the parameters of the simulation.
 * - "rightPanel" contains two other panels: "simulationPanel" on the top part of the screen; "activeEntitiesPanel" on the bottom part of the screen.
 */

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame homePage = new JFrame("Crowd Simulator");
        homePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homePage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        homePage.setLayout(new GridBagLayout());

        //declaring panels
        SettingPanel settingsPanel = new SettingPanel();
        JPanel rightPanel = new JPanel(new BorderLayout());
        SimulationPanel simulationPanel = new SimulationPanel();
        ActiveEntitiesPanel activeEntitiesPanel = ActiveEntitiesPanel.getInstance();

        //gbd for settings panel
        GridBagConstraints gbdSettings = new GridBagConstraints();
        gbdSettings.gridx = 0;
        gbdSettings.gridy = 0;
        gbdSettings.gridheight = 1;
        gbdSettings.gridwidth = 1;
        gbdSettings.weighty = 1;
        gbdSettings.weightx = 0;
        gbdSettings.anchor = GridBagConstraints.LINE_START;
        gbdSettings.fill = GridBagConstraints.VERTICAL;

        //gbd for right panel
        GridBagConstraints gbdRight = new GridBagConstraints();
        gbdRight.gridx = 1;
        gbdRight.gridy = 0;
        gbdRight.gridheight = 1;
        gbdRight.gridwidth = 1;
        gbdRight.weighty = 1;
        gbdRight.weightx = 1;
        gbdRight.anchor = GridBagConstraints.CENTER;
        gbdRight.fill = GridBagConstraints.BOTH;

        //adding the 3 sections on the homepage
        homePage.add(settingsPanel, gbdSettings);
        homePage.add(rightPanel, gbdRight);
        rightPanel.add(simulationPanel, BorderLayout.CENTER);
        rightPanel.add(activeEntitiesPanel, BorderLayout.SOUTH);

        homePage.setVisible(true);
    }
}