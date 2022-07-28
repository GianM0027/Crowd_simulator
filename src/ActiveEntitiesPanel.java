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
        JSplitPane obstaclesAndPoiTab = new JSplitPane();
        JSplitPane researchAndPeople = new JSplitPane();
        this.addTab("Obstacles and Way Points", obstaclesAndPoiTab);
        this.addTab("Pedestrians", researchAndPeople);

        //panels to split in tab 1
        JScrollPane obstacles = new JScrollPane();
        JScrollPane wayPoints = new JScrollPane();
        obstaclesAndPoiTab.setLeftComponent(obstacles);
        obstaclesAndPoiTab.setRightComponent(wayPoints);

        //panel to split in tab 2
        JPanel researchFilters = new JPanel();
        JScrollPane activePedestrians = new JScrollPane();
        researchAndPeople.setLeftComponent(researchFilters);
        researchAndPeople.setRightComponent(activePedestrians);
    }


    public JFrame getFrame() {
        return frame;
    }
}
