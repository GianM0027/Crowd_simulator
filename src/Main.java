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
        SimulationPanel simulationPanel = new SimulationPanel(homePage);
        ActiveEntitiesPanel activeEntitiesPanel = new ActiveEntitiesPanel(homePage);

        //Defining JSplitPane to create resizable windows
        JSplitPane leftRightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane upDownSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        leftRightSplit.setLeftComponent(settingsPanel);
        leftRightSplit.setRightComponent(upDownSplit);
        upDownSplit.setTopComponent(simulationPanel);
        upDownSplit.setBottomComponent(activeEntitiesPanel);

        homePage.add(leftRightSplit);

        homePage.setVisible(true);

        //PROVA
        //prova();
    }


    static public void prova(){
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel label = new JLabel("CACCA");

        GridBagConstraints gbd = new GridBagConstraints();
        gbd.anchor = GridBagConstraints.LINE_START;
        frame.add(label,gbd);

        frame.setVisible(true);
    }
}
