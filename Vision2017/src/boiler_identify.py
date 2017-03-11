'''
Finds the centers of the lower and upper pieces of tape on the boiler.
Returns (x, y) of the center of the top of the bounding box of the top
tape, but (-1, -1) if no tape found.
'''
import cv2
import numpy as np

def findBoiler(frame, lower, upper):
	mask = cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), lower, upper)
	# cnts: just the contours alone
	cnts = cv2.findContours(mask,cv2.RETR_LIST,
		cv2.CHAIN_APPROX_SIMPLE)[0]
	
	# vals contains (contour, area) of all contours
	vals = []
	
	# loop over the contours
	for c in cnts:
		area = cv2.contourArea(c)
		if (area > 5 and area < 105):
			box = cv2.boundingRect(c)
			vals.append((area, box[0] + box[2]/2, box[1]))

	vals.sort(key=lambda x: x[0], reverse=True)	

	for i in range[0, len(vals) - 2]:
		for j in range[i, len(vals) - 1]:
			# give up on the rest of area ratio is less than 1/2 because it's sorted by size
			if (vals[i][0] * (2/5) > vals[j][0]):
				break

			# if area ratio is around 1/2 and the center x values are around the same, it's a match!
			if (vals[i][0] * (3/5) > vals[j][0] and vals[i][1] + 50 > vals[j][1] and vals[i][1] - 50 < vals[j][1]):
				return (vals[i][1], vals[i][2])

	return (-1, -1)
	
	# if there are fewer than 2 contours or the biggest two contours are still tiny, return all -1
	# if (len(vals) < 2):
	# 	return (-1, -1)
	# else:
	# 	box = cv2.boundingRect(vals[0][0])
	# 	return (box[0] + box[2]/2, box[1])
	# #			   x  +  width/2,	 y
