import models.Group;
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
        this.obstaclesTab = new JPanel(new GridBagLayout());
        this.wayPointsTab = new JPanel(new GridBagLayout());
        JPanel researchAndPeople = new JPanel(new GridBagLayout());

        this.addTab("Pedestrians", researchAndPeople);
        this.addTab("Obstacles", obstaclesTab);
        this.addTab("Way Points", wayPointsTab);

        //panels to split in researchAndPeople tab
        GridBagConstraints gbdResearch = new GridBagConstraints(0,0,1,1,0,1,
                GridBagConstraints.FIRST_LINE_START,GridBagConstraints.BOTH,new Insets(0,0,0,0),10,10);
        GridBagConstraints gbdPedestrian = new GridBagConstraints(1,0,1,1,1,1,
                GridBagConstraints.FIRST_LINE_START,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        this.researchFilters = new JPanel(new GridBagLayout());
        this.activePedestrians = new JPanel(new GridBagLayout());
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
    public void setObstaclesTab() {
        //first of writing on this tab every label or old list must be canceled from the panel
        //first of writing on this tab every old item must be canceled from the panel
        this.obstaclesTab.removeAll();

        //if the number of obstacles in the simulation is 0 (the simulation has not started yet) an apposite message is shown
        if (Simulation.getInstance().getNumberOfObstacles() == 0) {
            JLabel noObstacles = new JLabel("Start the simulation to view the active obstacles");
            noObstacles.setForeground(Color.GRAY);
            obstaclesTab.add(noObstacles);
        }
        //else if the simulation is started, you retrieve the list of obstacles and show it
        else {
                List<Obstacle> obstaclesList = Simulation.getInstance().getObstacles();
                List<Obstacle> obstacleList = Simulation.getInstance().getObstacles();
                DefaultListModel listModel = new DefaultListModel();

                for (int i = 1; i <= obstacleList.size(); i++) {
                    listModel.add(i - 1, "Obstacle " + i + ": " + obstacleList.get(i - 1).getPositionString());
                }

                JList obstacles = new JList(listModel);

                JScrollPane scrollPane = new JScrollPane(obstacles);
                scrollPane.setPreferredSize(new Dimension(this.wayPointsTab.getWidth() - 8, this.wayPointsTab.getHeight() - 1));
                scrollPane.setPreferredSize(new Dimension(this.obstaclesTab.getWidth() - 8, this.obstaclesTab.getHeight() - 1));
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

            //if the number of way points in the simulation is 0 (the simulation has not started yet) an apposite message is shown
            if(Simulation.getInstance().getNumberOfWayPoints() == 0){
                JLabel noWayPoints = new JLabel("Start the simulation to view the active way points");
                noWayPoints.setForeground(Color.GRAY);
                wayPointsTab.add(noWayPoints);
            }
            //else if the simulation is started, you retrieve the list of way points and show it
            else {
                List<WayPoint> wayPointsList = Simulation.getInstance().getWayPoints();
                DefaultListModel listModel = new DefaultListModel();

                for(int i = 1; i <= wayPointsList.size(); i++) {
                    listModel.add(i-1, "Way Point " + i + ": " + wayPointsList.get(i-1).getPositionString());
                }

                JList wayPoints = new JList(listModel);

                JScrollPane scrollPane = new JScrollPane(wayPoints);
                scrollPane.setPreferredSize(new Dimension(this.wayPointsTab.getWidth()-8,this.wayPointsTab.getHeight()-1));
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
            GridBagConstraints gbdOrderByLabel = new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,0);
            JLabel orderByLabel = new JLabel("Order by:");
            String[] optionsOrderBy = {"Group", "Gender", "Age", "Velocity", "Energy"};
            this.orderBy = new JComboBox<>(optionsOrderBy);
            orderBy.addActionListener(e -> updateFilteredCrowd());
            researchFilters.add(orderByLabel, gbdOrderByLabel);
            GridBagConstraints gbdOrderByField = new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,0);
            researchFilters.add(orderBy, gbdOrderByField);

            //dropdown menu to select gender
            JLabel gender = new JLabel("Filter by Gender:");
            String[] optionsGender = {"All", "Male", "Female"};
            this.genderFilter = new JComboBox<>(optionsGender);
            genderFilter.addActionListener(e -> updateFilteredCrowd());
            GridBagConstraints gbdGenderLabel = new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,0);
            researchFilters.add(gender, gbdGenderLabel);
            GridBagConstraints gbdGenderField = new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,0);
            researchFilters.add(genderFilter, gbdGenderField);

            //dropdown menu to select age
            JLabel age = new JLabel("Filter by Age:");
            String[] optionsAge = {"All", "Child", "Young", "Old"};
            this.ageFilter = new JComboBox<>(optionsAge);
            ageFilter.addActionListener(e -> updateFilteredCrowd());
            GridBagConstraints gbdAgeLabel = new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,0);
            researchFilters.add(age, gbdAgeLabel);
            GridBagConstraints gbdAgeField = new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,0);
            researchFilters.add(ageFilter, gbdAgeField);

            //range slider to select a range of velocity to consider
            JLabel velocity = new JLabel("Filter by Velocity:");
            this.velocityFilter = new RangeSlider(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY);
            velocityFilter.setFocusable(false);
            velocityFilter.setMinorTickSpacing(1);
            velocityFilter.setMajorTickSpacing(1);
            velocityFilter.setValue(Constant.MIN_VELOCITY);
            velocityFilter.setExtent(Constant.MAX_VELOCITY);
            velocityFilter.setPaintLabels(true);
            velocityFilter.setPaintTicks(true);
            velocityFilter.addChangeListener(e -> updateFilteredCrowd());
            GridBagConstraints gbdVelocityLabel = new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,5);
            researchFilters.add(velocity, gbdVelocityLabel);
            GridBagConstraints gbdVelocityField = new GridBagConstraints(1,3,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,5);
            researchFilters.add(velocityFilter, gbdVelocityField);

            //range slider to select a range of velocity to consider
            JLabel energy = new JLabel("Filter by Energy:");
            this.energyFilter = new RangeSlider(Constant.MIN_ENERGY_OLD, Constant.MAX_ENERGY_CHILD);
            energyFilter.setFocusable(false);
            energyFilter.setMinorTickSpacing(1);
            energyFilter.setMajorTickSpacing(5);
            energyFilter.setValue(Constant.MIN_ENERGY_OLD);
            energyFilter.setExtent(Constant.MAX_ENERGY_CHILD);
            energyFilter.setPaintLabels(true);
            energyFilter.setPaintTicks(true);
            energyFilter.addChangeListener(e -> updateFilteredCrowd());
            GridBagConstraints gbdEnergyLabel = new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,5);
            researchFilters.add(energy, gbdEnergyLabel);
            GridBagConstraints gbdEnergyField = new GridBagConstraints(1,4,1,1,0,0,GridBagConstraints.LINE_START,
                    GridBagConstraints.HORIZONTAL,new Insets(5,10,5,10),0,5);
            researchFilters.add(energyFilter, gbdEnergyField);

            if(!Simulation.getInstance().isRunning())
                disableFilters();
            else enableFilters();
        }

        /**
         * Set the tab that shows the active pedestrians and that allows to research them filtering the results
         * */
        public void setPedestriansTab(List<Pedestrian> crowd){
            //first of writing on this tab every label or old list must be canceled from the panel
            this.activePedestrians.removeAll();

            //if the number of pedestrians in the simulation is 0 or there are not pedestrians that fulfill the filters, an apposite message is shown
            if(Simulation.getInstance().getNumberOfPeople() == 0 || crowd == null || crowd.isEmpty()){
                JLabel noObstacles = new JLabel("There are no pedestrians to show");
                noObstacles.setForeground(Color.GRAY);
                activePedestrians.add(noObstacles);
            }
            //else if the simulation is started, you retrieve the list of pedestrians and show it
            else {

                //Grouped by group
                if(this.orderBy.getSelectedItem().toString().equals("Group")){
                    JScrollPane scrollGroupsPane = new JScrollPane();
                    JPanel allGroupsPanel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbd = new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.FIRST_LINE_START,
                            GridBagConstraints.HORIZONTAL,new Insets(0,0,10,0),0,0);

                    //retrieve groups from the filtered crowd and show them
                    List<Group> groups = new ArrayList<>(Simulation.getInstance().getGroups());
                    for(Group group : groups){
                        ArrayList<Pedestrian> pedestrians = new ArrayList<>(group.getPedestrians());
                        pedestrians.retainAll(crowd);

                        JPanel groupPanel = new JPanel(new BorderLayout());
                        JPanel labelPanel = new JPanel(new BorderLayout());
                        JButton colorGroup = new JButton();
                        colorGroup.setPreferredSize(new Dimension(5,5));
                        JLabel groupLabel = new JLabel("Group number " + group.getGroupID() + "   -   " + "Goal points: " + group.getGoalPointstoString());
                        groupLabel.setBackground(Color.GREEN);

                        DefaultListModel listModel = new DefaultListModel();
                        for (int j = 1; j <= pedestrians.size(); j++) {
                            listModel.add(j - 1, "Pedestrian " + j + ": " + pedestrians.get(j - 1).getPositionString() +
                                    "   -   " + pedestrians.get(j - 1).getGenderString() +
                                    "   -   " + pedestrians.get(j - 1).getAgeString() +
                                    "   -   " + "Velocity: " + String.format("%.2f", pedestrians.get(j - 1).getVelocity()) +
                                    "   -   " + "Energy: " + pedestrians.get(j - 1).getEnergy() +
                                    "   -   " + "Group ID: " + pedestrians.get(j - 1).getGroupID());
                        }
                        JList pedestriansList = new JList(listModel);

                        this.activePedestrians.removeAll();

                        groupPanel.add(labelPanel, BorderLayout.NORTH);
                        labelPanel.setBackground(Color.LIGHT_GRAY);
                        labelPanel.add(groupLabel);
                        groupPanel.add(pedestriansList, BorderLayout.CENTER);

                        allGroupsPanel.add(groupPanel, gbd);
                        gbd.gridy++;
                    }


                    JViewport view = new JViewport();
                    view.add(allGroupsPanel);
                    scrollGroupsPane.setViewport(view);
                    scrollGroupsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollGroupsPane.setPreferredSize(new Dimension(this.activePedestrians.getWidth() - 8, this.activePedestrians.getHeight() - 8));

                    this.activePedestrians.add(scrollGroupsPane);
                }

                //grouped by feature
                else {
                    DefaultListModel listModel = new DefaultListModel();

                    for (int i = 1; i <= crowd.size(); i++) {
                        listModel.add(i - 1, "Pedestrian " + i + ": " + crowd.get(i - 1).getPositionString() +
                                "   -   " + crowd.get(i - 1).getGenderString() +
                                "   -   " + crowd.get(i - 1).getAgeString() +
                                "   -   " + "Velocity: " + String.format("%.2f", crowd.get(i - 1).getVelocity()) +
                                "   -   " + "Energy: " + crowd.get(i - 1).getEnergy());
                    }

                    //this.activePedestrians.removeAll();
                    JList pedestiansList = new JList(listModel);

                    JScrollPane scrollPane = new JScrollPane(pedestiansList);
                    scrollPane.setPreferredSize(new Dimension(this.activePedestrians.getWidth() - 8, this.activePedestrians.getHeight() - 8));
                    this.activePedestrians.add(scrollPane);
                }
            }
            this.activePedestrians.revalidate();
            this.activePedestrians.repaint();
        }

        /**
         * Every time there is a change in the settings of the "filters panel", this function modifies, updates and shows the
         * list of active pedestrians and the active pedestrians' tab
         * */
        public void updateFilteredCrowd(){
            ArrayList<Pedestrian> filteredCrowd = new ArrayList<>();

            if(Simulation.getInstance().getCrowd() != null)
                filteredCrowd = new ArrayList<>(Simulation.getInstance().getCrowd());

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
                    Support.sortByGender(filteredCrowd, 0, filteredCrowd.size()-1);
                    break;
                case "Age":
                    Support.sortByAge(filteredCrowd, 0, filteredCrowd.size()-1);
                    break;
                case "Velocity":
                    Support.quickSortByVelocity(filteredCrowd, 0, filteredCrowd.size()-1);
                    break;
                case "Energy":
                    Support.quickSortByEnergy(filteredCrowd, 0, filteredCrowd.size()-1);
                    break;
                default:
            }

            setPedestriansTab(filteredCrowd);
        }

        /**
         * draw the topbar for each group when pedestrians are grouped by groupID
         * */
        private void topGroupPanel(JPanel panel, int groupID){

        }


    /***************************************    SUPPORT FUNCTIONS    *****************************************/

    public void disableFilters(){
        orderBy.setEnabled(false);
        ageFilter.setEnabled(false);
        genderFilter.setEnabled(false);
        velocityFilter.setEnabled(false);
        energyFilter.setEnabled(false);
    }

    public void enableFilters(){
        orderBy.setEnabled(true);
        ageFilter.setEnabled(true);
        genderFilter.setEnabled(true);
        velocityFilter.setEnabled(true);
        energyFilter.setEnabled(true);
    }
}
