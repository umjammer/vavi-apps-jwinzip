# vavi.awt.gamepad

## Situation

- as for test `Gamepad`
  - test got error below 
  - setting `vavi.games.input.hid4java.darwinOpenDevicesNonExclusive` `true` doesn't work
    - hid4java ... `../../java/hid4java`
  - currently `Hub.app` is running that uses the same uid, pid device via hid4java
    - Hub.app ... `../vavi-apps-hub` 

```text
9月 27, 2026 7:05:44 午後 org.hid4java.macos.MacosHidDevice internalOpen
重大: java.io.IOException: create: failed to open IOHIDDevice from mach entry: (0xE00002C5)
java.io.IOException: create: failed to open IOHIDDevice from mach entry: (0xE00002C5)
	at org.hid4java.macos.MacosHidDevice.internalOpen(MacosHidDevice.java:225)
	at org.hid4java.macos.MacosHidDevice.getReportDescriptor(MacosHidDevice.java:549)
	at org.hid4java.HidDevice.getReportDescriptor(HidDevice.java:418)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.attach(Hid4JavaEnvironmentPlugin.java:159)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.lambda$enumerate$0(Hid4JavaEnvironmentPlugin.java:107)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.enumerate(Hid4JavaEnvironmentPlugin.java:105)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.getControllers(Hid4JavaEnvironmentPlugin.java:226)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.getController(Hid4JavaEnvironmentPlugin.java:265)
	at vavi.awt.gamepad.Gamepad.start(Gamepad.java:78)
	at vavi.awt.gamepad.Gamepad.main(Gamepad.java:94)

9月 27, 2026 7:05:44 午後 vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin lambda$enumerate$0
情報: check system property vavi.games.input.hid4java.darwinOpenDevicesNonExclusive is true
9月 27, 2026 7:05:44 午後 vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin lambda$enumerate$0
重大: create: failed to open IOHIDDevice from mach entry: (0xE00002C5)
java.io.IOException: create: failed to open IOHIDDevice from mach entry: (0xE00002C5)
	at org.hid4java.macos.MacosHidDevice.internalOpen(MacosHidDevice.java:225)
	at org.hid4java.macos.MacosHidDevice.getReportDescriptor(MacosHidDevice.java:549)
	at org.hid4java.HidDevice.getReportDescriptor(HidDevice.java:418)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.attach(Hid4JavaEnvironmentPlugin.java:159)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.lambda$enumerate$0(Hid4JavaEnvironmentPlugin.java:107)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.enumerate(Hid4JavaEnvironmentPlugin.java:105)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.getControllers(Hid4JavaEnvironmentPlugin.java:226)
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.getController(Hid4JavaEnvironmentPlugin.java:265)
	at vavi.awt.gamepad.Gamepad.start(Gamepad.java:78)
	at vavi.awt.gamepad.Gamepad.main(Gamepad.java:94)

Exception in thread "main" java.util.NoSuchElementException: no device: mid: 1356(0x54c), pid: 2508(0x9cc))
	at vavi.games.input.hid4java.spi.Hid4JavaEnvironmentPlugin.getController(Hid4JavaEnvironmentPlugin.java:272)
	at vavi.awt.gamepad.Gamepad.start(Gamepad.java:78)
	at vavi.awt.gamepad.Gamepad.main(Gamepad.java:94)
```

## Mission

- investigate why above doesn't work