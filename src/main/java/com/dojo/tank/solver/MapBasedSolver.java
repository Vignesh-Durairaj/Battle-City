package com.dojo.tank.solver;

import static clientlib.Action.BEFORE_TURN;
import static clientlib.Elements.AI_TANK_DOWN;
import static clientlib.Elements.AI_TANK_LEFT;
import static clientlib.Elements.AI_TANK_RIGHT;
import static clientlib.Elements.AI_TANK_UP;
import static clientlib.Elements.BATTLE_WALL;
import static clientlib.Elements.BULLET;
import static clientlib.Elements.CONSTRUCTION_DESTROYED;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_DOWN;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_DOWN_LEFT;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_DOWN_RIGHT;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_DOWN_TWICE;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_LEFT;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_LEFT_RIGHT;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_LEFT_TWICE;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_RIGHT;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_RIGHT_TWICE;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_RIGHT_UP;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_UP;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_UP_DOWN;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_UP_LEFT;
import static clientlib.Elements.CONSTRUCTION_DESTROYED_UP_TWICE;
import static clientlib.Elements.OTHER_TANK_DOWN;
import static clientlib.Elements.OTHER_TANK_LEFT;
import static clientlib.Elements.OTHER_TANK_RIGHT;
import static clientlib.Elements.OTHER_TANK_UP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import clientlib.Action;
import clientlib.Elements;
import clientlib.Point;
import clientlib.SampleSolver;

public class MapBasedSolver extends SampleSolver {
	
	private Map<Point, Elements> gameBoard;
	private Point currentPos;
	private Point[] recentlyVisited = new Point[10];
	private int moveCounter = 0;
	
	@Override
	public String move() {
		return super.move();
	}
	
	private String nextMovement() {
		return up(BEFORE_TURN);
	}
	
	private String something () {
		return null;
	}
	
	private String isClearPath() {
		if(!gameBoard.containsKey(currentPos.rightPos()) && 
				!gameBoard.containsKey(currentPos.rightPos().rightPos())) {
			return  right(BEFORE_TURN);
		} else if (!gameBoard.containsKey(currentPos.downPos()) && 
				!gameBoard.containsKey(currentPos.downPos().downPos())) {
			return  down(BEFORE_TURN);
		} else if (!gameBoard.containsKey(currentPos.leftPos()) && 
				!gameBoard.containsKey(currentPos.leftPos().leftPos())) {
			return  right(BEFORE_TURN);
		}else if (!gameBoard.containsKey(currentPos.upPos()) && 
				!gameBoard.containsKey(currentPos.upPos().upPos())) {
			return  right(BEFORE_TURN);
		} else {
			return  act();
		}
			
	}
	
	private Map<Point, Elements> getCurrentBoard() {
		Map<Point, Elements> gameBoard = new HashMap<>();
		
		gameBoard.putAll(getElementPositions(BULLET));
		
		gameBoard.putAll(getElementPositions(BATTLE_WALL));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_DOWN));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_UP));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_LEFT));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_RIGHT));
		
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_DOWN_TWICE));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_UP_TWICE));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_LEFT_TWICE));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_RIGHT_TWICE));
		
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_LEFT_RIGHT));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_UP_DOWN));
		
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_UP_LEFT));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_RIGHT_UP));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_DOWN_LEFT));
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED_DOWN_RIGHT));
		
		gameBoard.putAll(getElementPositions(CONSTRUCTION_DESTROYED));
		
		gameBoard.putAll(getElementPositions(OTHER_TANK_UP));
		gameBoard.putAll(getElementPositions(OTHER_TANK_RIGHT));
		gameBoard.putAll(getElementPositions(OTHER_TANK_DOWN));
		gameBoard.putAll(getElementPositions(OTHER_TANK_LEFT));
		
		gameBoard.putAll(getElementPositions(AI_TANK_UP));
		gameBoard.putAll(getElementPositions(AI_TANK_RIGHT));
		gameBoard.putAll(getElementPositions(AI_TANK_DOWN));
		gameBoard.putAll(getElementPositions(AI_TANK_LEFT));
		
		return gameBoard;
	}
	
	private Map<Point, Elements> getCurrentBoard(List<Point> points, Elements element) {
		return points
			.stream()
			.collect(Collectors.toMap(point -> point, ele -> element));
	}
	
	private Map<Point, Elements> getElementPositions(final Elements element) {
		return getCurrentBoard(getCoordinates(element), element);
	}
	
	private boolean isOutOfBounds(Point point){
        return point.getX() >= field.length || point.getY() >= field.length || point.getX() < 0 || point.getY() < 0;
    }
	
	private boolean isBarrierAt(Point point){
        return getBarriers().contains(point);
    }

	private boolean isAnyOfAt(Point point, Elements... elements){
        boolean result = false;
    	for (Elements el : elements){
            result = isAt(point, el);
            if(result) break;
        }
         return result;
    }

	private boolean isAt(Point point, Elements element){
        return gameBoard.get(point).equals(element);
    }
}
