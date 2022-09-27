import models.*;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        for(int i = 0; i < building.getRooms().size(); i++) {
            building.getRooms().get(i).drawRoom(g2D);
        }

        g2D.setStroke(new BasicStroke(0));
    }


    private void drawEntities(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //draw obstacles
        g2D.setPaint(Color.BLACK);
        for (Obstacle obstacle : obstacles) {
            g2D.fill(obstacle.getObstacleShape());

            /* print bounds
            g2D.fill(new Ellipse2D.Double(obstacle.getEntityBounds().getUpLeft().getX(), obstacle.getEntityBounds().getUpLeft().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(obstacle.getEntityBounds().getUpRight().getX(), obstacle.getEntityBounds().getUpRight().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(obstacle.getEntityBounds().getUp().getX(), obstacle.getEntityBounds().getUp().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(obstacle.getEntityBounds().getLeft().getX(), obstacle.getEntityBounds().getLeft().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(obstacle.getEntityBounds().getRight().getX(), obstacle.getEntityBounds().getRight().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(obstacle.getEntityBounds().getBottomLeft().getX(), obstacle.getEntityBounds().getBottomLeft().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(obstacle.getEntityBounds().getBottomRight().getX(), obstacle.getEntityBounds().getBottomRight().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(obstacle.getEntityBounds().getBottom().getX(), obstacle.getEntityBounds().getBottom().getY(), 2, 2));
        */
        }

        //draw way points
        g2D.setPaint(Color.red);
        for (WayPoint wayPoint : wayPoints) {
            g2D.fill(wayPoint.getWayPointShape());
            g2D.drawString(Integer.toString(wayPoint.getWaypointID()), (int)wayPoint.getPosition().getX(), (int)wayPoint.getPosition().getY());

            /* print bounds
            g2D.fill(new Ellipse2D.Double(wayPoint.getEntityBounds().getUpLeft().getX(), wayPoint.getEntityBounds().getUpLeft().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(wayPoint.getEntityBounds().getUpRight().getX(), wayPoint.getEntityBounds().getUpRight().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(wayPoint.getEntityBounds().getUp().getX(), wayPoint.getEntityBounds().getUp().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(wayPoint.getEntityBounds().getLeft().getX(), wayPoint.getEntityBounds().getLeft().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(wayPoint.getEntityBounds().getRight().getX(), wayPoint.getEntityBounds().getRight().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(wayPoint.getEntityBounds().getBottomLeft().getX(), wayPoint.getEntityBounds().getBottomLeft().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(wayPoint.getEntityBounds().getBottomRight().getX(), wayPoint.getEntityBounds().getBottomRight().getY(), 2, 2));
            g2D.fill(new Ellipse2D.Double(wayPoint.getEntityBounds().getBottom().getX(), wayPoint.getEntityBounds().getBottom().getY(), 2, 2));
        */
        }

        //for each group of pedestrians, draw its components
        for (Group group : groups) {
            g2D.setPaint(group.getColor());

            for (Pedestrian pedestrian: group.getPedestrians()) {
                g2D.fill(pedestrian.getPedestrianShape());
                g2D.setPaint(Color.BLACK);
                g2D.setStroke(new BasicStroke(1));
                g2D.draw(pedestrian.getPedestrianShape());
                g2D.setStroke(new BasicStroke(0));
                g2D.setPaint(group.getColor());

                /* print bounds
                g2D.fill(new Ellipse2D.Double(pedestrian.getEntityBounds().getUpLeft().getX(), pedestrian.getEntityBounds().getUpLeft().getY(), 2, 2));
                g2D.fill(new Ellipse2D.Double(pedestrian.getEntityBounds().getUpRight().getX(), pedestrian.getEntityBounds().getUpRight().getY(), 2, 2));
                g2D.fill(new Ellipse2D.Double(pedestrian.getEntityBounds().getUp().getX(), pedestrian.getEntityBounds().getUp().getY(), 2, 2));
                g2D.fill(new Ellipse2D.Double(pedestrian.getEntityBounds().getLeft().getX(), pedestrian.getEntityBounds().getLeft().getY(), 2, 2));
                g2D.fill(new Ellipse2D.Double(pedestrian.getEntityBounds().getRight().getX(), pedestrian.getEntityBounds().getRight().getY(), 2, 2));
                g2D.fill(new Ellipse2D.Double(pedestrian.getEntityBounds().getBottomLeft().getX(), pedestrian.getEntityBounds().getBottomLeft().getY(), 2, 2));
                g2D.fill(new Ellipse2D.Double(pedestrian.getEntityBounds().getBottomRight().getX(), pedestrian.getEntityBounds().getBottomRight().getY(), 2, 2));
                g2D.fill(new Ellipse2D.Double(pedestrian.getEntityBounds().getBottom().getX(), pedestrian.getEntityBounds().getBottom().getY(), 2, 2));
            */
            }
        }
    }



    /******************************    Handle pedestrians' movement    *************************************/
    @Override
    public void actionPerformed(ActionEvent e) {

        for (Pedestrian pedestrian : crowd) {
            if(pedestrian.getGroup().hasStartedWalking())
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
