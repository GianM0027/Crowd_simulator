import models.Obstacle;
import support.*;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A tabbed pane that shows all the currently active entities
 * */
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

    /**
     * There must be only one instance of the panel at a time, every access to public methods of the class ActiveEntitiesPanel
     * is done through this function
     * */
    public static ActiveEntitiesPanel getInstance(){
        if(instance == null)
            instance = new ActiveEntitiesPanel();
        return instance;
    }

    /**
     * Set three tabs, one for obstacles, one for way points, one for pedestrians with their content
     * */
    private void setInternalLayout(){
        //declaring and adding panes to the three tabs
        this.obstaclesTab = new JPanel();
        this.wayPointsTab = new JPanel();
        this.researchAndPeople = new JSplitPane();
        this.addTab("Obstacles", obstaclesTab);
        this.addTab("Way Points", wayPointsTab);
        this.addTab("Pedestrians", researchAndPeople);

        //panels to split in researchAndPeople tab
        this.researchFilters = new JPanel();
        this.activePedestrians = new JPanel();
        researchAndPeople.setLeftComponent(researchFilters);
        researchAndPeople.setRightComponent(activePedestrians);

        //setting internal layout of Tabs
        setObstaclesTab();
        setWayPointsTab();
        setPedestriansTab();
    }

    /**
     * Set the tab that shows the active obstacles
     * */
    public void setObstaclesTab(){
        //first of writing on this tab every label or old list must be canceled from the panel
        this.obstaclesTab.removeAll();
        this.obstaclesTab.repaint();

        //if the number of obstacles in the simulation is 0 (the simulation has not started yet) an apposite message is shown
        if(Simulation.getInstance().getNumberOfObstacles() == 0){
            obstaclesTab.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active obstacles");
            noObstacles.setForeground(Color.GRAY);
            obstaclesTab.add(noObstacles);
        }
        //else if the simulation is started, you retrieve the list of obstacles and show it
        else {
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

    /**
     * Set the tab that shows the active way points
     * */
    public void setWayPointsTab(){

        //if the number of way points in the simulation is 0 (the simulation has not started yet) an apposite message is shown
        if(Simulation.getInstance().getNumberOfWayPoints() == 0){
            wayPointsTab.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active way points");
            noObstacles.setForeground(Color.GRAY);
            wayPointsTab.add(noObstacles);
        }
    }

    /**
     * Set the tab that shows the active pedestrians and that allows to research them filtering the results
     * */
    private void setPedestriansTab(){
        //setting filters layout
        researchFilters.setLayout(new GridBagLayout());
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.insets = new Insets(5,5,5,5);

        //dropdown menu to select gender
        JLabel gender = new JLabel("Gender:");
        String[] optionsGender = {"All", "Male", "Female"};
        JComboBox<String> genderBox = new JComboBox<>(optionsGender);
        gbd.gridx = 0;
        gbd.gridy = 0;
        researchFilters.add(gender, gbd);
        gbd.gridx = 1;
        gbd.gridy = 0;
        researchFilters.add(genderBox, gbd);

        //dropdown menu to select age
        JLabel age = new JLabel("Age:");
        String[] optionsAge = {"All", "Child", "Young", "Old"};
        JComboBox<String> ageBox = new JComboBox<>(optionsAge);
        gbd.gridx = 0;
        gbd.gridy = 1;
        researchFilters.add(age, gbd);
        gbd.gridx = 1;
        gbd.gridy = 1;
        researchFilters.add(ageBox, gbd);

        //range slider to select a range of velocity to consider
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

        //vedi se aggiungere anche opzione di ricerca per energia

        //if the number of pedestrians in the simulation is 0 (the simulation has not started yet) an apposite message is shown
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
