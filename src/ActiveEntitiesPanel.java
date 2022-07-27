import javax.swing.*;
import java.awt.*;

public class ActiveEntitiesPanel extends JPanel{
    private JFrame frame;

    public ActiveEntitiesPanel(JFrame frame){
        this.frame = frame;

        this.setBorder(BorderFactory.createEtchedBorder());
        this.setPreferredSize(new Dimension(500, 300));
        this.setBackground(Color.WHITE);
    }


    public JFrame getFrame() {
        return frame;
    }
}
