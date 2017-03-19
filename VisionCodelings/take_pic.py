import cv2, subprocess
import numpy as np
import time

subprocess.call("uvcdynctrl -d video1 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video1 -s \"Exposure (Absolute)\" 5", shell = True)

cap = cv2.VideoCapture(0)

ret, frame = cap.read()

name = "img" + str(time.time()) + ".jpg"

cv2.imwrite(name, frame)
cap.release()

