'''
Finds the centers of the lower and upper pieces of tape on the boiler.
Returns (upper x, upper y, lower x, lower y) with all -1 if no tape found.
'''
from access_nt import NTClient
import cv2
import numpy as np

nt = NTClient()

# Example for writing to networkTables
nt.changeSubTable("vision")  # should do this once at the beginning of the program to safeguard against the table being something else
nt.write("left_center_x", 0)  # consult with writer of vision display widget to know what to name the keys

# lower and upper makes the HSV range for the tape. a probably-good range is
# upper = np.array([83, 20, 255])
def findCenters(frame, lower, upper):
	mask = cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), lower, upper)

	# cnts: just the contours alone
	cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST,
		cv2.CHAIN_APPROX_SIMPLE)[1]
	# values: contours with big enough areas [contour, dropping kernel, x of bounding box, y of bounding box]
	values = []
	# loop over the contours
	for c in cnts:
		if cv2.contourArea(c) > 10:
			box = cv2.boundingRect(c)
			x = box[0] + box[2] / 2
			height = 5
			while(mask[box[1] + height][x]):
				height += 1
			values.append([c, height, box[0], box[1]])
			
	values = sorted(values, key=lambda x: x[3])
	
	bestTargetScore = 1000;
	bestTargetIndices = (-1, -1)
	for a in range (0, len(values) - 1):
		for b in range (a + 1, len(values)):
			# make sure they're kinda close in x
			if (abs(values[a][2] - values[b][2]) < 30):
				droppingKernels = abs(values[a][1] / values[b][1] - 2)
				yDifference = abs((values[b][3] - values[a][3]) / values[a][1] - 2)
				score = droppingKernels + yDifference
				if (score > bestTargetScore):
					bestTargetScore = score
					bestTargetIndices = (a, b)
	if (bestTargetScore > 2):
		return (-1, -1, -1, -1)
	else:
		MU = cv2.moments(values[bestTargetIndices[0]][0])
		ML = cv2.moments(values[bestTargetIndices[1]][0])
		return (MU['m10'] / MU['m00'], MU['m01'] / MU['m00'], ML['m10'] / ML['m00'], ML['m01'] / ML['m00'])
