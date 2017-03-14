'''
Returns x of center of the left, y of center of the left, 
x of the center of the right, y of the center of the right, 
y of the bottom of the bounding box of the left, y of the top of the bounding box of the left, 
y of the bottom of the bounding box of the right, and y of the top of the bounding box of the right
All -1 if not found
Must contain code to identify peg marks after our initial basic contour-finding process
Must return the centers of both strips of tape
'''

import numpy as np
import cv2
import operator

def findTape(frame, lower, upper):
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
	mask = cv2.inRange(hsv, lower, upper)
	cnts, hierarchy = cv2.findContours(mask.copy(), cv2.RETR_LIST,
							cv2.CHAIN_APPROX_SIMPLE)
	
	cnts2 = []

	for c in cnts:
		epsilon = 0.05 * cv2.arcLength(c, True)
		c = cv2.approxPolyDP(c, epsilon, True)

		area = cv2.contourArea(c)

		if (len(c) == 4 and area > 100 and cv2.isContourConvex(c)): 
			cnts2.append((area, c))
			
	# sorts contours by largest to smallest area
	if cnts2 != None:
		cnts2.sort(key=operator.itemgetter(0), reverse=True)

	for i in range(0, len(cnts2) - 1):
		for j in range(i + 1, len(cnts2)):
			if (cnts2[i][0] / 2) < cnts2[j][0]:
				Mi = cv2.moments(np.array(cnts2[i][1]))
				Mj = cv2.moments(np.array(cnts2[j][1]))
				
				if Mi["m00"] == 0 or Mj["m00"] == 0:
					return -1, -1, -1, -1, -1, -1, -1, -1				
				else:
					# centroid stuff
					centerXi = 640 - int(Mi["m10"] / Mi["m00"])
					centerYi = 360 - int(Mi["m01"] / Mi["m00"])
					
					centerXj = 640 - int(Mj["m10"] / Mj["m00"])
					centerYj = 360 - int(Mj["m01"] / Mj["m00"])
					
					# x, y, w, h
					iBox = cv2.boundingRect(cnts2[i][1])
					jBox = cv2.boundingRect(cnts2[j][1])
					
					if centerXi > centerXj:
						return centerXj, centerYj, centerXi, centerYi, \
							360 - jBox[1], 360 - jBox[1] - jBox[3], \
							360 - iBox[1], 360 - iBox[1] - iBox[3]
					else:
						return centerXi, centerYi, centerXj, centerYi, \
							360 - iBox[1], 360 - iBox[1] - iBox[3], \
							360 - jBox[1], 360 - jBox[1] - jBox[3]
				break

	return -1, -1, -1, -1, False
