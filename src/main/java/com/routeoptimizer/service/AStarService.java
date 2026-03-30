package com.routeoptimizer.service;

import com.routeoptimizer.model.*;
import java.util.*;

public class AStarService {

    public List<Node> findPath(org.routeoptimizer.model.Graph graph, Node start, Node goal) {

        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Double> fScore = new HashMap<>();
        Map<Node, Node> cameFrom = new HashMap<>();

        PriorityQueue<NodeDistance> openSet =
                new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));

        // Initialize scores
        for (Node node : graph.adjList.keySet()) {
            gScore.put(node, Double.MAX_VALUE);
            fScore.put(node, Double.MAX_VALUE);
        }

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));

        openSet.add(new NodeDistance(start, fScore.get(start)));

        while (!openSet.isEmpty()) {

            Node current = openSet.poll().node;

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            for (Edge edge : graph.adjList.get(current)) {

                Node neighbor = edge.getTarget();

                // Safety check
                gScore.putIfAbsent(neighbor, Double.MAX_VALUE);

                double tentative = gScore.get(current) + edge.getWeight();

                if (tentative < gScore.get(neighbor)) {

                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentative);

                    double f = tentative + heuristic(neighbor, goal);
                    fScore.put(neighbor, f);

                    openSet.add(new NodeDistance(neighbor, (int)f));
                }
            }
        }

        return new ArrayList<>();
    }

    private double heuristic(Node a, Node b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {

        List<Node> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }
}