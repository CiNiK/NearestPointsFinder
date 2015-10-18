/*
* @author Vitaliy Karpachev <vitaliy.karpachev@gmail.com>
*
* A mutable data type that uses a 2d-tree to represent a set of points on the plane.
* A 2d tree recursively divides plane into two halfplanes.It is a generalization of
* a BST to two-dimensional keys. Each point corresponds to an axis-aligned rectangle
* on the plane, which encloses all of the points in its subtree. The root corresponds
* to the whole plane. In general, odd levels of the tree divide the plane by y-coordinate,
* even - by x-coordinate. The prime advantage of a 2d-tree over a BST is that it supports
* efficient implementation of range search and nearest neighbor search.
* */
package kdtree;

import java.util.Stack;

public class KdTree {
    private static final boolean VERTICAL = true;
    private Node root = null;
    private int size = 0;
    private int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

    private class Node {
        private final Point point;
        private final boolean isVertical;
        private Node left;
        private Node right;

        public Node(Point point, boolean isVertical) {
            this.point = point;
            this.isVertical = isVertical;
        }
    }

    public KdTree(final Point[] points) {
        for (Point p : points) {
            insert(p);
        }
    }

    private void defineBounds(Point p) {
        if (p.x() < minX) minX = p.x();
        if (p.x() > maxX) maxX = p.x();
        if (p.y() < minY) minY = p.y();
        if (p.y() > maxY) maxY = p.y();
    }

    /* Return all points that lie inside the circle with given center and radius. */
    public Stack<Point> radius(Point center, double radius) {
        return radius(center.x(),center.y(),radius);
    }

    private Stack<Point> radius(int x, int y, double radius){
        Stack<Point> pointsInCircle = new Stack<>();
        int roundRad = (int) Math.round(radius + 0.5);
        Rectangle searchRect = new Rectangle(x - roundRad, y - roundRad, x + roundRad, y + roundRad);
        Iterable<Point> pointsInRange = this.range(searchRect);
        double squaredRadius = radius * radius;
        for (Point neighbor : pointsInRange) {
            int dx = neighbor.x() - x;
            int dy = neighbor.y() - y;
            if (dx * dx + dy * dy <= squaredRadius) pointsInCircle.add(neighbor);
        }
        return pointsInCircle;
    }

    /* Return number of points in the tree. */
    public int size() {
        return size;
    }

    /* Add the point p to the tree (if it is not already in the tree)*/
    public void insert(Point p) {
        if(p == null) throw new IllegalArgumentException("Try to add null to the tree");
        defineBounds(p);
        root = insertTo(root, p, VERTICAL);
    }

    /* Return all points that lies inside the rectangle*/
    public Stack<Point> range(Rectangle rect) {
        Stack<Point> pointsInRange = new Stack<>();
        range(root, new Rectangle(minX, minY, maxX, maxY), rect, pointsInRange);
        return pointsInRange;
    }

    private void range(Node node, Rectangle currentRect, Rectangle searchRect, Stack<Point> pointsInRange) {
        if (node == null) return;
        if (currentRect.intersects(searchRect)) {
            if (searchRect.contains(node.point)) pointsInRange.push(node.point);
            range(node.left, leftRectangleFrom(node, currentRect), searchRect, pointsInRange);
            range(node.right, rightRectangleFrom(node, currentRect), searchRect, pointsInRange);
        }
    }

    /*Check is the tree contain the point p*/
    public boolean contains(Point p) {
        return contains(root, p);
    }

    private boolean contains(Node node, Point p) {
        if (node == null) return false;

        if (node.point.equals(p)) return true;

        if (isPointInLeftSubtree(node, p))
            return contains(node.left, p);
        else
            return contains(node.right, p);
    }

    /* Find a nearest neighbor in the set to p.
    *
    * @return nearest point to given point or null if tree is empty
    * */
    public Point nearest(Point point) {
        return nearestTo(point, root, new Rectangle(minX, minY, maxX, maxY), null);
    }

