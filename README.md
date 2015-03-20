#COMP3111 Project

##What is in this README:
- Tutorial websites
- Errors + How-to
- To-Do
- Phrase 1 requirement
- [Working] Phrase 2 requirement

###Tutorials
* <a href="http://www.tutorialspoint.com/sqlite/sqlite_java.htm">SQLite</a>. I LITERALLY copy and paste things from here.
* <a href="https://www.siteground.com/tutorials/git/commands.htm">Some basic git commands</a>
* Anything else you want to learn?
* And Google, of course

#### Error #1: Missing required library sqlite-jbdc
- Locate "sqlite-jdbc-3.8.7.jar" in "calendar"
- If it does not exist, download the latest version of JBDC from <a href="https://bitbucket.org/xerial/sqlite-jdbc/downloads">here</a>
- Right click project "calendar" on the left
- Properties >> Java Build Path >> Libraries
- "Add external JARs"
- select the .jar
- done

#### Error #2: Not able to open eclipse.exe/eclipse.app
* Just download another one from <a href="https://eclipse.org/downloads/">here</a>. Apparently eclipse cannot be sync to GitHub very successfully.

#### How-to #1: Import project to GitHub
- Make sure you are not already in the folder you want to import
	* For example, you are in D:\GitHub\COMP3111, and the folder you want to import is D:\GitHub\COMP3111\calendar, this is fine
	* But, if you are in D:\GitHub\COMP3111\calendar and you want to import D:\GitHub\COMP3111\calendar, it won't work.
- Import existing directory
- Select that folder.
- Done

###To-Do in this branch
* Some GUI are still incomplete/missing
	* ModifyLocationDialog looks ugly
		* wrap it in a JPanel maybe
		* fixed size maybe?
	* AddLocationDialog can be merge into ModifyLocationDialog?
* Most buttons are still useless
* More SQLite implementation
	* Write data from SQLite across all DB
	* Read data from SQLite across all DB
	* Update data from SQLite across all DB
	* Delete data from SQLite across all DB
* Admin mode has not been implemented yet(Phrase 2)
	* A UserDialog for admin to view and edit information of each users
* Need to figure out if the current implementation is suitable for phrase 2
	* e.g. if there is anything that will required extensive changes in order to make phrase 2 possible



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
		* Do not program directly using computer's clock for testing purpose?

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
