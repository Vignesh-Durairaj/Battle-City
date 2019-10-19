package clientlib;

import java.util.Objects;

public class Point {

    protected int x;
    protected int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this(point.getX(), point.getY());
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

	@Override
	public String toString() {
		return "Position Co-ordinates [x=" + x + ", y=" + y + "]";
	}
	
	public Point upPos() {
		return new Point(this.x, this.y + 1);
	}
	
	public Point rightPos() {
		return new Point(this.x + 1, this.y);
	}
	
	public Point downPos() {
		return new Point(this.x, this.y - 1);
	}
	
	public Point leftPos() {
		return new Point(this.x - 1, this.y);
	}
	
	public Point getTopRight() {
		return new Point(this.x + 1, this.y + 1);
	}
	
	public Point getBottomRight() {
		return new Point(this.x + 1, this.y - 1);
	}
	
	public Point getTopLeft() {
		return new Point(this.x - 1, this.y + 1);
	}
	
	public Point getBottomLeft() {
		return new Point(this.x - 1, this.y - 1);
	}
    
}
