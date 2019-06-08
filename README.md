# TestSNMP
Performs very basic SNMP checking of Raspberry Pis that are used for development work

I created this application about a year ago as something to perform some basic monitoring of the stack of Raspberry Pis I was using for development purposes.  I decided on using the snmp4j library in Java over what I had found for python for several reasons.  The first reason is that it was counterintuitive to how I thought a monitoring library would operate.  The second reason to use it was because what I had found in python lacked the real flexibility I needed.  The third reason, is that I didn’t want to be tethered to a web browser.  Version 1, which is what I am using, uses IPv4 only, shows network utilization, CPU usage and memory usage.  I have plans for version 2, but have some research to do to see about implementation of my desires.
Those details include the following:

Support for IPv6.
Special services support such as firewall, http, database etc.  This is where the research comes in.
A popup hover window that gives details such as IPv4 and IPv6 address, and those services.
The ability to use version 2c for snmp polling.
Possible support for traps.  This one is dependent on whether I really want to report an event after the device has recovered.  Examples include ‘system has rebooted’.

It’s not the best, but it is what I could come up with over the weekend.  I am looking to keep the graphical interface small.  I was hoping to get around an issue without using JNI.  The issue being that this is primarily on a laptop and if it goes to sleep, when the laptop is awakened, the app doesn’t resume polling and the virtual machine doesn’t provide a mechanism for detecting it, hence the use of JNI.
