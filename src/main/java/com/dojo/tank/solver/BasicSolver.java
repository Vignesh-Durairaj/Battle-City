package com.dojo.tank.solver;

import static clientlib.Action.AFTER_TURN;
import static clientlib.Action.BEFORE_TURN;
import static clientlib.Elements.AI_TANK_DOWN;
import static clientlib.Elements.AI_TANK_LEFT;
import static clientlib.Elements.AI_TANK_RIGHT;
import static clientlib.Elements.AI_TANK_UP;
import static clientlib.Elements.BULLET;
import static clientlib.Elements.OTHER_TANK_DOWN;
import static clientlib.Elements.OTHER_TANK_LEFT;
import static clientlib.Elements.OTHER_TANK_RIGHT;
import static clientlib.Elements.OTHER_TANK_UP;
import static com.dojo.tank.enums.Direction.DOWN;
import static com.dojo.tank.enums.Direction.LEFT;
import static com.dojo.tank.enums.Direction.RIGHT;
import static com.dojo.tank.enums.Direction.UP;
import static java.lang.Math.abs;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.dojo.tank.enums.Direction;

import clientlib.Elements;
import clientlib.Point;
import clientlib.SampleSolver;

public class BasicSolver extends SampleSolver {

	private static int HORIZONTAL_DISTANCE_THRESHOLD = 5;
	private static int VERTICAL_DISTANCE_THRESHOLD = 12;
	private static int MAX_DiSTANCE_THRESHOLD = 15;
	private static final Elements[] ALL_OTHER_TANKS = new Elements[] {
			OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT,
            AI_TANK_UP, AI_TANK_RIGHT, AI_TANK_DOWN, AI_TANK_LEFT};

	int counter = 1;
	int struckFor = 0;
	Point prevPos;
	boolean freshStart = true;
	boolean gotStuck = false;
	
	@Override
	public String move() {
		
		// TODO: Work on this before the main event. 
		// It will be midnight in Shenzhen. 
		// You'll be damn dead, if you are got stuck in the shield wall square.
		
		Point myPos = getPlayerTankCoordinates().get(0); 
		if(counter <= 25 && gotStuck) {
			counter ++;
			return left(BEFORE_TURN);
		}
		
        System.out.println("Current Postion : " + myPos);
        String nextMove = null;
        
        nextMove = turnAndHitNearbyTank(myPos);
        if (nextMove != null) return nextMove;
        
        nextMove = guidedMove(myPos);
        if (nextMove != null) return nextMove;
        
        return act();
        
	}
	
	private String turnAndHitNearbyTank(Point myPos) {
		String action = null;
		
		if (isAnyOfAt(myPos.getX(), myPos.getY()-1, ALL_OTHER_TANKS)){
            System.out.println("Target is UP");
            action = up(AFTER_TURN);
        } else if (isAnyOfAt(myPos.getX()+1, myPos.getY(), ALL_OTHER_TANKS)){
            System.out.println("Target is RIGHT");
            action = right(AFTER_TURN);
        } else if (isAnyOfAt(myPos.getX(), myPos.getY()+1, ALL_OTHER_TANKS)){
            System.out.println("Target is DOWN");
            action = down(AFTER_TURN);
        } else if (isAnyOfAt(myPos.getX()-1, myPos.getY(), ALL_OTHER_TANKS)){
            System.out.println("Target is LEFT");
            action = left(AFTER_TURN);
        }
		
		return action;
	}
	
