import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel{
    private JFrame frame;

    public SimulationPanel(JFrame frame){
        this.frame = frame;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        //setting top bar
        setTopBar();

        //Setting simulation area
        setInternalLayout();
    }

    private void setTopBar(){
        JToolBar topBar = new JToolBar();
        topBar.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Simulation Panel");
        topBar.setFloatable(false);
        topBar.add(label);

        this.add(topBar,BorderLayout.NORTH);
    }

    private void setInternalLayout(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(500, this.frame.getHeight()/2+this.frame.getHeight()/9));
        this.add(panel, BorderLayout.CENTER);

        JLabel label = new JLabel("*Simulation");
        panel.add(label);
    }

    public JFrame getFrame() {
        return frame;
    }
}

