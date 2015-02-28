#COMP3111 Project

##Phrase 1 deadline: April 3rd

* Single User calendar
	* No other users are present or can view/access the information of the calendar.
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
		* Do not program directly using computer's clock for testing purpose

##Phrase 2 deadline: May 8th

##Where to start

Go to the main class first. I(Louis) will be adding more and more comments explaining how the program works.

##How to use GitHub

####Method 1: Use Eclipse + dedicated GitHub client

- Download GitHub client for your OS
- install GitHub client and login
- press the plus icon on the top left corner
- Go to clone
- select the repo "louis993546/COMP-3111"
- Save it wherever you want{folder X} (except Dropbox/Google Drive/OneDrive folder. They will ruin everything)
- Wait for it to finish cloning
- Open eclipse
- set workspace as {folder X}
- file >> Import Project
- search "git". There should only be 1 choice left
- "import from existing folder"(something like that)
- Press "Add" and select {folder X} again
- click next all the way to the end
- Congragulation!

####Method 2: Use Eclipse and EGit plugin

* <a href="http://eclipsesource.com/blogs/tutorials/egit-tutorial/">EGit Tutorial</a>
