# Status: Works. Tool.
# Plain ol' camera. No alteration; just for testing cameras and choosing ports.

import numpy as np
import cv2
import subprocess

subprocess.call("uvcdynctrl -d video1 -s \"Exposure, Auto\" 1", shell=True)
subprocess.call("uvcdynctrl -d video1 -s \"Exposure (Absolute)\" 5", shell=True)

cap = cv2.VideoCapture(1)
ret = cap.set(3,320)
ret = cap.set(4,180)
while(True):
    # Capture frame-by-frame
    ret, frame = cap.read()
    
    cv2.imshow('frame',frame)
    if cv2.waitKey(1) & 0xFF == 27:
        break

# When everything done, release the capture
cv2.imwrite("/pic.jpg", frame)
cap.release()
cv2.destroyAllWindows()
