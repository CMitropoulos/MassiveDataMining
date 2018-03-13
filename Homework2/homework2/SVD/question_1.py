# -*- coding: utf-8 -*-
"""
Created on Fri Mar  9 17:51:33 2018

@author: Chris Mitropoulos
"""

import numpy as np
from scipy import linalg 

M = np.array([[1,2], [2, 1], [3, 4], [4, 3]])
#print the SVD of M 
U,S,V_transpose = linalg.svd(M, full_matrices=False)
print("U =", U)
print("S=",S)
print("V_transpose=",V_transpose)
#print eigenvalue decomposition of Mt M
Evals, Evecs = linalg.eigh(np.dot(np.transpose(M),M))

print("Evals=", Evals)
print("Evecs=", Evecs)

#add evals and corresponding Evecs to a dictionary
Edict = {}
for i in range(0,Evals.size):
    Edict.update({Evals[i] : Evecs[:,i]})   
#Print sorted values
for key in sorted(Edict.iterkeys(), reverse=True):
    print "%s: %s" % (key, Edict[key])
