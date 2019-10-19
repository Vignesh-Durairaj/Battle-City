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
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.dojo.tank.enums.Direction;

import clientlib.Elements;
import clientlib.Point;
import clientlib.SampleSolver;

public class BasicSolver extends SampleSolver {

	int counter = 1;
	boolean gotStuck = false;
	
	private static final Elements[] ALL_OTHER_TANKS = new Elements[] {
			OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT,
            AI_TANK_UP, AI_TANK_RIGHT, AI_TANK_DOWN, AI_TANK_LEFT};
	
	@Override
	public String move() {
		
		if(counter <= 25 && gotStuck) {
			counter ++;
			return left(BEFORE_TURN);
		}
		
		Point myPos = getPlayerTankCoordinates().get(0);   
        System.out.println("Current Postion : " + myPos);
        String nextMove = null;
        
        nextMove = moveToHitNearbyTank(myPos);
        if (nextMove != null) return nextMove;
        
        nextMove = someLogic(myPos);
        if (nextMove != null) return nextMove;
        
        return act();
        
	}
	
	private String moveToHitNearbyTank(Point myPos) {
		String action = null;
		
		if (isAnyOfAt(myPos.getX(),myPos.getY()-1, ALL_OTHER_TANKS)){
            System.out.println("Target is UP");
            action = up(AFTER_TURN);
        } else if (isAnyOfAt(myPos.getX()+1, myPos.getY(), ALL_OTHER_TANKS)){
            System.out.println("Target is RIGHT");
            action = right(AFTER_TURN);
        } else if (isAnyOfAt(myPos.getX(),myPos.getY()+1, ALL_OTHER_TANKS)){
            System.out.println("Target is DOWN");
            action = down(AFTER_TURN);
        } else if (isAnyOfAt(myPos.getX()-1,myPos.getY(), ALL_OTHER_TANKS)){
            System.out.println("Target is LEFT");
            action = left(AFTER_TURN);
        }
		
		return action;
	}
	
	private String someLogic(final Point myPos) {
		List<Direction> directions = new ArrayList<>(asList(UP, RIGHT, DOWN, LEFT));
	       
        if(isBarrierAt(myPos.getX(),myPos.getY()-1) || isAt(myPos.getX(),myPos.getY()-1, BULLET)){
        	directions.set(0, null);
        }
        if(isBarrierAt(myPos.getX()+1,myPos.getY()) || isAt(myPos.getX()+1,myPos.getY(), BULLET)){
        	directions.set(1, null);
        }
        if(isBarrierAt(myPos.getX(),myPos.getY()+1) || isAt(myPos.getX(),myPos.getY()+1, BULLET)){
        	directions.set(2, null);
        }
        if(isBarrierAt(myPos.getX()-1,myPos.getY() )|| isAt(myPos.getX()-1,myPos.getY(), BULLET)){
        	directions.set(3, null);
        }
           
       System.out.println("Instructions : " + directions);
       
        for ( Point tankPos: getOtherPlayersTanks()) {
            for (int ii = 1; ii < 3; ii++) {
                if (tankPos.getY() == myPos.getY()) {
                    if (tankPos.getX() + ii == myPos.getX() && directions.get(3) != null) {
                        System.out.println("enemy is left in " + ii);
                        return left(AFTER_TURN);
                    }
                    if (tankPos.getX() - ii == myPos.getX() && directions.get(1) != null) {
                        System.out.println("enemy is right in " + ii);
                        return right(AFTER_TURN);
                    }
                }
                if (tankPos.getX() == myPos.getX()) {
                    if (tankPos.getY() + ii == myPos.getY() && directions.get(0) != null) {
                        System.out.println("enemy is up in " + ii);
                        return up(AFTER_TURN);
                    }
                    if (tankPos.getY() - ii == myPos.getY() && directions.get(2) != null) {
                        System.out.println("enemy is down in " + ii);
                        return down(AFTER_TURN);
                    }
                }
            }
        }
        
        for ( Point enemyTankPos: getOtherPlayersTanks()) {
            if (enemyTankPos.getY() == myPos.getY() && Math.abs(enemyTankPos.getX() - myPos.getX()) <= 12) {
                if (enemyTankPos.getX() < myPos.getX() && directions.get(3) != null) {
                    System.out.println("Enemy tank is LEFT" + enemyTankPos);
                    return left(AFTER_TURN);
                }
                if (enemyTankPos.getX() > myPos.getX() && directions.get(1) != null) {
                    System.out.println("Enemy tank is RIGHT" + enemyTankPos);
                    return right(AFTER_TURN);
                }
            }
            if (enemyTankPos.getX() == myPos.getX() && Math.abs(enemyTankPos.getY() - myPos.getY()) <= 5) {
                if (enemyTankPos.getY() < myPos.getY() && directions.get(0) != null) {
                    System.out.println("Enemy tank is UP" + enemyTankPos);
                    return up(AFTER_TURN);
                }
                if (enemyTankPos.getY() > myPos.getY() && directions.get(2) != null) {
                    System.out.println("Enemy tank is DOWN" + enemyTankPos);
                    return down(AFTER_TURN);
                }
            }
        }
        
        int a; 
        boolean ok;
        do {
            ok = true;
            a = ThreadLocalRandom.current().nextInt(0, directions.size());
            System.out.println("a = " + a);
            if (directions.get(a) == null)
            {
                ok = false;
                directions.remove(a);
                System.out.println("size became " + directions.size());
            }
        }while(ok == false);
        System.out.println("Total movement " + directions.size() +" and action = " + a);
        
        switch (directions.get(a)){                                 
            case UP:
                return up();
            case RIGHT:
                return right();
            case DOWN:
                return down();
            case LEFT:
                return left();
            default: return down(AFTER_TURN);
        }
	}
}
