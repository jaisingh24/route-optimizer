package org.routeoptimizer.model;

import com.routeoptimizer.model.Edge;
import com.routeoptimizer.model.Node;

import java.util.*;

public class Graph {

    public Map<Node, List<Edge>> adjList = new HashMap<>();

    public void addNode(Node node) {
        adjList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node source, Node dest, int weight) {
        adjList.putIfAbsent(source, new ArrayList<>());
        adjList.putIfAbsent(dest, new ArrayList<>());
       adjList.get(source).add(new Edge(dest, weight));
        adjList.get(dest).add(new Edge(source, weight));
    }

    public java.util.Map<Node, java.util.List<Edge>> getAdjList() {
        return adjList;
    }

    public void setAdjList(java.util.Map<Node, java.util.List<Edge>> adjList) {
        this.adjList = adjList;
    }
}