import models.Obstacle;
import models.Pedestrian;
import models.WayPoint;
import support.*;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A tabbed pane that shows all the currently active entities
 * */
public class ActiveEntitiesPanel extends JTabbedPane{
    private static ActiveEntitiesPanel instance;
    private JPanel obstaclesTab;
    private JPanel wayPointsTab;
    private JPanel researchAndPeople;
    private JPanel researchFilters;
    private JPanel activePedestrians;


    private JComboBox orderBy;
    private JComboBox ageFilter;
    private JComboBox genderFilter;
    private JSlider velocityFilter;
    private JSlider energyFilter;


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


    /***************************************    INTERNAL LAYOUT SETTINGS    *****************************************/
    /**
     * Set three tabs, one for obstacles, one for way points, one for pedestrians with their content
     * */
    private void setInternalLayout(){
        //declaring and adding panes to the three tabs
        this.obstaclesTab = new JPanel();
        this.wayPointsTab = new JPanel();
        this.researchAndPeople = new JPanel();
        this.addTab("Obstacles", obstaclesTab);
        this.addTab("Way Points", wayPointsTab);
        this.addTab("Pedestrians", researchAndPeople);

        //panels to split in researchAndPeople tab
        researchAndPeople.setLayout(new GridBagLayout());
        GridBagConstraints gbdResearch = new GridBagConstraints(0,0,1,1,0.01,1,
                GridBagConstraints.FIRST_LINE_START,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        GridBagConstraints gbdPedestrian = new GridBagConstraints(1,0,1,1,1,1,
                GridBagConstraints.FIRST_LINE_START,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        this.researchFilters = new JPanel();
        this.activePedestrians = new JPanel();
        activePedestrians.setBorder(BorderFactory.createEtchedBorder());
        researchFilters.setBorder(BorderFactory.createEtchedBorder());
        researchAndPeople.add(researchFilters, gbdResearch);
        researchAndPeople.add(activePedestrians, gbdPedestrian);

        //setting internal layout of Tabs
        setObstaclesTab();
        setWayPointsTab();
        setFiltersTab();
        setPedestriansTab(Simulation.getInstance().getCrowd());
    }

    /**
     * Set the tab that shows the active obstacles
     * */
    public void setObstaclesTab(){
        //first of writing on this tab every label or old list must be canceled from the panel
        this.obstaclesTab.removeAll();
        obstaclesTab.setLayout(new GridBagLayout());

        //if the number of obstacles in the simulation is 0 (the simulation has not started yet) an apposite message is shown
        if(Simulation.getInstance().getNumberOfObstacles() == 0){
            JLabel noObstacles = new JLabel("Start the simulation to view the active obstacles");
            noObstacles.setForeground(Color.GRAY);
            obstaclesTab.add(noObstacles);
        }
        //else if the simulation is started, you retrieve the list of obstacles and show it
        else {
            List<Obstacle> obstaclesList = Simulation.getInstance().getObstacles();
            DefaultListModel listModel = new DefaultListModel();

            for(int i = 1; i <= obstaclesList.size(); i++) {
                listModel.add(i-1, "Obstacle " + i + ": " + obstaclesList.get(i-1).getPositionString());
            }

            this.obstaclesTab.removeAll();
            this.repaint();
            JList obstacles = new JList(listModel);

            JScrollPane scrollPane = new JScrollPane(obstacles);
            scrollPane.setPreferredSize(new Dimension(this.obstaclesTab.getWidth()-10,this.obstaclesTab.getHeight()-10));
            this.obstaclesTab.add(scrollPane);
        }
        this.obstaclesTab.repaint();
    }

    /**
     * Set the tab that shows the active way points
     * */
    public void setWayPointsTab(){
        //first of writing on this tab every label or old list must be canceled from the panel
        this.wayPointsTab.removeAll();
        wayPointsTab.setLayout(new GridBagLayout());

        //if the number of way points in the simulation is 0 (the simulation has not started yet) an apposite message is shown
        if(Simulation.getInstance().getNumberOfWayPoints() == 0){
            wayPointsTab.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active way points");
            noObstacles.setForeground(Color.GRAY);
            wayPointsTab.add(noObstacles);
        }
        //else if the simulation is started, you retrieve the list of way points and show it
        else {
            List<WayPoint> wayPointsList = Simulation.getInstance().getWayPoints();
            DefaultListModel listModel = new DefaultListModel();

            for(int i = 1; i <= wayPointsList.size(); i++) {
                listModel.add(i-1, "Way Point " + i + ": " + wayPointsList.get(i-1).getPositionString());
            }

            this.wayPointsTab.removeAll();
            this.repaint();
            JList wayPoints = new JList(listModel);

            JScrollPane scrollPane = new JScrollPane(wayPoints);
            scrollPane.setPreferredSize(new Dimension(this.wayPointsTab.getWidth()-10,this.wayPointsTab.getHeight()-10));
            this.wayPointsTab.add(scrollPane);
        }
        this.wayPointsTab.repaint();
    }

    /**
     * Set the tab that allows to research Pedestrians filtering the results
     * */
    public void setFiltersTab(){
        //first of writing on this tab every label or old list must be canceled from the panel
        this.researchFilters.removeAll();

        //setting "order by" menu
        researchFilters.setLayout(new GridBagLayout());
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.insets = new Insets(5,10,5,10);
        gbd.anchor = GridBagConstraints.LINE_START;

        JLabel orderByLabel = new JLabel("Order by:");
        String[] optionsOrderBy = {"Default", "Gender", "Age", "Velocity", "Energy"};
        this.orderBy = new JComboBox<>(optionsOrderBy);
        orderBy.addActionListener(e -> updateFilteredCrowd());
        gbd.gridx = 0;
        gbd.gridy = 0;
        researchFilters.add(orderByLabel, gbd);
        gbd.gridx = 1;
        gbd.gridy = 0;
        gbd.fill = GridBagConstraints.HORIZONTAL;
        researchFilters.add(orderBy, gbd);


        //dropdown menu to select gender
        JLabel gender = new JLabel("Gender:");
        String[] optionsGender = {"All", "Male", "Female"};
        this.genderFilter = new JComboBox<>(optionsGender);
        genderFilter.addActionListener(e -> updateFilteredCrowd());
        gbd.gridx = 0;
        gbd.gridy = 1;
        researchFilters.add(gender, gbd);
        gbd.gridx = 1;
        gbd.gridy = 1;
        researchFilters.add(genderFilter, gbd);

        //dropdown menu to select age
        JLabel age = new JLabel("Age:");
        String[] optionsAge = {"All", "Child", "Young", "Old"};
        this.ageFilter = new JComboBox<>(optionsAge);
        ageFilter.addActionListener(e -> updateFilteredCrowd());
        gbd.gridx = 0;
        gbd.gridy = 2;
        researchFilters.add(age, gbd);
        gbd.gridx = 1;
        gbd.gridy = 2;
        researchFilters.add(ageFilter, gbd);

        //range slider to select a range of velocity to consider
        JLabel velocity = new JLabel("Velocity:");
        this.velocityFilter = new RangeSlider(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY);
        velocityFilter.setFocusable(false);
        velocityFilter.setMinorTickSpacing(1);
        velocityFilter.setMinorTickSpacing(1);
        velocityFilter.setValue(Constant.MIN_VELOCITY); //value è il valore del pallino di sinistra
        velocityFilter.setExtent(Constant.MAX_VELOCITY); //extent + value è il valore del pallino di destra
        velocityFilter.setMajorTickSpacing(2);
        velocityFilter.setPaintLabels(true);
        velocityFilter.setPaintTicks(true);
        velocityFilter.addChangeListener(e -> updateFilteredCrowd());
        gbd.gridx = 0;
        gbd.gridy = 3;
        researchFilters.add(velocity, gbd);
        gbd.gridx = 1;
        gbd.gridy = 3;
        gbd.ipady = 5;
        researchFilters.add(velocityFilter, gbd);

        //range slider to select a range of velocity to consider
        JLabel energy = new JLabel("Energy:");
        this.energyFilter = new RangeSlider(Constant.MIN_ENERGY_OLD, Constant.MAX_ENERGY_CHILD);
        energyFilter.setFocusable(false);
        energyFilter.setMinorTickSpacing(1);
        energyFilter.setMinorTickSpacing(1);
        energyFilter.setValue(Constant.MIN_ENERGY_OLD); //value è il valore del pallino di sinistra
        energyFilter.setExtent(Constant.MAX_ENERGY_CHILD); //extent + value è il valore del pallino di destra
        energyFilter.setMajorTickSpacing(5);
        energyFilter.setPaintLabels(true);
        energyFilter.setPaintTicks(true);
        energyFilter.addChangeListener(e -> updateFilteredCrowd());
        gbd.gridx = 0;
        gbd.gridy = 4;
        researchFilters.add(energy, gbd);
        gbd.gridx = 1;
        gbd.gridy = 4;
        gbd.ipady = 5;
        researchFilters.add(energyFilter, gbd);
    }

    /**
     * Set the tab that shows the active pedestrians and that allows to research them filtering the results
     * */
    public void setPedestriansTab(List<Pedestrian> crowd){
        //first of writing on this tab every label or old list must be canceled from the panel
        this.activePedestrians.removeAll();
        activePedestrians.setLayout(new GridBagLayout());

        //if the number of pedestrians in the simulation is 0 or there are not pedestrians that fulfill the filters, an apposite message is shown
        if(Simulation.getInstance().getNumberOfPeople() == 0 || crowd == null || crowd.isEmpty()){
            activePedestrians.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("There are no pedestrians to show");
            noObstacles.setForeground(Color.GRAY);
            activePedestrians.add(noObstacles);
        }
        //else if the simulation is started, you retrieve the list of pedestrians and show it
        else {
            DefaultListModel listModel = new DefaultListModel();

            for(int i = 1; i <= crowd.size(); i++) {
                listModel.add(i-1, "Pedestrian " + i + ": " + crowd.get(i-1).getPositionString() +
                        "   -   " + crowd.get(i-1).getGenderString() + "   -   " + crowd.get(i-1).getAgeString() +
                        "   -   " + "Velocity: " + crowd.get(i-1).getVelocity() + "   -   " + "Energy: " + crowd.get(i-1).getEnergy());
            }

            this.activePedestrians.removeAll();
            this.repaint();
            JList pedestiansList = new JList(listModel);

            JScrollPane scrollPane = new JScrollPane(pedestiansList);
            scrollPane.setPreferredSize(new Dimension(this.activePedestrians.getWidth()-12,this.activePedestrians.getHeight()-12));
            this.activePedestrians.add(scrollPane);
        }
        this.activePedestrians.repaint();
    }

    public void updateFilteredCrowd(){
        ArrayList<Pedestrian> filteredCrowd = new ArrayList<>(Simulation.getInstance().getCrowd());

        String genderFilter = this.genderFilter.getSelectedItem().toString();
        String ageFilter = this.ageFilter.getSelectedItem().toString();
        int minVelocityFilter = this.velocityFilter.getValue();
        int maxVelocityFilter = minVelocityFilter + this.velocityFilter.getExtent();
        int minEnergyFilter = this.energyFilter.getValue();
        int maxEnergyFilter = minEnergyFilter + this.energyFilter.getExtent();

        //if there has been a change in the default filter settings remove those element that do not match the filters
        if(!genderFilter.equals("All"))
            filteredCrowd.removeIf(p -> !p.getGenderString().equals(genderFilter));
        if(!ageFilter.equals("All"))
            filteredCrowd.removeIf(p -> !p.getAgeString().equals(ageFilter));
        if(minVelocityFilter != Constant.MIN_VELOCITY || maxVelocityFilter  != Constant.MAX_VELOCITY)
            filteredCrowd.removeIf(p -> p.getVelocity() < minVelocityFilter || p.getVelocity() > maxVelocityFilter);
        if(minEnergyFilter != Constant.MIN_ENERGY_OLD || maxEnergyFilter  != Constant.MAX_ENERGY_CHILD)
            filteredCrowd.removeIf(p -> p.getEnergy() < minEnergyFilter || p.getEnergy() > maxEnergyFilter);

        //check if it is required a specific sorting
        switch (orderBy.getSelectedItem().toString()){
            case "Gender":
                Support.sortPedestriansByGender(filteredCrowd);
            case "Age":
                Support.sortPedestriansByAge(filteredCrowd);
            case "Velocity":
                Support.sortPedestriansByVelocity(filteredCrowd);
            case "Energy":
                Support.sortPedestriansByEnergy(filteredCrowd);
            default:

        }

        setPedestriansTab(filteredCrowd);
    }


    /***************************************    ACCESSORS    *****************************************/

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
