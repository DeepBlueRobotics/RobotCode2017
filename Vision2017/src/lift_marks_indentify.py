'''
Must contain code to identify peg marks after our initial basic contour-finding process
Must return the centers of both strips of tape
'''
from access_nt import NTClient
import numpy as np
import cv2


nt = NTClient()

#Example for writing to networkTables
nt.changeSubTable("vision") #should do this once at the beginning of the program to safeguard against the table being something else
nt.write("left_center_x", 0) #consult with writer of vision display widget to know what to name the keys

lowerRange = np.array([  0,   0,   0]) # [hue, sat, val]
upperRange = np.array([255, 255, 255]) # [hue, sat, val]
camNum = 1

def findStuff(self):
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
            
            if (len(c) == 4) and ((minRectArea * 0.9) < contourArea):
                M = cv2.moments(c)
                cX = int(M["m10"] / M["m00"])
                cY = int(M["m01"] / M["m00"])
                
            
            
            
            
            
            
            
            
        
        
