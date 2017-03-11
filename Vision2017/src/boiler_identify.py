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
<<<<<<< HEAD
        for c in cnts:
                vals.append((c, cv2.contourArea(c)))
							
	vals.sort(key=lambda x: x[1], reverse=True)	
	
	# if the biggest two contours are still tiny, return all -1 
	if (len(vals) < 2): # no tape found
=======
	for c in cnts:
		area = cv2.contourArea(c)
		if (area > 5 and area < 105):
			vals.append((c, area))

	vals.sort(key=lambda x: x[1], reverse=True)	
	
	# if there are fewer than 2 contours or the biggest two contours are still tiny, return all -1
	if (len(vals) < 2):
>>>>>>> a0dc76e1da27491723489b4c765017d1afa592db
		return (-1, -1)
	elif (vals[1][1] < 5):
                return (-1, -1)
	else:
		box = cv2.boundingRect(vals[0][0])
		return (box[0] + box[2]/2, box[1])
	#               x  +  width/2,     y