	private String guidedMove(final Point myPos) {
		// Get possible moves
		Set<Direction> directions = new HashSet<>(asList(UP, RIGHT, DOWN, LEFT));
	       
        if(isBarrierAt(myPos.getX(), myPos.getY() - 1) || 
        		isAt(myPos.getX(), myPos.getY() - 1, BULLET)){
        	directions.remove(UP);
        }
        
        if(isBarrierAt(myPos.getX() + 1, myPos.getY()) || 
        		isAt(myPos.getX() + 1, myPos.getY(), BULLET)){
        	directions.remove(RIGHT);
        }
        if(isBarrierAt(myPos.getX(), myPos.getY() + 1) || 
        		isAt(myPos.getX(), myPos.getY() + 1, BULLET)){
        	directions.remove(DOWN);
        }
        if(isBarrierAt(myPos.getX() - 1, myPos.getY()) || 
        		isAt(myPos.getX() - 1, myPos.getY(), BULLET)){
        	directions.remove(LEFT);
        }
           
       System.out.println("Instructions : " + directions);
       
        for (Point tankPos: getOtherPlayersTanks()) {
            for (int i = 1; i < 3; i++) {
                if (tankPos.getY() == myPos.getY()) {
                    if (tankPos.getX() + i == myPos.getX() && directions.contains(LEFT)) {
                        System.out.println("Enemy tank is LEFT : " + i);
                        return left(AFTER_TURN);
                    }
                    if (tankPos.getX() - i == myPos.getX() && directions.contains(RIGHT)) {
                        System.out.println("Enemy tank is RIGHT : " + i);
                        return right(AFTER_TURN);
                    }
                }
                if (tankPos.getX() == myPos.getX()) {
                    if (tankPos.getY() + i == myPos.getY() && directions.contains(UP)) {
                        System.out.println("Enemy tank is UP : " + i);
                        return up(AFTER_TURN);
                    }
                    if (tankPos.getY() - i == myPos.getY() && directions.contains(DOWN)) {
                        System.out.println("Enemy tank is DOWN : " + i);
                        return down(AFTER_TURN);
                    }
                }
            }
        }
        
        for (Point enemyTankPos: getOtherPlayersTanks()) {
            if (enemyTankPos.getY() == myPos.getY() && 
            		abs(enemyTankPos.getX() - myPos.getX()) <= VERTICAL_DISTANCE_THRESHOLD) {
                if (enemyTankPos.getX() < myPos.getX() && directions.contains(LEFT)) {
                    System.out.println("Enemy tank is LEFT : " + enemyTankPos);
                    return left(AFTER_TURN);
                }
                if (enemyTankPos.getX() > myPos.getX() && directions.contains(RIGHT)) {
                    System.out.println("Enemy tank is RIGHT : " + enemyTankPos);
                    return right(AFTER_TURN);
                }
            }
            
            if (enemyTankPos.getX() == myPos.getX() && 
            		abs(enemyTankPos.getY() - myPos.getY()) <= HORIZONTAL_DISTANCE_THRESHOLD) {
                if (enemyTankPos.getY() < myPos.getY() && directions.contains(UP)) {
                    System.out.println("Enemy tank is UP : " + enemyTankPos);
                    return up(AFTER_TURN);
                }
                if (enemyTankPos.getY() > myPos.getY() && directions.contains(DOWN)) {
                    System.out.println("Enemy tank is DOWN : " + enemyTankPos);
                    return down(AFTER_TURN);
                }
            }
        }
        
        // To get the next random movement. TODO: Make it smart
        
        Point nearestTank = getNearestTank(myPos);
        Direction newDirection = UP;
        if (nearestTank != null || getDistanceBetween(myPos, nearestTank) <= MAX_DiSTANCE_THRESHOLD) {
        	if (abs(nearestTank.getX() - myPos.getX()) <= abs(nearestTank.getY() - myPos.getY())) {
        		newDirection = myPos.getX() > nearestTank.getX() ? DOWN : UP;
        	} else {
        		newDirection = myPos.getY() > nearestTank.getY() ? LEFT : RIGHT;
        	} 
        	System.out.println("Chasing for the nearest tank pos : " + newDirection);
        } else {
        	List<Direction> availableDirections = new ArrayList<>();
            availableDirections.addAll(directions);
            newDirection = availableDirections.get(ThreadLocalRandom.current().nextInt(0, directions.size()));
            System.out.println("Getting into a new position : " + newDirection);
        }
        
        
        
        switch (newDirection){                                 
            case UP: return up(BEFORE_TURN);
            case RIGHT: return right(BEFORE_TURN);
            case DOWN: return down(BEFORE_TURN);
            case LEFT: return left(BEFORE_TURN);
            default: return down(AFTER_TURN);
        }
	}
	
	private Point  getNearestTank(final Point myPos) {
		List<Point> allTanks = getCoordinates(ALL_OTHER_TANKS);
		Point nearestTankPos = allTanks
			.stream()
			.min(Comparator.comparingInt(tankPos -> getDistanceBetween(myPos, tankPos)))
			.orElse(null);
		System.out.println("Nearest Tank : " + nearestTankPos);
		return nearestTankPos;
	}
	
	private int getDistanceBetween(Point posOne, Point posTwo) {
		return abs(posOne.getX() - posTwo.getX()) + abs(posOne.getY() - posTwo.getY());
	}
}
