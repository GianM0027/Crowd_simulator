import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame homePage = new JFrame("Crowd Simulation");
        homePage.setSize(720, 480);
        //homePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homePage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        homePage.setVisible(true);
        homePage.setLayout(new BorderLayout());

        //declaring panels
        JPanel settingsPanel = new JPanel();
        JPanel rightPartHomePage = new JPanel();
        JPanel simulationPanel = new JPanel();
        JPanel activeEntitiesPanel = new JPanel();

        //setting panels
        settingsPanel.setPreferredSize(new Dimension(homePage.getWidth() / 4, homePage.getHeight()));
        homePage.add(settingsPanel, "West");
        rightPartHomePage.setPreferredSize(new Dimension(homePage.getWidth() - homePage.getWidth() / 4, homePage.getHeight()));
        homePage.add(rightPartHomePage, "East");
        rightPartHomePage.setLayout(new BorderLayout());
        simulationPanel.setPreferredSize(new Dimension(rightPartHomePage.getWidth(), homePage.getHeight() / 2));
        rightPartHomePage.add(simulationPanel, "North");
        activeEntitiesPanel.setPreferredSize(new Dimension(rightPartHomePage.getWidth(), homePage.getHeight() / 2));
        rightPartHomePage.add(activeEntitiesPanel, "South");

        homePage.pack();

        //remove them when they are useless
        settingsPanel.setBackground(Color.RED);
        simulationPanel.setBackground(Color.GREEN);
        activeEntitiesPanel.setBackground(Color.BLUE);
        rightPartHomePage.setBackground(Color.GRAY);
    }
}
