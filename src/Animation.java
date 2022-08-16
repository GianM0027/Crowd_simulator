import models.*;
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

    private JPanel panel;
    private List<Pedestrian> crowd;
    private List<Group> groups;
    private List<Obstacle> obstacles;
    private List<WayPoint> wayPoints;
    private Building building;

    //simulation parameters
    private Timer timer;

    public Animation(JPanel panel, List<Pedestrian> crowd, List<Obstacle> obstacles, List<WayPoint> wayPoints, List<Group> groups){
        this.panel = panel;
        this.groups = groups;
        this.crowd = crowd;
        this.obstacles = obstacles;
        this.wayPoints = wayPoints;
        this.building = new Building();

        this.setPreferredSize(new Dimension(this.panel.getWidth(), this.panel.getHeight()));

        timer = new Timer(Constant.ANIMATION_DELAY, this);
        timer.start();
    }


    /*****************************   Paint background and fixed entities   ******************************/
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        //building points
        g2D.setStroke(new BasicStroke(Constant.BUILDING_STROKE));

        Point entranceWallUpUp = new Point(Constant.BUILDING_DISTANCE_LEFT, Constant.BUILDING_DISTANCE_UP_DOWN);
        Point entranceWallUpDown = new Point(Constant.BUILDING_DISTANCE_LEFT, this.panel.getHeight()/2 - 20 - Constant.BUILDING_DISTANCE_UP_DOWN);
        Point entranceWallDownUp = new Point(Constant.BUILDING_DISTANCE_LEFT, this.panel.getHeight()/2 + 20);
        Point entranceWallDownDown = new Point(Constant.BUILDING_DISTANCE_LEFT, this.panel.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - 1);
        Point exitWallUpUp = new Point(this.panel.getWidth() - Constant.BUILDING_DISTANCE_RIGHT, Constant.BUILDING_DISTANCE_UP_DOWN);
        Point exitWallUpDown = new Point(this.panel.getWidth() - Constant.BUILDING_DISTANCE_RIGHT, this.panel.getHeight()/2 - 20 - Constant.BUILDING_DISTANCE_UP_DOWN);
        Point exitWallDownUp = new Point(this.panel.getWidth() - Constant.BUILDING_DISTANCE_RIGHT,this.panel.getHeight()/2 + 20);
        Point exitWallDownDown = new Point(this.panel.getWidth() - Constant.BUILDING_DISTANCE_RIGHT, this.panel.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - 1);

        //draw builing
        g2D.drawLine(entranceWallUpUp.x, entranceWallUpUp.y, entranceWallUpDown.x, entranceWallUpDown.y);
        g2D.drawLine(entranceWallDownUp.x, entranceWallDownUp.y, entranceWallDownDown.x, entranceWallDownDown.y);
        g2D.drawLine(exitWallUpUp.x, exitWallUpUp.y, exitWallUpDown.x, exitWallUpDown.y);
        g2D.drawLine(exitWallDownUp.x, exitWallDownUp.y, exitWallDownDown.x, exitWallDownDown.y);
        g2D.drawLine(entranceWallUpUp.x, entranceWallUpUp.y, exitWallUpUp.x, exitWallUpUp.y);
        g2D.drawLine(entranceWallDownDown.x, entranceWallDownDown.y, exitWallDownDown.x, exitWallDownDown.y);

        g2D.setStroke(new BasicStroke(0));

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
        for(int i = 0; i < crowd.size(); i++){
            if(crowd.get(i).getBounds().getRight().x == panel.getWidth() - Constant.BUILDING_DISTANCE_RIGHT - 1){
                crowd.get(i).setxVelocity(-1);
                crowd.get(i).setPosition(new Point(crowd.get(i).getPosition().x + crowd.get(i).getxVelocity(), crowd.get(i).getPosition().y));
            }
            if(crowd.get(i).getBounds().getLeft().x == 0) {
                crowd.get(i).setxVelocity(1);
                crowd.get(i).setPosition(new Point(crowd.get(i).getPosition().x + crowd.get(i).getxVelocity(), crowd.get(i).getPosition().y));
            }

            crowd.get(i).setPosition(new Point(crowd.get(i).getPosition().x + crowd.get(i).getxVelocity(), crowd.get(i).getPosition().y));
        }


        this.removeAll();
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
