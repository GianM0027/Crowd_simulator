import models.Crowd;
import models.Obstacle;
import models.WayPoint;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Implements the movement and the printing functions of the simulation
 * */
public class Animation extends JPanel {

    private JPanel panel;
    private Crowd crowd;
    private List<Obstacle> obstacles;
    private List<WayPoint> wayPoints;

    public Animation(JPanel panel, Crowd crowd, List<Obstacle> obstacles, List<WayPoint> wayPoints){
        this.panel = panel;
        this.crowd = crowd;
        this.obstacles = obstacles;
        this.wayPoints = wayPoints;

        this.setPreferredSize(new Dimension(this.panel.getWidth(), this.panel.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        //draw obstacles
        for(int i = 0; i < obstacles.size(); i++){
            g2D.setPaint(Color.BLACK);
            g2D.fillRect(obstacles.get(i).getPosition().x, obstacles.get(i).getPosition().y, 10, 10);
        }

        //draw way points
        for(int i = 0; i < wayPoints.size(); i++){
            g2D.setPaint(Color.red);
            g2D.fillOval(wayPoints.get(i).getPosition().x, wayPoints.get(i).getPosition().y, 10, 10);
        }

    }
}
