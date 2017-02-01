'''
Must contain code to identify boiler marks after our initial basic contour-finding process
Must contain a function to return the centers of both strips of tape
'''
from access_nt import NTClient

nt = NTClient()

#Example for writing to networkTables
nt.changeSubTable("vision") #should do this once at the beginning of the program to safeguard against the table being something else
nt.write("left_center_x", 0) #consult with writer of vision display widget to know what to name the keys

def findStuff(self):
     #Structure all of this as you think best