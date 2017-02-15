'''
Finds the centers of the lower and upper pieces of tape on the boiler.
Returns (x, y) of the center of the top of the bounding box of the top
tape, but (-1, -1) if no tape found.
'''
import cv2
import numpy as np

def findBoiler(frame, lower, upper):
        mask = cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), lower, upper)	# cnts: just the contours alone
	cnts = cv2.findContours(cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), lower, upper), cv2.RETR_LIST,
		cv2.CHAIN_APPROX_SIMPLE)[1]
	
	# vals contains (contour, area) of all contours
	vals = []
	
	# loop over the contours
	if (cnts != None):
                print "cnts not None"
		for c in cnts:
			if cv2.contourArea(c) > 16:
				box = cv2.boundingRect(c)
				x = box[0] + box[2] / 2
				height = 5
				while(height <= box[3]):
                                        if (not mask[box[1] + height][x]):
                                                break
					height += 1
				values.append([height, x, box[1]])
								
	values = sorted(values, key=lambda x: x[2])
	
	bestTargetScore = 1000;
	bestTargetIndices = (-1, -1)
	for a in range (0, len(values) - 1):
		for b in range (a + 1, len(values)):
			# make sure they're kinda close in x
			if (abs(values[a][1] - values[b][1]) < 30):
				# how close the dropping kernels to the top one 
				# being twice the bottom one
				droppingKernels = abs(values[a][0] / values[b][0] - 2)
				# how close the y diff is to being twice the upper
				# tape's dropping kernel
				yDifference = abs((values[b][2] - values[a][2]) / values[a][0] - 2)
				
				# lower score is better (golf-style)
				score = droppingKernels + yDifference
				if (score > bestTargetScore):
					bestTargetScore = score
					bestTargetIndices = (a, b)
	# if all of the contour pairs have shitty scores, or none with 
	# close x vals, were found, then say no tape found
	cv2.imshow('mask',mask)
	if (bestTargetScore > 2):
=======
	for c in cnts:
		vals.append(c, cv2.contourArea(c))
							
	vals.sort(key=lambda x: x[1])	
	
	# if the biggest two contours are still tiny, return all -1 
	if (vals[1][1] < 5): # no tape found
>>>>>>> c66e8fa00213f8f41271b793e65e21cdb2ed581a
		return (-1, -1)
	else:
		box = cv2.boundingRect(vals[0][0])
		return (box[0] + box[2]/2, box[1])
	#               x  +  width/2,     y
