import numpy as np
import cv2

cap = cv2.VideoCapture(0)

def nothing(x):
    pass

cv2.namedWindow('sliders')

cv2.createTrackbar('LH','sliders',0,255,nothing)
cv2.createTrackbar('UH','sliders',0,255,nothing)
cv2.createTrackbar('sigma','sliders',75,150,nothing)



# while (True):
foobar, im = cap.read() 
blur = cv2.bilateralFilter(im,9,cv2.getTrackbarPos('sigma','sliders'),cv2.getTrackbarPos('sigma','sliders'))
hsv = cv2.cvtColor(blur,cv2.COLOR_BGR2HSV)
#     ret,thresh = cv2.threshold(imgray,127,255,0)

lower = np.array([cv2.getTrackbarPos('LH','sliders'), 100, 100])
upper = np.array([cv2.getTrackbarPos('UH','sliders'), 255, 255])
                  
mask = cv2.inRange(hsv, lower, upper)

image, contours, hierarchy = cv2.findContours(mask,cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
print contours
img = cv2.drawContours(blur, contours, -1, (0,0,255), 3)
#     cv2.imshow("bilateral", img)

keyPress = cv2.waitKey(1) & 0xFF
# if keyPress == ord('q'):
#     break

cap.release()    
cv2.destroyAllWindows()