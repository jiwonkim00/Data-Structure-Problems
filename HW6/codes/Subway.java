import java.io.*;
import java.util.*;
public class Subway {

    public static HashMap<String, Station> find_by_name;    //subway name -> get Station
    public static HashMap<String, String> find_by_number;  //subway number -> get name
    private static final int INFINITY = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        //BufferedReader bfr = new BufferedReader(new FileReader(args[0]));
        BufferedReader bfr = new BufferedReader(new FileReader("./text/subway.txt"));
        String input;
        find_by_name = new HashMap<>();
        find_by_number = new HashMap<>();

        // TODO : Save the input in graph
        // 1) Save vertex
        while ((input = bfr.readLine()) != null) {   //0: subway number 1: subway name 2: line number
            String[] info = input.split(" ");
            if (info.length != 3) break;
            String number = info[0];
            String name = info[1];
            String line = info[2];

            find_by_number.put(number, name);

            if (find_by_name.get(name) == null) {   // new station add
                Station new_station = new Station(name, number, line);
                find_by_name.put(name, new_station);

            } else {    // line add
                find_by_name.get(name).number_and_line.put(number, line);
            }

        }

        // 2) Save edges
        while ((input = bfr.readLine()) != null) {  //0: from 1: to 2: weight of edge
            String[] edge_info = input.split(" ");
            if (edge_info.length != 3) break;

            String from_num = edge_info[0];
            String from_name = find_by_number.get(from_num);
            String to_num = edge_info[1];
            String to_name = find_by_number.get(to_num);
            int weight = Integer.parseInt(edge_info[2]);

            String line = find_by_name.get(from_name).number_and_line.get(from_num);

            Edge edge = new Edge(from_name, to_name, weight, line);   //destination, weight info
            find_by_name.get(from_name).add_edge(edge);

        }

        // 3) Save transfer time
        while ((input = bfr.readLine()) != null) {  //0: name 2: transfer time
            String[] trsf_info = input.split(" ");
            String name = trsf_info[0];
            int trsf_time = Integer.parseInt(trsf_info[1]);
            find_by_name.get(name).transfer_time = trsf_time;

        }

