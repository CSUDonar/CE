package com.donar.alg;

public class Edge {
	String nodename1;
	String nodename2;
	int weight=1;
	boolean isLaterAdded=false;
	public Edge(String nodename1, String nodename2, int weight) {
		this.nodename1 = nodename1;
		this.nodename2 = nodename2;
		this.weight = weight;
	}
	public String getNodename1() {
		return nodename1;
	}
	public void setNodename1(String nodename1) {
		this.nodename1 = nodename1;
	}
	public String getNodename2() {
		return nodename2;
	}
	public void setNodename2(String nodename2) {
		this.nodename2 = nodename2;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nodename1 == null) ? 0 : nodename1.hashCode());
		result = prime * result
				+ ((nodename2 == null) ? 0 : nodename2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
			Edge other = (Edge) obj;
			if ((nodename1.equals(other.nodename1)&&nodename2.equals(other.nodename2))||(nodename1.equals(other.nodename2)&&nodename2.equals(other.nodename1)))
			return true;
		
		return false;
	}
	
}
