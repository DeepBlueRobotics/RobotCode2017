import cv2
import numpy as np
 
lower = np.array([43, 0, 252])
upper = np.array([78, 3, 252])

cap = cv2.VideoCapture(0)

while(1):
    ret, frame = cap.read()
    cv2.imshow('frame',frame)
    
    k = cv2.waitKey(1) & 0xFF
    if k == 27:
        break
cv2.imwrite('dark.jpg',frame)
cap.release()
cv2.destroyAllWindows()