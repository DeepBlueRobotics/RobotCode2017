from networktables import NetworkTables
import logging
logging.basicConfig(level=logging.DEBUG)

debug = True
shooterRunning = False
gearRunning = False



    #print("valueChanged: key: '%s'; value: %s; isNew: %s" % (key, value, isnew))

def connectionListener(connected, info):
        print(info, '; Connected=%s' %connected)

class NTClient:
    def __init__(self): 
        NetworkTables.initialize(server='roboRIO-199-FRC.local')
        self.nt = NetworkTables.getTable("SmartDashboard/Vision")
        self.gearRunning = False
        self.shooterRunning = False
        if debug:
            NetworkTables.addGlobalListener(self.valueChanged)
    def valueChanged(self, key, value, isnew):
        if key == '/SmartDashboard/Vision/shooterRunning' and not isnew:
            self.shooterRunning = value
            print key
            print self.shooterRunning
        if key == '/SmartDashboard/Vision/gearRunning' and not isnew:
            self.gearRunning = value
            print key
            print self.gearRunning
            
         
    def changeSubTable(self, subtable):
        """Specify the subtable the client should now refer to"""
        self.nt = NetworkTables.getTable("SmartDashboard/" + subtable)
    
    
    def write(self, subtable, key, value):
        """Specify a subtable that you want to write a value to, the key of the value, and the value"""
        self.nt = NetworkTables.getTable("SmartDashboard/" + subtable)
        if type(value) == bool:
            self.nt.putBoolean(key, value)
        if type(value) == int or type(value) == float:
            self.nt.putNumber(key, value)
        if type(value) == type(""):
            self.nt.putString(key, value)
        self.nt = NetworkTables.getTable("SmartDashboard/Vision")
                
    def get(self, subtable, key, defaultValue):
        """Specify a subtable that you want to retrieve a value from, the key of the value, and the value"""
        self.nt = NetworkTables.getTable("SmartDashboard/" + subtable)
#        if type(defaultValue) == bool:
#            return self.nt.getBoolean(key, defaultValue)
#        if type(defaultValue) == int or type(defaultValue) == float:
#            return self.nt.getNumber(key, defaultValue)
#        if type(defaultValue) == type(""):
#            return self.nt.getString(key, defaultValue)
        return self.nt.getValue(key, defaultValue=None)
        self.nt = NetworkTables.getTable("/SmartDashboard/Vision")

    def getGear(self):
        return self.gearRunning
    def getShooter(self):
        return self.shooterRunning
            
    
