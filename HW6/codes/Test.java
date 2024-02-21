import java.util.*;

public class Test {
    public static void main(String[] args) {
        DijkstraAlgorithm.Graph graph = new DijkstraAlgorithm.Graph();

        // Add stations
        graph.addStation("S1", "Station 1");
        graph.addStation("S2", "Station 2");
        graph.addStation("S3", "Station 3");
        graph.addStation("S4", "Station 4");
        graph.addStation("S5", "Station 5");

        // Add lines and edges
        graph.addLineToStation("S1", 5);
        graph.addEdge("S1", "S2", 4, 0);
        graph.addEdge("S2", "S3", 2, 3);
        graph.addEdge("S3", "S4", 1, 0);

        graph.addLineToStation("S4", 10);
        graph.addEdge("S4", "S5", 3, 0);

        // Perform Dijkstra's algorithm
        String sourceStationNumber = "S1";
        String destinationStationNumber = "S5";
        DijkstraAlgorithm.dijkstra(graph, sourceStationNumber, destinationStationNumber);
    }
}
class DijkstraAlgorithm {
    private static final int INF = Integer.MAX_VALUE;

    public static void dijkstra(Graph graph, String sourceStationNumber, String destinationStationNumber) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<Station> pq = new PriorityQueue<>(Comparator.comparingInt(station -> dist.get(station.stationNumber)));

        for (String stationNumber : graph.getStations().keySet()) {
            dist.put(stationNumber, INF);
            prev.put(stationNumber, null);
        }

        Station sourceStation = graph.getStation(sourceStationNumber);
        dist.put(sourceStation.stationNumber, 0);
        pq.add(sourceStation);

        while (!pq.isEmpty()) {
            Station currentStation = pq.poll();

            if (currentStation.stationNumber.equals(destinationStationNumber))
                break;

            for (Line line : currentStation.lines) {
                for (Edge edge : line.getEdges()) {
                    Station nextStation = edge.getDestination();
                    int transferTime = edge.getTransferTime();
                    int newDistance = dist.get(currentStation.stationNumber) + edge.getWeight() + transferTime;

                    if (newDistance < dist.get(nextStation.stationNumber)) {
                        dist.put(nextStation.stationNumber, newDistance);
                        prev.put(nextStation.stationNumber, currentStation.stationNumber);
                        pq.add(nextStation);
                    }
                }
            }
        }

        if (prev.get(destinationStationNumber) == null) {
            System.out.println("No path exists between the source and destination stations.");
            return;
        }

        // Construct the shortest path
        List<String> path = new ArrayList<>();
        String currentStationNumber = destinationStationNumber;
        while (currentStationNumber != null) {
            path.add(currentStationNumber);
            currentStationNumber = prev.get(currentStationNumber);
        }
        Collections.reverse(path);

        // Print the shortest path and its length
        System.out.println("Shortest path from station " + sourceStationNumber + " to station " + destinationStationNumber + ":");
        for (String stationNumber : path) {
            System.out.print(stationNumber + " ");
        }
        System.out.println("\nShortest path length: " + dist.get(destinationStationNumber));
    }

    static class Station implements Comparable<Station> {
        public String stationNumber;
        public String stationName;
        public List<Line> lines;

        Station(String stationNumber, String stationName) {
            this.stationNumber = stationNumber;
            this.stationName = stationName;
            this.lines = new ArrayList<>();
        }


        void addLine(Line line) {
            lines.add(line);
        }

        @Override
        public int compareTo(Station other) {
            return Integer.compare(this.getTransferTime(), other.getTransferTime());
        }

        private int getTransferTime() {
            int transferTime = 0;
            for (Line line : lines) {
                transferTime = Math.max(transferTime, line.getTransferTime());
            }
            return transferTime;
        }
    }

    static class Line {
        private List<Edge> edges;
        private int transferTime;

        Line(int transferTime) {
            this.edges = new ArrayList<>();
            this.transferTime = transferTime;
        }

        List<Edge> getEdges() {
            return edges;
        }

        int getTransferTime() {
            return transferTime;
        }

        void addEdge(Edge edge) {
            edges.add(edge);
        }
    }

    static class Edge {
        private Station destination;
        private int weight;
        private int transferTime;

        Edge(Station destination, int weight, int transferTime) {
            this.destination = destination;
            this.weight = weight;
            this.transferTime = transferTime;
        }

        Station getDestination() {
            return destination;
        }

        int getWeight() {
            return weight;
        }

        int getTransferTime() {
            return transferTime;
        }
    }

    static class Graph {
        private Map<String, Station> stations;

        Graph() {
            this.stations = new HashMap<>();
        }

        void addStation(String stationNumber, String stationName) {
            Station station = new Station(stationNumber, stationName);
            stations.put(stationNumber, station);
        }

        void addLineToStation(String stationNumber, int transferTime) {
            Station station = stations.get(stationNumber);
            Line line = new Line(transferTime);
            station.addLine(line);
        }

        void addEdge(String sourceStationNumber, String destinationStationNumber, int weight, int transferTime) {
            Station sourceStation = stations.get(sourceStationNumber);
            Station destinationStation = stations.get(destinationStationNumber);
            Line currentLine = sourceStation.lines.get(sourceStation.lines.size() - 1);
            Edge edge = new Edge(destinationStation, weight, transferTime);
            currentLine.addEdge(edge);
        }

        Map<String, Station> getStations() {
            return stations;
        }

        Station getStation(String stationNumber) {
            return stations.get(stationNumber);
        }
    }
}


