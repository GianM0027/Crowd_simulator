import models.*;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
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

    public Animation(JPanel panel, List<Pedestrian> crowd, List<Obstacle> obstacles, List<WayPoint> wayPoints, List<Group> groups){
        this.parentPanel = panel;
        this.groups = groups;
        this.crowd = crowd;
        this.obstacles = obstacles;
        this.wayPoints = wayPoints;

        this.setPreferredSize(new Dimension(this.parentPanel.getWidth(), this.parentPanel.getHeight()));
        this.building = new Building(this.parentPanel.getWidth(), this.parentPanel.getHeight());

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
/*

        Point entranceWallUpUp = new Point(Constant.BUILDING_DISTANCE_LEFT, Constant.BUILDING_DISTANCE_UP_DOWN);
        Point entranceWallUpDown = new Point(Constant.BUILDING_DISTANCE_LEFT, this.getHeight()/2 - Constant.BUILDING_DOOR_SIZE/2 - Constant.BUILDING_DISTANCE_UP_DOWN);
        Point entranceWallDownUp = new Point(Constant.BUILDING_DISTANCE_LEFT, this.getHeight()/2 + Constant.BUILDING_DOOR_SIZE/2);
        Point entranceWallDownDown = new Point(Constant.BUILDING_DISTANCE_LEFT, this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - 1);
        Point exitWallUpUp = new Point(this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT, Constant.BUILDING_DISTANCE_UP_DOWN);
        Point exitWallUpDown = new Point(this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT, this.getHeight()/2 - Constant.BUILDING_DOOR_SIZE/2 - Constant.BUILDING_DISTANCE_UP_DOWN);
        Point exitWallDownUp = new Point(this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT,this.getHeight()/2 + Constant.BUILDING_DOOR_SIZE/2);
        Point exitWallDownDown = new Point(this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT, this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - 1);

        building.setEntranceUpUp(entranceWallUpUp);
        building.setEntranceUpDown(entranceWallUpDown);
        building.setEntranceDownUp(entranceWallDownUp);
        building.setEntranceDownDown(entranceWallDownDown);
        building.setExitUpUp(exitWallUpUp);
        building.setExitUpDown(exitWallUpDown);
        building.setExitDownUp(exitWallDownUp);
        building.setExitDownDown(exitWallDownDown);

        //draw builing
        g2D.drawLine(entranceWallUpUp.x, entranceWallUpUp.y, entranceWallUpDown.x, entranceWallUpDown.y);
        g2D.drawLine(entranceWallDownUp.x, entranceWallDownUp.y, entranceWallDownDown.x, entranceWallDownDown.y);
        g2D.drawLine(exitWallUpUp.x, exitWallUpUp.y, exitWallUpDown.x, exitWallUpDown.y);
        g2D.drawLine(exitWallDownUp.x, exitWallDownUp.y, exitWallDownDown.x, exitWallDownDown.y);
        g2D.drawLine(entranceWallUpUp.x, entranceWallUpUp.y, exitWallUpUp.x, exitWallUpUp.y);
        g2D.drawLine(entranceWallDownDown.x, entranceWallDownDown.y, exitWallDownDown.x, exitWallDownDown.y);
*/
    }

    private void drawEntities(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //draw obstacles
        g2D.setPaint(Color.BLACK);
        for (Obstacle obstacle : obstacles) {
            //entity
            g2D.fill(obstacle.getObstacleShape());

            /*   TEST BORDI RETTANGOLO
            g2D.fillRect(obstacle.getBounds().getBoundsRectangle().x, obstacle.getBounds().getBoundsRectangle().y,
                    obstacle.getBounds().getBoundsRectangle().width, obstacle.getBounds().getBoundsRectangle().height);

            System.out.println("Ostacolo: " + obstacle.getPositionString());
            System.out.println("Bounds: " + obstacle.getBounds().getUpLeft());*/
        }

        //draw way points
        g2D.setPaint(Color.red);
        for (WayPoint wayPoint : wayPoints) {
            g2D.fill(wayPoint.getWayPointShape());
        }

        //draw groups of pedestrians
        for (Group group : groups) {
            g2D.setPaint(group.getColor());

            for (int j = 0; j < group.getSizeGroup(); j++) {
                g2D.fill(group.getPedestrians().get(j).getPedestrianShape());
            }
        }
    }



    /******************************    Handle pedestrians' movement    *************************************/
    @Override
    public void actionPerformed(ActionEvent e) {
        Point2D nextPosition;

        for (Pedestrian pedestrian : crowd) {
            nextPosition = pedestrian.nextPosition(this);
            //nextPosition = pedestrian.pedestrianAvoidance(crowd, nextPosition);
            //nextPosition = pedestrian.obstacleAvoidance(obstacles, nextPosition);

            pedestrian.setPosition(nextPosition);
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
