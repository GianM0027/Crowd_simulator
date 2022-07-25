import javax.swing.*;
import java.awt.*;

public class ActiveEntitiesPanel extends JPanel{
    private GridBagConstraints gbd = new GridBagConstraints();
    private JFrame frame;

    public ActiveEntitiesPanel(JFrame frame){
        this.frame = frame;

        gbd.gridx = 1;
        gbd.gridy = 1;
        gbd.fill = GridBagConstraints.BOTH;
        gbd.weightx = 0.7;
        gbd.weighty = 0.4;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);
    }

    public GridBagConstraints getGbd() {
        return gbd;
    }


    public JFrame getFrame() {
        return frame;
    }
}
