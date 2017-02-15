'''
Finds the centers of the lower and upper pieces of tape on the boiler.
Returns (x, y) of the center of the top of the bounding box of the top
tape, but (-1, -1) if no tape found.
'''
import cv2
import numpy as np

def findBoiler(frame, lower, upper):
	# cnts: just the contours alone
	cnts = cv2.findContours(cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), lower, upper), cv2.RETR_LIST,
		cv2.CHAIN_APPROX_SIMPLE)[1]
	
	# vals contains (contour, area) of all contours
	vals = []
	
	# loop over the contours
	for c in cnts:
		vals.append(c, cv2.contourArea(c))
							
	vals.sort(key=lambda x: x[1])	
	
	# if the biggest two contours are still tiny, return all -1 
	if (vals[1][1] < 5): # no tape found
		return (-1, -1)
	else:
		box = cv2.boundingRect(vals[0][0])
		return (box[0] + box[2]/2, box[1])
	#               x  +  width/2,     y
