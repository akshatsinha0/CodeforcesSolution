import java.util.*;
import java.io.*;

public class GridColoring {
    static class Cell implements Comparable<Cell> {
        int x, y;
        int chebyshev;
        int manhattan;

        Cell(int x, int y, int cx, int cy) {
            this.x = x;
            this.y = y;
            this.chebyshev = Math.max(Math.abs(x - cx), Math.abs(y - cy));
            this.manhattan = Math.abs(x - cx) + Math.abs(y - cy);
        }

        public int compareTo(Cell o) {
            if (chebyshev != o.chebyshev) return chebyshev - o.chebyshev;
            return manhattan - o.manhattan;
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            int cx = (n + 1) / 2;
            int cy = (m + 1) / 2;

            // List all cells with their coordinates
            List<Cell> cells = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    cells.add(new Cell(i, j, cx, cy));
                }
            }
            // Sort by chebyshev, then manhattan (for tiebreaker)
            Collections.sort(cells);


            for (Cell cell : cells) {
                System.out.println(cell.x + " " + cell.y);
            }

        }
    }
}

