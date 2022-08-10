import models.Obstacle;
import models.Pedestrian;
import models.WayPoint;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Implements the movement and the printing functions of the simulation
 * */
public class Animation extends JPanel implements ActionListener {

    private JPanel panel;
    private List<Pedestrian> crowd;
    private List<Obstacle> obstacles;
    private List<WayPoint> wayPoints;
    private Timer timer;

    public Animation(JPanel panel, List<Pedestrian> crowd, List<Obstacle> obstacles, List<WayPoint> wayPoints){
        this.panel = panel;
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

        //draw obstacles
        g2D.setPaint(Color.BLACK);
        for(int i = 0; i < obstacles.size(); i++){
            //entity
            g2D.fillOval(obstacles.get(i).getPosition().x, obstacles.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);

            //bounds
            g2D.drawOval(obstacles.get(i).getBounds().getUpLeft().x, obstacles.get(i).getBounds().getLeft().y,
                    obstacles.get(i).getBounds().getWidth(), obstacles.get(i).getBounds().getHeight());
        }

        //draw way points
        g2D.setPaint(Color.red);
        for(int i = 0; i < wayPoints.size(); i++){
            g2D.fillOval(wayPoints.get(i).getPosition().x, wayPoints.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);

            //bounds
            g2D.drawOval(wayPoints.get(i).getBounds().getUpLeft().x, wayPoints.get(i).getBounds().getLeft().y,
                    wayPoints.get(i).getBounds().getWidth(), wayPoints.get(i).getBounds().getHeight());
        }

        //draw pedestrians
        g2D.setPaint(Color.blue);
        for(int i = 0; i < crowd.size(); i++){
            g2D.fillOval(crowd.get(i).getPosition().x, crowd.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);

            //bounds
            g2D.drawOval(crowd.get(i).getBounds().getUpLeft().x, crowd.get(i).getBounds().getLeft().y,
                    crowd.get(i).getBounds().getWidth(), crowd.get(i).getBounds().getHeight());
        }
    }



    /******************************    Handle pedestrians' movement    *************************************/
    @Override
    public void actionPerformed(ActionEvent e) {
        int xVelocity = 1;
        int yVelocity = 1;

        //random animation for testing the panel
        for(int i = 0; i < crowd.size(); i++){
            crowd.get(i).setPosition(new Point(crowd.get(i).getPosition().x + xVelocity, crowd.get(i).getPosition().y));
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
