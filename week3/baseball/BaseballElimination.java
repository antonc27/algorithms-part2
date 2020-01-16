/* *****************************************************************************
 *  Name: BaseballElimination
 *  Date: 14/01/20
 *  Description: Represents a sports division and determines which teams are mathematically eliminated
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {

    private int n;
    private String[] teams;
    private Map<String, Integer> teamToIdx;

    private int[] wins;
    private int[] loss;
    private int[] left;

    private int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();

        teams = new String[n];
        teamToIdx = new HashMap<>();

        wins = new int[n];
        loss = new int[n];
        left = new int[n];

        g = new int[n][n];

        in.readLine();
        for (int i = 0; i < n; i++) {
            String line = in.readLine();
            String[] split = line.trim().split(" +");

            teams[i] = split[0];
            teamToIdx.put(teams[i], i);

            wins[i] = Integer.parseInt(split[1]);
            loss[i] = Integer.parseInt(split[2]);
            left[i] = Integer.parseInt(split[3]);

            for (int j = 0; j < n; j++) {
                g[i][j] = Integer.parseInt(split[4 + j]);
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    private void checkTeam(String team) {
        if (team == null || !teamToIdx.containsKey(team)) throw new IllegalArgumentException("Null team");
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);
        int idx = teamToIdx.get(team);
        return wins[idx];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);
        int idx = teamToIdx.get(team);
        return loss[idx];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeam(team);
        int idx = teamToIdx.get(team);
        return left[idx];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        int i = teamToIdx.get(team1);
        int j = teamToIdx.get(team2);
        return g[i][j];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);
        if (isTrivialElimination(team)) {
            return true;
        } else {
            FlowNetwork fn = buildFlowNetwork(team);
            int v = fn.V();
            FordFulkerson ff = new FordFulkerson(fn, v - 2, v - 1);
            return !checkFullEdgesFromSource(fn);
        }
    }

    private boolean checkFullEdgesFromSource(FlowNetwork fn) {
        int s = fn.V() - 2;
        for (FlowEdge edge : fn.adj(s)) {
            if (edge.from() != s) continue;
            if (edge.flow() < edge.capacity()) {
                return false;
            }
        }
        return true;
    }

    private boolean isTrivialElimination(String team) {
        int idx = teamToIdx.get(team);
        int possibleWins = wins[idx] + left[idx];
        for (int i = 0; i < n; i++) {
            if (i == idx) continue;
            if (possibleWins < wins[i]) return true;
        }
        return false;
    }

    private FlowNetwork buildFlowNetwork(String team) {
        int idx = teamToIdx.get(team);

        // team vertices count
        int tvCount = n - 1;

        // game vertices count
        int gvCount = tvCount * (tvCount - 1) / 2;

        // total vertices count
        int v = 1 + gvCount + tvCount + 1;

        FlowNetwork fn = new FlowNetwork(v);

        int s = v - 2;
        int t = v - 1;

        // game vertices
        int count = 0;
        // Map<Integer, Integer>
        for (int i = 0; i < n; i++) {
            if (i == idx) continue;
            for (int j = i+1; j < n; j++) {
                if (j == idx) continue;

                // games left
                FlowEdge gl = new FlowEdge(s, count, g[i][j]);
                fn.addEdge(gl);

                // games to teams
                int iCount = i + ((i > idx) ? -1 : 0);
                FlowEdge gti = new FlowEdge(count, gvCount + iCount, Double.POSITIVE_INFINITY);
                fn.addEdge(gti);
                int jCount = j + ((j > idx) ? -1 : 0);
                FlowEdge gtj = new FlowEdge(count, gvCount + jCount, Double.POSITIVE_INFINITY);
                fn.addEdge(gtj);

                count++;
            }
        }

        // still win
        int possibleWins = wins[idx] + left[idx];
        int teamCount = 0;
        for (int i = 0; i < n; i++) {
            if (i == idx) continue;
            FlowEdge sw = new FlowEdge(gvCount + teamCount, t, possibleWins - wins[i]);
            fn.addEdge(sw);
            teamCount++;
        }

        return fn;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        if (!isEliminated(team)) return null;

        if (isTrivialElimination(team)) {
            return Collections.singletonList(maxScoreTeam());
        }

        List<String> coe = new ArrayList<>();

        FlowNetwork fn = buildFlowNetwork(team);
        int v = fn.V();
        FordFulkerson ff = new FordFulkerson(fn, v - 2, v - 1);

        int idx = teamToIdx.get(team);
        int tvCount = n - 1;
        int gvCount = tvCount * (tvCount - 1) / 2;

        for (int i = 0; i < n; i++) {
            if (i == idx) continue;
            int iCount = i + ((i > idx) ? -1 : 0);
            if (ff.inCut(gvCount + iCount)) {
                coe.add(teams[i]);
            }
        }

        return coe;
    }

    private String maxScoreTeam() {
        String leader = "";
        int leaderScore = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (wins[i] > leaderScore) {
                leaderScore = wins[i];
                leader = teams[i];
            }
        }
        return leader;
    }

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
