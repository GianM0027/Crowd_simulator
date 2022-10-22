import support.ConfirmationWindow;
import support.RangeSlider;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Panel with controls and settings of the simulation
 * */
public class SettingPanel extends JPanel {

    private JTextField numberOfPeople, numberOfWayPoints, numberOfObstacles;
    private RangeSlider groupsSize;
    private JButton playButton, pauseButton, stopButton, confirmButton;


    public SettingPanel(){
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setLayout(new BorderLayout(10,10));

        setInternalLayout();
    }

    /**
     * Set the internal layout
     * */
    private void setInternalLayout(){
        //declaring 2 sections (1 containing topBar + buttons, 1 scrollable contains options and settings)
        JPanel topPanel = new JPanel(new BorderLayout(10,10));
        this.add(topPanel, BorderLayout.NORTH);
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(optionsPanel);

        //setting top bar
        setTopBar(topPanel);

        //setting start and pause buttons
        addButtons(topPanel);

        //Adding options and simulation's parameters in setting panel
        addOptions(optionsPanel);

        //Adding button for stopping the simulation (last thing to add in this page)
        addConfirmButton(optionsPanel);
    }


    /**
     * Set a grey bar on the top of the panel with the name of that panel
     * */
    private void setTopBar(JPanel panel){
        JToolBar topBar = new JToolBar();
        topBar.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Settings Menu");
        topBar.setFloatable(false);
        topBar.add(label);

        panel.add(topBar,BorderLayout.NORTH);
    }

