#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Apr  4 12:23:36 2018

@author: mitro
"""

import networkx as nx

#G is the original graph
G = nx.Graph()

G.add_nodes_from(["A", "B", "C", "D", "E", "F", "G", "H"])
G.add_edges_from([("A","B"),("A","C"), ("A","D"), ("B","C"), ("B","D"),("D","C"), ("A", "G")])
G.add_edges_from([("E","F"),("E","G"), ("F","G"), ("G","H")])
#nx.draw(G, with_labels=True)

#Question 1(a)
G_a = nx.Graph(G)
G_a.remove_edge("A","G")
mod_a = nx.community.modularity(G, G_a)
print("modularity for question 1a: ", mod_a)
#nx.draw(G_a, with_labels=True)

#Question 1(b)
G_b = nx.Graph(G)
G_b.add_edge("E","H")
mod_b = nx.community.modularity(G_b, G_a)
print("modularity for question 1b: ", mod_b)
#nx.draw(G_b, with_labels=True)

#Question 1(c)
G_c = nx.Graph(G)
G_c.add_edge("F","A")
mod_c = nx.community.modularity(G_c, G_a)
print("modularity for question 1c: ", mod_c)
#nx.draw(G_c, with_labels=True)