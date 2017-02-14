'''
Must contain code to identify peg marks after our initial basic contour-finding process
Must return the centers of both strips of tape
'''

import numpy as np
import cv2
import operator
import subprocess

subprocess.call("uvcdynctrl -d video0 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video0 -s \"Exposure (Absolute)\" 5", shell = True)

kernel = np.ones((5,5),np.uint8)

def findTape(cap, lower, upper):
    img = np.zeros((480, 640, 3), np.uint8)

    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    mask = cv2.inRange(hsv, lower, upper)

    cv2.imshow("mask", cv2.bitwise_and(frame,frame, mask= mask))

    cnts, hierarchy = cv2.findContours(mask.copy(), cv2.RETR_LIST,
                            cv2.CHAIN_APPROX_SIMPLE)
    
    cnts2 = []

    for c in cnts:
        epsilon = 0.05 * cv2.arcLength(c, True)
        c = cv2.approxPolyDP(c, epsilon, True)
        cv2.drawContours(img, [c], 0, (255, 255, 255), 3)

        area = cv2.contourArea(c)

        if (len(c) == 4 and area > 1000 and cv2.isContourConvex(c)): 

            cv2.drawContours(img, [c], -1, (0, 0, 255), 3)

            cnts2.append((area, c))

    # sorts contours by largest to smallest area
    if cnts2 != None:
        cnts2.sort(key=operator.itemgetter(0), reverse=True)

    for i in range(0, len(cnts2) - 1):
        for j in range(i + 1, len(cnts2)):
            if (cnts2[i][0] / 2) < cnts2[j][0]:
                
                cv2.drawContours(img, [cnts2[i][1], cnts2[j][1]], -1, (255, 0, 0), 3)

                Mi = cv2.moments(np.array(cnts2[i][1]))
                if Mi["m00"] != 0:
                    centerXi = int(Mi["m10"] / Mi["m00"])
                    centerYi = int(Mi["m01"] / Mi["m00"])
                else:
                    return -1, -1, -1, -1, False


                Mj = cv2.moments(np.array(cnts2[j][1]))
                if Mj["m00"] != 0:
                    centerXj = int(Mj["m10"] / Mj["m00"])
                    centerYj = int(Mj["m01"] / Mj["m00"])
                else:
                    return -1, -1, -1, -1, False

                if centerXi > centerXj:
                    return centerXj, centerYj, centerXi, centerYi, True


    return -1, -1, -1, -1, False