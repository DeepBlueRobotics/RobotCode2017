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

# Gear Tape values
gearFrameCounter = -1
leftGearTapeX, leftGearTapeY, rightGearTapeX, rightGearTapeY = 0


while(True):
    if 0 == 0:  # Condition should be based on whether a certain boolean value in NetworkTables says the shooter command is running
        ret, shooterFrame = shooterCap.read()
        # Run boiler identification script
        boiler_identify.findTape(shooterFrame, lowerHSV, upperHSV)
        
        print ":D"  # Placeholder line that will be changed later
    
   
    """gear tape identification code from here"""

    gearFrameCounter = 0 if nt.get("AutoAlignGear", "running") == True
    
    if gearFrameCounter != -1:
        ret, gearFrame = gearCap.read()
        # Run gear mark identification
        lx, ly, rx, ry = lift_marks_identify.findTape(gearFrame, lowerHSV, upperHSV)
        
        leftGearTapeX += lx
        leftGearTapeY += ly
        rightGearTapeX += rx
        rightGearTapeY += ry

        gearFrameCounter += 1
        print "gear tape identified {} times".format(gearFrameCounter)   

        if gearFrameCounter == 3:
            leftGearTapeX /= 3
            leftGearTapeY /= 3
            rightGearTapeX /= 3
            rightGearTapeY /= 3

            nt.write("AutoAlignGear", "leftCenterX", leftGearTapeX)
            nt.write("AutoAlignGear", "leftCenterY", leftGearTapeY)
            nt.write("AutoAlignGear", "rightCenterX", rightGearTapeX)
            nt.write("AutoAlignGear", "rightCenterY", rightGearTapeY)

            gearFrameCounter = -1
            print "gear tape identified {} time".format(gearFrameCounter)        

    
