import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame homePage = new JFrame("Crowd Simulation");
        homePage.setSize(720, 480);
        //homePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homePage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        homePage.setLayout(new GridBagLayout());

        //declaring panels
        SettingPanel settingsPanel = new SettingPanel(homePage);
        SimulationPanel simulationPanel = new SimulationPanel(homePage);
        ActiveEntitiesPanel activeEntitiesPanel = new ActiveEntitiesPanel(homePage);

        //setting panels
        homePage.add(settingsPanel,settingsPanel.getGbd());
        homePage.add(simulationPanel,simulationPanel.getGbd());
        homePage.add(activeEntitiesPanel,activeEntitiesPanel.getGbd());

        homePage.setVisible(true);

        //PROVA
        //prova();
    }


    static public void prova(){
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel red = new JPanel();
        JPanel blue = new JPanel();
        red.setBackground(Color.RED);
        blue.setBackground(Color.BLUE);
        red.setPreferredSize(new Dimension(100,100));
        blue.setPreferredSize(new Dimension(100,100));

        frame.add(red, BorderLayout.NORTH);
        frame.add(blue, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
