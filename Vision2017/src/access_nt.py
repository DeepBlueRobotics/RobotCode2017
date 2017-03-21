from networktables import NetworkTables
import logging
logging.basicConfig(level=logging.DEBUG)

debug = False
shooterRunning = False
gearRunning = False
values = {}


def connectionListener(connected, info):
	print(info, '; Connected=%s' %connected)

class NTClient:
	def __init__(self): 
		NetworkTables.initialize(server='roboRIO-199-FRC.local')
		self.nt = NetworkTables.getTable("SmartDashboard/Vision")
		self.gearRunning = False
		self.shooterRunning = False
		NetworkTables.addGlobalListener(self.valueChanged)
		
	def valueChanged(self, key, value, isnew):
		"""Used by the GlobalListener to change desired values, if change in them detected"""
		if key == '/SmartDashboard/Vision/shooterRunning' and not isnew:
			self.shooterRunning = value
			if debug:
				print key
				print self.shooterRunning
		if key == '/SmartDashboard/Vision/gearRunning' and not isnew:
			self.gearRunning = value
			if debug:
				print key
				print self.gearRunning
		if key[:25] == '/SmartDashboard/HSVrange/':
			self.values[key[25:]] = value
			
		 
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
				
	#Does not work for unknown reason, use getGear() and getShooter() instead
	def get(self, subtable, key, defaultValue):
		"""Specify a subtable that you want to retrieve a value from, the key of the value, and the value"""
		self.nt = NetworkTables.getTable("SmartDashboard/" + subtable)
		return self.nt.getValue(key, defaultValue=None)
		self.nt = NetworkTables.getTable("/SmartDashboard/Vision")

	# works, I hope ;)
	def getHSV(self, key, defaultValue):
		if key in self.values:
			return self.values[key]
		else:
			return defaultValue

	def getGear(self):
		return self.gearRunning
	def getShooter(self):
		return self.shooterRunning
			
	
