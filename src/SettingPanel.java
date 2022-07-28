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
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(optionsPanel);

        //setting top bar
        setTopBar(topPanel);

        //setting start and pause buttons
        addButtons(topPanel);

        //Adding label and text fields into the panel
        addLabelAndTextField(optionsPanel, "Number of people:                   ");
        addLabelAndTextField(optionsPanel, "Number of obstacles:              ");
        addLabelAndTextField(optionsPanel, "Number of way points:             ");
        addLabelAndTextField(optionsPanel, "Number of groups:                   ");

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
        JButton pauseSimulationButton = new JButton(pauseButton);
        pauseSimulationButton.setFocusable(false);
        pauseSimulationButton.addActionListener(e -> startSimulation());
        buttonsPanel.add(pauseSimulationButton, gbd);

        gbd.gridx = 1;
        JButton startSimulationButton = new JButton(playButton);
        startSimulationButton.setFocusable(false);
        startSimulationButton.addActionListener(e -> startSimulation());
        buttonsPanel.add(startSimulationButton, gbd);

        gbd.gridx = 2;
        JButton stopSimulationButton = new JButton(stopButton);
        stopSimulationButton.setFocusable(false);
        stopSimulationButton.addActionListener(e -> stopSimulation());
        buttonsPanel.add(stopSimulationButton, gbd);

        panel.add(buttonsPanel, BorderLayout.CENTER);
    }

    private void addLabelAndTextField(JPanel panel, String label){
        JLabel jlabel = new JLabel(label);
        JSpinner inputNumber = new JSpinner();

        SpinnerModel spinnerModel = new SpinnerNumberModel(0,0,MAX_PEOPLE,1);
        inputNumber.setModel(spinnerModel);

        JPanel line = new JPanel();
        line.add(jlabel);
        line.add(inputNumber);

        GridBagConstraints gbdPanel = new GridBagConstraints();
        gbdPanel.anchor = GridBagConstraints.FIRST_LINE_START;
        gbdPanel.gridx = 0;
        gbdPanel.insets = new Insets(5,5,5,5);

        panel.add(line, gbdPanel);
    }

    private void addConfirmButton(JPanel panel){
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFocusable(false);

        GridBagConstraints gbd = new GridBagConstraints();
        gbd.fill = GridBagConstraints.HORIZONTAL;
        gbd.anchor = GridBagConstraints.PAGE_END;
        gbd.insets = new Insets(10,20,10,20);
        gbd.gridy = 100000;
        gbd.weighty = 1;
        panel.add(confirmButton, gbd);

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
