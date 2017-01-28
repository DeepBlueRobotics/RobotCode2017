from networktables import NetworkTables
import logging
logging.basicConfig(level=logging.DEBUG)

debug = false

class NTClient:
    def __init__(self): 
        NetworkTables.initialize(server='roboRIO-199-FRC.local')
        self.nt = NetworkTables.getTable("SmartDashboard/vision")
        if debug:
            NetworkTables.addGlobalListener(valueChanged)
            
    
    def write(self, key, value):
        if type(value) == bool:
            self.nt.putBoolean(key, value)
        if type(value) == int or type(value) == float:
            self.nt.putNumber(key, value)
        if type(value) == type(""):
            self.nt.putString(key, value)
        
    def get(self, key, defaultValue):
        if type(defaultValue) == bool:
            self.nt.getBoolean(key, defaultValue)
        if type(defaultValue) == int or type(defaultValue) == float:
            self.nt.getNumber(key, defaultValue)
        if type(defaultValue) == type(""):
            self.nt.getString(key, defaultValue)
            
    def valueChanged(key, value, isnew):
        print("valueChanged: key: '%s'; value: %s; isNew: %s" % (key, value, isnew))

    def connectionListener(connected, info):
        print(info, '; Connected=%s' %connected)
