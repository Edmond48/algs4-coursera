/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

// TODO document the code or you won't be able to read it later

public class BaseballElimination {

    private final int n;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    private final String[] teamName;
    private final BST<String, Integer> teamID = new BST<String, Integer>();

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        // read from input stream
        In in = new In(filename);

        // build data structure
        n = Integer.parseInt(in.readLine());
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        teamName = new String[n];
        for (int i = 0; i < n; i++) {
            String name = in.readString();
            teamID.put(name, i);
            teamName[i] = name;
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return teamID.keys();
    }

    // number of wins for given team
    public int wins(String team) {
        throwIfNotValid(team);
        int id = teamID.get(team);
        return w[id];
    }

    // number of losses for given team
    public int losses(String team) {
        throwIfNotValid(team);
        int id = teamID.get(team);
        return l[id];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        throwIfNotValid(team);
        int id = teamID.get(team);
        return r[id];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        throwIfNotValid(team1, team2);
        int id1 = teamID.get(team1);
        int id2 = teamID.get(team2);
        return g[id1][id2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

        throwIfNotValid(team);

        int id = teamID.get(team);
        int potential = w[id] + r[id];
        for (int i = 0; i < n; i++) {
            if (potential < w[i]) return true;
        }

        FlowNetwork flowNet = networkBuilder(team);
        int s = n * n / 2 - n / 2;
        int t = s + 1;
        FordFulkerson query = new FordFulkerson(flowNet, s, t);
        int maxCapacity = 0;
        for (FlowEdge e : flowNet.adj(s)) {
            maxCapacity += e.capacity();
        }
        if (maxCapacity > query.value()) return true;
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

        throwIfNotValid(team);

        Queue<String> iterator = new Queue<String>();
        int id = teamID.get(team);
        int potential = w[id] + r[id];
        for (int i = 0; i < n; i++) {
            if (potential < w[i]) {
                iterator.enqueue(teamName[i]);
                return iterator;
            }
        }

        FlowNetwork flowNet = networkBuilder(team);
        int s = n * n / 2 - n / 2;
        int t = s + 1;
        FordFulkerson query = new FordFulkerson(flowNet, s, t);
        int maxCapacity = 0;
        for (FlowEdge e : flowNet.adj(s)) {
            maxCapacity += e.capacity();
        }
        if (maxCapacity > query.value()) {
            int dif = s - n + 1;
            for (int j = dif; j < s; j++) {
                int currentID = j - dif;
                if (currentID >= id) currentID++;
                if (query.inCut(j)) iterator.enqueue(teamName[currentID]);
            }
            return iterator;
        }
        return null;
    }

    // helper method to build a digraph
    private FlowNetwork networkBuilder(String team) {
        int id = teamID.get(team);
        int s = n * n / 2 - n / 2;
        int t = s + 1;
        FlowNetwork net = new FlowNetwork(t + 1);
        int matchUp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i == id || j == id) continue;
                if (g[i][j] <= 0) matchUp++;
                else net.addEdge(new FlowEdge(s, matchUp++, g[i][j]));
            }
        }

        int teamVer = matchUp;

        int currentTeam = 0;
        for (int k = 0; k < n - 2; k++) {
            for (int m = 0; m < n - 2 - k; m++) {
                net.addEdge(new FlowEdge(currentTeam + m, matchUp, Double.POSITIVE_INFINITY));
                net.addEdge(
                        new FlowEdge(currentTeam + m, matchUp + m + 1, Double.POSITIVE_INFINITY));
            }
            currentTeam += n - 2 - k;
            matchUp++;
        }

        for (int p = teamVer; p < s; p++) {
            if (p - teamVer == id) teamVer--;
            int cap = w[id] + r[id] - w[p - teamVer];
            if (cap > 0) net.addEdge(new FlowEdge(p, t, cap));
        }
        return net;
    }

    // helper method for throwing
    private void throwIfNotValid(String... names) {
        for (String name : names) {
            if (name == null) throw new IllegalArgumentException("Team can't be null");
            if (!teamID.contains(name))
                throw new IllegalArgumentException("Team does not exist: " + name);
        }
    }

    // client
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
