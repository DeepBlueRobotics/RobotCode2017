import numpy as np
import cv2, subprocess

subprocess.call("uvcdynctrl -d video0 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video0 -s \"Exposure (Absolute)\" 5", shell = True)

cap = cv2.VideoCapture(0)

while (True):
    bar, frame = cap.read()

    cv2.imshow("camera", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
