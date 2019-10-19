package com.dojo.tank.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum Direction {

	UP, 
	DOWN, 
	LEFT,
	RIGHT;
	
	public static Direction randomEnum(){
		Direction[] directions = Direction.values();
        return directions[ThreadLocalRandom.current().nextInt(0, directions.length - 1)];
    }
}
