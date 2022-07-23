import javax.swing.*;
import java.awt.*;

public class SettingPanel{
    private JPanel panel;
    private GridBagConstraints gbd = new GridBagConstraints();
    private JFrame frame;
    public SettingPanel(JFrame frame){
        this.frame = frame;
        this.panel = new JPanel();
        gbd.gridx = 0;
        gbd.gridy = 0;
        gbd.gridheight = 2;
        gbd.fill = GridBagConstraints.BOTH;
        gbd.weightx = 0.2;
        gbd.weighty = 1;
        this.panel.setBorder(BorderFactory.createEtchedBorder());
        this.panel.setBackground(Color.WHITE);
    }

    public GridBagConstraints getGbd() {
        return gbd;
    }

    public JPanel getPanel(){
        return panel;
    }

    public JFrame getFrame() {
        return frame;
    }
}
