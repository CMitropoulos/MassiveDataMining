


# In[1]:


import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans


# In[9]:


c1 = np.loadtxt('../data/c1.txt')
c2 = np.loadtxt('../data/c2.txt')
data = np.loadtxt('../data/data.txt')


# In[35]:


c1_cost = []
c2_cost = []
for i in range(1,21):
    kmeans_c1 = KMeans(n_clusters=10, random_state=0, init=c1,max_iter=i, n_jobs=-1).fit(data)
    kmeans_c2 = KMeans(n_clusters=10, random_state=0, init=c2,max_iter=i, n_jobs=-1).fit(data)
    

    c1_cost.append(kmeans_c1.inertia_)
    c2_cost.append(kmeans_c2.inertia_)
    


# In[39]:


plt.plot(c1_cost, 'r*')
plt.title("Cost for c1 initialization")
plt.xlabel('Iteration')
plt.ylabel('Cost')


# In[38]:


plt.plot(c2_cost, 'r*')
plt.title("Cost for c2 initialization")
plt.xlabel('Iteration')
plt.ylabel('Cost')


# In[32]:


#calculate percentages
#C19
c1_per =abs(c1_cost[9] - c1_cost[0])/c1_cost[0]
#c2
c2_per =abs(c2_cost[9] - c2_cost[0])/c2_cost[0]




