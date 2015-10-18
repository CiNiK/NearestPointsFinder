package points.kdtree;
/*
* @author Vitaliy Karpachev <vitaliy.karpachev@gmail.com>
*
* Immutable implementation of 2D point.
* */
public class Point {
    private int x;
    private int y;

    /*
    * @param x the x-coordinate
    * @param y the y-coordinate
    * @throws IllegalArgumentException if either x or y is not lie between -99 000 and 99 000
    */
    public Point(int x, int y) {
        if(x < -99_000 || y < -99_000 || x > 99_000 || y > 99_000)
            throw new IllegalArgumentException("Coordinates must lies between -99 000 and 99 000");
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public long getSquaredDistance(Point to){
        return (x - to.x)*(x - to.x) + (y - to.y)*(y - to.y);
    }

    public double getDistance(Point to){
        return Math.sqrt(getSquaredDistance(to));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x +
                ", y=" + y + "}";
    }

    public static void main(String[] args) {

    }
}
