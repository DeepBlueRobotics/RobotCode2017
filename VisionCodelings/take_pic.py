import cv2, subprocess
import numpy as np
import time

subprocess.call("uvcdynctrl -d video1 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video1 -s \"Exposure (Absolute)\" 5", shell = True)
subprocess.call("uvcdynctrl -d video0 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video0 -s \"Exposure (Absolute)\" 5", shell = True)
subprocess.call("uvcdynctrl -d video2 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video2 -s \"Exposure (Absolute)\" 5", shell = True)
cap = cv2.VideoCapture(1)

cap.open(1)

ret, frame = cap.read()

name = "/home/pi/Documents/RobotCode2017/RobotCode2017/img" + str(time.time()) + ".ppm"

cv2.imwrite(name, frame)
cap.release()

