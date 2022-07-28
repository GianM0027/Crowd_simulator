import javax.swing.*;
import java.awt.*;

public class ActiveEntitiesPanel extends JTabbedPane{
    private JFrame frame;

    public ActiveEntitiesPanel(JFrame frame){
        this.frame = frame;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setPreferredSize(new Dimension(500, this.frame.getHeight()/2-this.frame.getHeight()/9));

        setInternalLayout();
    }


    private void setInternalLayout(){
        //split panes for the two tabs
        JPanel obstaclesTab = new JPanel();
        JPanel wayPointsTab = new JPanel();
        JSplitPane researchAndPeople = new JSplitPane();
        this.addTab("Obstacles", obstaclesTab);
        this.addTab("Way Points", wayPointsTab);
        this.addTab("Pedestrians", researchAndPeople);

        //panel to split in researchAndPeople tab
        JPanel researchFilters = new JPanel();
        JScrollPane activePedestrians = new JScrollPane();
        researchAndPeople.setLeftComponent(researchFilters);
        researchAndPeople.setRightComponent(activePedestrians);
    }


    public JFrame getFrame() {
        return frame;
    }
}
