import numpy as np
import cv2, operator

# subprocess.call("uvcdynctrl -d video1 -s \"Exposure, Auto\" 1", shell = True)
# subprocess.call("uvcdynctrl -d video1 -s \"Exposure (Absolute)\" 5", shell = True)

cap = cv2.VideoCapture(0)

def nothing(x):
    pass

cv2.namedWindow('sliders',cv2.WINDOW_NORMAL)

cv2.createTrackbar('LH','sliders', 0, 179, nothing)
cv2.createTrackbar('UH','sliders', 179, 179, nothing)

cv2.createTrackbar('LS','sliders', 0, 255, nothing)
cv2.createTrackbar('US','sliders', 21, 255, nothing)

cv2.createTrackbar('LV','sliders', 238, 255, nothing)
cv2.createTrackbar('UV','sliders', 255, 255, nothing)

while True:
    ret, frame = cap.read()

    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    lower = np.array([cv2.getTrackbarPos('LH','sliders'),cv2.getTrackbarPos('LS','sliders'),cv2.getTrackbarPos('LV','sliders')])
    upper = np.array([cv2.getTrackbarPos('UH','sliders'),cv2.getTrackbarPos('US','sliders'),cv2.getTrackbarPos('UV','sliders')])

    mask = cv2.inRange(hsv, lower, upper)
    maskRGB = cv2.cvtColor(mask,cv2.COLOR_GRAY2RGB)
    cnts = cv2.findContours(mask.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)[1]

    maxArea = 0
    maxAreaCnt = None

    for c in cnts:
        epsilon = 0.05 * cv2.arcLength(c,True)
        c = cv2.approxPolyDP(c, epsilon, True)
        
        area = cv2.contourArea(c)
        
        bound = cv2.boundingRect(c)
        # boundArea = cv2.contourArea(bound)
        boundArea = bound[2] * bound[3]

        # checks if the thing is a quadrilateral, then records area and centers if true
        if (len(c) == 4) and area > maxArea:
            maxArea = area
            maxAreaCnt = c

    if maxAreaCnt != None:
        cv2.drawContours(maskRGB, [maxAreaCnt], 0, (0, 255, 0), 5)

        # cv2.circle(maskRGB, tuple(maxAreaCnt[0][0]), 5, (255, 0, 0))
        # cv2.circle(maskRGB, tuple(maxAreaCnt[1][0]), 5, (0, 0, 255))
        # cv2.circle(maskRGB, tuple(maxAreaCnt[2][0]), 5, (255, 255, 0))
        # cv2.circle(maskRGB, tuple(maxAreaCnt[3][0]), 5, (0, 255, 255))

        for i in range(0, 4):
            cv2.putText(maskRGB, str(i), tuple(maxAreaCnt[i][0]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
        
        

    cv2.imshow('sliders', np.zeros((1,1,3), np.uint8))
    cv2.imshow('contours', maskRGB)


    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

