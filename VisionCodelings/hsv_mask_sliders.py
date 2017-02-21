# Formerly known as Arthur (Dent).
# Status: Demo and tool. Works.
# Tool to find HSV ranges for whatever target.

import cv2, subprocess
import numpy as np

def nothing(x):
    pass

subprocess.call("uvcdynctrl -d video1 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video1 -s \"Exposure (Absolute)\" 5", shell = True)

black = np.zeros((1,1,3), np.uint8)

dlower = np.array([65, 175, 70])
dupper = np.array([100, 255, 200])

cv2.namedWindow('sliders',cv2.WINDOW_NORMAL)

cv2.createTrackbar('LH','sliders',dlower[0],179,nothing)
cv2.createTrackbar('UH','sliders',dupper[0],179,nothing)

cv2.createTrackbar('LS','sliders',dlower[1],255,nothing)
cv2.createTrackbar('US','sliders',dupper[1],255,nothing)

cv2.createTrackbar('LV','sliders',dlower[2],255,nothing)
cv2.createTrackbar('UV','sliders',dupper[2],255,nothing)

cap = cv2.VideoCapture(1)
# ret = cap.set(3,320)
# ret = cap.set(4,180)


while(1):
    ret, frame = cap.read()
    k = cv2.waitKey(1)
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
