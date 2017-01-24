# Status: Works with the right image files. Tool.
# Takes two static images: a picture of the tape with the light on 
# and one with the light off, with the camera in the same position.
# Does Arthur (HSV sliders) on the absdiff between the two images.
# Use save_pics.py to take those pictures.

import numpy as np
import cv2

light = cv2.imread('light.jpg')
dark = cv2.imread('dark.jpg')
subtracted = cv2.absdiff(light, dark)
hsv = cv2.cvtColor(subtracted, cv2.COLOR_BGR2HSV)

cv2.namedWindow('sliders')

def draw(trash1):
    lower = np.array([cv2.getTrackbarPos('LH','sliders'),cv2.getTrackbarPos('LS','sliders'),cv2.getTrackbarPos('LV','sliders')])
    upper = np.array([cv2.getTrackbarPos('UH','sliders'),cv2.getTrackbarPos('US','sliders'),cv2.getTrackbarPos('UV','sliders')])
    mask = cv2.inRange(hsv, lower, upper)
    masked = cv2.bitwise_and(subtracted,subtracted, mask= mask)
    cv2.imshow('sliders',masked)
    cv2.imshow('mask',mask)
    
cv2.createTrackbar('LH','sliders',0,179,draw)
cv2.createTrackbar('UH','sliders',179,179,draw)

cv2.createTrackbar('LS','sliders',0,255,draw)
cv2.createTrackbar('US','sliders',255,255,draw)

cv2.createTrackbar('LV','sliders',0,255,draw)
cv2.createTrackbar('UV','sliders',255,255,draw)


cv2.waitKey(0)
cv2.destroyAllWindows()