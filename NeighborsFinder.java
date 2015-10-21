
import kdtree.KdTree;
import kdtree.Point;

import java.io.*;
import java.util.ArrayList;

public class NeighborsFinder {
    public static void main(String[] args) {
        Point[] points = null;
        try {
            if (args.length != 0) {
                points = getPointsFromFile(args[0]);
            } else {
                points = getPointsFromStandardInput();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (points != null) printRadiusAndNeighbors(points);
    }

    private static void printRadiusAndNeighbors(Point[] points) {
        if(points.length < 2) {
            System.out.println("There are less then 2 points");
            return;
        }
        KdTree tree = new KdTree(points);
        for (Point point : points) {
            Point nearest = tree.nearest(point);
            if(nearest == null) break;
            double radius = nearest.getDistance(point);
            int neighborsCount = tree.radius(point, 2 * radius).size() - 1;
            System.out.print(point + " radius = ");
            System.out.format("%.2f", radius);
            System.out.println(", has " + neighborsCount + " neighbor(s)");
        }
    }

    private static Point[] getPointsFromStandardInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        ArrayList<Point> points = new ArrayList<>();
        System.out.println("Print two coordinates through whitespace. Type 'end' to view result");
        while (!(input = reader.readLine()).equals("end")) {
            try {
                addPoint(points, input);
            } catch (Exception ex) {
                System.out.print("Invalid input(");
                System.out.println(ex.toString()+").");
            }
        }
        reader.close();
        return points.toArray(new Point[points.size()]);
    }

    private static Point[] getPointsFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        ArrayList<Point> points = new ArrayList<>();
        String input;
        while ((input = reader.readLine()) != null) {
            try {
                addPoint(points, input);
            } catch (Exception ex) {
                System.out.print("Invalid input file format(");
                System.out.println(ex.toString()+").");
                reader.close();
                return null;
            }
        }
        reader.close();
        return points.toArray(new Point[points.size()]);
    }

    private static void addPoint(ArrayList<Point> points, String input) {
        String[] coords = input.split(" ");
        if (coords.length != 2) throw new IllegalArgumentException("Wrong arguments count");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        points.add(new Point(x, y));
    }
}
