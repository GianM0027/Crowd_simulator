import javax.swing.*;
import java.awt.*;

public class ActiveEntitiesPanel extends JTabbedPane{
    private JFrame frame;

    public ActiveEntitiesPanel(JFrame frame){
        this.frame = frame;
        this.setBorder(BorderFactory.createEtchedBorder());

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
        JPanel activePedestrians = new JPanel();
        researchAndPeople.setLeftComponent(researchFilters);
        researchAndPeople.setRightComponent(activePedestrians);

        //setting Tabs
        setObstaclesTab(obstaclesTab);
        setWayPointsTab(wayPointsTab);
        setPedestriansTab(researchFilters, activePedestrians);
    }

    private void setObstaclesTab(JPanel panel){
        //setta il listener per quando c'è da mostrare la lista

        if(Simulation.getInstance().getNumberOfObstacles() == 0){
            panel.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active obstacles");
            noObstacles.setForeground(Color.GRAY);
            panel.add(noObstacles);
        }
    }

    private void setWayPointsTab(JPanel panel){
        //setta il listener per quando c'è da mostrare la lista

        if(Simulation.getInstance().getNumberOfWayPoints() == 0){
            panel.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active way points");
            noObstacles.setForeground(Color.GRAY);
            panel.add(noObstacles);
        }
    }

    private void setPedestriansTab(JPanel leftPanel, JPanel rightPanel){
        //setting filters layout


        //setta il listener per quando c'è da mostrare la lista

        if(Simulation.getInstance().getNumberOfPeople() == 0){
            rightPanel.setLayout(new GridBagLayout());
            JLabel noObstacles = new JLabel("Start the simulation to view the active pedestrians");
            noObstacles.setForeground(Color.GRAY);
            rightPanel.add(noObstacles);
        }
    }


    public JFrame getFrame() {
        return frame;
    }
}
