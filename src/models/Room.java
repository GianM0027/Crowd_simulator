package models;

import support.EntityBound;
import support.constants.Constant;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Room{

    private Line2D door;

    private Line2D leftWall;
    private Line2D rightWall;
    private Line2D doorWallLeft;
    private Line2D doorWallRight;

    public Room(Point2D roomPosition, double width, double height){
        leftWall = new Line2D.Double(roomPosition.getX(), roomPosition.getY(), roomPosition.getX(), roomPosition.getY() + height);
        rightWall = new Line2D.Double(roomPosition.getX() + width, roomPosition.getY(), roomPosition.getX() + width, roomPosition.getY() + height);

        //if the room is on the upper part of the building
        if(roomPosition.getY() <= Constant.BUILDING_DISTANCE_UP_DOWN*2){
            doorWallLeft = new Line2D.Double(roomPosition.getX(), roomPosition.getY() + height, roomPosition.getX() + width/2 - Constant.BUILDING_DOOR_SIZE/2d, roomPosition.getY() + height);
            doorWallRight = new Line2D.Double(roomPosition.getX() + width/2 + Constant.BUILDING_DOOR_SIZE/2d, roomPosition.getY() + height, roomPosition.getX() + width, roomPosition.getY() + height);
        }
        //else the room is in the bottom part of the building
        else{
            doorWallLeft = new Line2D.Double(roomPosition.getX(), roomPosition.getY(), roomPosition.getX() + width/2 - Constant.BUILDING_DOOR_SIZE/2d, roomPosition.getY());
            doorWallRight = new Line2D.Double(roomPosition.getX() + width/2 + Constant.BUILDING_DOOR_SIZE/2d, roomPosition.getY(), roomPosition.getX() + width, roomPosition.getY());
        }
    }

    public Room(double roomPositionX, double roomPositionY, double width, double height){
        leftWall = new Line2D.Double(roomPositionX, roomPositionY, roomPositionX, roomPositionY + height);
        rightWall = new Line2D.Double(roomPositionX + width, roomPositionY, roomPositionX + width, roomPositionY + height);

        //if the room is on the upper part of the building
        if(roomPositionY <= Constant.BUILDING_DISTANCE_UP_DOWN*2){
            doorWallLeft = new Line2D.Double(roomPositionX, roomPositionY + height, roomPositionX + width/2 - Constant.BUILDING_DOOR_SIZE/2d, roomPositionY + height);
            doorWallRight = new Line2D.Double(roomPositionX + width, roomPositionY + height, roomPositionX + width/2 + Constant.BUILDING_DOOR_SIZE/2d, roomPositionY + height);
        }
        //else the room is in the bottom part of the building
        else{
            doorWallLeft = new Line2D.Double(roomPositionX, roomPositionY, roomPositionX + width/2 - Constant.BUILDING_DOOR_SIZE/2d, roomPositionY);
            doorWallRight = new Line2D.Double(roomPositionX + width, roomPositionY, roomPositionX + width/2 + Constant.BUILDING_DOOR_SIZE/2d, roomPositionY);
        }
    }

    public boolean checkCollision(Entity entity){
        EntityBound entityBounds = new EntityBound(entity);

        return entityBounds.getBoundsRectangle().intersectsLine(leftWall) || entityBounds.getBoundsRectangle().intersectsLine(rightWall) ||
                entityBounds.getBoundsRectangle().intersectsLine(doorWallLeft) || entityBounds.getBoundsRectangle().intersectsLine(doorWallRight);
    }

    public void drawRoom(Graphics2D g2D){
        g2D.draw(leftWall);
        g2D.draw(rightWall);
        g2D.draw(doorWallLeft);
        g2D.draw(doorWallRight);
    }

    public Line2D getDoor() {
        return door;
    }

    public Line2D getLeftWall() {
        return leftWall;
    }

    public Line2D getRightWall() {
        return rightWall;
    }

    public Line2D getDoorWallLeft() {
        return doorWallLeft;
    }

    public Line2D getDoorWallRight() {
        return doorWallRight;
    }
}
