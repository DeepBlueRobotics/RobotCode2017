'''
This program itself is thus far untested.
This tests the functionality of the vision (shooter or gear) 
on the raspberry pi. It doesn't check processing frame rate.
It includes HSV range sliders and the exposure script. To run
gear camera script instead of shooter script, replace all
boiler_identify with lift_marks_identify and findCenters with
findTape. Use hsv_mask_sliders vision codeling to find range
first and change lower[] and upper[].
'''

import numpy as np
import cv2
import boiler_identify

cap = cv2.VideoCapture(0)
ret = cap.set(3,320)
ret = cap.set(4,180)

subprocess.call("uvcdynctrl -d video0 -s \"Exposure, Auto\" 1", shell=True)
subprocess.call(
	"uvcdynctrl -d video0 -s \"Exposure (Absolute)\" 5", shell=True)

lower = np.array([0, 0, 0])
upper = np.array([179, 255, 255])

while(True):
    # Capture frame-by-frame
    ret, frame = cap.read()
    
    centers = boiler_identify.findCenters(frame, lower, upper)
    
    cv2.circle(frame, centers[0], 2, (255,0,255))
    cv2.circle(frame, centers[1], 2, (255,0,255))
    cv2.putText(frame, centrs, (10,500), cv2.FONT_HERSHEY_SIMPLEX, 2, (0,255,0), 13, cv2.LINE_AA)

    cv2.imshow('frame',frame)
    if cv2.waitKey(1) == ord('q'):
        break

# When everything done, release the capture
cap.release()
cv2.destroyAllWindows()