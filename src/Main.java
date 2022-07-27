import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame homePage = new JFrame("Crowd Simulation");
        homePage.setSize(1080, 720);
        //homePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homePage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        homePage.setLayout(new BorderLayout());

        //declaring panels
        /*
        * 2 Main areas:
        *       Left corner of the frame for settings (settingsPanel)
        *       Center of frame for simulation + alive entities panel (mainPanel)
        * */
        SettingPanel settingsPanel = new SettingPanel(homePage);
        JPanel mainPanel = new JPanel(new BorderLayout());
        SimulationPanel simulationPanel = new SimulationPanel(homePage);
        ActiveEntitiesPanel activeEntitiesPanel = new ActiveEntitiesPanel(homePage);


        //putting panels into the main frame
        mainPanel.add(simulationPanel, BorderLayout.CENTER);
        mainPanel.add(activeEntitiesPanel, BorderLayout.SOUTH);
        homePage.add(mainPanel, BorderLayout.CENTER);

        homePage.add(settingsPanel, BorderLayout.WEST);

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
