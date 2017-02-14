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
	cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST,
		cv2.CHAIN_APPROX_SIMPLE)[1]
	# values: contours with big enough areas 
	# [dropping kernel, center x of bounding box, y of top of bounding box]
	values = []
	# loop over the contours
	if (cnts != None):
		for c in cnts:
			if cv2.contourArea(c) > 10:
				box = cv2.boundingRect(c)
				x = box[0] + box[2] / 2
				height = 5
				while(mask[box[1] + height][x]):
					height += 1
				values.append([height, x, box[1]])
								
	values = sorted(values, key=lambda x: x[3])
	
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
	if (bestTargetScore > 2):
		return (-1, -1)
	else:
		return (values[bestTargetIndices[0]][1], values[bestTargetIndices[0]][2])
