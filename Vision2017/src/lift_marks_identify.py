'''
Returns the left and right gear tapes' (l & r) center-x (cx) and -y (cy), tops (t), bottoms (b),
and the boolean if successful in the order of:
	lcx, lcy, rcx, rcy, lb,  lt, rb, rt, success
Returns -1 for all and False for success if not successful at finding the tapes.
'''

import numpy as np
import cv2
import operator

def findTape(frame, lower, upper):
	# filters image
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
	mask = cv2.inRange(hsv, lower, upper)
	cnts, hierarchy = cv2.findContours(mask.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)

	cnts2 = []

	# puts all the contours that have 4 verticies and an area more than 100 in cnts2
	for c in cnts:
		area = cv2.contourArea(c)

		if (area > 100):
			epsilon = 0.05 * cv2.arcLength(c, True)
			cPoly = cv2.approxPolyDP(c, epsilon, True)

			if (len(cPoly) == 4 and cv2.isContourConvex(cPoly)):
				cnts2.append((area, cPoly))

	# sorts contours by largest to smallest area
	if cnts2 != None:
		cnts2.sort(key=operator.itemgetter(0), reverse=True)

	# narrows down contours to final matching pair and returns them if found
	for i in range(0, len(cnts2) - 1):
		for j in range(i + 1, len(cnts2)):
			if (cnts2[i][0] / 2) < cnts2[j][0]:
				Mi = cv2.moments(np.array(cnts2[i][1]))
				Mj = cv2.moments(np.array(cnts2[j][1]))

				if Mi["m00"] == 0 or Mj["m00"] == 0:
					return -1, -1, -1, -1, -1, -1, -1, -1, False
				else:
					# centroid stuff
					centerXi = 640 - int(Mi["m10"] / Mi["m00"])
					centerYi = 360 - int(Mi["m01"] / Mi["m00"])

					centerXj = 640 - int(Mj["m10"] / Mj["m00"])
					centerYj = 360 - int(Mj["m01"] / Mj["m00"])

					# x, y, w, h
					iBox = cv2.boundingRect(cnts2[i][1])
					jBox = cv2.boundingRect(cnts2[j][1])

					boti = 360 - iBox[1]
					topi = 360 - iBox[1] - iBox[3]

					botj = 360 - jBox[1]
					topj = 360 - jBox[1] - jBox[3]

					if centerXi < centerXj:
						return centerXi, centerYi, centerXj, centerYi, \
							boti, topi, botj, topj, True
					else:
						return centerXj, centerYj, centerXi, centerYi, \
							botj, topj, boti, topi, True
				break

	# returns this if no countours found
	return -1, -1, -1, -1, -1, -1, -1, -1, False
