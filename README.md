# Custom Minimal Spanning Tree

## Description 
Building a spanning tree from input data should obey these three basic rules:

1. The graph has to be a tree and contain all nodes.
2. The graph should contain maximum possible number of edges of some particular length, regardless of the cost associated with those edges.
3. The total sum of edge lengths of resulting graph respecting the first and the second rule should be minimized.

![Image 1](https://i.imgur.com/kWlbEW3.png)

_Image 1. Schemes of possible connections between graph nodes. Possible connections are depicted as edges. The connections which may form the desired spanning tree are highlighted in red. The cases a), b), c) correspond to the Examples 1, 2, 3 below. In some cases (e.g. in cases b and c) more than one spanning tree may satisfy the problem demands. However, all such spanning trees share the same total length. The case a) illustrates the importance of rule 3. There exist spanning trees containing edges (1, 5) and (5, 4) which satisfy rule 2 but do not satisfy rule 3 as the total length of any of them is at least 17._

## Task
List of all possible connections between the nodes and the respective connection lengths is given. Calculate the total length of a spanning tree which satisfies all three rules presented in description.

The data sets for the task were created by teachers of the Czech Technical University for subject 'Advanced Algorithmization'.

## Input
The first line of the input consists of two integers N and M separated by space. Value N is the number of nodes. Value M is the number of possible connections between pairs of nodes. Next, there are M lines, where each line represents one possible connection. The line contains three integers D1, D2, L separated by space, meaning that the length of possible connection between nodes D1 and D2 is equal to L. The detectors are numbered from 1 to N. The possible connections are listed in a random order. It holds N ≤ 30000 and M ≤ 300000. Each length is at most 10^9.

## Output
The output is one line containing the minimum total length of the required network. The result is not greater than 2^60.

## Example 1
![Input/Output 1](https://i.imgur.com/tdPqcFn.png)

Example 1 input data and the resulting network are depicted in Image 1 a).
## Example 2
![Input/Output 2](https://i.imgur.com/t8hXYnJ.png)

Example 2 input data and the resulting network are depicted in Image 1 b).
## Example 3
![Input/Output 3](https://i.imgur.com/syLswQF.png)

Example 2 input data and the resulting network are depicted in Image 1 c).

## Solution
The task is implemented in Java using optimized Union-Find algorithm (explained [here](https://www.cs.princeton.edu/~rs/AlgsDS07/01UnionFind.pdf)). In algorithm there is used a modified version of HashMap implemented by authors Justin Couch, Alex Chaffee and Stephen Colebourne. This HashMap uses only primitive integers and not Java Integer objects, which resulted in improved performance. 
