import javax.swing.*;
import java.awt.*;

import static support.constants.Constant.*;

public class SettingPanel extends JPanel {
    private GridBagConstraints gbd = new GridBagConstraints();
    private JFrame frame;

    public SettingPanel(JFrame frame){
        this.frame = frame;
        this.gbd.gridx = 0;
        this.gbd.gridy = 0;
        this.gbd.gridheight = 2;
        this.gbd.fill = GridBagConstraints.BOTH;
        this.gbd.weightx = 0.15;
        this.gbd.weighty = 1;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(10,10));

        setInternalLayout();
    }


    private void setInternalLayout(){
        //declaring 2 sections (1 containing topBar + buttons, 1 scrollable contains options and settings)
        JPanel topPanel = new JPanel(new BorderLayout(10,10));
        topPanel.setBackground(Color.WHITE);
        this.add(topPanel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(); //decidi un layout manager
        JScrollPane optionScroll = new JScrollPane(optionsPanel);
        optionsPanel.setBackground(Color.GREEN);
        this.add(optionScroll, BorderLayout.CENTER);

        /*setting scroll bar
        JScrollBar scrollBar = new JScrollBar();
        optionsPanel.add(scrollBar, BorderLayout.EAST);*/

        //setting top bar
        setTopBar(topPanel);

        //startSimulationButton settings
        addButtons(topPanel);

        //Adding label and text fields into the panel
        addLabelAndTextField(optionsPanel, "Number of people");
        addLabelAndTextField(optionsPanel, "Number of obstacles");
        addLabelAndTextField(optionsPanel, "Number of points of interest");
    }


    private void setTopBar(JPanel panel){//vedi se aggiungere pannello in cui mettere la topBar
        JToolBar topBar = new JToolBar();
        topBar.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Settings Menu");
        topBar.setFloatable(false);
        topBar.add(label);

        panel.add(topBar,BorderLayout.NORTH);
    }

    private void addButtons(JPanel panel){
        JPanel buttonsPanel = new JPanel(new BorderLayout(10,10));

        buttonsPanel.setLayout(new BorderLayout(10,10));
        buttonsPanel.setBounds(0,0,this.getWidth(),this.getHeight()/10);
        buttonsPanel.setBackground(Color.WHITE);

        JButton startSimulationButton = new JButton("START SIMULATION");
        startSimulationButton.addActionListener(e -> startSimulation());
        startSimulationButton.setPreferredSize(new Dimension(this.getWidth()/3, 40));
        buttonsPanel.add(startSimulationButton, BorderLayout.NORTH);

        JButton stopSimulationButton = new JButton("STOP SIMULATION");
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

        panel.add(jlabel);
        panel.add(inputNumber);
        panel.add(confirmButton);

        this.add(panel);
    }



    private void startSimulation(){
        System.out.println("Simulation Started");
    }

    private void stopSimulation(){
        System.out.println("Simulation Stopped");
    }

    public GridBagConstraints getGbd() {
        return gbd;
    }

    public JFrame getFrame() {
        return frame;
    }

}
