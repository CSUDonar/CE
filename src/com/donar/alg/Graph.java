package com.donar.alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class Graph {
	final int ADD_COST = 3;
	int[][] graph;
	HashMap<String, Integer> id2idx;
	HashMap<Integer, String> idx2id;
	int nodenum;
	HashSet<Edge> edges;
	List<String> nodes;
	int cursor = 0;// 当前处理到的点的位置
	int cost = 0;

	public Graph(HashSet<Edge> edgeList, List<String> nodeList, int cursor) {
		this.cursor = cursor;
		edges = new HashSet<>();
		edges.addAll(edgeList);
		nodes = nodeList;
		// 生成Id 和 index 之间的映射 获取定点的个数
		int i = 0;
		HashMap<String, Integer> id2idx = new HashMap<>();
		for (String node : nodeList) {
			if (!id2idx.containsKey(node)) {
				id2idx.put(node, i++);
			}
		}
		this.id2idx = id2idx;
		nodenum = i;

		// 将映射倒置 为 idx2id
		HashMap<Integer, String> idx2id = new HashMap<>();
		Set<Entry<String, Integer>> entrySet = id2idx.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			idx2id.put(entry.getValue(), entry.getKey());
		}
		this.idx2id = idx2id;

		// 生成邻接矩阵 初始化数据为0
		graph = new int[nodenum][nodenum];
		for (int j = 0; j < nodenum; j++) {
			for (int j2 = 0; j2 < nodenum; j2++) {
				graph[j][j2] = 0;
			}
		}

		// 将边及权值填入矩阵
		for (Edge edge : edgeList) {
			String nodename1 = edge.nodename1;
			String nodename2 = edge.nodename2;
			graph[id2idx.get(nodename1)][id2idx.get(nodename2)] = edge.weight;
			graph[id2idx.get(nodename2)][id2idx.get(nodename1)] = edge.weight;
		}
	}

	public boolean isComplete() {
		return (nodenum * (nodenum - 1) >> 1 == edges.size());
	}

	public boolean isConnect(int index1, int index2) {
		return graph[index1][index2] > 0;
	}

	// 针对无向图
	public void deleteEdge(int index1, int index2) {
		if (graph[index1][index2] > 0) {

			graph[index1][index2] = -1;
			graph[index2][index1] = -1;
			String one = idx2id.get(index1);
			String two = idx2id.get(index2);
			Iterator<Edge> it = edges.iterator();
			while (it.hasNext()) {
				Edge edge = it.next();
				if ((edge.nodename1.equals(one) && edge.nodename2.endsWith(two))
						|| (edge.nodename1.equals(two) && edge.nodename2
								.endsWith(one))) {
					cost += edge.weight;// 开销为边的权值
					it.remove();
					break;
				}
			}
			// System.out.println("delete "+index1 +","+index2);
		}
	}

	public void addEdge(int index1, int index2) {
		if (graph[index1][index2] <= 0) {

			graph[index1][index2] = ADD_COST;
			graph[index2][index1] = ADD_COST;
			String one = idx2id.get(index1);
			String two = idx2id.get(index2);
			Edge edge = new Edge(idx2id.get(index1), idx2id.get(index2),
					ADD_COST);
			edge.isLaterAdded = true;
			edges.remove(edge);
			edges.add(edge);
			cost += ADD_COST;
			// System.out.println("add    "+index1 +","+index2);
		}
	}

	public Graph copyGraph() {
		List<Edge> edges = new ArrayList<>();
		for (Edge e : this.edges) {
			edges.add(e);
		}

		List<String> nodes = new ArrayList<>();

		for (String n : this.nodes) {
			nodes.add(n);
		}
		return new Graph(this.edges, this.nodes, this.cursor);
	}

	public void printMartrix() {
		for (int i = 0; i < nodenum; i++) {
			for (int j = 0; j < nodenum; j++) {
				System.out.print((int) graph[i][j] + "\t");
			}
			System.out.println();
		}
	}

	public boolean isTriangle(int i, int j, int k) {
		return (graph[i][j] != 0 && graph[i][k] != 0 && graph[j][k] != 0);
	}

	public Edge findEdge(int m, int n) {
		String index1 = idx2id.get(m);
		String index2 = idx2id.get(n);
		Iterator<Edge> it = edges.iterator();
		Edge temp = null;
		while (it.hasNext()) {
			temp = it.next();
			if (temp.equals(new Edge(index1, index2, 0))) {
				return temp;
			}
		}
		return null;
	}

	public boolean canDoDelete(int m, int n) {
		Edge edge = findEdge(m, n);
		if (edge.isLaterAdded == false) {
			return true;
		}
		return false;
	}

	public boolean canDoAdd(int m, int n) {
		if (graph[m][n] == -1) {
			return false;
		}
		return true;
	}

	public void handleByRandom() {
		for (int i = this.cursor; i < nodenum; i++) {
			for (int j = 0; j < nodenum; j++) {
				if (!isConnect(i, j) && i != j) {
					for (int k = 0; k < nodenum; k++) {
						if (k == i || k == j)
							continue;
						if (isConnect(i, k) && isConnect(j, k)) {
							Random random = new Random();
							switch ((int) (random.nextInt(3))) {
							case 0:
								if (canDoDelete(i, k)) {
									deleteEdge(i, k);
									break;
								}
							case 1:
								if (canDoDelete(j, k)) {
									deleteEdge(j, k);
									break;
								}
							case 2:
								if (canDoAdd(i, j)) {
									addEdge(i, j);
									break;
								}
							default:
								for (int k2 = 0; k2 < nodenum; k2++) {
									for (int l = 0; l < nodenum; l++) {
										if (graph[k2][l] == -1) {
											graph[k2][l] = 0;
										}
									}
								}
								cursor = 0;
								handleByRandom();
							}
						}
					}
				}
			}
			cursor++;
		}
	}

	public void handleByCompare() {
		for (int i = this.cursor; i < nodenum; i++) {
			for (int j = 0; j < nodenum; j++) {
				if (!isConnect(i, j) && i != j) {
					for (int k = 0; k < nodenum; k++) {
						if (k == i || k == j)
							continue;
						if (isConnect(i, k) && isConnect(j, k)) {
							int a = 0;
							int addcost = getTotalAddCost(i, j);
							int deletecost1 = getTotalDeleteCost(k, i);
							int deletecost2 = getTotalDeleteCost(k, j);
							if (addcost <= deletecost1
									&& addcost <= deletecost2)
								a = 0;
							if (deletecost1 <= addcost
									&& deletecost1 <= deletecost2)
								a = 1;
							if (deletecost2 <= addcost
									&& deletecost2 <= deletecost1)
								a = 2;
							switch (a) {
							case 0:
								if (canDoAdd(i, j)) {
									addEdge(i, j);
									break;
								}
								
							case 1:
								if (canDoDelete(i, k)) {
									deleteEdge(i, k);
									break;
								}
								
							case 2:
								if (canDoDelete(j, k)) {
									deleteEdge(j, k);
									break;
								}
							default:
								for (int k2 = 0; k2 < nodenum; k2++) {
									for (int l = 0; l < nodenum; l++) {
										if (graph[k2][l] == -1) {
											graph[k2][l] = 0;
										}
									}
								}
								cursor = 0;
								handleByRandom();
							}
						}
					}
				}
			}
			cursor++;
		}
	}

	/**
	 * 
	 * @param index
	 *            i
	 * @param index2
	 *            j
	 * @return
	 */
	public int getTotalAddCost(int index, int index2) {
		int totalcost = 0;
		for (int i = 0; i < nodenum; i++) {
			if (graph[index][i] <= 0 && graph[index2][i] > 0) {
				totalcost += ADD_COST;
			}
		}
		
		for (int i = 0; i < nodenum; i++) {
			if (graph[index2][i] <= 0 && graph[index][i] > 0) {
				totalcost += ADD_COST;
			}
		}
		
		totalcost-=ADD_COST;
		
		return totalcost;
	}

	/**
	 * 
	 * @param index
	 *            度为2 的点
	 * @param index2
	 *            度为1 的点
	 */
	public int getTotalDeleteCost(int index, int index2) {
		int totalcost = 0;
		for (int i = 0; i < nodenum; i++) {
			if (graph[index][i] > 0 && graph[index2][i] > 0) {
				totalcost += graph[index2][i];
			}
		}
		return totalcost;
	}

	public int[][] getGraph() {
		return graph;
	}

	public void setGraph(int[][] graph) {
		this.graph = graph;
	}

	public HashMap<String, Integer> getId2idx() {
		return id2idx;
	}

	public void setId2idx(HashMap<String, Integer> id2idx) {
		this.id2idx = id2idx;
	}

	public HashMap<Integer, String> getIdx2id() {
		return idx2id;
	}

	public void setIdx2id(HashMap<Integer, String> idx2id) {
		this.idx2id = idx2id;
	}

	public int getNodenum() {
		return nodenum;
	}

	public void setNodenum(int nodenum) {
		this.nodenum = nodenum;
	}

	public HashSet<Edge> getEdges() {
		return edges;
	}

	public void setEdges(HashSet<Edge> edges) {
		this.edges = edges;
	}

	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getADD_COST() {
		return ADD_COST;
	}

}
