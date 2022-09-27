package models;

import support.EntityBound;
import support.constants.Constant;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Room{

    private Rectangle2D roomRectangle;

    private Door door;

    private Line2D leftWall;
    private Line2D rightWall;
    private Line2D doorWallLeft;
    private Line2D doorWallRight;

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

        door = new Door(doorWallLeft.getP2(), doorWallRight.getP2());

        roomRectangle = new Rectangle2D.Double(roomPositionX, roomPositionY, width, height);
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

    public Door getDoor() {
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

    public Rectangle2D getRoomRectangle() {
        return roomRectangle;
    }
    public Rectangle2D getDoorFreeSpace() {
        return new Rectangle2D.Double(door.getPosition().getX() - 20, door.getPosition().getY() - 20, Constant.BUILDING_DOOR_SIZE + 20, 40);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomRectangle, room.roomRectangle) && Objects.equals(door, room.door) && Objects.equals(leftWall, room.leftWall) && Objects.equals(rightWall, room.rightWall) && Objects.equals(doorWallLeft, room.doorWallLeft) && Objects.equals(doorWallRight, room.doorWallRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomRectangle, door, leftWall, rightWall, doorWallLeft, doorWallRight);
    }
}
