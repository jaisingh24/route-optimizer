package com.routeoptimizer.service;

import com.routeoptimizer.model.*;
import java.util.*;

public class DijkstraService {

    public Map<Node, Double> shortestPath(org.routeoptimizer.model.Graph graph, Node source) {

        Map<Node, Double> dist = new HashMap<>();

        PriorityQueue<NodeDistance> pq =
                new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));

        for (Node node : graph.adjList.keySet()) {
            dist.put(node, Double.MAX_VALUE);
        }

        dist.put(source, 0.0);
        pq.add(new NodeDistance(source, 0.0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();

            for (Edge edge : graph.adjList.get(current.node)) {

                double newDist = dist.get(current.node) + edge.getWeight();

                if (newDist < dist.get(edge.getTarget())) {
                    dist.put(edge.getTarget(), newDist);
                    pq.add(new NodeDistance(edge.getTarget(), newDist));
                }
            }
        }

        return dist;
    }
}