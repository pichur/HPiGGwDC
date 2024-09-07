package edu.put.pw.hpiggwdc;

public class Vertex {
	int i;
	double x, y;

    public Vertex(int i, double x, double y) {
    	this.i = i;
        this.x = x;
        this.y = y;
    }
    
    public double distanceTo(Vertex other) {
    	double dx = this.x - other.x;
    	double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
