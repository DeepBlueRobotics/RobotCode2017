'''
Must:
Run script to set camera exposure
Run boiler identification on loop and put to SmartDashboard if shooter command running
Run gear mark identification only ~three times and put to SmartDashboard if gear command run
'''

from access_nt import NTClient  # have similar lines to import the boiler identifier and gear identifier class 

import boiler_identify
import lift_marks_indentify

nt = NTClient()


# Exposure script



while(True):
    if 0 == 0:  # Condition should be based on whether a certain boolean value in NetworkTables says the shooter command is running
        # Run boiler identification script
        print ":D"  # Placeholder line that will be changed later
        
    if 1 == 1:  # Condition should be based on whether a certain boolean value in NetworkTables says the gear command is running
        # Run gear mark identification
        print "D:"  # Another placeholder
