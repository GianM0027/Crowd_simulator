import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static constants.Constants.*;

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
        this.setLayout(new BorderLayout());

        setInternalLayout();
    }

    private void setInternalLayout(){
        //startSimulationButton settings
        addButtons();

        //Settings panel
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridBagLayout());
        optionPanel.setBackground(Color.WHITE);
        this.add(optionPanel);

        //Adding label and text fields in the panel
        GridBagConstraints gbd = new GridBagConstraints();
        gbd.gridy = 0;
        gbd.insets = new Insets(5,5,5,5);
        gbd = addLabelAndTextField(optionPanel , "Number of people", gbd);
        gbd = addLabelAndTextField(optionPanel, "Number of obstacles", gbd);
        gbd = addLabelAndTextField(optionPanel, "Number of points of interest", gbd);
    }


    private GridBagConstraints addLabelAndTextField(JPanel panel, String label, GridBagConstraints gbd){
        JLabel jlabel = new JLabel(label);
        JSpinner inputNumber = new JSpinner();
        JButton confirmButton = new JButton("Confirm");

        SpinnerModel spinnerModel = new SpinnerNumberModel(0,0,MAX_PEOPLE,1);
        inputNumber.setModel(spinnerModel);
        gbd.anchor = GridBagConstraints.FIRST_LINE_START;

        gbd.gridx = 0;
        gbd.weightx = 0.8;
        panel.add(jlabel, gbd);
        gbd.gridx = 1;
        gbd.weightx = 0.5;
        gbd.fill = GridBagConstraints.HORIZONTAL;
        panel.add(inputNumber, gbd);
        gbd.gridx = 2;
        gbd.weightx = 0.3;
        gbd.fill = GridBagConstraints.NONE;
        panel.add(confirmButton, gbd);

        gbd.gridx = 0;
        gbd.gridy++;
        return gbd;
    }

    private void addButtons(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(new BorderLayout(10,10));
        buttonPanel.setBounds(0,0,this.getWidth(),this.getHeight()/10);
        buttonPanel.setBackground(Color.WHITE);

        JButton startSimulationButton = new JButton("START SIMULATION");
        startSimulationButton.addActionListener(e -> startSimulation());
        startSimulationButton.setPreferredSize(new Dimension(this.getWidth()/3, 40));
        buttonPanel.add(startSimulationButton, BorderLayout.NORTH);

        JButton stopSimulationButton = new JButton("STOP SIMULATION");
        stopSimulationButton.addActionListener(e -> stopSimulation());
        stopSimulationButton.setPreferredSize(new Dimension(this.getWidth()/3, 40));
        buttonPanel.add(stopSimulationButton, BorderLayout.SOUTH);

        this.add(buttonPanel, BorderLayout.NORTH);
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