    private Point nearestTo(final Point point, final Node node, Rectangle rect, final Point applicant) {
        if (node == null) return applicant;
        Point nearest = applicant;
        if (nearest == null || nearest.getSquaredDistance(point) >= rect.distanceSquaredTo(point)) {
            if (nearest == null || nearest.getSquaredDistance(point) > node.point.getSquaredDistance(point))
                if (point != node.point)
                    nearest = node.point;

            Rectangle left = leftRectangleFrom(node, rect);
            Rectangle right = rightRectangleFrom(node, rect);
            if (isPointInLeftSubtree(node, point)) {
                nearest = nearestTo(point, node.left, left, nearest);
                nearest = nearestTo(point, node.right, right, nearest);
            } else {
                nearest = nearestTo(point, node.right, right, nearest);
                nearest = nearestTo(point, node.left, left, nearest);
            }
        }
        return nearest;
    }

    private Rectangle leftRectangleFrom(Node node, Rectangle parentRect) {
        if (node.isVertical)
            return new Rectangle(parentRect.minX(), parentRect.minY(), node.point.x(), parentRect.maxY());
        else
            return new Rectangle(parentRect.minX(), parentRect.minY(), parentRect.maxX(), node.point.y());
    }

    private Rectangle rightRectangleFrom(Node node, Rectangle parentRect) {
        if (node.isVertical)
            return new Rectangle(node.point.x(), parentRect.minY(), parentRect.maxX(), parentRect.maxY());
        else
            return new Rectangle(parentRect.minX(), node.point.y(), parentRect.maxX(), parentRect.maxY());
    }

    private Node insertTo(Node node, Point point, boolean isVertical) {
        if (node == null) {
            size++;
            return new Node(point, isVertical);
        }

        if (node.point.equals(point)) return node;

        if (isPointInLeftSubtree(node, point))
            node.left = insertTo(node.left, point, !node.isVertical);
        else
            node.right = insertTo(node.right, point, !node.isVertical);
        return node;
    }

    private boolean isPointInLeftSubtree(Node node, Point point) {
        return (node.isVertical && node.point.x() > point.x()) || (!node.isVertical && node.point.y() > point.y());
    }

    /* Simple unit testing*/
    public static void main(String[] args) {
        Point[] points = new Point[20];
        points[0] = new Point(6,6);
        points[1] = new Point(0,6);
        points[2] = new Point(2,7);
        points[3] = new Point(4,8);
        points[4] = new Point(5,10);
        points[5] = new Point(7,10);
        points[6] = new Point(8,8);
        points[7] = new Point(10,7);
        points[8] = new Point(10,5);
        points[9] = new Point(8,4);
        points[10] = new Point(7,2);
        points[11] = new Point(6,0);
        points[12] = new Point(5,2);
        points[13] = new Point(4,4);
        points[14] = new Point(2,5);
        points[15] = new Point(7,5);
        points[16] = new Point(7,3);
        points[17] = new Point(6,5);
        points[18] = new Point(6,12);
        points[19] = new Point(12,6);
        KdTree tree = new KdTree(points);
        /*Equals testing*/
        if(tree.size() != 20 ) System.out.println("Size equals "+tree.size()+", expected 20");
        /*Nearest testing*/
        if(points[17] != tree.nearest(points[0]))
            System.out.println("Nearest to "+points[0]+"is "+tree.nearest(points[0])+", expected "+points[17]);
        int count;
        /*Points in range testing*/
        if ((count = tree.range(new Rectangle(4,4,8,8)).size()) != 7)
            System.out.println("Count of points in "+new Rectangle(4,4,8,8)+" is "+count+", expected 7");
        /*Points in circle testing*/
        if ((count = tree.radius(new Point(6,6),2.9).size()) != 7)
            System.out.println("Count of points in circle(center 6,6; radius 2.5)"+" is "+count+", expected 7");
    }
}
