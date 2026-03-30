package com.routeoptimizer.controller;

import com.routeoptimizer.model.*;
import  com.routeoptimizer.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class RouteController {

    private double haversine(Node a, Node b) {
        double R = 6371;

        double dLat = Math.toRadians(b.x - a.x);
        double dLon = Math.toRadians(b.y - a.y);

        double lat1 = Math.toRadians(a.x);
        double lat2 = Math.toRadians(b.x);

        double aVal = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(aVal), Math.sqrt(1-aVal));

        return R * c;
    }

    @GetMapping("/route")
    public Map<String, Object> getRoute(@RequestParam String from, @RequestParam String to) {

        org.routeoptimizer.model.Graph graph = new org.routeoptimizer.model.Graph();



        // Create nodes
        Node hawaMahal = new Node("Hawa Mahal", 26.9239, 75.8267);
        Node cityPalace = new Node("City Palace", 26.9255, 75.8236);
        Node p1 = new Node("P1", 26.9305, 75.8302);
        Node p2 = new Node("P2", 26.9358, 75.8328);
        Node p3 = new Node("P3", 26.9402, 75.8355);
        Node jalMahal = new Node("Jal Mahal", 26.9535, 75.8462);
        Node p4 = new Node("P4", 26.9600, 75.8480);
        Node p5 = new Node("P5", 26.9652, 75.8489);
        Node p6 = new Node("P6", 26.9700, 75.8495);
        Node amberFort = new Node("Amber Fort", 26.9855, 75.8513);
        Node p7 = new Node("P7", 26.9000, 75.8200);
        Node p8 = new Node("P8", 26.8800, 75.8150);
        Node p9 = new Node("P9", 26.8600, 75.8130);
        Node airport = new Node("Airport", 26.8242, 75.8122);
        Node p10 = new Node("P10", 26.9100, 75.8250);

        // Old city route
        graph.addEdge(hawaMahal, cityPalace, (int) haversine(hawaMahal, cityPalace));
        graph.addEdge(cityPalace, p1, (int) haversine(cityPalace, p1));
        graph.addEdge(p1, p2, (int) haversine(p1, p2));
        graph.addEdge(p2, p3, (int) haversine(p2, p3));
        graph.addEdge(p3, jalMahal, (int) haversine(p3, jalMahal));

// Jal Mahal to Amber
        graph.addEdge(jalMahal, p4, (int) haversine(jalMahal, p4));
        graph.addEdge(p4, p5, (int) haversine(p4, p5));
        graph.addEdge(p5, p6, (int) haversine(p5, p6));
        graph.addEdge(p6, amberFort, (int) haversine(p6, amberFort));

// City to Airport route
        graph.addEdge(hawaMahal, p10, (int) haversine(hawaMahal, p10));
        graph.addEdge(p10, p7, (int) haversine(p10, p7));
        graph.addEdge(p7, p8, (int) haversine(p7, p8));
        graph.addEdge(p8, p9, (int) haversine(p8, p9));
        graph.addEdge(p9, airport, (int) haversine(p9, airport));

        Map<String, Node> map = new HashMap<>();

        map.put("Hawa Mahal", hawaMahal);
        map.put("City Palace", cityPalace);
        map.put("Jal Mahal", jalMahal);
        map.put("Amber Fort", amberFort);
        map.put("Airport", airport);
        Node start = map.get(from);
        Node goal = map.get(to);


        AStarService aStar = new AStarService();

        List<Node> path = aStar.findPath(graph, start, goal);

        // 👉 Calculate total distance
        double totalDistance = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            totalDistance += haversine(path.get(i), path.get(i + 1));
        }

        // 👉 Return JSON
        Map<String, Object> response = new HashMap<>();
        response.put("path", path);
        response.put("distance", totalDistance);

        return response;
    }
}