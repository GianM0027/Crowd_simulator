package models;

import processing.core.PVector;
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


        entrance = new Door(entranceWallUpDown, entranceWallDownUp);
        exit = new Door(exitWallUpDown, exitWallDownUp);

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
    public boolean checkCollision(Entity entity){
        EntityBound entityBounds = new EntityBound(entity);

        if(entityBounds.getBoundsRectangle().intersectsLine(entranceUpWall) || entityBounds.getBoundsRectangle().intersectsLine(entranceBottomWall) ||
                entityBounds.getBoundsRectangle().intersectsLine(exitUpWall) || entityBounds.getBoundsRectangle().intersectsLine(exitBottomWall) ||
                entityBounds.getBoundsRectangle().intersectsLine(upWall) || entityBounds.getBoundsRectangle().intersectsLine(bottomWall))
            return true;

        for(Room room: this.rooms)
            if(room.checkCollision(entity))
                return true;

        return false;
    }

    /**
     * Return true if an entity is closer to the building more than the "bounds distance"
     * */
    public boolean checkFutureCollision(Pedestrian pedestrian){
        Pedestrian temp = new Pedestrian(pedestrian.getPosition(), -1, this);

        if(temp.getEntityBounds().intersectsLine(entranceUpWall) || temp.getEntityBounds().intersectsLine(entranceBottomWall) ||
                temp.getEntityBounds().intersectsLine(exitUpWall) || temp.getEntityBounds().intersectsLine(exitBottomWall) ||
                temp.getEntityBounds().intersectsLine(upWall) || temp.getEntityBounds().intersectsLine(bottomWall))
            return true;

        for(Room room: this.rooms)
            if(room.checkCollision(pedestrian))
                return true;

        return false;
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
}