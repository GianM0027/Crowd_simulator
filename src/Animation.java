import models.Group;
import models.Obstacle;
import models.Pedestrian;
import models.WayPoint;
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

    //simulation parameters
    private Timer timer;

    public Animation(JPanel panel, List<Pedestrian> crowd, List<Obstacle> obstacles, List<WayPoint> wayPoints, List<Group> groups){
        this.panel = panel;
        this.groups = groups;
        this.crowd = crowd;
        this.obstacles = obstacles;
        this.wayPoints = wayPoints;

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

        //draw the building
        g2D.setStroke(new BasicStroke(Constant.BUILDING_STROKE));

        g2D.drawLine(Constant.BUILDING_DISTANCE_LEFT, Constant.BUILDING_DISTANCE_UP_DOWN, Constant.BUILDING_DISTANCE_LEFT, this.getHeight()/2 - 20);
        g2D.drawLine(Constant.BUILDING_DISTANCE_LEFT,this.getHeight()/2 + 20, Constant.BUILDING_DISTANCE_LEFT, this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - 1);

        g2D.drawLine(this.getWidth() - 1,0, this.getWidth() - 4, this.getHeight()/2 - 10);
        g2D.drawLine(this.getWidth() - 1,this.getHeight()/2 + 10, this.getWidth() - 4, this.getHeight() - 1);

        g2D.setStroke(new BasicStroke(0));


        //draw obstacles
        g2D.setPaint(Color.BLACK);
        for(int i = 0; i < obstacles.size(); i++){
            //entity
            g2D.fillOval(obstacles.get(i).getPosition().x, obstacles.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);

            //bounds
            g2D.drawOval(obstacles.get(i).getBounds().getCenter().x,obstacles.get(i).getBounds().getCenter().y, 2, 2);
            g2D.drawOval(obstacles.get(i).getBounds().getUpLeft().x,obstacles.get(i).getBounds().getUpLeft().y, 2, 2);
            g2D.drawOval(obstacles.get(i).getBounds().getLeft().x,obstacles.get(i).getBounds().getLeft().y, 2, 2);
            g2D.drawOval(obstacles.get(i).getBounds().getBottomLeft().x,obstacles.get(i).getBounds().getBottomLeft().y, 2, 2);
            g2D.drawOval(obstacles.get(i).getBounds().getUp().x,obstacles.get(i).getBounds().getUp().y, 2, 2);
            g2D.drawOval(obstacles.get(i).getBounds().getBottom().x,obstacles.get(i).getBounds().getBottom().y, 2, 2);
            g2D.drawOval(obstacles.get(i).getBounds().getUpRight().x,obstacles.get(i).getBounds().getUpRight().y, 2, 2);
            g2D.drawOval(obstacles.get(i).getBounds().getRight().x,obstacles.get(i).getBounds().getRight().y, 2, 2);
            g2D.drawOval(obstacles.get(i).getBounds().getBottomRight().x,obstacles.get(i).getBounds().getBottomRight().y, 2, 2);
        }

        //draw way points
        g2D.setPaint(Color.red);
        for(int i = 0; i < wayPoints.size(); i++){
            g2D.fillOval(wayPoints.get(i).getPosition().x, wayPoints.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);

            //bounds
            g2D.drawOval(wayPoints.get(i).getBounds().getCenter().x,wayPoints.get(i).getBounds().getCenter().y, 2, 2);
            g2D.drawOval(wayPoints.get(i).getBounds().getUpLeft().x,wayPoints.get(i).getBounds().getUpLeft().y, 2, 2);
            g2D.drawOval(wayPoints.get(i).getBounds().getLeft().x,wayPoints.get(i).getBounds().getLeft().y, 2, 2);
            g2D.drawOval(wayPoints.get(i).getBounds().getBottomLeft().x,wayPoints.get(i).getBounds().getBottomLeft().y, 2, 2);
            g2D.drawOval(wayPoints.get(i).getBounds().getUp().x,wayPoints.get(i).getBounds().getUp().y, 2, 2);
            g2D.drawOval(wayPoints.get(i).getBounds().getBottom().x,wayPoints.get(i).getBounds().getBottom().y, 2, 2);
            g2D.drawOval(wayPoints.get(i).getBounds().getUpRight().x,wayPoints.get(i).getBounds().getUpRight().y, 2, 2);
            g2D.drawOval(wayPoints.get(i).getBounds().getRight().x,wayPoints.get(i).getBounds().getRight().y, 2, 2);
            g2D.drawOval(wayPoints.get(i).getBounds().getBottomRight().x,wayPoints.get(i).getBounds().getBottomRight().y, 2, 2);
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
            if(crowd.get(i).getBounds().getRight().x == panel.getWidth()-1){
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