        // TODO : Find the shortest path by Dijkstra
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true)
        {
            try
            {
                input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input);
            }
            catch (IOException e)
            {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }


    }
    private static void command(String input) {
        // 1) Parse input, 2) Perform Dijkstra (print result)

        //1)
        String[] stations = input.split(" ");
        String from_name = stations[0];
        String to_name = stations[1];

        //2)
        dijkstra(from_name, to_name);

        //for test
//        System.out.println(from_name +" info: ");
//        for (String x : find_by_name.get(from_name).number_and_line.keySet()) {
//            System.out.println("# " + x + " (line " +find_by_name.get(from_name).number_and_line.get(x) + ")");
//        }
//        System.out.println("Transfer time: " + find_by_name.get(from_name).transfer_time);
//        System.out.println("Adjacent edges: ");
//        for (Edge x : find_by_name.get(from_name).adj_stations) {
//            System.out.println(x.start + " - " + x.destination + " (" + x.weight+ ")");
//        }

    }

    private static void dijkstra(String source, String destination) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, Edge> previous = new HashMap<>();
        //List<Edge> path = new ArrayList<>();
        HashSet<String> visited = new HashSet<>();

        for (String station : find_by_name.keySet()) {
            distances.put(station, INFINITY);
            previous.put(station, null);
        }
        distances.put(source, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(v -> distances.get(v)));

        String current = source;
        visited.add(current);
        Edge last_edge;

        while (visited.size() != find_by_name.keySet().size()) {
            System.out.println(current);
            // System.out.println("Current: " + current + "(TF: " + find_by_name.get(current).transfer_time + "), " + "(D: " + distances.get(current) + ")");
//            System.out.print("Queue: ");
//            for (String x : pq) {
//                System.out.print(x + "(" + distances.get(x) + ") ");
//            }
//            System.out.println();

//            if (current.equals(destination))
//                break;

            //last_edge = previous.get(current);
            for (Edge edge : find_by_name.get(current).adj_stations) {
                last_edge = previous.get(current);
//                if (visited.contains(edge.destination)) {
//                    //System.out.println("Visited: " + edge.destination);
//                    continue;
//                }

                //edge.print_edge();
                String next = edge.destination;

                int transfer = 0;

                if (last_edge != null) {

                    //System.out.println("last_edge line: " + last_edge.line + ", this_edge line: " + edge.line  );
                    if (!Objects.equals(edge.line, last_edge.line)) {
                        transfer = find_by_name.get(current).transfer_time;
                        //System.out.println("Need to transfer");
                    }
                }
                int newDistance = distances.get(current) + edge.weight + transfer;

//                Edge changed = null;
//
//                if (transfer != 0) {
//                    //System.out.println("Let's Compare!");
//                    for (Edge e : find_by_name.get(current).adj_stations) {
//                        if (visited.contains(e.destination) && (Objects.equals(edge.line, e.line))) {
//                            //e.print_edge();
//                            int compareDistance = distances.get(e.destination) + find_by_name.get(e.destination).transfer_time + e.weight + edge.weight;
//                            if (compareDistance < newDistance) {
//                                newDistance = compareDistance;
//                                changed = e;
//                                //System.out.println("Previous changed");
//                            }
//                        }
//                    }
//
//                }


                if (newDistance < distances.get(next)) {
                    //System.out.println(next+ "old D: " + distances.get(next)+", new D: " + newDistance);
                    distances.put(next, newDistance);
//                    if (changed != null) {
//                        //previous.put(current, changed);
//                    }

                    previous.put(next, edge);
                    pq.offer(next);
                }
            }
            current = pq.poll();
            visited.add(current);

        }

        List<String> shortestPath = new ArrayList<>();
        current = destination;
        Edge this_edge = previous.get(current);
        shortestPath.add(0, current);
        Edge edge;

        while (true) {   //current != null ?
            current = this_edge.start;
            edge = previous.get(current);
            if (edge == null) break;

            if (!Objects.equals(this_edge.line, edge.line)) {
                shortestPath.add(0, "[" + current + "]");
                //System.out.println("TF " + current);
            } else {
                shortestPath.add(0, current);
                //System.out.println(current);
            }
            this_edge = edge;
        }
        shortestPath.add(0, source);    //add the first

        StringBuilder stb = new StringBuilder();
        for (String station : shortestPath) {
            stb.append(station).append(" ");
        }
        System.out.println(stb.toString().trim());

        int totalDistance = distances.get(destination);
        System.out.println(totalDistance);
    }
}

class Station implements Comparable<Station>{
    public String station_name;
    public Map<String, String> number_and_line;
    public String station_number;
    public List<String> line;
    public List<Edge> adj_stations;
    public int transfer_time;
    int distance;
    Edge used_edge;

    public Station(String name, int dis) {
        this.station_name = name;
        this.distance = dis;
    }

    public Station(String name, String number, String line) {
        station_name = name;
        number_and_line = new HashMap<>();
        number_and_line.put(number, line);
        station_number = number;
        this.line = new ArrayList<>();
        this.line.add(line);
        adj_stations = new ArrayList<>();
        used_edge = null;
        transfer_time = 5;
    }

    public void add_edge(Edge edge) {
        adj_stations.add(edge);
    }
    public int compareTo(Station obj) {
        return Integer.compare(this.distance, obj.distance);

    }

}
class Edge {
    public String start;
    public String destination;
    public int weight;
    public String line;

    public Edge(String start, String name, int w, String line) {
        this.start = start;
        destination = name;
        weight = w;
        this.line = line;
    }
    public void print_edge() {
        System.out.println(start + " - " + destination + "(line: "+ line + ", weight: " + weight+")");
    }

}