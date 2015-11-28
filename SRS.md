# System Requirements Specification for ADAM
The Advanced Driving Assistance on a Mobile (ADAM) is a solution to assist while driving with a commodity mobile device.
This system operates within city/overland traffic boundaries.


## Feature List
### Functional requirements:

#### Driving

1. Show a warning if the distance is below the average break distance (including human reaction and actual breaking time) (may require further calibration or knowledge of break effectiveness)
2. Inform with a warning if a lane departure is expected to happen within the next x(e.g. 5 ) seconds
3. Show which lane is about to be crossed

#### Traffic and flow information

4. Give an indication if the cars in front are about to break by detecting the brakelight
5. Detect and show speed signs (including city signs, end of speed limits) and show the detected speed limit
6. Detect priority in traffic by showing the direction which has priority  

#### Dashcam

7. Record video footage in case of a major incident
8. Display last detected information such as speed, cars in front, detected signs and lanes

### Non-functional requirements
1. The solution should run on a commodity Android device with at least 4 CPU cores and a GPU with OpenGL ES 2.x support, support OpenCV runtime, at least 512 MB RAM, back-facing camera with at least FullHD resolution, sustainable charging (run on power-source with battery charging)
2. The solution should run in almost real-time (no longer than the average humand reaction time) to detect objects/situations
3. The system shall not record any data and must withdraw video footage after processing
4. No car signs should be disclosed and any detected signs should be anonymoused while showing the captured video
5. The system shall reach a detection ratio of at least 50% for lanes, 75% for traffic signs, 50% for brakelight, 75% for lane departure
6. The solution requires a steady power connection to bridge longer driving sessions

## Use Cases

Element | Content
------- | -------
Use Case ID |	W1
Name/Summary | Warning of lane departure
Priority | high
Preconditions | -
Postconditions |User has been informed about any expected lane departure
Primary Actor | User
Secondary Actor(s) | Video stream
Trigger | User starts system
Main Scenario | 1.	User starts Application
2.	User selects lane departure warning mode
3.	System displays a warning whenever a lane departure is expected to happen
4.	User deselects lane departure warning mode or quits Application
Extensions | 2a No video stream available
2b System displays missing video stream warning
Open Issues | -


Use Case ID	W2
Name/Summary	Warning of critical braking distance
Priority	high
Preconditions	-
Postconditions	User has been informed about any critical braking distances detected
Primary Actor	User
Secondary Actor(s)	Video stream
Trigger	User starts system
Main Scenario	1.	User starts Application
2.	User selects critical braking distance warning mode
3.	System displays a warning whenever the braking distance to a vehicle upfront gets critical
4.	User deselects critical braking distance warning mode or quits Application
Extensions	2a No video stream available
2b System displays missing video stream warning
Open Issues	-

Use Case ID	W3
Name/Summary	Information of street signs
Priority	middle
Preconditions	-
Postconditions	User has been informed about any street signs detected
Primary Actor	User
Secondary Actor(s)	Video stream
Trigger	User starts system
Main Scenario	1.	User starts Application
2.	User selects Street Sign Information mode
3.	System displays detected street signs and their speed information
4.	User deselects Street Sign Information mode or quits Application
Extensions	2a No video stream available
2b System displays missing video stream warning
Open Issues	-

Use Case ID	W4
Name/Summary	Get video footage
Priority	middle
Preconditions	-
Postconditions	If incident has been marked, video has been saved
Primary Actor	User
Secondary Actor(s)	Video Stream
Trigger	User starts system
Main Scenario	1.	User starts Application
2.	User starts Recording
3.	System shows record mode and incident button
4.	(opt.) User marks incident
5.	(opt.) Video footage is made anonymous permanently saved and success is displayed
6.	User stops Recording or quits Application
Extensions	3a No Video stream available or no disk space
3b System displays record failure
Open Issues	-

Use Case ID	W5
Name/Summary	Warning of braking vehicles
Priority	low
Preconditions	-
Postconditions	User has been informed about any braking vehicles in front
Primary Actor	User
Secondary Actor(s)	Video stream
Trigger	User starts system
Main Scenario	1.	User starts Application
2.	User selects critical braking distance warning mode
3.	System displays a warning whenever a vehicle’s braking lights in front are illuminated
4.	User deselects critical braking distance warning mode or quits Application
Extensions	2a No video stream available
2b System displays missing video stream warning
Open Issues	-

 