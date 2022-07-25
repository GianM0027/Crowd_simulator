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
        SettingPanel settingsPanel = new SettingPanel(homePage);
        SimulationPanel simulationPanel = new SimulationPanel(homePage);
        ActiveEntitiesPanel activeEntitiesPanel = new ActiveEntitiesPanel(homePage);

        //setting panels
        homePage.add(settingsPanel,settingsPanel.getGbd());
        homePage.add(simulationPanel,simulationPanel.getGbd());
        homePage.add(activeEntitiesPanel,activeEntitiesPanel.getGbd());
    }
}
