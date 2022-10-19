package models;

import support.EntityBound;
import support.constants.Constant;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room{

    private Rectangle2D roomRectangle;

    private Door door;
    private Door frontDoorOut;
    private Door frontDoorIn;

    private List<WayPoint> wayPoints;
    private Line2D leftWall;
    private Line2D rightWall;
    private Line2D doorWallLeft;
    private Line2D doorWallRight;

    private List<Rectangle2D> wallsList;

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

        door = new Door(doorWallLeft.getP2(), doorWallRight.getP2(), this);
        if(roomPositionY <= Constant.BUILDING_DISTANCE_UP_DOWN*2)
            setFrontDoorsRoomUp();
        else
            setFrontDoorsRoomDown();

        createWallsList();

        roomRectangle = new Rectangle2D.Double(roomPositionX, roomPositionY, width, height);
    }

    private void createWallsList(){
        this.wallsList = new ArrayList<>();
        wallsList.add(new Rectangle2D.Double(leftWall.getX1()-3, leftWall.getY1()-3, Constant.BUILDING_STROKE, leftWall.getY2()-leftWall.getY1()));
        wallsList.add(new Rectangle2D.Double(rightWall.getX1()-3, rightWall.getY1()-3, Constant.BUILDING_STROKE, rightWall.getY2()-rightWall.getY1()));
        wallsList.add(new Rectangle2D.Double(doorWallLeft.getX1()-3, doorWallLeft.getY1()-3, doorWallLeft.getX2()-doorWallLeft.getX1()+5, Constant.BUILDING_STROKE));
        wallsList.add(new Rectangle2D.Double(doorWallRight.getX2()-3, doorWallRight.getY2()-3, doorWallRight.getX1()-doorWallRight.getX2()+5, Constant.BUILDING_STROKE));
    }

    public Line2D checkCollision(Entity entity){
        Rectangle2D entityBoundsRec = new EntityBound(entity).getBoundsRectangle();

        if(!entityBoundsRec.intersects(door.getShape())) {
            if (entityBoundsRec.intersectsLine(leftWall))
                return leftWall;
            if (entityBoundsRec.intersectsLine(rightWall))
                return rightWall;
            if (entityBoundsRec.intersectsLine(doorWallLeft))
                return doorWallLeft;
            if (entityBoundsRec.intersectsLine(doorWallRight))
                return doorWallRight;
        }

        return null;
    }

    public void pointsCollision(EntityBound pBounds, List<Integer> collisionPoints){

        for(Rectangle2D r : this.wallsList){
            //check if pedestrian collided on the wall
            if(pBounds.getBoundsRectangle().intersects(r)){
                //if there is a collision find which point of the pedestrian collided
                for(Point2D point : pBounds.getBorderPoints()){

                    if(r.contains(point) && point.equals(pBounds.getRight()))
                        collisionPoints.add(Constant.RIGHT); //collided the right part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getLeft()))
                        collisionPoints.add(Constant.LEFT); //collided the left part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getBottom()))
                        collisionPoints.add(Constant.BOTTOM); //collided the bottom part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getUp()))
                        collisionPoints.add(Constant.UP); //collided the upper part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getUpRight()))
                        collisionPoints.add(Constant.UP_RIGHT); //collided the upper-right part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getUpLeft()))
                        collisionPoints.add(Constant.UP_LEFT); //collided the upper-left part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getBottomLeft()))
                        collisionPoints.add(Constant.BOTTOM_LEFT); //collided the bottom-left part of the pedestrian

                    if(r.contains(point) && point.equals(pBounds.getBottomRight()))
                        collisionPoints.add(Constant.BOTTOM_RIGHT); //collided the bottom-right part of the pedestrian
                }
            }
        }
    }


    public void drawRoom(Graphics2D g2D){
        g2D.draw(leftWall);
        g2D.draw(rightWall);
        g2D.draw(doorWallLeft);
        g2D.draw(doorWallRight);
    }

    public boolean distanceIsEnough(Entity entity){
        EntityBound entityBounds = new EntityBound(entity);

        if (leftWall.ptLineDist(entityBounds.getCenter()) < Constant.GOAL_DISTANCE + Constant.PEDESTRIAN_HEIGHT)
            return false;
        if (rightWall.ptLineDist(entityBounds.getCenter()) < Constant.GOAL_DISTANCE + Constant.PEDESTRIAN_HEIGHT)
            return false;
        if (doorWallLeft.ptLineDist(entityBounds.getCenter()) < Constant.GOAL_DISTANCE + Constant.PEDESTRIAN_HEIGHT)
            return false;
        if (doorWallRight.ptLineDist(entityBounds.getCenter()) < Constant.GOAL_DISTANCE + Constant.PEDESTRIAN_HEIGHT)
            return false;

        return true;
    }

    /**
     * true if in this room there are waypoints currently contained also in the goal list of the parameter pedestrian
     * */
    public boolean hasWaypoints(Pedestrian pedestrian){
        for(WayPoint w : pedestrian.getGoalsList())
            if(this.wayPoints.contains(w))
                return true;
        return false;
    }

    /**
     * true if this room contains the current goal of the pedestrian
     * */
    public boolean hasCurrentWaypoint(Pedestrian pedestrian){
        return this.wayPoints.contains(pedestrian.getCurrentGoal()) || this.wayPoints.contains(pedestrian.getGoalsList().get(0));
    }

    public Door getDoor() {
        return door;
    }

    public Door getFrontDoorOut() {
        return frontDoorOut;
    }

    public Door getFrontDoorIn() {
        return frontDoorIn;
    }

    public List<Rectangle2D> getWallsList() {
        return wallsList;
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
    public void setWayPoints(List<WayPoint> wayPoints){
        this.wayPoints = new ArrayList<>();
        for (WayPoint w : wayPoints){
            if(this.roomRectangle.contains(w.getPosition()))
                this.wayPoints.add(w);
        }
        for(WayPoint w : this.wayPoints)
            w.setRoom(this);
    }

    private void setFrontDoorsRoomUp(){
        Point2D positionOut = new Point2D.Double(this.door.getPosition().getX(), this.door.getPosition().getY() + (Constant.PEDESTRIAN_HEIGHT));  //-6
        Point2D positionIn = new Point2D.Double(this.door.getPosition().getX(), this.door.getPosition().getY() - (Constant.PEDESTRIAN_HEIGHT + 6));    //+3
        frontDoorOut = new Door(positionOut, this);
        frontDoorIn = new Door(positionIn, this);
    }

    private void setFrontDoorsRoomDown(){
        Point2D positionOut = new Point2D.Double(this.door.getPosition().getX(), this.door.getPosition().getY() - (Constant.PEDESTRIAN_HEIGHT + 6)); //+3
        Point2D positionIn = new Point2D.Double(this.door.getPosition().getX(), this.door.getPosition().getY() + (Constant.PEDESTRIAN_HEIGHT));  //-6
        frontDoorOut = new Door(positionOut, this);
        frontDoorIn = new Door(positionIn, this);
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
