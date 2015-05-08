package com.donar.show;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;

import com.donar.alg.Graph;

public class GraphPaint extends PApplet {
	 Graph graph;
	 public GraphPaint(Graph graph){
		 this.graph=graph;
	 }
	int nodeCount;
	Node[] nodes = new Node[100];
	HashMap nodeTable = new HashMap();

	int edgeCount;
	Edge[] edges = new Edge[500];

	static final Color nodeColor = new Color(204,255,102);
	static final Color selectColor = Color.blue;
	static final Color fixedColor = Color.gray;
	static final Color edgeColor = new Color(255,153,0);

	PFont font;

	public void setup() {
		size(400, 400);
		loadData();
		font = createFont("SansSerif", 10);
	}

	void loadData() {
		List<String> nodesList=graph.getNodes();
		for (String string : nodesList) {
			addNode(string);
		}
		int[][] matrix=graph.getGraph();
		for (int i = 0; i < graph.getNodenum(); i++) {
			for (int j = 0; j <i; j++) {
				if(matrix[i][j]>0){
					addEdge(graph.getIdx2id().get(i), graph.getIdx2id().get(j));
				}
			}
		}
	}

	void addEdge(String fromLabel, String toLabel) {
		
		
		Node from = findNode(fromLabel);
		Node to = findNode(toLabel);
		from.increment();
		to.increment();

		for (int i = 0; i < edgeCount; i++) {
			if (edges[i].from == from && edges[i].to == to) {
				edges[i].increment();
				return;
			}
		}

		Edge e = new Edge(from, to);
		e.increment();
		if (edgeCount == edges.length) {
			edges = (Edge[]) expand(edges);
		}
		edges[edgeCount++] = e;
	}

	

	Node findNode(String label) {
		Node n = (Node) nodeTable.get(label);
		if (n == null) {
		}
		return n;
	}

	Node addNode(String label) {
		Node n = new Node(label);
		if (nodeCount == nodes.length) {
			nodes = (Node[]) expand(nodes);
		}
		nodeTable.put(label, n);
		nodes[nodeCount++] = n;
		return n;
	}

	public void draw() {
		// if (record) {
		// beginRecord(PDF, "output.pdf");
		// }

		background(153,0,51);
		textFont(font);
		smooth();

		for (int i = 0; i < edgeCount; i++) {
			edges[i].relax();
		}
		for (int i = 0; i < nodeCount; i++) {
			nodes[i].relax();
		}
		for (int i = 0; i < nodeCount; i++) {
			nodes[i].update();
		}
		for (int i = 0; i < edgeCount; i++) {
			edges[i].draw();
		}
		for (int i = 0; i < nodeCount; i++) {
			nodes[i].draw();
		}

//		if (record) {
//			endRecord();
//			record = false;
//		}
	}

	// boolean record;
	//
	// public void keyPressed() {
	// if (key == 'r') {
	// record = true;
	// }
	// }

	Node selection;

	public void mousePressed() {
		// Ignore anything greater than this distance
		float closest = 20;
		for (int i = 0; i < nodeCount; i++) {
			Node n = nodes[i];
			float d = dist(mouseX, mouseY, n.x, n.y);
			if (d < closest) {
				selection = n;
				closest = d;
			}
		}
		if (selection != null) {
			if (mouseButton == LEFT) {
				selection.fixed = true;
			} else if (mouseButton == RIGHT) {
				selection.fixed = false;
			}
		}
	}

	public void mouseDragged() {
		if (selection != null) {
			selection.x = mouseX;
			selection.y = mouseY;
		}
	}

	public void mouseReleased() {
		selection = null;
	}

	class Node {
		float x, y;
		float dx, dy;
		boolean fixed;
		String label;
		int count=5;

		Node(String label) {
			this.label = label;
			x = random(width-10);
			y = random(height-10);
		}

		void increment() {
			count++;
		}

		void relax() {
			float ddx = 0;
			float ddy = 0;

			for (int j = 0; j < nodeCount; j++) {
				Node n = nodes[j];
				if (n != this) {
					float vx = x - n.x;
					float vy = y - n.y;
					float lensq = vx * vx + vy * vy;
					if (lensq == 0) {
						ddx += random(1);
						ddy += random(1);
					} else if (lensq < 100 * 100) {
						ddx += vx / lensq;
						ddy += vy / lensq;
					}
				}
			}
			float dlen = mag(ddx, ddy) / 2;
			if (dlen > 0) {
				dx += ddx / dlen;
				dy += ddy / dlen;
			}
		}

		void update() {
			if (!fixed) {
				x += constrain(dx, -5, 5);
				y += constrain(dy, -5, 5);

				x = constrain(x, 0, width);
				y = constrain(y, 0, height);
			}
			dx /= 2;
			dy /= 2;
		}

		public void draw() {
			fill(nodeColor.getRGB());
			stroke(0);
			strokeWeight(0.5f);

			ellipse(x, y, 12, 12);
			float w = textWidth(label);

			
				fill(0);
				textAlign(CENTER, CENTER);
				text(label, x, y);
			
		}
	}

	class Edge {
		Node from;
		Node to;
		float len;
		int count;

		Edge(Node from, Node to) {
			this.from = from;
			this.to = to;
			this.len = 50;
		}

		void increment() {
			count++;
		}

		void relax() {
			float vx = to.x - from.x;
			float vy = to.y - from.y;
			float d = mag(vx, vy);
			if (d > 0) {
				float f = (len - d) / (d * 3);
				float dx = f * vx;
				float dy = f * vy;
				to.dx += dx;
				to.dy += dy;
				from.dx -= dx;
				from.dy -= dy;
			}
		}

		void draw() {
			stroke(edgeColor.getRGB());
			strokeWeight(0.35f);
			line(from.x, from.y, to.x, to.y);
		}
	}
}
