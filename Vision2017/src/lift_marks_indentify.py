'''
Must contain code to identify peg marks after our initial basic contour-finding process
Must return the centers of both strips of tape
'''

import numpy as np
import cv2

def findTape(cap, lowerRange, upperRange):
    #Structure all of this as you think best
    cap = cv2.VideoCapture(camNum)
     
    center1 = [0, 0]
    center2 = [0, 0]
    
    for i in range(0, 5):
        ret, frame = cap.read()
        
        hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
        mask = cv2.inRange(hsv, lowerRange, upperRange)
        
        cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)[1]
        
        
        
        for c in cnts:
            epsilon = 0.05*cv2.arcLength(c,True)
            c = cv2.approxPolyDP(c,epsilon,True)
            
            contourArea = cv2.contourArea(c)
            
            minRect = cv2.minAreaRect(c)
            minRectArea = cv2.contourArea(minRect)
            
            # checks if the thing is a rectangle
            if (len(c) == 4) and ((minRectArea * 0.9) < contourArea):
                M = cv2.moments(c)
                cX = int(M["m10"] / M["m00"])
                cY = int(M["m01"] / M["m00"])
                
            
            
            
            
            
            
            
            
        
        
