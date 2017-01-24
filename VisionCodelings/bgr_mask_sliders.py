# BGR version of Arthur.
# Status: Unfinished. Demo and tool.
# Tool to find BGR ranges for whatever target.

import cv2
import numpy as np

def nothing(x):
    pass

black = np.zeros((1,1,3), np.uint8)

dlower = np.array([0, 0, 0])
dupper = np.array([255, 255, 255])

cv2.namedWindow('sliders',cv2.WINDOW_NORMAL)

cv2.createTrackbar('LB','sliders',dlower[0],179,nothing)
cv2.createTrackbar('UB','sliders',dupper[0],179,nothing)

cv2.createTrackbar('LG','sliders',dlower[1],255,nothing)
cv2.createTrackbar('UG','sliders',dupper[1],255,nothing)

cv2.createTrackbar('LR','sliders',dlower[2],255,nothing)
cv2.createTrackbar('UR','sliders',dupper[2],255,nothing)

cap = cv2.VideoCapture(0)
ret = cap.set(3,1280)
ret = cap.set(4,720)


while(1):
    ret, frame = cap.read()
    k = cv2.waitKey(1) & 0xFF
    if k == 27:
        break

    lower = np.array([cv2.getTrackbarPos('LB','sliders'),cv2.getTrackbarPos('LG','sliders'),cv2.getTrackbarPos('LR','sliders')])
    upper = np.array([cv2.getTrackbarPos('UB','sliders'),cv2.getTrackbarPos('UG','sliders'),cv2.getTrackbarPos('UR','sliders')])
    
    mask = cv2.inRange(frame, lower, upper)
    
    cv2.imshow('sliders',black)
    cv2.imshow('masked',cv2.bitwise_and(frame,frame, mask= mask))
    
print "lower = np.array([%s, %s, %s])\nupper = np.array([%s, %s, %s])" % \
    (cv2.getTrackbarPos('LB','sliders'),cv2.getTrackbarPos('LG','sliders'),cv2.getTrackbarPos('LR','sliders'),\
     cv2.getTrackbarPos('UB','sliders'),cv2.getTrackbarPos('UG','sliders'),cv2.getTrackbarPos('UR','sliders'))
cap.release()
cv2.destroyAllWindows()