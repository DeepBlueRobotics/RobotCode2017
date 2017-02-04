'''
Must:
Run script to set camera exposure
Run boiler identification on loop and put to SmartDashboard if shooter command running
Run gear mark identification only ~three times and put to SmartDashboard if gear command run
Set the right ports for cams
'''


""" Imports and Inits """
from access_nt import NTClient  # have similar lines to import the boiler identifier and gear identifier class 
import cv2, subprocess
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

# Gear Tape values
doFindGearTape = False
gearFailCounter = 0



""" Exposure script """
# shooter camera
subprocess.call("uvcdynctrl -d video0 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video0 -s \"Exposure (Absolute)\" 5", shell = True)

# gear camera
subprocess.call("uvcdynctrl -d video1 -s \"Exposure, Auto\" 1", shell = True)
subprocess.call("uvcdynctrl -d video1 -s \"Exposure (Absolute)\" 5", shell = True)


""" Main Loop """
while(True):

    """ boiler tape identification code """
    if 0 == 0:  # Condition should be based on whether a certain boolean value in NetworkTables says the shooter command is running
        ret, shooterFrame = shooterCap.read()
        # Run boiler identification script
        boiler_identify.findTape(shooterFrame, lowerHSV, upperHSV)
        
        print ":D"  # Placeholder line that will be changed later
    
   
    """ gear tape identification code """

    doFindGearTape = True if nt.get("AutoAlignGear", "running") == True
    
    if doFindGearTape:
        if gearFailCounter < 5:
            ret, gearFrame = gearCap.read()
            # Run gear mark identification
            lx, ly, rx, ry, success = lift_marks_identify.findTape(gearFrame, lowerHSV, upperHSV)

            if not success:
                gearFailCounter += 1

            else:
                nt.write("AutoAlignGear", "leftCenterX", lx)
                nt.write("AutoAlignGear", "leftCenterY", lyY)
                nt.write("AutoAlignGear", "rightCenterX", rx)
                nt.write("AutoAlignGear", "rightCenterY", ry)
                # nt.write("AutoAlignGear", "pegX", (lx + rx) / 2)
                # nt.write("AutoAlignGear", "pegY", (ly + ry) / 2)
                nt.write("AutoAlignGear", "OH YEAH", True)
                print "Gear tape identification success."

                gearFailCounter = 0
                doFindGearTape = False
        else:
            nt.write("AutoAlignGear", "OH YEAH", False) 

            gearFailCounter = 0
            doFindGearTape = False  

    
