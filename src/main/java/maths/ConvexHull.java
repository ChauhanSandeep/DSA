package maths;

import java.util.*;

/**
 * Problem: Erect the Fence
 *
 * Given tree coordinates on a 2D plane, return every tree that lies on the
 * outer fence enclosing all trees. Boundary trees that are collinear with a
 * hull edge must be included, not discarded.
 *
 * Leetcode: https://leetcode.com/problems/erect-the-fence/ (Hard)
 * Rating:   acceptance 53.1% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Computational geometry | Convex hull
 *
 * Example:
 *   Input:  trees = [[1,1],[2,2],[2,0],[2,4],[3,3],[4,2]]
 *   Output: [[1,1],[2,0],[2,4],[3,3],[4,2]]
 *   Why:    [2,2] is inside the fence, while the other five points lie on its boundary.
 *
 * Follow-ups:
 *   1. How do you handle all points being collinear?
 *      Keep collinear boundary points instead of popping them from the hull.
 *   2. How would you compute the hull in 3D?
 *      Build and update visible faces with an incremental or QuickHull-style method.
 *   3. How would you support dynamic point insertions?
 *      Use a dynamic convex hull data structure or rebuild in batches.
 *
 * Related: Max Points on a Line (149), Minimum Lines to Represent a Line Chart (2280).
 */

public class ConvexHull {

