'''
Must contain code to identify peg marks after our initial basic contour-finding process
Must return the centers of both strips of tape
'''

import numpy as np
import cv2
import operator
import subprocess

cap = cv2.VideoCapture(0)

# subprocess.call("uvcdynctrl -d video1 -s \"Exposure, Auto\" 1", shell = True)
# subprocess.call("uvcdynctrl -d video1 -s \"Exposure (Absolute)\" 5", shell = True)


def nothing(x):
    pass

cv2.namedWindow('sliders', cv2.WINDOW_NORMAL)

cv2.createTrackbar('LH', 'sliders', 0, 179, nothing)
cv2.createTrackbar('UH', 'sliders', 179, 179, nothing)

cv2.createTrackbar('LS', 'sliders', 0, 255, nothing)
cv2.createTrackbar('US', 'sliders', 255, 255, nothing)

cv2.createTrackbar('LV', 'sliders', 0, 255, nothing)
cv2.createTrackbar('UV', 'sliders', 255, 255, nothing)


def calcLine(p1, p2):
    m = (p1[1] - p2[1]) / (p1[0] - p2[0])
    b = p1[1] - m * p1[0]
    return m, b


def closeToLine(m, b, p):
    y = m * p[0] + b
    return True if (p[1] >= y - 15) and (p[1] <= y + 15) else return False


def findTape(frame, lowerHSV, upperHSV):

    img = np.zeros((640, 480, 3), np.uint8)

    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    mask = cv2.inRange(hsv, lowerHSV, upperHSV)

    cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST,
                            cv2.CHAIN_APPROX_SIMPLE)[1]

    cv2.drawContours(img, cnts, -1, (255, 255, 255), 3)

    # cntAreas = []
    # centerX = []
    # centerY = []

    # contains tuples of contours in format: (area, x-val of center, y-val of
    # center)
    cnts2 = []

    for c in cnts:
        epsilon = 0.05 * cv2.arcLength(c, True)
        c = cv2.approxPolyDP(c, epsilon, True)

        area = cv2.contourArea(c)

        minRect = cv2.boundingRect(c)
        print minRect
        minRectArea = 200  # cv2.contourArea(minRect)

        # checks if the thing is a rectangle, then records area and centers if
        # true
        if (len(c) == 4) and ((minRectArea * 0.9) < area):

            cv2.drawContours(img, [c], -1, (0, 0, 255), 3)

            # Past self: "WTF are moments?!" Later past self: "Chill, don't
            # worry about it, just assume it works."
            M = cv2.moments(c)
            centerX = int(M["m10"] / M["m00"])
            centerY = int(M["m01"] / M["m00"])

            # tup = (len(centerX) - 1, area)
            # cntAreas.append(area)

            c.sort()

            cnts2.append((area, c))

    # sorts contours by largest to smallest area
    if cnts2 is not None:
        cnts2.sort(key=operator.itemgetter(0), reverse=True)

    for i in range(0, len(cnts2) - 1):
        m1, b1 = calcLine(cnts2[i][0], cnts2[i][1])
        m2, b2 = calcLine(cnts2[i][2], cnts2[i][3])

        for j in range(i + 1, len(cnts2) - 1):

            # if this contour's area is too small, there's no use in looking
            # through the rest because they're sorted by area
            if cnts2[i][0] * 2 / 3 < cnts2[j][0]:
                break

            """ Following commented out code does not work because of varying camera perspectives. """
            # # if they line up, it fills the final requirement and yey. Returns the centers.
            # else if (cnts2[i][2] - 15 <= cnts2[j][2]) and (cnts2[i][2] + 15 >= cnts2[j][2]):
            #     if cnts2[i][1] > cnts2[j][1]:
            #         return cnts2[j][1], cnts2[j][2], cnts2[i][1], cnts2[i][2], True
            #     else:
            # return cnts2[i][1], cnts2[i][2], cnts2[j][1], cnts2[j][2], True

while True:
    ret, frame = cap.read()

    lower = np.array([cv2.getTrackbarPos('LH', 'sliders'), cv2.getTrackbarPos(
        'LS', 'sliders'), cv2.getTrackbarPos('LV', 'sliders')])
    upper = np.array([cv2.getTrackbarPos('UH', 'sliders'), cv2.getTrackbarPos(
        'US', 'sliders'), cv2.getTrackbarPos('UV', 'sliders')])
    findTape(frame, lower, upper)

    if cv2.waitKey(5) & 0xFF == ord('q'):
        break
