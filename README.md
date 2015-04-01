#COMP3111 Project

###Tutorials
* <a href="http://www.tutorialspoint.com/sqlite/sqlite_java.htm">SQLite</a>. I LITERALLY copy and paste things from here.
* <a href="https://www.siteground.com/tutorials/git/commands.htm">Some basic git commands</a>
* Google
* <a href="http://www.javapractices.com/topic/TopicAction.do?Id=234">Fake System Clock</a>

#### How-to #1: Import project to GitHub
- Make sure you are not already in the folder you want to import
	* For example, you are in D:\GitHub\COMP3111, and the folder you want to import is D:\GitHub\COMP3111\calendar, this is fine
	* But, if you are in D:\GitHub\COMP3111\calendar and you want to import D:\GitHub\COMP3111\calendar, it won't work.
- "File" >> "Import" >> Search "Existing Projects into Workspace"
- Select that folder.
- Done

###To-Do in this branch
* Check the various TODO in eclipse

##Phrase 1 deadline: April 3rd

* [DONE?] Single User calendar in GUI
	* No other users are present or can view/access the information of the calendar.
	* Need to extra implementation in this phrase
* Basic event scheduling
	* Able to provide basic scheduling capabilities for an individual user.
		* starting time
		* end time
		* event description(more like title)
		* optional event location
			* must be from a list of predefined locations in the system
				* uniquely identified by its name
			* can be added though separate interface
				* which does not exist yet
		* frequency
			* one time
			* daily
			* weekly
			* monthly
			* for how long
				* This will simply create a bunch of appointments and add them all to the database
		* optional reminder
			* how much time ahead the reminder should be triggered
			* display to the user at or less than the specified time interval before the scheduled time of the event
			* only be given ONCE
		* optional description for the event.
	* Time specification
		* in intervals of 15 minutes
			* 00
			* 15
			* 30
			* 45
	* Successfully scheduled event should cause GUI to change accordingly
		* Show in daily view
		* That day need to change color
	* Event validity
		* Cannot schedule event in the past
		* Cannot schedule events overlap in time or in space
		* Can only ,odify/delete event happening in the future
	* Time machine
		* Create a custom clock

##Phrase 2 deadline: May 8th

* Multi-User
	* Normal user
	* Admin
* Different user sees different calendar
	* Each appointments needs to specified who have the right to view/edit it
* Group event
	* Cannot be (Same place && Same Time)
	* Each participant cannot go to 2+ event at the same time
	* Appointment will not be added unless all participants reply "Yes"
	* Only event creator can delete it
* TBC
