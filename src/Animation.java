import models.*;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the movement and the printing functions of the simulation
 * */
public class Animation extends JPanel implements ActionListener {

    private JPanel parentPanel;
    private List<Pedestrian> crowd;
    private List<Group> groups;
    private List<Obstacle> obstacles;
    private List<WayPoint> wayPoints;
    private Building building;

    //simulation parameters
    private Timer timer;

    public Animation(JPanel panel, List<Pedestrian> crowd, List<Obstacle> obstacles, List<WayPoint> wayPoints, List<Group> groups, Building building){
        this.parentPanel = panel;
        this.groups = groups;
        this.crowd = crowd;
        this.obstacles = obstacles;
        this.wayPoints = wayPoints;

        this.setPreferredSize(new Dimension(this.parentPanel.getWidth(), this.parentPanel.getHeight()));
        this.building = building;

        timer = new Timer(Constant.ANIMATION_DELAY, this);
        timer.start();
    }


    /*****************************   Paint background and fixed entities   ******************************/
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBuilding(g);
        drawEntities(g);
    }

    private void drawBuilding(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        g2D.setStroke(new BasicStroke(Constant.BUILDING_STROKE));
        building.drawExternalArea(g2D);
        for(int i = 0; i < building.getRooms().size(); i++)
            building.getRooms().get(i).drawRoom(g2D);
        g2D.setStroke(new BasicStroke(0));
    }


    private void drawEntities(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //draw obstacles
        g2D.setPaint(Color.BLACK);
        for (Obstacle obstacle : obstacles) {
            g2D.fill(obstacle.getObstacleShape());
        }

        //draw way points
        g2D.setPaint(Color.red);
        for (WayPoint wayPoint : wayPoints) {
            g2D.fill(wayPoint.getWayPointShape());
        }

        //for each group of pedestrians, draw its components
        for (Group group : groups) {
            g2D.setPaint(group.getColor());

            for (Pedestrian pedestrian: group.getPedestrians()) {
                g2D.fill(pedestrian.getPedestrianShape());
            }
        }
    }



    /******************************    Handle pedestrians' movement    *************************************/
    @Override
    public void actionPerformed(ActionEvent e) {
        Point2D nextPosition;

        for (Pedestrian pedestrian : crowd) {
            pedestrian.nextPosition(this, new ArrayList<>(crowd));
        }

        repaint();
    }


    /******************************    Animation commands    *************************************/
    public void pause(){
        this.timer.stop();
    }

    public void resume(){
        this.timer.start();
    }
}
