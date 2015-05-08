package com.donar.alg;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.donar.show.MyFrame;


public class Main {
	public static void main(String[] args) {
		String[] nodes={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
//		String[] nodes={"A","B","C","D","E","F"};
		List<String> nodeList= Arrays.asList(nodes);
		HashSet<Edge> edgeList= new HashSet<>();
		Random r = new Random();
		for (int i = 0; i <50; i++) {
			int m=0,n=0;
			
			while(m==n){
				m=r.nextInt(26);
				n=r.nextInt(26);
			}
			edgeList.add(new Edge(nodes[m], nodes[n], 5));
		}
		
		
		
//		edgeList.add(new Edge("A", "B", 5));
//		edgeList.add(new Edge("A", "E", 5));
//		edgeList.add(new Edge("B", "D", 5));
//		edgeList.add(new Edge("C", "E", 5));
//		edgeList.add(new Edge("D", "E", 5));
//		edgeList.add(new Edge("F", "E", 5));

//		edgeList.add(new Edge("A", "B", 5));
//		edgeList.add(new Edge("B", "C", 5));
//		edgeList.add(new Edge("C", "D", 5));
//		edgeList.add(new Edge("D", "E", 5));
//		edgeList.add(new Edge("E", "F", 5));
	
		//�����
		Graph graph =null;
		Graph graphbest=null;
		int bestcost=1000000;
		Graph graphorg =new Graph(edgeList, nodeList, 0);
		for (int i = 0; i < 3000; i++) {
			graph= new Graph(edgeList, nodeList, 0);
			graph.handleByRandom();
			if (graph.cost<bestcost) {
				bestcost=graph.cost;
				graphbest=graph;
				System.out.println("random :"+graphbest.cost);
			}
		}
		new MyFrame(graphbest,100,200,"ramdom result");
		new MyFrame(graphorg,940,200,"original");
		
		
		graph =new Graph(edgeList, nodeList, 0);
		graph.handleByCompare();
		Graph graph2 =new Graph(edgeList, nodeList, 0);
		System.out.println("compare :" +graph.cost);
		new MyFrame(graph,520,200,"compare result");
	}
}
