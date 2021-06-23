/**
   * All material is for learning only.
   * All who use this material - whatever the purpose:
   * - must keep the reference to the original material created by:
   * - Helder Oliveira / Denis Botnaru
   * and available at the address of your repository:
   * - https://github.com/lireupt/ public.
   * This material may not be used in no circumstance to replicate:
   * - integral or partially, with purposes of obtaining financial gains from it.
   */


import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class Data {
				
	private int[][] data; // 2d array declaration containing bitmap data
	private List<Point> joints;
	private Queue<Point> endPoints;
	private boolean[][] visited;

	public void readPbm(String filename) throws IOException {
		Scanner sc = new Scanner(new File(filename));
		
		// Read formet file P2
		if (!sc.nextLine().equals("P2")) {
			sc.close();
			throw new IOException("Format not supported");
		}
		
		// Reads width, height, and maximum value of bitmap file
		int width = sc.nextInt();	
		int height = sc.nextInt();
		int maxValue = sc.nextInt();
		data = new int[height][width];
		
		// Read data from bitmap
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				data[y][x] = sc.nextInt();
			}
		}
		sc.close();
	}
		
	public void detectJoints() {
		joints = new ArrayList<>();
		for (int x = 0; x < data[0].length; x++) {
			for (int y = 0; y < data.length; y++) {
				if (isPoint(x, y) && countNeighbors(x, y) > 2) {
					// Here we do not add the point that has 2 or more neighbors
					if (countNeighborsJoint(x, y) < 2) {
						joints.add(new Point(x, y));
					}
				}
			}
		}
		System.out.println("Junções:");
		joints.stream().forEach(point -> {
			System.out.printf("(%d,%d)\n", point.x, point.y);
		});
	}
	
	public void detectEndPoints() {
		endPoints = new LinkedList<Point>();
		for (int x = 0; x < data[0].length; x++) {
			for (int y = 0; y < data.length; y++) {
				if (isPoint(x, y) && countNeighbors(x, y) == 1) {
					endPoints.add(new Point(x, y));
				}
			}
		}
		System.out.println("EndPoints:");
		endPoints.stream().forEach(point -> {
			System.out.printf("(%d,%d)\n", point.x, point.y);
		});
	}
	
	public void compareLines() {
		List<Line> lines = new ArrayList<>();
		visited = new boolean[data.length][data[0].length];
		
		// Find the lines between endpoints and joins
		endPoints.stream().forEach(endPoint -> {
			markJointsUnvisited();
			lines.add(findLine(endPoint));
		});
		
		// Find lines between joins
		joints.stream().forEach(joint -> {
			markJointsUnvisited();
			Line line = findLine(joint);
			// Lines with 2 joins are not added to the array
			if (line.size() > 2) {
				lines.add(line);
			}
		});
		
		Collections.sort(lines);
		System.out.println("Linhas:");
		lines.stream().forEach(line -> {
			System.out.println(line);
		});
	}
	
	// Marks all unvisited joins
	private void markJointsUnvisited() {
		joints.stream().forEach(joint -> {
			visited[joint.y][joint.x] = false;
		});
	}
	
	// Find the beginning of a line on a given endpoint
	private Line findLine(Point endPoint) {
		Line line = new Line();
		Stack<Point> stack = new Stack<>();
		Point current = null;
		
		stack.push(endPoint);
		visited[endPoint.y][endPoint.x] = true;
		current = endPoint;
		
		Point newPoint;
		while (!stack.isEmpty()) {
			if (!current.equals(endPoint) && (isJoint(current) || isEndPoint(current))) {
				break;
			}
			
			// Search for neighbors to the north
			newPoint = findNewPoint(current.x, current.y - 1, stack);
			if (newPoint != null) {
				current = newPoint;
				continue;
			}
			
			// Search for neighbors to the northeast
			newPoint = findNewPoint(current.x + 1, current.y - 1, stack);
			if (newPoint != null) {
				current = newPoint;
				continue;
			}
			
			// Search neighbors to the east
			newPoint = findNewPoint(current.x + 1, current.y, stack);
			if (newPoint != null) {
				current = newPoint;
				continue;
			}
			
			// Search for neighbors in the southeast
			newPoint = findNewPoint(current.x + 1, current.y + 1, stack);
			if (newPoint != null) {
				current = newPoint;
				continue;
			}
			
			// Search for neighbors to the south
			newPoint = findNewPoint(current.x, current.y + 1, stack);
			if (newPoint != null) {
				current = newPoint;
				continue;
			}
			
			// Search for neighbors to the southwest
			newPoint = findNewPoint(current.x - 1, current.y + 1, stack);
			if (newPoint != null) {
				current = newPoint;
				continue;
			}
			
			// Search for neighbors to the west
			newPoint = findNewPoint(current.x - 1, current.y, stack);
			if (newPoint != null) {
				current = newPoint;
				continue;
			}
			
			// Search for neighbors to the northwest
			newPoint = findNewPoint(current.x - 1, current.y - 1, stack);
			if (newPoint != null) {
				current = newPoint;
				continue;
			}
			
			// When it comes to the end, guard and build the line
			line.addPoint(stack.pop());
			if (stack.isEmpty())
				break;
			current = stack.peek();
		}
		
		// Adds Stack points to the line
		while (!stack.empty()) {
			line.addPoint(stack.pop());
		}
		return line;
	}
	
	// Find a new point a (x,y) that has not yet been visited, push to stack and mark as visited
	// Returns 0 if not found
	private Point findNewPoint(int x, int y, Stack<Point> stack) {
		if (!visited[y][x] && isPoint(x, y)) {
			Point point = new Point(x, y);
			visited[y][x] = true;
			stack.push(point);
			return point;
		}
		return null;
	}
	
	// Returns true if a given point is within the joints list
	private boolean isJoint(Point point) {
		return joints.contains(point);
	}
	
	// Returns true if a given point is in the endPoint list
	private boolean isEndPoint(Point point) {
		return endPoints.contains(point);
	}

	// Returns neighbours number
	private int countNeighbors(int x, int y) {
		return countPoint(x - 1, y) + countPoint(x - 1, y - 1) +
			   countPoint(x, y - 1) + countPoint(x + 1, y - 1) +
			   countPoint(x + 1, y) + countPoint(x + 1, y + 1) +
			   countPoint(x, y + 1) + countPoint(x - 1, y + 1);
	}
	
	// Returns true if the value y,x == 0
	private boolean isPoint(int x, int y) {
		return x >= 0 && x < data[0].length && y >= 0 && y < data.length && data[y][x] == 0;
	}
	
    // Returns 1 if x,y is valid point, else 0
	private int countPoint(int x, int y) {
		return isPoint(x, y) ? 1 : 0;
	}
	
	// Returns a number of Joints
	private int countNeighborsJoint(int x, int y) {
		return countPointJoint(x - 1, y) + countPointJoint(x - 1, y - 1) +
				countPointJoint(x, y - 1) + countPointJoint(x + 1, y - 1) +
				countPointJoint(x + 1, y) + countPointJoint(x + 1, y + 1) +
				countPointJoint(x, y + 1) + countPointJoint(x - 1, y + 1);
	}
	
	// Returns 1 if y,x is a valid point and this within the joint list, so it returns 0
	private int countPointJoint(int x, int y) {
		return (isPoint(x, y) && isJoint(new Point(x, y))) ? 1 : 0;
	}
	
}	