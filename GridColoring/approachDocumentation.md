What the problem asks

There is an n×m grid with both n and m odd.

Cells are initially white and must be colored one by one in some order.

After coloring any cell, all already-colored cells that are farthest from it (by chessboard distance, with Manhattan distance as a tie-breaker) receive a penalty.

The task is to output a coloring order such that no cell accumulates more than 3 penalties by the end.

It is guaranteed that at least one such order exists.

Key geometric idea used by the code

The program colors cells in waves from the center outward.

It measures how far each cell is from the grid’s center using chessboard distance (Chebyshev distance), and uses Manhattan distance only to decide ties among cells at the same chessboard distance.

It then sorts all cells by increasing chessboard distance from the center, with Manhattan distance as a secondary key, and outputs that order.

Why “center-out” works intuitively

Penalties are given to the colored cells farthest from the newly colored cell.

If coloring proceeds from the center outwards, then when coloring a cell, the already-colored cells tend to be closer to the newly colored cell, and the ones that are farthest are on the “frontier” of the already-colored region.

By expanding the colored region layer by layer (rings around the center), the number of times any specific cell ends up being the farthest gets controlled and kept within a small constant bound, which in this problem can be proven to be at most 3.

The odd×odd grid ensures a unique center at coordinates ((n+1)/2, (m+1)/2), which anchors the layering cleanly and avoids ambiguity.

How the program implements this

It reads each test case and computes the center cell (cx, cy) as:

cx = (n + 1) / 2

cy = (m + 1) / 2
Because n and m are odd, cx and cy are integers and identify the unique center.

It creates a list of all grid cells (i, j) with 1 ≤ i ≤ n and 1 ≤ j ≤ m.

For each cell (i, j), it computes:

Chebyshev distance from the center: max(|i − cx|, |j − cy|)

Manhattan distance from the center: |i − cx| + |j − cy|
The Chebyshev distance captures the “layer” or “ring” around the center.
The Manhattan distance is used only to break ties inside the same ring.

It sorts all cells by:

Primary key: Chebyshev distance (increasing)

Secondary key: Manhattan distance (increasing)
After sorting, cells are ordered center-out, ring by ring, with a consistent tie-breaking inside each ring.

It prints the coordinates in this sorted order, which is the coloring schedule.

Why this meets the penalty requirement

The structure of penalties targets already-colored cells that are farthest from the current cell.

When growing outward from the center, already-colored cells are clustered near the center, so they are seldom the farthest as the frontier moves outward, and each old cell is only occasionally the farthest from the next colored cell.

The tie-breaking with Manhattan distance within the same Chebyshev layer just fixes a deterministic order but does not affect the main “center-out” property.

For odd×odd grids, this strategy has a known guarantee that no cell receives more than 3 penalties overall.

Performance and constraints

The total number of printed cells over all test cases is at most 5,000, which is small.

Building the list, computing distances, sorting, and printing are all well within time and memory limits.

The program uses only basic data structures and a standard sort, making it simple and reliable.

Talking least:-

The program lists all grid cells.

It sorts them by how close they are to the center using chessboard distance, breaking ties using Manhattan distance.

It outputs that order.

This “center-out” ordering ensures a valid coloring where no cell’s penalty count exceeds 3, which is exactly what the problem asks for.