import javax.swing.*;
import java.awt.*;

import static support.constants.Constant.*;

public class SettingPanel extends JPanel {
    private JFrame frame;

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
        JPanel optionsPanel = new JPanel(new GridBagLayout()); //decidi un layout manager
        this.add(optionsPanel);

        //setting top bar
        setTopBar(topPanel);

        //setting start and pause buttons
        addButtons(topPanel);

        //Adding label and text fields into the panel
        addLabelAndTextField(optionsPanel, "Number of people");
        addLabelAndTextField(optionsPanel, "Number of obstacles");
        addLabelAndTextField(optionsPanel, "Number of points of interest");


        //Adding button for stopping the simulation (last thing to add in this page)
        addStopButton(optionsPanel);
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
        JPanel buttonsPanel = new JPanel(new BorderLayout(5,5));

        buttonsPanel.setBounds(0,0,this.getWidth(),this.getHeight()/10);

        JButton startSimulationButton = new JButton("START SIMULATION");
        startSimulationButton.addActionListener(e -> startSimulation());
        startSimulationButton.setPreferredSize(new Dimension(this.getWidth()/3, 40));
        buttonsPanel.add(startSimulationButton, BorderLayout.NORTH);

        JButton stopSimulationButton = new JButton("PAUSE SIMULATION");
        stopSimulationButton.addActionListener(e -> stopSimulation());
        stopSimulationButton.setPreferredSize(new Dimension(this.getWidth()/3, 40));
        buttonsPanel.add(stopSimulationButton, BorderLayout.SOUTH);

        panel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void addLabelAndTextField(JPanel panel, String label){
        JLabel jlabel = new JLabel(label);
        JSpinner inputNumber = new JSpinner();
        JButton confirmButton = new JButton("Confirm");

        SpinnerModel spinnerModel = new SpinnerNumberModel(0,0,MAX_PEOPLE,1);
        inputNumber.setModel(spinnerModel);

        JPanel line = new JPanel();
        line.add(jlabel);
        line.add(inputNumber);
        line.add(confirmButton);

        GridBagConstraints gbdPanel = new GridBagConstraints();
        gbdPanel.anchor = GridBagConstraints.FIRST_LINE_START;
        gbdPanel.gridx = 0;
        gbdPanel.insets = new Insets(5,5,5,5);

        panel.add(line, gbdPanel);
    }

    private void addStopButton(JPanel panel){
        JButton stopButton = new JButton("Stop Simulation");

        GridBagConstraints gbd = new GridBagConstraints();
        gbd.fill = GridBagConstraints.HORIZONTAL;
        gbd.anchor = GridBagConstraints.PAGE_END;
        gbd.insets = new Insets(10,20,10,20);
        gbd.gridy = 100000;
        gbd.weighty = 1;
        panel.add(stopButton, gbd);
    }


    private void startSimulation(){
        System.out.println("Simulation Started");
    }

    private void stopSimulation(){
        System.out.println("Simulation Stopped");
    }


    public JFrame getFrame() {
        return frame;
    }

}
