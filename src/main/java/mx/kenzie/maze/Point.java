package mx.kenzie.maze;

import java.util.*;

/**
 * A 2D lattice point, representing either a wall cell or a path cell.
 * */
public record Point(int x, int y) implements Path {

    public boolean isNextTo(Point point) {
        if (point.x != this.x && point.y != this.y) return false;
        return Math.abs(point.x - x) < 2 && Math.abs(point.y - y) < 2;
    }

    public Point correct() {
        if (x % 2 == 0 && y % 2 == 0) return this;
        return new Point(x / 2 * 2, y / 2 * 2);
    }

    @Override
    public Path union(Path other) {
        return new FixedPath(this).union(other);
    }

    @Override
    public Path intersect(Path other) {
        return other.contains(this) ? this : new FixedPath();
    }

    @Override
    public void cut(Maze maze) {
        final State current = maze.get(this);
        if (current == null) return;
        maze.set(this, switch (current) {
            case WALL -> State.EMPTY;
            default -> State.INTERSECTION;
        });

    }

    @Override
    public void cut(Maze maze, State state) {
        maze.set(this, state);
    }

    @Override
    public Point last() {
        return this;
    }

    @Override
    public Point first() {
        return this;
    }

    @Override
    public Collection<Point> collect() {
        return Collections.singleton(this);
    }

    @Override
    public boolean contains(Point point) {
        return this.equals(point);
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public Iterator<Point> iterator() {
        return Collections.singleton(this).iterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
