from networktables import NetworkTables

class NTClient:
    def __init__(self): 
        NetworkTables.initialize(server='10.1.99.2')
        self.nt = NetworkTables.getTable("SmartDashboard/vision")
    
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
