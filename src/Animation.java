import models.Obstacle;
import models.Pedestrian;
import models.WayPoint;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Implements the movement and the printing functions of the simulation
 * */
public class Animation extends JPanel {

    private JPanel panel;
    private List<Pedestrian> crowd;
    private List<Obstacle> obstacles;
    private List<WayPoint> wayPoints;

    public Animation(JPanel panel, List<Pedestrian> crowd, List<Obstacle> obstacles, List<WayPoint> wayPoints){
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
        g2D.setPaint(Color.BLACK);
        for(int i = 0; i < obstacles.size(); i++){
            g2D.fillRect(obstacles.get(i).getPosition().x, obstacles.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);
            g2D.drawRoundRect((int)obstacles.get(i).getPosition().getX() - Constant.BOUNDS_DISTANCE,
                    (int)obstacles.get(i).getPosition().getY() - Constant.BOUNDS_DISTANCE,
                    obstacles.get(i).getBounds().getWidth(), obstacles.get(i).getBounds().getHeight(),
                    obstacles.get(i).getBounds().getWidth()/4,obstacles.get(i).getBounds().getHeight()/4);
        }

        //draw way points
        g2D.setPaint(Color.red);
        for(int i = 0; i < wayPoints.size(); i++){
            g2D.fillOval(wayPoints.get(i).getPosition().x, wayPoints.get(i).getPosition().y, Constant.ENTITY_SIZE, Constant.ENTITY_SIZE);
        }
    }
}
