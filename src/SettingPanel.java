import support.AllertWindow;

import javax.swing.*;
import java.awt.*;


import static support.constants.Constant.*;


public class SettingPanel extends JPanel {
    private JFrame frame;

    private JSpinner numberOfPeople, numberOfWayPoints, numberOfGroups, numberOfObstacles;
    private JButton playButton, pauseButton, stopButton, confirmButton;

    public SettingPanel(JFrame frame){
        this.frame = frame;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setLayout(new BorderLayout(10,10));

        setInternalLayout();
    }


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

        //Adding label and text fields into the panel
        addLabelsAndSpinners(optionsPanel);

        //Adding button for stopping the simulation (last thing to add in this page)
        addConfirmButton(optionsPanel);
    }


    private void setTopBar(JPanel panel){
        JToolBar topBar = new JToolBar();
        topBar.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Settings Menu");
        topBar.setFloatable(false);
        topBar.add(label);

        panel.add(topBar,BorderLayout.NORTH);
    }

    private void addButtons(JPanel panel){
        JPanel buttonsPanel = new JPanel(new GridBagLayout());

        //icons
        Icon playButton = new ImageIcon("src/media/playButton.png");
        Icon pauseButton = new ImageIcon("src/media/pauseButton.png");
        Icon stopButton = new ImageIcon("src/media/stopButton.png");

        GridBagConstraints gbd = new GridBagConstraints();
        gbd.insets = new Insets(5,10,5,10);

        gbd.gridx = 0;
        this.pauseButton = new JButton(pauseButton);
        this.pauseButton.setFocusable(false);
        this.pauseButton.addActionListener(e -> Simulation.getInstance().pauseSimulation());
        buttonsPanel.add(this.pauseButton, gbd);

        gbd.gridx = 1;
        this.playButton = new JButton(playButton);
        this.playButton.setFocusable(false);
        this.playButton.addActionListener(e -> startSimulation());
        buttonsPanel.add(this.playButton, gbd);

        gbd.gridx = 2;
        this.stopButton = new JButton(stopButton);
        this.stopButton.setFocusable(false);
        this.stopButton.addActionListener(e -> stopSimulation());
        buttonsPanel.add(this.stopButton, gbd);

        disableButtons();

        panel.add(buttonsPanel, BorderLayout.CENTER);
    }

    private void addLabelsAndSpinners(JPanel panel){
        //Gbd option for instructions and for every couple (label + spinner)
        GridBagConstraints gbdPanel = new GridBagConstraints();
        gbdPanel.anchor = GridBagConstraints.FIRST_LINE_START;
        gbdPanel.gridx = 0;
        gbdPanel.insets = new Insets(5,5,0,5);

        //instructions for setting panel
        JLabel instructions1 = new JLabel("Set values for each field, then press \"confirm\"");
        instructions1.setForeground(Color.GRAY);
        panel.add(instructions1,gbdPanel);
        gbdPanel.insets = new Insets(0,5,15,5);
        JLabel instructions2 = new JLabel("to apply them to the simulation:");
        instructions2.setForeground(Color.GRAY);
        panel.add(instructions2,gbdPanel);

        //Gbd option adjustment (label + spinner)
        gbdPanel.gridx = 0;
        gbdPanel.insets = new Insets(5,5,5,5);

        //labels
        JLabel numPeopleLabel = new JLabel("Number of people:                    ");
        JLabel numWayPointsLabel = new JLabel("Number of way points:             ");
        JLabel numObstaclesLabel = new JLabel("Number of obstacles:              ");
        JLabel numGroupsLabel = new JLabel("Number of groups:                   ");

        //spinners
        this.numberOfPeople = new JSpinner();
        SpinnerModel numOfPeopleModel = new SpinnerNumberModel(0,0,MAX_PEOPLE,1);
        this.numberOfPeople.setModel(numOfPeopleModel);

        this.numberOfWayPoints = new JSpinner();
        SpinnerModel numOfWayPointsModel = new SpinnerNumberModel(0,0,MAX_WAY_POINTS,1);
        this.numberOfWayPoints.setModel(numOfWayPointsModel);

        this.numberOfObstacles = new JSpinner();
        SpinnerModel numOfObstaclesModel = new SpinnerNumberModel(0,0,MAX_OBSTACLES,1);
        this.numberOfObstacles.setModel(numOfObstaclesModel);

        this.numberOfGroups = new JSpinner();
        SpinnerModel numOfGroupsModel = new SpinnerNumberModel(0,0,MAX_GROUPS,1);
        this.numberOfGroups.setModel(numOfGroupsModel);

        //panels where to put every couple (label + spinner)
        JPanel line1 = new JPanel();
        line1.add(numPeopleLabel);
        line1.add(numberOfPeople);
        panel.add(line1, gbdPanel);

        JPanel line2 = new JPanel();
        line2.add(numWayPointsLabel);
        line2.add(numberOfWayPoints);
        panel.add(line2, gbdPanel);

        JPanel line3 = new JPanel();
        line3.add(numObstaclesLabel);
        line3.add(numberOfObstacles);
        panel.add(line3, gbdPanel);

        JPanel line4 = new JPanel();
        line4.add(numGroupsLabel);
        line4.add(numberOfGroups);
        panel.add(line4, gbdPanel);
    }

    private void addConfirmButton(JPanel panel){
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.addActionListener(e -> setParameters());
        this.confirmButton.setFocusable(false);

        GridBagConstraints gbd = new GridBagConstraints();
        gbd.fill = GridBagConstraints.HORIZONTAL;
        gbd.anchor = GridBagConstraints.PAGE_END;
        gbd.insets = new Insets(10,20,10,20);
        gbd.gridy = 100000;
        gbd.weighty = 1;
        panel.add(this.confirmButton, gbd);

    }

    private void setParameters(){
        Simulation.getInstance().setParameters((int)numberOfPeople.getValue(), (int)numberOfWayPoints.getValue(),
                (int)numberOfGroups.getValue(), (int)numberOfObstacles.getValue());
    }

    private void startSimulation(){
        if(Simulation.getInstance().missingInputs())
            return;
        Simulation.getInstance().startSimulation();
        enableButtons();
    }

    private void stopSimulation(){
        if(new AllertWindow("Do you really want to stop the simulation? It will restore all the settings").isConfirmed()) {
            this.numberOfPeople.setValue(0);
            this.numberOfObstacles.setValue(0);
            this.numberOfGroups.setValue(0);
            this.numberOfWayPoints.setValue(0);
            disableButtons();
            Simulation.getInstance().stopSimulation();
        }
    }

    public void disableButtons(){
        this.stopButton.setEnabled(false);
        this.pauseButton.setEnabled(false);
    }

    public void enableButtons(){
        this.stopButton.setEnabled(true);
        this.pauseButton.setEnabled(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JSpinner getNumberOfPeople() {
        return numberOfPeople;
    }

    public JSpinner getNumberOfWayPoints() {
        return numberOfWayPoints;
    }

    public JSpinner getNumberOfGroups() {
        return numberOfGroups;
    }

    public JSpinner getNumberOfObstacles() {
        return numberOfObstacles;
    }
}
