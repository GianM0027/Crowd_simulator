import models.Crowd;
import models.Obstacle;
import models.WayPoint;
import support.*;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActiveEntitiesPanel extends JTabbedPane{
    private static ActiveEntitiesPanel instance;
    private JPanel obstaclesTab;
    private JPanel wayPointsTab;
    private JSplitPane researchAndPeople;
    private JPanel researchFilters;
    private JPanel activePedestrians;


    public ActiveEntitiesPanel(){
        this.setBorder(BorderFactory.createEtchedBorder());

        setInternalLayout();
    }

    //only one instance of the panel at a time
    public static ActiveEntitiesPanel getInstance(){
        if(instance == null)
            instance = new ActiveEntitiesPanel();
        return instance;
    }

    private void setInternalLayout(){
        //split panes for the two tabs
        this.obstaclesTab = new JPanel();
        this.wayPointsTab = new JPanel();
        this.researchAndPeople = new JSplitPane();
        this.addTab("Obstacles", obstaclesTab);
        this.addTab("Way Points", wayPointsTab);
        this.addTab("Pedestrians", researchAndPeople);

        //panel to split in researchAndPeople tab
        this.researchFilters = new JPanel();
        this.activePedestrians = new JPanel();
        researchAndPeople.setLeftComponent(researchFilters);
        researchAndPeople.setRightComponent(activePedestrians);

        //setting Tabs
        setObstaclesTab();
        setWayPointsTab();
        setPedestriansTab();
    }


    public void setObstaclesTab(){
        if(Simulation.getInstance().getNumberOfObstacles() == 0){
            obstaclesTab.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active obstacles");
            noObstacles.setForeground(Color.GRAY);
            obstaclesTab.add(noObstacles);
        }else {
            List<Obstacle> obstaclesList = Simulation.getInstance().getObstacles();
            DefaultListModel listModel = new DefaultListModel();

            for(int i = 1; i <= obstaclesList.size(); i++) {
                listModel.add(i-1, "Obstacle " + i + ": " + "[" + (int)obstaclesList.get(i-1).getPosition().getX() + ", " + (int)obstaclesList.get(i-1).getPosition().getY() + "]");
            }

            this.obstaclesTab.removeAll();
            this.repaint();
            JList obstacles = new JList(listModel);
            this.obstaclesTab.add(obstacles);
        }
    }

    public void setWayPointsTab(){
        //setta il listener per quando c'è da mostrare la lista

        if(Simulation.getInstance().getNumberOfWayPoints() == 0){
            wayPointsTab.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active way points");
            noObstacles.setForeground(Color.GRAY);
            wayPointsTab.add(noObstacles);
        }
    }

    private void setPedestriansTab(){
        //setting filters layout
        researchFilters.setLayout(new GridBagLayout());
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.insets = new Insets(5,5,5,5);

        JLabel gender = new JLabel("Gender:");
        String[] optionsGender = {"All", "Male", "Female"};
        JComboBox<String> genderBox = new JComboBox<>(optionsGender);
        gbd.gridx = 0;
        gbd.gridy = 0;
        researchFilters.add(gender, gbd);
        gbd.gridx = 1;
        gbd.gridy = 0;
        researchFilters.add(genderBox, gbd);

        JLabel age = new JLabel("Age:");
        String[] optionsAge = {"All", "Child", "Young", "Old"};
        JComboBox<String> ageBox = new JComboBox<>(optionsAge);
        gbd.gridx = 0;
        gbd.gridy = 1;
        researchFilters.add(age, gbd);
        gbd.gridx = 1;
        gbd.gridy = 1;
        researchFilters.add(ageBox, gbd);

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
        researchFilters.add(velocity, gbd);
        gbd.gridx = 1;
        gbd.gridy = 2;
        researchFilters.add(velocitySlider, gbd);


        if(Simulation.getInstance().getNumberOfPeople() == 0){
            activePedestrians.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active pedestrians");
            noObstacles.setForeground(Color.GRAY);
            activePedestrians.add(noObstacles);
        }
    }

    public JPanel getObstaclesTab() {
        return obstaclesTab;
    }

    public JPanel getWayPointsTab() {
        return wayPointsTab;
    }

    public JPanel getResearchFilters() {
        return researchFilters;
    }

    public JPanel getActivePedestrians() {
        return activePedestrians;
    }
}
