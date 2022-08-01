import support.*;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;

public class ActiveEntitiesPanel extends JTabbedPane{
    private JFrame frame;

    public ActiveEntitiesPanel(JFrame frame){
        this.frame = frame;
        this.setBorder(BorderFactory.createEtchedBorder());

        setInternalLayout();
    }


    private void setInternalLayout(){
        //split panes for the two tabs
        JPanel obstaclesTab = new JPanel();
        JPanel wayPointsTab = new JPanel();
        JSplitPane researchAndPeople = new JSplitPane();
        this.addTab("Obstacles", obstaclesTab);
        this.addTab("Way Points", wayPointsTab);
        this.addTab("Pedestrians", researchAndPeople);

        //panel to split in researchAndPeople tab
        JPanel researchFilters = new JPanel();
        JPanel activePedestrians = new JPanel();
        researchAndPeople.setLeftComponent(researchFilters);
        researchAndPeople.setRightComponent(activePedestrians);

        //setting Tabs
        setObstaclesTab(obstaclesTab);
        setWayPointsTab(wayPointsTab);
        setPedestriansTab(researchFilters, activePedestrians);
    }

    private void setObstaclesTab(JPanel panel){
        //setta il listener per quando c'è da mostrare la lista

        if(Simulation.getInstance().getNumberOfObstacles() == 0){
            panel.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active obstacles");
            noObstacles.setForeground(Color.GRAY);
            panel.add(noObstacles);
        }
    }

    private void setWayPointsTab(JPanel panel){
        //setta il listener per quando c'è da mostrare la lista

        if(Simulation.getInstance().getNumberOfWayPoints() == 0){
            panel.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active way points");
            noObstacles.setForeground(Color.GRAY);
            panel.add(noObstacles);
        }
    }

    private void setPedestriansTab(JPanel leftPanel, JPanel rightPanel){
        //setting filters layout
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.insets = new Insets(5,5,5,5);

        JLabel gender = new JLabel("Gender:");
        String[] optionsGender = {"All", "Male", "Female"};
        JComboBox<String> genderBox = new JComboBox<>(optionsGender);
        gbd.gridx = 0;
        gbd.gridy = 0;
        leftPanel.add(gender, gbd);
        gbd.gridx = 1;
        gbd.gridy = 0;
        leftPanel.add(genderBox, gbd);

        JLabel age = new JLabel("Age:");
        String[] optionsAge = {"All", "Child", "Young", "Old"};
        JComboBox<String> ageBox = new JComboBox<>(optionsAge);
        gbd.gridx = 0;
        gbd.gridy = 1;
        leftPanel.add(age, gbd);
        gbd.gridx = 1;
        gbd.gridy = 1;
        leftPanel.add(ageBox, gbd);

        JLabel velocity = new JLabel("Velocity:");
        JSlider velocitySlider = new RangeSlider(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY);
        velocitySlider.setFocusable(false);
        velocitySlider.setMinorTickSpacing(1);
        velocitySlider.setMinorTickSpacing(1);
        velocitySlider.setValue(Constant.MIN_VELOCITY); //value è il valore del pallino di sinistra
        velocitySlider.setExtent(Constant.MAX_VELOCITY); //extent è il valore del pallino di destra
        velocitySlider.setMajorTickSpacing(2);
        velocitySlider.setPaintLabels(true);
        velocitySlider.setPaintTicks(true);

        gbd.gridx = 0;
        gbd.gridy = 2;
        leftPanel.add(velocity, gbd);
        gbd.gridx = 1;
        gbd.gridy = 2;
        leftPanel.add(velocitySlider, gbd);

        JLabel energy = new JLabel("Energy:");



        //setta il listener per quando c'è da mostrare la lista

        if(Simulation.getInstance().getNumberOfPeople() == 0){
            rightPanel.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active pedestrians");
            noObstacles.setForeground(Color.GRAY);
            rightPanel.add(noObstacles);
        }
    }


    public JFrame getFrame() {
        return frame;
    }
}