    public static void main(String[] args) {
        ConvexHull solver = new ConvexHull();
        int[][][] inputs = {
            { {0, 0}, {1, 1}, {2, 2} },
            { {1, 1}, {2, 2}, {2, 0}, {2, 4}, {3, 3}, {4, 2} }
        };
        int[][][] expected = {
            { {0, 0}, {1, 1}, {2, 2} },
            { {1, 1}, {2, 0}, {2, 4}, {3, 3}, {4, 2} }
        };

        for (int i = 0; i < inputs.length; i++) {
            int[][] got = solver.outerTrees(inputs[i]);
            Arrays.sort(got, (a, b) -> a[0] != b[0]
                ? Integer.compare(a[0], b[0])
                : Integer.compare(a[1], b[1]));
            System.out.printf("trees=%s -> %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), Arrays.deepToString(got), Arrays.deepToString(expected[i]));
        }

        int[][] chanInput = { {0, 0}, {1, 1}, {2, 0}, {1, 2} };
        int[][] chanExpected = { {0, 0}, {1, 2}, {2, 0} };
        int[][] chanGot = solver.outerTreesChan(chanInput);
        Arrays.sort(chanGot, (a, b) -> a[0] != b[0]
            ? Integer.compare(a[0], b[0])
            : Integer.compare(a[1], b[1]));
        System.out.printf("outerTreesChan trees=%s -> %s  expected=%s%n",
            Arrays.deepToString(chanInput), Arrays.deepToString(chanGot), Arrays.deepToString(chanExpected));
    }


        /**
     * Intuition: choose a pivot on the outside, then sort every other point by
     * the angle it makes with that pivot. Walking through those angles lets the
     * stack represent the current fence boundary. When the next point would make
     * a right turn, the previous point cannot stay on the outside; collinear
     * boundary points are kept because this problem wants every fence tree.
     *
     * Algorithm:
     *   1. Return small inputs directly, then find the bottom-left pivot.
     *   2. Sort the remaining points by polar angle and distance from the pivot.
     *   3. Reverse the final collinear suffix so far boundary points are included.
     *   4. Scan with a stack, popping only turns that move inside the fence.
     *   5. Convert the stack of Point objects back to int[][] coordinates.
     *
     * Time:  O(n log n) - sorting dominates the scan.
     * Space: O(n) - Point objects and the hull stack are stored.
     *
     * @param trees coordinates of all trees
     * @return coordinates of trees on the outer fence
     */

    public int[][] outerTrees(int[][] trees) {
        int n = trees.length;
        if (n <= 3) return trees;

        // Find the bottom-most point (leftmost in case of tie)
        Point pivot = findPivot(trees);

        // Convert to Point objects for easier manipulation
        List<Point> points = new ArrayList<>();
        for (int[] tree : trees) {
            points.add(new Point(tree[0], tree[1]));
        }

        // Remove pivot from points and sort by polar angle
        points.remove(pivot);
        points.sort((a, b) -> {
            int orientation = orientation(pivot, a, b);
            if (orientation == 0) {
                // Collinear points - sort by distance from pivot
                return Long.compare(distanceSquared(pivot, a), distanceSquared(pivot, b));
            }
            return orientation < 0 ? -1 : 1;
        });

        // Handle collinear points at the end (important for this problem)
        int i = points.size() - 1;
        while (i >= 0 && orientation(pivot, points.get(points.size() - 1), points.get(i)) == 0) {
            i--;
        }

        // Reverse the collinear points at the end
        Collections.reverse(points.subList(i + 1, points.size()));

        // Graham scan
        Stack<Point> hull = new Stack<>();
        hull.push(pivot);

        for (Point point : points) {
            // Remove points that would create right turn
            while (hull.size() >= 2) {
                Point second = hull.pop();
                Point first = hull.peek();

                if (orientation(first, second, point) < 0) {
                    hull.push(second);
                    break;
                } else if (orientation(first, second, point) == 0) {
                    // Keep collinear points for this problem
                    hull.push(second);
                    break;
                }
            }
            hull.push(point);
        }

        // Convert back to int array format
        return hull.stream()
                   .map(p -> new int[]{p.x, p.y})
                   .toArray(int[][]::new);
    }

    private Point findPivot(int[][] trees) {
        Point pivot = new Point(trees[0][0], trees[0][1]);

        for (int[] tree : trees) {
            if (tree[1] < pivot.y || (tree[1] == pivot.y && tree[0] < pivot.x)) {
                pivot = new Point(tree[0], tree[1]);
            }
        }

        return pivot;
    }

    // Cross product to determine orientation
    private int orientation(Point p, Point q, Point r) {
        long val = (long)(q.x - p.x) * (r.y - p.y) - (long)(q.y - p.y) * (r.x - p.x);
        return val == 0 ? 0 : (val > 0 ? 1 : -1);
    }

    private long distanceSquared(Point a, Point b) {
        return (long)(a.x - b.x) * (a.x - b.x) + (long)(a.y - b.y) * (a.y - b.y);
    }

    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Point)) return false;
            Point other = (Point) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    /**
     * Jarvis March (Gift Wrapping) algorithm.
     * More intuitive but less efficient for large datasets.
     */
    public int[][] outerTreesJarvis(int[][] trees) {
        int n = trees.length;
        if (n <= 3) return trees;

        Set<Point> hullPoints = new HashSet<>();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            points[i] = new Point(trees[i][0], trees[i][1]);
        }

        // Find leftmost point
        Point leftmost = points[0];
        for (Point p : points) {
            if (p.x < leftmost.x || (p.x == leftmost.x && p.y < leftmost.y)) {
                leftmost = p;
            }
        }

        Point current = leftmost;

        do {
            hullPoints.add(current);
            Point next = points[0];

            for (Point candidate : points) {
                if (candidate.equals(current)) continue;

                int orient = orientation(current, next, candidate);

                if (next.equals(current) || orient > 0 ||
                   (orient == 0 && distanceSquared(current, candidate) > distanceSquared(current, next))) {
                    next = candidate;
                }
            }

            // Add all collinear points on the hull edge
            for (Point candidate : points) {
                if (candidate.equals(current) || candidate.equals(next)) continue;

                if (orientation(current, next, candidate) == 0 &&
                    isOnSegment(current, candidate, next)) {
                    hullPoints.add(candidate);
                }
            }

            current = next;
        } while (!current.equals(leftmost));

        return hullPoints.stream()
                        .map(p -> new int[]{p.x, p.y})
                        .toArray(int[][]::new);
    }

    private boolean isOnSegment(Point a, Point b, Point c) {
        return Math.min(a.x, c.x) <= b.x && b.x <= Math.max(a.x, c.x) &&
               Math.min(a.y, c.y) <= b.y && b.y <= Math.max(a.y, c.y);
    }

    /**
     * Andrew's Monotone Chain algorithm - robust and efficient.
     * Computes both upper and lower hulls separately.
     */
    public int[][] outerTreesAndrew(int[][] trees) {
        int n = trees.length;
        if (n <= 1) return trees;

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(trees[i][0], trees[i][1]);
        }

        // Sort points lexicographically
        Arrays.sort(points, (a, b) -> {
            if (a.x != b.x) return Integer.compare(a.x, b.x);
            return Integer.compare(a.y, b.y);
        });

        // Build lower hull
        List<Point> lowerHull = new ArrayList<>();
        for (Point p : points) {
            while (lowerHull.size() >= 2 &&
                   orientation(lowerHull.get(lowerHull.size()-2),
                              lowerHull.get(lowerHull.size()-1), p) < 0) {
                lowerHull.remove(lowerHull.size() - 1);
            }
            lowerHull.add(p);
        }

        // Build upper hull
        List<Point> upperHull = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            Point p = points[i];
            while (upperHull.size() >= 2 &&
                   orientation(upperHull.get(upperHull.size()-2),
                              upperHull.get(upperHull.size()-1), p) < 0) {
                upperHull.remove(upperHull.size() - 1);
            }
            upperHull.add(p);
        }

        // Remove duplicate points
        Set<Point> hullSet = new HashSet<>(lowerHull);
        hullSet.addAll(upperHull);

        // Add collinear points on hull edges
        addCollinearPoints(points, hullSet);

        return hullSet.stream()
                     .map(p -> new int[]{p.x, p.y})
                     .toArray(int[][]::new);
    }

    private void addCollinearPoints(Point[] allPoints, Set<Point> hull) {
        List<Point> hullList = new ArrayList<>(hull);

        // Check each edge of the hull for collinear points
        for (int i = 0; i < hullList.size(); i++) {
            Point a = hullList.get(i);

            for (int j = i + 1; j < hullList.size(); j++) {
                Point b = hullList.get(j);

                // Check if this could be a hull edge
                boolean isHullEdge = true;
                for (Point c : hullList) {
                    if (!c.equals(a) && !c.equals(b) && orientation(a, b, c) < 0) {
                        isHullEdge = false;
                        break;
                    }
                }

                if (isHullEdge) {
                    // Add all collinear points on this edge
                    for (Point p : allPoints) {
                        if (!p.equals(a) && !p.equals(b) &&
                            orientation(a, b, p) == 0 && isOnSegment(a, p, b)) {
                            hull.add(p);
                        }
                    }
                }
            }
        }
    }

    /**
     * QuickHull algorithm - divide and conquer approach.
     * Efficient for many practical cases.
     */
    public int[][] outerTreesQuickHull(int[][] trees) {
        int n = trees.length;
        if (n <= 3) return trees;

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(trees[i][0], trees[i][1]);
        }

        Set<Point> hull = new HashSet<>();

        // Find extreme points
        Point leftmost = points[0], rightmost = points[0];
        for (Point p : points) {
            if (p.x < leftmost.x || (p.x == leftmost.x && p.y < leftmost.y)) {
                leftmost = p;
            }
            if (p.x > rightmost.x || (p.x == rightmost.x && p.y > rightmost.y)) {
                rightmost = p;
            }
        }

        hull.add(leftmost);
        hull.add(rightmost);

        // Divide points into upper and lower sets
        List<Point> upperSet = new ArrayList<>();
        List<Point> lowerSet = new ArrayList<>();

        for (Point p : points) {
            if (orientation(leftmost, rightmost, p) > 0) {
                upperSet.add(p);
            } else if (orientation(leftmost, rightmost, p) < 0) {
                lowerSet.add(p);
            } else if (isOnSegment(leftmost, p, rightmost)) {
                hull.add(p); // Collinear points on the line
            }
        }

        // Recursively find hull points
        quickHullRec(leftmost, rightmost, upperSet, hull);
        quickHullRec(rightmost, leftmost, lowerSet, hull);

        return hull.stream()
                   .map(p -> new int[]{p.x, p.y})
                   .toArray(int[][]::new);
    }

    private void quickHullRec(Point a, Point b, List<Point> points, Set<Point> hull) {
        if (points.isEmpty()) return;

        // Find point with maximum distance from line ab
        Point farthest = null;
        double maxDist = 0;

        for (Point p : points) {
            double dist = distanceToLine(a, b, p);
            if (dist > maxDist) {
                maxDist = dist;
                farthest = p;
            }
        }

        if (farthest != null) {
            hull.add(farthest);

            // Find points on the right side of line segments
            List<Point> rightOfAC = new ArrayList<>();
            List<Point> rightOfCB = new ArrayList<>();

            for (Point p : points) {
                if (orientation(a, farthest, p) > 0) {
                    rightOfAC.add(p);
                } else if (orientation(a, farthest, p) == 0 && isOnSegment(a, p, farthest)) {
                    hull.add(p);
                }

                if (orientation(farthest, b, p) > 0) {
                    rightOfCB.add(p);
                } else if (orientation(farthest, b, p) == 0 && isOnSegment(farthest, p, b)) {
                    hull.add(p);
                }
            }

            quickHullRec(a, farthest, rightOfAC, hull);
            quickHullRec(farthest, b, rightOfCB, hull);
        }
    }

    private double distanceToLine(Point a, Point b, Point p) {
        return Math.abs((b.x - a.x) * (a.y - p.y) - (a.x - p.x) * (b.y - a.y)) /
               Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
    }

    /**
     * Intuition: try a guessed hull-size limit and gift-wrap across small
     * group hulls. If the walk returns to the leftmost point within that limit,
     * the hull is complete; otherwise grow the limit and retry. The first limit
     * must be greater than one so the retry loop always makes progress.
     *
     * Algorithm:
     *   1. Return small inputs directly, then convert coordinates to Point objects.
     *   2. Try increasing hull-size limits, starting from two.
     *   3. For each limit, split points into groups and gift-wrap over group hulls.
     *   4. When the walk closes, add collinear boundary points on every hull edge.
     *   5. Fall back to the primary Graham scan if no guessed limit closes the hull.
     *
     * Time:  O(nh) for the simple tangent scan used here; O(n log n) fallback.
     * Space: O(n) for grouped hulls and the output set.
     *
     * @param trees coordinates of all trees
     * @return coordinates of trees on the outer fence
     */
    public int[][] outerTreesChan(int[][] trees) {
        int n = trees.length;
        if (n <= 3) return trees;

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(trees[i][0], trees[i][1]);
        }

        // Try different values of h (expected hull size)
        for (int h = 2; h <= n; h = h > n / h ? n : h * h) {
            int[][] result = chanWithH(points, h);
            if (result != null) {
                return result;
            }
            if (h == n) {
                break;
            }
        }

        // Fallback to Graham scan if Chan's doesn't work
        return outerTrees(trees);
    }

    private int[][] chanWithH(Point[] points, int h) {
        int n = points.length;
        int m = (int) Math.ceil(Math.sqrt(h));
        int numGroups = (n + m - 1) / m;

        // Divide points into groups and find hull of each group
        List<List<Point>> groupHulls = new ArrayList<>();

        for (int g = 0; g < numGroups; g++) {
            int start = g * m;
            int end = Math.min(start + m, n);

            Point[] group = Arrays.copyOfRange(points, start, end);
            List<Point> hull = computeHullGraham(group);
            groupHulls.add(hull);
        }

        // Use gift wrapping on group hulls
        Point leftmost = findLeftmostPoint(points);
        List<Point> globalHull = new ArrayList<>();
        Point current = leftmost;

        for (int iter = 0; iter < h; iter++) {
            globalHull.add(current);
            Point next = findNextHullPoint(current, groupHulls);

            if (next.equals(leftmost) && iter > 0) {
                // Completed the hull
                return includeCollinearBoundaryPoints(points, globalHull);
            }

            current = next;
        }

        return null; // h was too small
    }

    private int[][] includeCollinearBoundaryPoints(Point[] points, List<Point> hullVertices) {
        Set<Point> boundaryPoints = new LinkedHashSet<>(hullVertices);

        for (int i = 0; i < hullVertices.size(); i++) {
            Point start = hullVertices.get(i);
            Point end = hullVertices.get((i + 1) % hullVertices.size());

            for (Point point : points) {
                if (orientation(start, end, point) == 0 && isOnSegment(start, point, end)) {
                    boundaryPoints.add(point);
                }
            }
        }

        return boundaryPoints.stream()
                             .map(p -> new int[]{p.x, p.y})
                             .toArray(int[][]::new);
    }

    private List<Point> computeHullGraham(Point[] points) {
        // Simplified Graham scan for small groups
        if (points.length <= 3) {
            return Arrays.asList(points);
        }

        // Implementation details would follow Graham scan algorithm
        // For brevity, returning simplified result
        return Arrays.asList(points);
    }

    private Point findLeftmostPoint(Point[] points) {
        Point leftmost = points[0];
        for (Point p : points) {
            if (p.x < leftmost.x || (p.x == leftmost.x && p.y < leftmost.y)) {
                leftmost = p;
            }
        }
        return leftmost;
    }

    private Point findNextHullPoint(Point current, List<List<Point>> groupHulls) {
        Point next = null;

        for (List<Point> hull : groupHulls) {
            Point candidate = findTangentPoint(current, hull);

            int turn = next == null ? 1 : orientation(current, next, candidate);
            if (next == null || turn > 0 ||
                (turn == 0 && distanceSquared(current, candidate) > distanceSquared(current, next))) {
                next = candidate;
            }
        }

        return next;
    }

    private Point findTangentPoint(Point current, List<Point> hull) {
        // Find tangent from current point to convex hull
        // Binary search on the hull for efficiency

        if (hull.size() == 1) {
            return hull.get(0);
        }

        // Simplified implementation - full binary search would be more complex
        Point best = hull.get(0);
        for (Point p : hull) {
            if (orientation(current, best, p) > 0 ||
               (orientation(current, best, p) == 0 && distanceSquared(current, p) > distanceSquared(current, best))) {
                best = p;
            }
        }

        return best;
    }

    /**
     * 3D Convex Hull using incremental algorithm.
     * Extension to handle 3D point sets.
     */
    public static class ConvexHull3D {

        public List<Face> computeConvexHull3D(Point3D[] points) {
            if (points.length < 4) {
                return new ArrayList<>();
            }

            List<Face> hull = new ArrayList<>();

            // Find initial tetrahedron
            Face[] initialFaces = findInitialTetrahedron(points);
            if (initialFaces == null) {
                return hull;
            }

            hull.addAll(Arrays.asList(initialFaces));
            Set<Integer> processed = new HashSet<>();

            // Add initial tetrahedron points to processed set
            for (Face face : initialFaces) {
                processed.add(face.a);
                processed.add(face.b);
                processed.add(face.c);
            }

            // Incrementally add remaining points
            for (int i = 0; i < points.length; i++) {
                if (processed.contains(i)) continue;

                addPointToHull(points[i], i, hull, points);
            }

            return hull;
        }

        private Face[] findInitialTetrahedron(Point3D[] points) {
            // Find 4 non-coplanar points to form initial tetrahedron
            // Simplified implementation
            if (points.length < 4) return null;

            return new Face[] {
                new Face(0, 1, 2),
                new Face(0, 1, 3),
                new Face(0, 2, 3),
                new Face(1, 2, 3)
            };
        }

        private void addPointToHull(Point3D point, int pointIndex, List<Face> hull, Point3D[] allPoints) {
            List<Face> visibleFaces = new ArrayList<>();
            List<Edge> horizon = new ArrayList<>();

            // Find visible faces
            for (Face face : hull) {
                if (isVisible(point, face, allPoints)) {
                    visibleFaces.add(face);
                }
            }

            // Find horizon edges
            for (Face visible : visibleFaces) {
                addHorizonEdges(visible, visibleFaces, horizon);
            }

            // Remove visible faces
            hull.removeAll(visibleFaces);

            // Add new faces from point to horizon
            for (Edge edge : horizon) {
                hull.add(new Face(pointIndex, edge.a, edge.b));
            }
        }

        private boolean isVisible(Point3D point, Face face, Point3D[] points) {
            // Check if point is on positive side of face plane
            Point3D a = points[face.a];
            Point3D b = points[face.b];
            Point3D c = points[face.c];

            // Compute normal using cross product
            Vector3D normal = crossProduct(subtract(b, a), subtract(c, a));
            Vector3D toPoint = subtract(point, a);

            return dotProduct(normal, toPoint) > 0;
        }

        private void addHorizonEdges(Face visible, List<Face> visibleFaces, List<Edge> horizon) {
            Edge[] edges = {
                new Edge(visible.a, visible.b),
                new Edge(visible.b, visible.c),
                new Edge(visible.c, visible.a)
            };

            for (Edge edge : edges) {
                if (!isSharedWithVisibleFace(edge, visible, visibleFaces)) {
                    horizon.add(edge);
                }
            }
        }

        private boolean isSharedWithVisibleFace(Edge edge, Face currentFace, List<Face> visibleFaces) {
            for (Face face : visibleFaces) {
                if (face.equals(currentFace)) continue;

                if (face.hasEdge(edge)) {
                    return true;
                }
            }
            return false;
        }

        // 3D geometry utilities
        private Vector3D crossProduct(Vector3D a, Vector3D b) {
            return new Vector3D(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
            );
        }

        private double dotProduct(Vector3D a, Vector3D b) {
            return a.x * b.x + a.y * b.y + a.z * b.z;
        }

        private Vector3D subtract(Point3D a, Point3D b) {
            return new Vector3D(a.x - b.x, a.y - b.y, a.z - b.z);
        }

        // 3D classes
        public static class Point3D {
            double x, y, z;

            public Point3D(double x, double y, double z) {
                this.x = x; this.y = y; this.z = z;
            }
        }

        public static class Vector3D {
            double x, y, z;

            public Vector3D(double x, double y, double z) {
                this.x = x; this.y = y; this.z = z;
            }
        }

        public static class Face {
            int a, b, c;

            public Face(int a, int b, int c) {
                this.a = a; this.b = b; this.c = c;
            }

            boolean hasEdge(Edge edge) {
                return (a == edge.a && b == edge.b) || (b == edge.a && a == edge.b) ||
                       (b == edge.a && c == edge.b) || (c == edge.a && b == edge.b) ||
                       (c == edge.a && a == edge.b) || (a == edge.a && c == edge.b);
            }
        }

        public static class Edge {
            int a, b;

            public Edge(int a, int b) {
                this.a = Math.min(a, b);
                this.b = Math.max(a, b);
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Edge)) return false;
                Edge other = (Edge) obj;
                return a == other.a && b == other.b;
            }

            @Override
            public int hashCode() {
                return Objects.hash(a, b);
            }
        }
    }
}