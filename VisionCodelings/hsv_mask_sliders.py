# Formerly known as Arthur (Dent).
# Status: Demo and tool. Works.
# Tool to find HSV ranges for whatever target.

import cv2
import numpy as np

def nothing(x):
    pass

black = np.zeros((1,1,3), np.uint8)

dlower = np.array([0, 0, 0])
dupper = np.array([179, 255, 255])

cv2.namedWindow('sliders',cv2.WINDOW_NORMAL)

cv2.createTrackbar('LH','sliders',dlower[0],179,nothing)
cv2.createTrackbar('UH','sliders',dupper[0],179,nothing)

cv2.createTrackbar('LS','sliders',dlower[1],255,nothing)
cv2.createTrackbar('US','sliders',dupper[1],255,nothing)

cv2.createTrackbar('LV','sliders',dlower[2],255,nothing)
cv2.createTrackbar('UV','sliders',dupper[2],255,nothing)

cap = cv2.VideoCapture(0)
ret = cap.set(3,1280)
ret = cap.set(4,720)


while(1):
    ret, frame = cap.read()
    k = cv2.waitKey(1) & 0xFF
    if k == 27:
        break

    lower = np.array([cv2.getTrackbarPos('LH','sliders'),cv2.getTrackbarPos('LS','sliders'),cv2.getTrackbarPos('LV','sliders')])
    upper = np.array([cv2.getTrackbarPos('UH','sliders'),cv2.getTrackbarPos('US','sliders'),cv2.getTrackbarPos('UV','sliders')])
    
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    
    mask = cv2.inRange(hsv, lower, upper)
    cv2.imshow('sliders',black)
    cv2.imshow('masked',cv2.bitwise_and(frame,frame, mask= mask))
    
print "lower = np.array([%s, %s, %s])\nupper = np.array([%s, %s, %s])" % \
    (cv2.getTrackbarPos('LH','sliders'),cv2.getTrackbarPos('LS','sliders'),\
    cv2.getTrackbarPos('LV','sliders'),cv2.getTrackbarPos('UH','sliders'),\
    cv2.getTrackbarPos('US','sliders'),cv2.getTrackbarPos('UV','sliders'))
cap.release()
cv2.destroyAllWindows()