import models.*;
import support.Support;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

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
        this.building = new Building();

        this.setPreferredSize(new Dimension(this.parentPanel.getWidth(), this.parentPanel.getHeight()));

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

        //building points
        g2D.setStroke(new BasicStroke(Constant.BUILDING_STROKE));

        Point entranceWallUpUp = new Point(Constant.BUILDING_DISTANCE_LEFT, Constant.BUILDING_DISTANCE_UP_DOWN);
        Point entranceWallUpDown = new Point(Constant.BUILDING_DISTANCE_LEFT, this.getHeight()/2 - 20 - Constant.BUILDING_DISTANCE_UP_DOWN);
        Point entranceWallDownUp = new Point(Constant.BUILDING_DISTANCE_LEFT, this.getHeight()/2 + 20);
        Point entranceWallDownDown = new Point(Constant.BUILDING_DISTANCE_LEFT, this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - 1);
        Point exitWallUpUp = new Point(this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT, Constant.BUILDING_DISTANCE_UP_DOWN);
        Point exitWallUpDown = new Point(this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT, this.getHeight()/2 - 20 - Constant.BUILDING_DISTANCE_UP_DOWN);
        Point exitWallDownUp = new Point(this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT,this.getHeight()/2 + 20);
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

        g2D.setStroke(new BasicStroke(0));
    }

    private void drawEntities(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        //draw obstacles
        g2D.setPaint(Color.BLACK);
        for(int i = 0; i < obstacles.size(); i++){
            //entity
            g2D.fillOval(obstacles.get(i).getPosition().x, obstacles.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);
        }

        //draw way points
        g2D.setPaint(Color.red);
        for(int i = 0; i < wayPoints.size(); i++){
            g2D.fillOval(wayPoints.get(i).getPosition().x, wayPoints.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);
        }

        //draw groups of pedestrians
        for(int i = 0; i < groups.size(); i++){

            g2D.setPaint(groups.get(i).getColor());

            for(int j = 0; j < groups.get(i).getSizeGroup(); j++){
                g2D.fillOval(groups.get(i).getPedestrians().get(j).getPosition().x,
                        groups.get(i).getPedestrians().get(j).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);

                //bounds
                g2D.drawOval(groups.get(i).getPedestrians().get(j).getBounds().getUpLeft().x,
                        groups.get(i).getPedestrians().get(j).getBounds().getUpLeft().y,
                        groups.get(i).getPedestrians().get(j).getBounds().getWidth(),
                        groups.get(i).getPedestrians().get(j).getBounds().getHeight());
            }
        }
    }



    /******************************    Handle pedestrians' movement    *************************************/
    @Override
    public void actionPerformed(ActionEvent e) {

        //random animation for testing the panel
        for (Pedestrian pedestrian : crowd) {
            Point goalPosition;

            if(!pedestrian.getGoalsList().isEmpty())
                goalPosition = pedestrian.getGoalsList().get(0).getPosition();
            else
                goalPosition = new Point(this.getWidth() + 20, this.getHeight()/2);

            if (goalPosition.x > pedestrian.getPosition().x) {
                pedestrian.setxVelocity(1);
            }
            else
                pedestrian.setxVelocity(-1);
            if (goalPosition.y > pedestrian.getPosition().y) {
                pedestrian.setyVelocity(1);
            }
            else
                pedestrian.setyVelocity(-1);

            if(Support.distance(pedestrian.getPosition(), goalPosition) < Constant.ENTITY_SIZE){
                pedestrian.setxVelocity(0);
                pedestrian.setyVelocity(0);
                pedestrian.getGoalsList().remove(0);
            }

            if(pedestrian.getPosition().x > this.getWidth()){
                pedestrian.setxVelocity(0);
                pedestrian.setyVelocity(0);
                pedestrian.getGoalsList().add(new WayPoint(new Point()));
            }

            pedestrian.setPosition(new Point(pedestrian.getPosition().x + pedestrian.getxVelocity(), pedestrian.getPosition().y + pedestrian.getyVelocity()));
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
