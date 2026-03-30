package com.routeoptimizer.service;



import com.routeoptimizer.model.Node;

public class NodeDistance {
    public Node node;
    public double distance;

    public NodeDistance(Node node, double distance) {
        this.node = node;
        this.distance = distance;
    }
}
