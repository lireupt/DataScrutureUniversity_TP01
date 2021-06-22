/**
* All material is for learning only.
* All who use this material - whatever the
* Purpose - must keep the reference to the original material, created by:
* Helder Oliveira / Denis Botnaru 
* and available at the address of your repository:
* https://github.com/lireupt/ public.
* This material may not be used in
* No circumstance to replicate - integral or
* partially - by authors/publishers to create books, and with
* purposes of obtaining financial gains from it.
*/


import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

class Line implements Comparable<Line> {
List<Point> points = new LinkedList<>();

// Adds one point at front
void addPoint(Point point) {
	points.add(0, point);
}

int size() {
	return points.size();
}

boolean contains(Point point) {
	return points.contains(point);
}

@Override
public String toString() {
	Point start = points.get(0);
	Point end = points.get(points.size() - 1);
	return String.format("((%d,%d),(%d,%d))", start.x, start.y, end.x, end.y);
}

// Lines are compared by their sizes in descending order
@Override
public int compareTo(Line line) {
	return line.size() - this.size();
}
}