    /**
     * Add play button, pause button and stop button
     * */
    private void addButtons(JPanel panel){
        JPanel buttonsPanel = new JPanel(new GridBagLayout());

        //icons
        ClassLoader cl = this.getClass().getClassLoader();
        ImageIcon playButton = new ImageIcon(cl.getResource("media/playButton.png"));
        ImageIcon pauseButton = new ImageIcon(cl.getResource("media/pauseButton.png"));
        ImageIcon stopButton = new ImageIcon(cl.getResource("media/stopButton.png"));

        //gbd and add buttons
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.insets = new Insets(5,10,5,10);

        gbd.gridx = 0;
        this.pauseButton = new JButton(pauseButton);
        this.pauseButton.setFocusable(false);
        this.pauseButton.addActionListener(e -> pauseSimulation());
        buttonsPanel.add(this.pauseButton, gbd);

        gbd.gridx = 1;
        this.playButton = new JButton(playButton);
        this.playButton.setFocusable(false);
        this.playButton.addActionListener(e -> {
            try {
                startSimulation();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonsPanel.add(this.playButton, gbd);

        gbd.gridx = 2;
        this.stopButton = new JButton(stopButton);
        this.stopButton.setFocusable(false);
        this.stopButton.addActionListener(e -> stopSimulation());
        buttonsPanel.add(this.stopButton, gbd);

        //when there is not an active simulation stop and pause buttons are disabled
        disableStopPauseButtons();

        panel.add(buttonsPanel, BorderLayout.CENTER);
    }

    /**
     * Add all the options for the simulation, every added field in this function has a label and a text field to fill
     * */
    private void addOptions(JPanel panel){
        //Gbd option for instructions and for every couple (label + spinner)
        GridBagConstraints gbdPanel = new GridBagConstraints();
        gbdPanel.anchor = GridBagConstraints.FIRST_LINE_START;
        gbdPanel.gridx = 0;
        gbdPanel.insets = new Insets(5,5,0,5);

        //instructions for setting panel
        JLabel instructions1 = new JLabel("Set values for each field, press \"confirm\" to apply");
        instructions1.setForeground(Color.GRAY);
        panel.add(instructions1,gbdPanel);
        gbdPanel.insets = new Insets(0,5,20,5);
        JLabel instructions2 = new JLabel("them to the simulation, then click on the play button");
        instructions2.setForeground(Color.GRAY);
        panel.add(instructions2,gbdPanel);

        //Gbd option adjustment (label + spinner)
        gbdPanel.gridx = 0;
        gbdPanel.fill = GridBagConstraints.HORIZONTAL;
        gbdPanel.insets = new Insets(10,10,10,10);

        //labels
        JLabel numPeopleLabel = new JLabel("Size of the crowd:");
        JLabel numWayPointsLabel = new JLabel("Number of goal points:");
        JLabel numObstaclesLabel = new JLabel("Number of obstacles:");

        //spinners
        this.numberOfPeople = new JTextField();
        this.numberOfPeople.setPreferredSize(new Dimension(80, 25));
        this.numberOfWayPoints = new JTextField();
        this.numberOfWayPoints.setPreferredSize(new Dimension(80, 25));
        this.numberOfObstacles = new JTextField();
        this.numberOfObstacles.setPreferredSize(new Dimension(80, 25));

        //panels where to put every couple (label + spinner)
        JPanel line1 = new JPanel(new BorderLayout());
        line1.add(numPeopleLabel, BorderLayout.WEST);
        line1.add(numberOfPeople, BorderLayout.EAST);
        panel.add(line1, gbdPanel);

        JPanel line2 = new JPanel(new BorderLayout());
        line2.add(numWayPointsLabel, BorderLayout.WEST);
        line2.add(numberOfWayPoints, BorderLayout.EAST);
        panel.add(line2, gbdPanel);

        JPanel line3 = new JPanel(new BorderLayout());
        line3.add(numObstaclesLabel, BorderLayout.WEST);
        line3.add(numberOfObstacles, BorderLayout.EAST);
        panel.add(line3, gbdPanel);



        //adding range slider for groups size
        JLabel numGroupsLabel = new JLabel("Select a range for the size of the groups:");
        this.groupsSize = new RangeSlider(Constant.MIN_GROUPS_SIZE, Constant.MAX_GROUPS_SIZE);
        groupsSize.setFocusable(false);
        groupsSize.setMinorTickSpacing(1);
        groupsSize.setMajorTickSpacing(2);
        groupsSize.setValue(Constant.MIN_GROUPS_SIZE);
        groupsSize.setExtent(Constant.MAX_GROUPS_SIZE);
        groupsSize.setPaintLabels(true);
        groupsSize.setPaintTicks(true);

        JPanel line4 = new JPanel(new BorderLayout(10,10));
        line4.add(numGroupsLabel, BorderLayout.NORTH);
        line4.add(groupsSize, BorderLayout.CENTER);
        panel.add(line4, gbdPanel);
    }

    /**
     * Add the "confirm" button at the bottom of the page
     * */
    private void addConfirmButton(JPanel panel){
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.addActionListener(e -> setSimulationParameters());
        this.confirmButton.setFocusable(false);

        //gbd settings
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.fill = GridBagConstraints.HORIZONTAL;
        gbd.anchor = GridBagConstraints.PAGE_END;
        gbd.insets = new Insets(10,20,10,20);
        gbd.gridy = 100000;
        gbd.weighty = 1;

        panel.add(this.confirmButton, gbd);
    }

    private void setSimulationParameters(){
        //controls before setting parameters, if any field is empty a warning message will be shown
        if(this.numberOfPeople.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Insert a value bigger than 0 in the 'number of people' field");
            return;
        } else if(Integer.parseInt(this.numberOfPeople.getText()) == 0){
            JOptionPane.showMessageDialog(null, "Insert a value bigger than 0 in the 'number of people' field");
            return;
        }
        if(this.numberOfWayPoints.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Insert a value bigger than 0 in the 'number of way points' field");
            return;
        }else if(Integer.parseInt(this.numberOfWayPoints.getText()) == 0){
            JOptionPane.showMessageDialog(null, "Insert a value bigger than 0 in the 'number of way points' field");
            return;
        }
        if(this.numberOfObstacles.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Insert a value bigger than 0 in the 'number of obstacles' field");
            return;
        }else if(Integer.parseInt(this.numberOfObstacles.getText()) == 0){
            JOptionPane.showMessageDialog(null, "Insert a value bigger than 0 in the 'number of obstacles' field");
            return;
        }
        if(this.groupsSize.getUpperValue() > Integer.parseInt(numberOfPeople.getText())) {
            JOptionPane.showMessageDialog(null, "Max value of 'size of groups' must be lower than the crowd size");
            return;
        }

        //if all the fields are correctly filled up, you set the parameters of the simulation even in the "Simulation" class
        Simulation.getInstance().setParameters(Integer.parseInt(this.numberOfPeople.getText()), groupsSize.getValue(), groupsSize.getUpperValue(),
                Integer.parseInt(this.numberOfObstacles.getText()), Integer.parseInt(this.numberOfWayPoints.getText()));
    }

    /**
     * Functionality of the "play button", it start a new simulation or resume an existing one
     * */
    private void startSimulation() throws IOException {
        //when pressed the play button but there are not valid parameters in the fields nothing happens
        if(Simulation.getInstance().missingParameters())
            return;

        //if it does not exist a simulation yet, you create a new one
        if(!Simulation.getInstance().isRunning()) {
            Simulation.getInstance().setIsRunning(true);
            Simulation.getInstance().startSimulation();
            enableStopPauseButtons();
        }

        //if already exists an active simulation, you resume the stopped animation
        else{
            enableStopPauseButtons();
            Simulation.getInstance().resumeSimulation();
        }
    }

    /**
     * Functionality of the "pause button", stop the timer of the currently active animation
     * */
    private void pauseSimulation(){
        this.playButton.setEnabled(true);
        Simulation.getInstance().pauseSimulation();
    }

    /**
     * Functionality of the "stop button", reset the simulation and all its parameters
     * */
    private void stopSimulation(){
        if(new ConfirmationWindow("Do you really want to stop the simulation? It will restore all the settings").isConfirmed()) {
            this.numberOfPeople.setText("");
            this.numberOfObstacles.setText("");
            this.groupsSize.setValue(Constant.MIN_GROUPS_SIZE);
            this.groupsSize.setUpperValue(Constant.MAX_GROUPS_SIZE);
            this.numberOfWayPoints.setText("");
            disableStopPauseButtons();
            Simulation.getInstance().stopSimulation();
        }
    }

    /**
     * Auxiliary functions
     * */
    public void disableStopPauseButtons(){
        this.playButton.setEnabled(true);
        this.stopButton.setEnabled(false);
        this.pauseButton.setEnabled(false);
    }

    public void enableStopPauseButtons(){
        this.playButton.setEnabled(false);
        this.stopButton.setEnabled(true);
        this.pauseButton.setEnabled(true);
    }
}
