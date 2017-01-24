# Status: Probably works. Demo.
# After you put an image with some shapes in image.jpg, it will find 
# contours and display their outlines and centers. Pretty much just 
# http://www.pyimagesearch.com/2016/02/01/opencv-center-of-contour/

import imutils
import cv2

image = cv2.imread('image.jpg')
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
blurred = cv2.GaussianBlur(gray, (5, 5), 0)
thresh = cv2.threshold(blurred, 60, 255, cv2.THRESH_BINARY)[1]

# find contours in the thresholded image
cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL,
    cv2.CHAIN_APPROX_SIMPLE)
cnts = cnts[0] if imutils.is_cv2() else cnts[1]

# loop over the contours
for c in cnts:
    # compute the center of the contour
    if cv2.contourArea(c) > 0:
        M = cv2.moments(c)
        cX = int(M["m10"] / M["m00"])
        cY = int(M["m01"] / M["m00"])
     
        # draw the contour and center of the shape on the image
        cv2.drawContours(image, [c], -1, (0, 255, 0), 2)
        cv2.circle(image, (cX, cY), 7, (255, 255, 255), -1)
        cv2.putText(image, "center", (cX - 20, cY - 20),
            cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 2)
     
        # show the image
        cv2.imshow("Image", image)
        cv2.waitKey(0)
# 
# cv2.imshow('image',image)
# cv2.imshow('blurred',blurred)
# cv2.imshow('thresh',thresh)
# cv2.waitKey(0)
# cv2.destroyAllWindows()