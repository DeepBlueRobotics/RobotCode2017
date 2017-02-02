'''
Must contain code to identify boiler marks after our initial basic contour-finding process
Must contain a function to return the centers of both strips of tape
'''
from access_nt import NTClient
import cv2
import numpy as np

nt = NTClient()

# Example for writing to networkTables
nt.changeSubTable("vision") # should do this once at the beginning of the program to safeguard against the table being something else
nt.write("left_center_x", 0) # consult with writer of vision display widget to know what to name the keys

# lower and upper makes the HSV range for the tape. a probably-good range is
# lower = np.array([40, 0, 250])
# upper = np.array([83, 20, 255])
def findCenters(frame, lower, upper):
	mask = cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), lower, upper)
	
	cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST,
		cv2.CHAIN_APPROX_SIMPLE)[1]
	
	# loop over the contours
	for c in cnts:
		if cv2.contourArea(c) > 10:
			M = cv2.moments(c)
			cX = int(M["m10"] / M["m00"])
			cY = int(M["m01"] / M["m00"])
			centers.append([cX, cY])
			
	centers = sorted(centers, key=lambda x: x[0])
	
	# the record for closest-in-x points - their distance and the index of the first point in centers[]
	closest = 1000
	index = -1;
	
	for i in range (0, len(centers) - 1):
		dist = abs(centers[i][0] - centers[i + 1][0])
		if (dist < closest):
			closest = dist
			index = i
	
	return (centers[index], centers[index + 1])