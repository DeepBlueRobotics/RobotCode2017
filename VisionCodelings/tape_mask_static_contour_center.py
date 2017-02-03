# Status: Tool and demo. Probably works.
# Arthur (HSV mask sliders) first. Prints upper and lower HSV arrays on esc. 
# Next, displays original mask, morphologically closes (removes black sound) 
# and displays again. Finally, it finds and dipsplays contour centers and 
# outlines on the original full-color image.

import cv2
import numpy as np

def nothing(x):
	pass

black = np.zeros((1, 1, 3), np.uint8)

dlower = np.array([40, 0, 250])
dupper = np.array([83, 20, 255])

cv2.namedWindow('sliders', cv2.WINDOW_NORMAL)

cv2.createTrackbar('LH', 'sliders', dlower[0], 179, nothing)
cv2.createTrackbar('UH', 'sliders', dupper[0], 179, nothing)

cv2.createTrackbar('LS', 'sliders', dlower[1], 255, nothing)
cv2.createTrackbar('US', 'sliders', dupper[1], 255, nothing)

cv2.createTrackbar('LV', 'sliders', dlower[2], 255, nothing)
cv2.createTrackbar('UV', 'sliders', dupper[2], 255, nothing)

cap = cv2.VideoCapture(0)
ret = cap.set(3, 1280)
ret = cap.set(4, 720)


while(1):
	ret, frame = cap.read()
	k = cv2.waitKey(1) & 0xFF
	if k == 27:
		break

	lower = np.array([cv2.getTrackbarPos('LH', 'sliders'), cv2.getTrackbarPos('LS', 'sliders'), cv2.getTrackbarPos('LV', 'sliders')])
	upper = np.array([cv2.getTrackbarPos('UH', 'sliders'), cv2.getTrackbarPos('US', 'sliders'), cv2.getTrackbarPos('UV', 'sliders')])
	
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
# 	 hsv = cv2.bilateralFilter(hsv, 20, 50, 50)
	
	mask = cv2.inRange(hsv, lower, upper)
	cv2.imshow('sliders', black)
	cv2.imshow('masked', cv2.bitwise_and(frame, frame, mask=mask))
	
print "lower = np.array([%s, %s, %s])\nupper = np.array([%s, %s, %s])" % \
	(cv2.getTrackbarPos('LH', 'sliders'), cv2.getTrackbarPos('LS', 'sliders'), \
	cv2.getTrackbarPos('LV', 'sliders'), cv2.getTrackbarPos('UH', 'sliders'), \
	cv2.getTrackbarPos('US', 'sliders'), cv2.getTrackbarPos('UV', 'sliders'))

cap.release()
cv2.destroyAllWindows()

kernel = np.ones((8, 8), np.uint8)
cv2.imshow('original mask', mask)
mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel)
cv2.imshow('closed', mask)

cv2.waitKey(0)
cv2.destroyAllWindows()

cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST,
	cv2.CHAIN_APPROX_SIMPLE)
cnts = cnts[1]
print len(cnts)

centers = [];

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

center1 = centers[index]
center2 = centers[index + 1]

print center1
print center2

cv2.imshow("Image", frame)

cv2.waitKey(0)
cv2.destroyAllWindows()
