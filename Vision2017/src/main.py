'''
Must:
Run script to set camera exposure
Run boiler identification on loop and put to SmartDashboard if shooter command running
Run gear mark identification only ~three times and put to SmartDashboard if gear command run
Set the right ports for cams
'''

from access_nt import NTClient  # have similar lines to import the boiler identifier and gear identifier class 
import cv2
import numpy as np
import boiler_identify
import lift_marks_indentify

nt = NTClient()

# cameras
shooterCap = cv2.VideoCapture(0)
gearCap = cv2.VideoCapture(1)

# HSV range
lowerHSV = np.array([40, 0, 250])
upperHSV = np.array([83, 20, 255])

# Exposure script




while(True):
    if 0 == 0:  # Condition should be based on whether a certain boolean value in NetworkTables says the shooter command is running
        ret, shooterFrame = shooterCap.read()
        # Run boiler identification script
        boiler_identify.findTape(shooterFrame, lowerHSV, upperHSV)
        
        print ":D"  # Placeholder line that will be changed later
        
    if 1 == 1:  # Condition should be based on whether a certain boolean value in NetworkTables says the gear command is running
        ret, gearFrame = gearCap.read()
        # Run gear mark identification
        lift_marks_identify.findTape(gearFrame, lowerHSV, upperHSV)
        
        print "D:"  # Another placeholder
