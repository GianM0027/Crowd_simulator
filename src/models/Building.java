package models;

import support.EntityBound;
import support.Support;
import support.constants.Constant;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Building {
    private double width;
    private double height;

    private List<Room> rooms;
    private Rectangle2D hall;
    private Door entrance;
    private Door exit;
    private Line2D entranceUpWall;
    private Line2D entranceBottomWall;
    private Line2D exitUpWall;
    private Line2D exitBottomWall;
    private Line2D upWall;
    private Line2D bottomWall;

    private List<Rectangle2D> wallsList;

    List<WayPoint> wayPoints;



    public Building(int panelWidth, int panelHeight){
        //dimension of the external structure
        width = panelWidth - Constant.BUILDING_DISTANCE_LEFT - Constant.BUILDING_DISTANCE_RIGHT;
        height = panelHeight - Constant.BUILDING_DISTANCE_UP_DOWN - 1;

        //points of the external structure
        Point2D entranceWallUpUp = new Point2D.Double(Constant.BUILDING_DISTANCE_LEFT, Constant.BUILDING_DISTANCE_UP_DOWN);
        Point2D entranceWallUpDown = new Point2D.Double(Constant.BUILDING_DISTANCE_LEFT, panelHeight/2 - Constant.BUILDING_DOOR_SIZE/2 - Constant.BUILDING_DISTANCE_UP_DOWN);
        Point2D entranceWallDownUp = new Point2D.Double(Constant.BUILDING_DISTANCE_LEFT, panelHeight/2 + Constant.BUILDING_DOOR_SIZE/2);
        Point2D entranceWallDownDown = new Point2D.Double(Constant.BUILDING_DISTANCE_LEFT, panelHeight - Constant.BUILDING_DISTANCE_UP_DOWN - 1);
        Point2D exitWallUpUp = new Point2D.Double(panelWidth - Constant.BUILDING_DISTANCE_RIGHT, Constant.BUILDING_DISTANCE_UP_DOWN);
        Point2D exitWallUpDown = new Point2D.Double(panelWidth - Constant.BUILDING_DISTANCE_RIGHT, panelHeight/2 - Constant.BUILDING_DOOR_SIZE/2 - Constant.BUILDING_DISTANCE_UP_DOWN);
        Point2D exitWallDownUp = new Point2D.Double(panelWidth - Constant.BUILDING_DISTANCE_RIGHT,panelHeight/2 + Constant.BUILDING_DOOR_SIZE/2);
        Point2D exitWallDownDown = new Point2D.Double(panelWidth - Constant.BUILDING_DISTANCE_RIGHT, panelHeight - Constant.BUILDING_DISTANCE_UP_DOWN - 1);

        //external structure with entrance and exit
        entranceUpWall = new Line2D.Double(entranceWallUpUp, entranceWallUpDown);
        entranceBottomWall = new Line2D.Double(entranceWallDownUp, entranceWallDownDown);
        exitUpWall = new Line2D.Double(exitWallUpUp, exitWallUpDown);
        exitBottomWall = new Line2D.Double(exitWallDownUp, exitWallDownDown);
        upWall = new Line2D.Double(entranceWallUpUp, exitWallUpUp);
        bottomWall = new Line2D.Double(entranceWallDownDown, exitWallDownDown);

        entrance = new Door(entranceWallUpDown, entranceWallDownUp, null);
        exit = new Door(exitWallUpDown, exitWallDownUp, null);

        //obtaining rectangles from walls
        buildWallsRectangles();

        //rooms of the building (up and down)
        rooms = new ArrayList<>();

        int upperRoomsNum = Support.getRandomValue(3, 6);
        Point2D roomPosition = new Point2D.Double(entranceUpWall.getP1().getX(), entranceUpWall.getP1().getY());
        for(int i = 0; i < upperRoomsNum; i++){
            rooms.add(new Room(roomPosition.getX() + ((width/upperRoomsNum)*i), roomPosition.getY(), width/upperRoomsNum, height/3));
        }

        int bottomRoomsNum = Support.getRandomValue(3, 6);
        roomPosition = new Point2D.Double(entranceUpWall.getX1(), height - height/3);
        for(int i = 0; i < bottomRoomsNum; i++){
            rooms.add(new Room(roomPosition.getX() + ((width/bottomRoomsNum)*i), roomPosition.getY(), width/bottomRoomsNum, height/3));
        }

        //hall of the building
        hall = new Rectangle2D.Double(Constant.BUILDING_DISTANCE_LEFT - 10, Constant.BUILDING_DISTANCE_UP_DOWN + rooms.get(0).getRoomRectangle().getHeight(),
                width + 20, height - height/3*2);
    }

    /**
     * Draw the external walls of the building (not the rooms)
     * */
    public void drawExternalArea(Graphics2D g2D){
        g2D.draw(entranceUpWall);
        g2D.draw(entranceBottomWall);
        g2D.draw(exitUpWall);
        g2D.draw(exitBottomWall);
        g2D.draw(upWall);
        g2D.draw(bottomWall);
    }


    /**
     * Return true if an entity is closer to the building more than the "bounds distance"
     * */
    public boolean distanceIsEnough(Entity entity){
        EntityBound entityBounds = new EntityBound(entity);
        int distance = Constant.GOAL_DISTANCE*2 + Constant.PEDESTRIAN_HEIGHT;

            if (entranceUpWall.ptLineDist(entityBounds.getCenter()) < distance)
                return false;
            if (entranceBottomWall.ptLineDist(entityBounds.getCenter()) < distance)
                return false;
            if (exitUpWall.ptLineDist(entityBounds.getCenter()) < distance)
                return false;
            if (exitBottomWall.ptLineDist(entityBounds.getCenter()) < distance)
                return false;
            if (upWall.ptLineDist(entityBounds.getCenter()) < distance)
                return false;
            if (bottomWall.ptLineDist(entityBounds.getCenter()) < distance)
                return false;


        for(Room room: this.rooms) {
            if(!room.distanceIsEnough(entity, distance))
                return false;
        }
        return true;
    }


    /**
     * Return an Array with the points of the pedestrian that collided with a wall
     * */
    public List<Integer> collisionPoints(Pedestrian p){
        EntityBound pBounds = new EntityBound(p);

        List<Integer> pointsCollision = new ArrayList<>();

        //check onn building walls
        for(Rectangle2D r : this.wallsList){

            //check if pedestrian collided on the wall
            if(pBounds.getBoundsRectangle().intersects(r)){
                //if there is a collision find which point of the pedestrian collided
                for(Point2D point : pBounds.getBorderPoints()){

                    if(r.contains(point) && point.equals(pBounds.getRight()))
                        pointsCollision.add(Constant.RIGHT); //collided the right part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getLeft()))
                        pointsCollision.add(Constant.LEFT); //collided the left part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getBottom()))
                        pointsCollision.add(Constant.BOTTOM); //collided the bottom part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getUp()))
                        pointsCollision.add(Constant.UP); //collided the upper part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getUpRight()))
                        pointsCollision.add(Constant.UP_RIGHT); //collided the upper-right part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getUpLeft()))
                        pointsCollision.add(Constant.UP_LEFT); //collided the upper-left part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getBottomLeft()))
                        pointsCollision.add(Constant.BOTTOM_LEFT); //collided the bottom-left part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getBottomRight()))
                        pointsCollision.add(Constant.BOTTOM_RIGHT); //collided the bottom-right part of the pedestrian
                }
            }
        }

        //check on rooms' walls
        for(Room room : rooms)
            room.pointsCollision(pBounds, pointsCollision);

        return pointsCollision;
    }


    /**
     * Return true if a pedestrian is closer to the building more than the minimum safety distance
     * */
    public boolean checkCollisionOnDoorFreeSpace(Entity entity){
        EntityBound entityBounds = new EntityBound(entity);

        for(Room room: this.rooms)
            if(room.getDoorFreeSpace().intersects(entityBounds.getBoundsRectangle()))
                return true;

        if(new Rectangle2D.Double(this.entrance.getPosition().getX(), this.entrance.getPosition().getY() - 10, this.width, Constant.BUILDING_DOOR_SIZE + 20).intersects(entityBounds.getBoundsRectangle()))
            return true;

        return false;
    }

    private void buildWallsRectangles(){
        wallsList = new ArrayList<>();

        wallsList.add(new Rectangle2D.Double(entranceUpWall.getX1()-3, entranceUpWall.getY1()-3, Constant.BUILDING_STROKE, entranceUpWall.getY2()-entranceUpWall.getY1() + 5));
        wallsList.add(new Rectangle2D.Double(entranceBottomWall.getX1()-3, entranceBottomWall.getY1()-2, Constant.BUILDING_STROKE, entranceBottomWall.getY2()-entranceBottomWall.getY1() + 5));
        wallsList.add(new Rectangle2D.Double(exitUpWall.getX1()-3, exitUpWall.getY1()-3, Constant.BUILDING_STROKE, exitUpWall.getY2()-exitUpWall.getY1() + 5));
        wallsList.add(new Rectangle2D.Double(exitBottomWall.getX1()-3, exitBottomWall.getY1()-2, Constant.BUILDING_STROKE, exitBottomWall.getY2()-exitBottomWall.getY1() + 5));
        wallsList.add(new Rectangle2D.Double(upWall.getX1()-3, upWall.getY1()-3, upWall.getX2()-upWall.getX1(), Constant.BUILDING_STROKE));
        wallsList.add(new Rectangle2D.Double(bottomWall.getX1()-3, bottomWall.getY1()-3, bottomWall.getX2()-bottomWall.getX1(), Constant.BUILDING_STROKE));

    }

    public Line2D getEntranceUpWall() {
        return entranceUpWall;
    }

    public Line2D getEntranceBottomWall() {
        return entranceBottomWall;
    }

    public Line2D getExitUpWall() {
        return exitUpWall;
    }

    public Line2D getExitBottomWall() {
        return exitBottomWall;
    }

    public Line2D getUpWall() {
        return upWall;
    }

    public Line2D getBottomWall() {
        return bottomWall;
    }

    public List<Rectangle2D> getWallsList() {
        return wallsList;
    }

    public Door getEntrance() {
        return entrance;
    }

    public Door getExit() {
        return exit;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Rectangle2D getHall() {
        return hall;
    }

    public void setWayPoints(List<WayPoint> wayPoints){
        this.wayPoints = wayPoints;
        for(Room room : rooms)
            room.setWayPoints(wayPoints);
    }
}