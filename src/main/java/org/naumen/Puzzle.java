/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.naumen;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;


interface PuzzleResolver {
    int[] resolve(int[] start);
}

class Node {
    final private int value;
    final private int[] arcs;
    Node(int value, int[] arcs) {
        this.value = value;
        this.arcs = arcs;
    }
    public int getValue() { return value; }
    public int[] getArcs() { return arcs; }
}

public class Puzzle implements PuzzleResolver {
    final static Node[] pattern = new Node[] {
        new Node(1, new int[] {1, 2}),
        new Node(2, new int[] {0, 2, 3}),
        new Node(3, new int[] {0, 1, 5}),
        new Node(4, new int[] {1, 4, 6}),
        new Node(0, new int[] {3, 5}),
        new Node(5, new int[] {2, 4,7}),
        new Node(6, new int[] {3, 7}),
        new Node(7, new int[] {5, 6})
    };

    public int[] resolve(int[] start) {
        Map<String, String> visited = searchState(pattern, toGraph(start, pattern));
        List<String> path = shortestPath(visited, pattern);
        return moves(path);
    }

    static public List<String> shortestPath(Map<String, String> visited, Node[] pattern) {
        LinkedList<String> path = new LinkedList<>();
        String state = snapshot(pattern);
        path.add(state);
        while (visited.containsKey(state)) {
            state = visited.get(state);
            path.addFirst(state);;
        }
        return path.subList(1, path.size());
    }

    static public Map<String, String> searchState(Node[] pattern, Node[] graph) {
        Map<String, String> visited = new HashMap<>();
        List<String> queue = new LinkedList<>();
        String state = snapshot(graph);
        visited.put(state, null);
        queue.add(state);
        while (queue.size() > 0) {
          Node[] current = toGraph(queue.remove(0), pattern);
          if (coincidence(toSeq(pattern), toSeq(current))) {
              break;
          }
          int i = indexOfSpot(current);
          for (int j : current[i].getArcs()) {
              Node[] newGraph = swap(current, i, j);
              String key = snapshot(newGraph);
              if (!visited.containsKey(key)) {
                visited.put(key, snapshot(current));
                queue.add(key);
              }
          }
        }
        return visited;
    }

    static public int[] moves(List<String> path) {
        int[] steps = new int[path.size() - 1];
        for (int i = 1; i < path.size(); i++) {
            int pos = indexOfSpot(path.get(i - 1));
            steps[i - 1] = Character.getNumericValue(path.get(i).charAt(pos));
        }
        return steps;
    }

    static public Node[] swap(Node[] graph, int i, int j) {
        Node[] state = new Node[graph.length];
        for (int index = 0; index < graph.length; index++) {
            if (index == i) {
                Node node = graph[j];
                Node newNode = new Node(node.getValue(), graph[index].getArcs());
                state[index] = newNode;
            } else if (index == j) {
                Node node = graph[i];
                Node newNode = new Node(node.getValue(), graph[index].getArcs());
                state[index] = newNode;
            } else {
                state[index] = graph[index];
            }
        }
        return state;
    }

    static public int indexOfSpot(String src) {
        char[] elems = src.toCharArray();
        for (int i = 0; i < elems.length; i++) {
            if (elems[i] == '0') return i;
        }
        return -1;
    }

    static public int indexOfSpot(Node[] graph) {
        for (int i = 0; i < graph.length; i++) {
            if (graph[i].getValue() == 0) return i;
        }
        return -1;
    }

    static public String snapshot(Node[] graph) {
        StringBuilder tmpl = new StringBuilder();
        for (Node node : graph) {
            tmpl.append(node.getValue());
        }
        return tmpl.toString().trim();
    }

    static public boolean coincidence(int[] seq1, int[] seq2) {
        if (seq1.length != seq2.length) return false;
        for (int i = 0; i < seq1.length; i++) {
            if (seq1[i] != seq2[i]) return false;
        }
        return true;
    }

    static public int[] toSeq(Node[] graph) {
        int[] values = new int[graph.length];
        for (int i = 0; i < graph.length; i++) {
            values[i] = graph[i].getValue();
        }
        return values;
    }

    static public Node[] toGraph(String src, Node[] pattern) throws IllegalArgumentException {
        char[] values = src.toCharArray();
        if (values.length != pattern.length) 
            throw new IllegalArgumentException("Short graph description");
        Node[] state = new Node[pattern.length];
        for (int i = 0; i < pattern.length; i++) {
            Node node = pattern[i];
            state[i] = new Node(Character.getNumericValue(values[i]), node.getArcs());
        }
        return state;
    }

    static public Node[] toGraph(int[] seq, Node[] pattern) throws IllegalArgumentException {
        if (seq.length != pattern.length) 
            throw new  IllegalArgumentException("Short sequence");
        Node[] state = new Node[pattern.length];
        for (int i = 0; i < seq.length; i++) {
            Node node = pattern[i];
            state[i] = new Node(seq[i], node.getArcs());
        }
        return state;
    }
}