# Status: Unfinished. 
# Arthur to find mask of tape. No morphology. Displays contour (only the
# ones with area > 10) outlines and centers for each frame (live).

import cv2
import numpy as np

def nothing(x):
    pass

black = cv2.imread('black.jpg')

dlower = np.array([40, 0, 250])
dupper = np.array([83, 20, 255])

cv2.namedWindow('sliders',cv2.WINDOW_NORMAL)

cv2.createTrackbar('LH','sliders',dlower[0],179,nothing)
cv2.createTrackbar('UH','sliders',dupper[0],179,nothing)

cv2.createTrackbar('LS','sliders',dlower[1],255,nothing)
cv2.createTrackbar('US','sliders',dupper[1],255,nothing)

cv2.createTrackbar('LV','sliders',dlower[2],255,nothing)
cv2.createTrackbar('UV','sliders',dupper[2],255,nothing)

cap = cv2.VideoCapture(0)
ret = cap.set(3,640)
ret = cap.set(4,360)

while(1):
    ret, frame = cap.read()
    k = cv2.waitKey(1) & 0xFF
    if k == 27:
        break

    lower = np.array([cv2.getTrackbarPos('LH','sliders'),cv2.getTrackbarPos('LS','sliders'),cv2.getTrackbarPos('LV','sliders')])
    upper = np.array([cv2.getTrackbarPos('UH','sliders'),cv2.getTrackbarPos('US','sliders'),cv2.getTrackbarPos('UV','sliders')])
    
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
#     hsv = cv2.bilateralFilter(hsv, 20, 50, 50)
    mask = cv2.inRange(hsv, lower, upper)
    
    cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST,
        cv2.CHAIN_APPROX_SIMPLE)[1]
        
    for c in cnts:
        M = cv2.moments(c)
        if cv2.contourArea(c) > 4:
            cX = int(M["m10"] / M["m00"])
            cY = int(M["m01"] / M["m00"])
            print "", cX, cY
        
            cv2.drawContours(frame, [c], 0, (200, 0, 150), 5)
            cv2.circle(frame, (cX, cY), 2, (255, 255, 0), -1)
        
    cv2.imshow('sliders',black)
    cv2.imshow('masked',cv2.bitwise_and(frame,frame, mask= mask))
    
print "lower = np.array([%s, %s, %s])\nupper = np.array([%s, %s, %s])" % \
    (cv2.getTrackbarPos('LH','sliders'),cv2.getTrackbarPos('LS','sliders'),\
    cv2.getTrackbarPos('LV','sliders'),cv2.getTrackbarPos('UH','sliders'),\
    cv2.getTrackbarPos('US','sliders'),cv2.getTrackbarPos('UV','sliders'))

cap.release()
cv2.destroyAllWindows()