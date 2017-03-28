'''
Must:
Run script to set camera exposure
Run boiler identification on loop and put to SmartDashboard if shooter command running
Run gear mark identification only ~three times and put to SmartDashboard if gear command run
Set the right ports for cams
'''

""" Imports and Inits """
from access_nt import NTClient  # have similar lines to import the boiler identifier and gear identifier class
import cv2
import subprocess
import numpy as np
import boiler_identify
import lift_marks_identify
# import time

oneTime = True

nt = NTClient()

# cameras
shooterCap = cv2.VideoCapture(0)
gearCap = cv2.VideoCapture(1)

try:
	# HSV ranges - getting
	shooterLowHSV = np.array(
		nt.getHSV("ShooterLowHue", 48), 
		nt.getHSV("ShooterLowSat", 175), 
		nt.getHSV("ShooterLowVal", 100)
	)
	shooterHighHSV = np.array(
		nt.getHSV("ShooterHighHue", 100), 
		nt.getHSV("ShooterHighSat", 255), 
		nt.getHSV("ShooterHighVal",200)
	)

	gearLowHSV = np.array(
		nt.getHSV("GearLowHue", 65), 
		nt.getHSV("GearLowSat", 175), 
		nt.getHSV("GearLowVal", 70)
	)
	gearHighHSV = np.array(
		nt.getHSV("GearHighHue", 100), 
		nt.getHSV("GearHighSat", 255), 
		nt.getHSV("GearHighVal",200)
	)

except Exception as e:
		nt.write("Vision", "pythonError", "Error at HSV value initialization: {}".format(str(e)))
		shooterLowHSV = np.array((48,175,100))
		shooterHighHSV = np.array((100,255,200))
		gearLowHSV = np.array((65,175,100))
		gearHighHSV = np.array((100,255,200))
		
#log = open("/tmp/vision.log", 'w')

# Gear Tape values
gearFailCounter = 0

""" Exposure script """
# shooter camera
subprocess.call("uvcdynctrl -d video0 -s \"Exposure, Auto\" 1", shell=True)
subprocess.call("uvcdynctrl -d video0 -s \"Exposure (Absolute)\" 5", shell=True)

# gear camera
subprocess.call("uvcdynctrl -d video1 -s \"Exposure, Auto\" 1", shell=True)
subprocess.call("uvcdynctrl -d video1 -s \"Exposure (Absolute)\" 5", shell=True)

# alt shooter camera
subprocess.call("uvcdynctrl -d video2 -s \"Exposure, Auto\" 1", shell=True)
subprocess.call("uvcdynctrl -d video2 -s \"Exposure (Absolute)\" 5", shell=True)

# alt gear camera
subprocess.call("uvcdynctrl -d video3 -s \"Exposure, Auto\" 1", shell=True)
subprocess.call("uvcdynctrl -d video3 -s \"Exposure (Absolute)\" 5", shell=True)

#log.write("log works.\n")
nt.write("Vision", "OH-YEAH", False)
nt.write("Vision", "gearRunning", False)
nt.write("Vision", "shooterRunning", False)

""" Main Loop """
while (True):
	try:
		""" boiler tape identification code """
		if nt.getShooter():
			if (not shooterCap.isOpened()):
				#log.write("shooter running\n")
				shooterCap.open(0)
				shooterCap.set(3, 640)
				shooterCap.set(4, 360)
			ret, shooterFrame = shooterCap.read()
			# Run boiler identification script
			centers = boiler_identify.findBoiler(
				shooterFrame, shooterLowHSV, shooterHighHSV)

			nt.write("Vision", "shooterCodeRunning", True)

			nt.write("Vision", "boilerFound", centers[0] != -1)
			if (centers[0] != -1):
				nt.write("Vision", "boilerX", centers[0])
				nt.write("Vision", "boilerY", centers[1])
		else:
			nt.write("Vision", "shooterCodeRunning", False)
			if (shooterCap.isOpened()):
				shooterCap.release()
		""" gear tape identification code """
		if nt.getGear():
			if (not gearCap.isOpened()):
				#log.write("gear running\n")
				gearCap.open(1)
				gearCap.set(3, 640)
				gearCap.set(4, 360)

			ret, gearFrame = gearCap.read()
			# Run gear mark identification
			lx, ly, rx, ry, lb, lt, rb, rt, success = lift_marks_identify.findTape(
							gearFrame, np.array([65, 175, 70]), np.array([100, 255, 200]))
			#lx, ly, rx, ry, lb, lt, rb, rt, success = lift_marks_identify.findTape(
			# 	gearFrame, gearLowHSV, gearHighHSV)

			nt.write("Vision", "gearCodeRunning", True)

			if (success):
				nt.write("Vision", "leftGearCenterX", lx)
				nt.write("Vision", "leftGearCenterY", ly)
				nt.write("Vision", "rightGearCenterX", rx)
				nt.write("Vision", "rightGearCenterY", ry)

				nt.write("Vision", "leftGearBottomY", lb)
				nt.write("Vision", "leftGearTopY", lt)
				nt.write("Vision", "rightGearBottomY", rb)
				nt.write("Vision", "rightGearTopY", rt)

				nt.write("Vision", "OH-YEAH", success)
			#elif oneTime:
				#cv2.imwrite("/home/pi/Documents/RobotCode2017/RobotCode2017/img" + str(time.time()) + ".ppm", gearFrame)
				#oneTime = False

		else:
			nt.write("Vision", "gearCodeRunning", False)
			nt.write("Vision", "OH-YEAH", False)
			oneTime = True
			#if (gearCap.isOpened()):
				#gearCap.release()
	except Exception as e:
		nt.write("Vision", "pythonError", "Error in main loop: {}".format(str(e)))
