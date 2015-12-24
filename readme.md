# Basic-multi-user-calendar
A simple calendar app with appointment function

####Dependency
* Java Runtime Environment (JRE)

####How to use

    java -jar  './Runnable JAR/24dec.jar'      # OR just double click the JAR file
    
####Layout

<img src="/docs/images/layout.png" height="200" />
####Feature

0. Allow multiple users (Not in the same time)
    1.  <img src="/docs/images/login.png" height="200" />
1. Group appointment (user can invite other users)
    1.  <img src="/docs/images/invite.png" height="200" />
    2.  <img src="/docs/images/confirm.png" height="200" />
2. Check conflicts for appointments
    1. in the same time for the same user or
        1.  <img src="/docs/images/TimeConflict.png" height="200" />
    2. in the same room for different users
        1.  <img src="/docs/images/LocationConflict.png" height="200" />
3. Notification a few minutes (defined by user) before appointment start time for current user
4. Users can make periodic appointments
    1.  <img src="/docs/images/periodic.png" height="200" />
5. Different permission
    1. For example, Admin users can edit others appointments or define available rooms while normal user cannot
6. A convenient fake clock for testing the app

