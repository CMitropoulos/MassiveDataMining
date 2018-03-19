# -*- coding: utf-8 -*-
"""
Created on Mon Mar 12 20:46:15 2018

@author: Christos Mitropoulos
"""

import numpy as np
import re
import operator

n = float(100) #number of nodes
beta = 0.8
#initialize matrix M
M = np.zeros((int(n),int(n))) #M is zero-based matrix 

filename ="../data/graph.txt"

degrees ={}
with open(filename) as f:
    linelist = f.read().splitlines() 
for line in linelist:
    edge = re.split(r'\t+', line)
    i = int(edge[0])-1
    j =int(edge[1])-1
    if M[j, i] == 0:#create M table
        M[j, i] = 1    
        #update degree of each node, only if edge is seen first time
        degrees.update({i:degrees.get(i,0)+1}) 
for key in degrees.keys():
    for i in range(0,int(n)):
        M[key,i] = M[key,i]/degrees.get(i)
    
#initialize rank array r
r = np.full((int(n),1),1/n)
identity_matrix = np.full((int(n),1), 1.0)

#iterative process of PageRank
for i in range(0,40):
    r = ((1-beta)/n)*identity_matrix + beta*np.dot(M, r)
    
#create rank dictionary
rank_dictionary ={}
for i in range(0,int(n)):
    rank_dictionary.update({i:r[i]})



sorted_rank_dictionary = sorted(rank_dictionary.items(), key=operator.itemgetter(1))

#print the top 5
print("TOP 5")
for i in range(int(n)-1,int(n)-6, -1):
    print("ID: ", sorted_rank_dictionary[i][0]+1, ",score: ",sorted_rank_dictionary[i][1])
print("\n")
#print the bottom 5
print("BOTTOM 5")
for i in range(0,5):
    print("ID: ", sorted_rank_dictionary[i][0]+1, ",score: ",sorted_rank_dictionary[i][1])
    