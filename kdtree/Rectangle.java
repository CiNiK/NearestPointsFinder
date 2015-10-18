package points.kdtree;

/*
* @author Vitaliy Karpachev <vitaliy.karpachev@gmail.com>
*
* Implementation of 2D axis-aligned rectangle.
* */
class Rectangle {
    private final int minX, maxX;
    private final int minY, maxY;

    /**
     * @param  minX the x-coordinate of the lower-left endpoint
     * @param  maxX the x-coordinate of the upper-right endpoint
     * @param  minY the y-coordinate of the lower-left endpoint
     * @param  maxY the y-coordinate of the upper-right endpoint
     * @throws IllegalArgumentException if any of minX,
     *         maxX, minY, or maxY is less than -99 000 or bigger that 99 000.
     * @throws IllegalArgumentException if maxX < minX or maxY < minY.
     */
    public Rectangle(int minX, int minY, int maxX, int maxY) {
        if(minX < -99_000 || minY < -99_000 || maxX > 99_000 || maxY > 99_000)
            throw new IllegalArgumentException("Coordinates must lies between -99 000 and 99 000");
        if (maxX < minX || maxY < minY)
            throw new IllegalArgumentException("Invalid rectangle. Wrong coordinates");
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public boolean contains(Point p)      {
        return (p.x() <= maxX && p.x() >= minX) && (p.y() <= maxY && p.y() >= minY);
    }
    public boolean intersects(Rectangle that){
        return  this.maxX >= that.minX && this.maxY >= that.minY &&
                that.maxX >= this.minX && that.maxY >= this.minY;
    }

    public double distanceTo(Point p) {
        return Math.sqrt(distanceSquaredTo(p));
    }

    public long distanceSquaredTo(Point p){
        if(this.contains(p)) return 0;
        int dx,dy;
        if(p.x() < minX)
            dx = p.x() - minX;
        else
            dx = p.x() - maxX;
        if(p.y() < minY)
            dy = p.y() - minY;
        else
            dy = p.y() - maxY;
        return dx*dx + dy*dy;
    }

    public int minX() {
        return minX;
    }

    public int maxX() {
        return maxX;
    }

    public int minY() {
        return minY;
    }

    public int maxY() {
        return maxY;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                '}';
    }

    /*Simple unit testing*/
    public static void main(String[] args) {
        /* Intersect testing */
        Rectangle big = new Rectangle(-1000,-1000,1000,1000);
        Rectangle intersected = new Rectangle(900,900,1100,1100);
        Rectangle notIntersected = new Rectangle(900,1100,1100,1200);
        if(!big.intersects(intersected))
            System.out.println(big+" doesn't intersect "+intersected+", intersection expected");
        if(big.intersects(notIntersected))
            System.out.println(big+" intersects "+notIntersected+", intersection NOT expected");
        /* Contains and distance testing */
        Point contained = new Point(0,0);
        Point notContained = new Point(1100,1100);
        if(!big.contains(contained))
            System.out.println(big+" not contains "+contained+", containing expected");
        if(big.contains(notContained))
            System.out.println(big+" contains "+notContained+", containing NOT expected");
        if(big.distanceTo(contained) != 0 )
            System.out.println("Distance from "+big+" to "+contained+" is"+big.distanceTo(contained)+", expected 0");
        if(big.distanceSquaredTo(notContained) != 20_000 )
            System.out.println("Squared distance from "+big+" to "+notContained+" is"+big.distanceTo(notContained)+", expected 20 000");
    }
}
