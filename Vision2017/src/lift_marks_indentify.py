'''
Must contain code to identify peg marks after our initial basic contour-finding process
Must return the centers of both strips of tape
'''

import numpy as np
import cv2
import operator

def findTape(cap, lowerHSV, upperHSV):
    
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    mask = cv2.inRange(hsv, lowerHSV, upperHSV)
    
    cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)[1]
    
    # cntAreas = []
    # centerX = []
    # centerY = []

    # contains tuples of contours in format: (area, x-val of center, y-val of center)
    cnts2 = []

    for c in cnts:
        epsilon = 0.05 * cv2.arcLength(c,True)
        c = cv2.approxPolyDP(c, epsilon, True)
        
        area = cv2.contourArea(c)
        
        minRect = cv2.minAreaRect(c)
        minRectArea = cv2.contourArea(minRect)
        
        # checks if the thing is a rectangle, then records area and centers if true
        if (len(c) == 4) and ((minRectArea * 0.9) < area):

            # Past self: "WTF are moments?!" Later past self: "Chill, don't worry about it, just assume it works."
            M = cv2.moments(c)
            centerX = int(M["m10"] / M["m00"])
            centerY = (int(M["m01"] / M["m00"])

            # tup = (len(centerX) - 1, area)
            # cntAreas.append(area)

            tup = (area, centerX, centerY)
            cnts2.append(tup)

    # sorts contours by largest to smallest area
    cnts2.sort(key=operator.itemgetter(0))
    cnts2.reverse()

    for i in range(0, len(cnts2) - 1):
        for j in range(i + 1, len(cnts2) - 1):

            # if this contour's area is too small, there's no use in looking through the rest because they're sorted by area
            if cnts2[i][0] * 0.8 < cnts2[j][0]:
                break

            # if they line up, it fills the final requirement and yey. Returns.
            else if (cnts2[i][2] - 15 <= cnts2[j][2]) and (cnts2[i][2] + 15 >= cnts2[j][2]):
                if cnts2[i][1] > cnts2[j][1]:
                    return cnts2[j][1], cnts2[j][2], cnts2[i][1], cnts2[i][2], True
                else:
                    return cnts2[i][1], cnts2[i][2], cnts2[j][1], cnts2[j][2], True

    return -1, -1, -1, -1, False