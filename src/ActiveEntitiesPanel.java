import javax.swing.*;
import java.awt.*;

public class ActiveEntitiesPanel extends JPanel{
    private JFrame frame;

    public ActiveEntitiesPanel(JFrame frame){
        this.frame = frame;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setLayout(new BorderLayout());

        //setting top bar
        setTopBar();
    }

    private void setTopBar(){
        JToolBar topBar = new JToolBar();
        topBar.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Alive Entities");
        topBar.setFloatable(false);
        topBar.add(label);

        this.add(topBar,BorderLayout.NORTH);
    }


    public JFrame getFrame() {
        return frame;
    }
}
