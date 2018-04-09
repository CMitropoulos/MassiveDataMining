#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Apr  4 12:23:36 2018

@author: mitro
"""
import numpy as np
import networkx as nx
import numpy.linalg as la

#G is the original graph
G = nx.Graph()

G.add_nodes_from(["A", "B", "C", "D", "E", "F", "G", "H"])
G.add_edges_from([("A","B"),("A","C"), ("A","D"), ("B","C"), ("B","D"),("D","C"), ("A", "G")])
G.add_edges_from([("E","F"),("E","G"), ("F","G"), ("G","H")])
#nx.draw(G, with_labels=True)


communities = [{"A", "B", "C", "D"},{"E", "F", "G", "H"}]
#Question 1(a)
G_a = nx.Graph(G)
G_a.remove_edge("A","G")
mod_a = nx.community.modularity(G, communities)
print("modularity for question 1a: ", mod_a)
#nx.draw(G_a, with_labels=True)

#Question 1(b)
G_b = nx.Graph(G)
G_b.add_edge("E","H")
mod_b = nx.community.modularity(G_b, communities)
print("modularity for question 1b: ", mod_b)
#nx.draw(G_b, with_labels=True)

#Question 1(c)
G_c = nx.Graph(G)
#G_c.add_edge("F","A")
G_c.add_edge("A","F")
mod_c = nx.community.modularity(G_c, communities)
print("modularity for question 1c: ", mod_c)
#nx.draw(G_c, with_labels=True)


#question 2a
A =nx.to_numpy_matrix(G)
print("Adjacency matrix:\n", A)

D = G.degree()
print("Degree matrix\n", D)

L = nx.laplacian_matrix(G)
L = L.toarray()
print("Laplacian matrix\n",L)

#question 2b
#calculate eigen values and eigen vectors of Laplacian
L_eigenValues, L_eigenVectors = la.eig(L)
print("Laplacian eigenvalues:\n", L_eigenValues)
print("Laplacian eigenvectors:\n", L_eigenVectors)

#question 2c
eig_val_sorted_indexes = np.argsort(L_eigenValues)
lamda2 = L_eigenVectors[:,eig_val_sorted_indexes[1]]
print("this is the eigenvector for the second smallest eigenvalue:\n",
      lamda2)


