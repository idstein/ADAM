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

See the [Use Case Diagram](https://github.com/idstein/ADAM/blob/master/ADAM%20Use%20Case%20Diagram.pdf)

Element | Content
------- | -------
Use Case ID | W1
Name/Summary | Warning of lane departure
Priority | high
Preconditions | -
Postconditions |User has been informed about any expected lane departure
Primary Actor | User
Secondary Actor(s) | -
Trigger | User starts system
Main Scenario | 1.	User starts Application<br>2.	User selects lane departure warning mode<br>3.	System displays a warning whenever a lane departure is expected to happen<br>4.	User deselects lane departure warning mode or quits Application
Extensions | 2a No video stream available<br>2b System displays missing video stream warning
Open Issues | -

Element | Content
------- | -------
Use Case ID | W2
Name/Summary | Warning of critical braking distance
Priority | high
Preconditions | -
Postconditions | User has been informed about any critical braking distances detected
Primary Actor | User
Secondary Actor(s) | -
Trigger | User starts system
Main Scenario | 1.	User starts Application<br>2.	User selects critical braking distance warning mode<br>3.	System displays a warning whenever the braking distance to a vehicle upfront gets critical<br>4.	User deselects critical braking distance warning mode or quits Application
Extensions | 2a No video stream available<br>2b System displays missing video stream warning
Open Issues | -

Element | Content
------- | -------
Use Case ID | W3
Name/Summary | Information of street signs
Priority | middle
Preconditions | -
Postconditions | User has been informed about any street signs detected
Primary Actor | User
Secondary Actor(s) | -
Trigger | User starts system
Main Scenario | 1.	User starts Application<br>2.	User selects Street Sign Information mode<br>3.	System displays detected street signs and their speed information<br>4.	User deselects Street Sign Information mode or quits Application
Extensions | 2a No video stream available<br>2b System displays missing video stream warning
Open Issues | -

Element | Content
------- | -------
Use Case ID | W4
Name/Summary | Get video footage
Priority | middle
Preconditions | -
Postconditions | If incident has been marked, video has been saved
Primary Actor | Video stream
Secondary Actor(s) | -
Trigger | User starts system
Main Scenario | 1.	User starts Application<br>2.	User starts Recording<br>3.	System shows record mode and incident button<br>4.	(opt.) User marks incident<br>5.	(opt.) Video footage is made anonymous permanently saved and success is displayed<br>6.	User stops Recording or quits Application
Extensions | 3a No Video stream available or no disk space<br>3b System displays record failure
Open Issues | -

Element | Content
------- | -------
Use Case ID | W5
Name/Summary | Warning of braking vehicles
Priority | low
Preconditions | -
Postconditions | User has been informed about any braking vehicles in front
Primary Actor | User
Secondary Actor(s) | -
Trigger | User starts system
Main Scenario | 1.	User starts Application<br>2.	User selects critical braking distance warning mode<br>3.	System displays a warning whenever a vehicle’s braking lights in front are illuminated<br>4.	User deselects critical braking distance warning mode or quits Application
Extensions | 2a No video stream available<br>2b System displays missing video stream warning
Open Issues | -

Element | Content
------- | -------
Use Case ID | W6
Name/Summary | Street speed signs recognition
Priority | middle
Preconditions | Video stream is available
Postconditions | Street signs in front of the device have been recognised and matched
Primary Actor | Video processor
Secondary Actor(s) | -
Trigger | New video footage
Main Scenario | 1.	Recognise any street sign in new footage<br>2.	Match recognised street sign to known sign database<br>3.	Get sign attributes (if available, f.ex. speed limit)
Extensions | 2a If sign cannot be matched get still image of recognised sign
Open Issues | -

Element | Content
------- | -------
Use Case ID | W7
Name/Summary | Driving direction recognition
Priority | high
Preconditions | Video stream is available
Postconditions | Driving direction correspondent to recognised lanes is known
Primary Actor | Video processor
Secondary Actor(s) | -
Trigger | New video footage
Main Scenario | 1.	Match current with previous video frames and extrapolate driving direction
Extensions | 1a If no previous footage is available (or too old), no calculation is possible
Open Issues | -

Element | Content
------- | -------
Use Case ID | W8
Name/Summary | Lane recognition
Priority | middle
Preconditions | Video stream is available
Postconditions | Lanes in front of the device have been recognised
Primary Actor | Video processor
Secondary Actor(s) | -
Trigger | New video footage
Main Scenario | 1.	Recognise lanes in current frame<br>2.	Match recognised lanes to previously recognised lanes
Extensions | 2a If no previous footage is available (or too old), no matching is possible
Open Issues | -

Element | Content
------- | -------
Use Case ID | W9
Name/Summary | Show video footage
Priority | middle
Preconditions | -
Postconditions | Selected video footage has been shown
Primary Actor | User
Secondary Actor(s) | -
Trigger | New video footage
Main Scenario | 1.	User starts Application<br>2.	User starts Recording<br>3.	System shows record mode and incident button<br>4.	(opt.) User marks incident<br>5.	(opt.) Video footage is made anonymous permanently saved and success is displayed<br>6.	User searches for saved video<br>7.	Video footage is shown<br>8.	User stops Recording or quits Application
Extensions | 3a No Video stream available or no disk space<br>3b System displays record failure
Open Issues | -

Element | Content
------- | -------
Use Case ID | W10
Name/Summary | Driving speed recognition
Priority | middle
Preconditions | Video stream data is available
Postconditions | Driving speed of device (vehicle) is known
Primary Actor | -
Secondary Actor(s) | -
Trigger | New video footage
Main Scenario | 1.	Match current with previous video frames and extrapolate driving direction
Extensions | 1a If no previous footage is available (or too old), no calculation is possible
Open Issues | -

Element | Content
------- | -------
Use Case ID | W11
Name/Summary | Vehicle brake light recognition
Priority | middle
Preconditions | Video stream is available
Postconditions | Brake lights of any recognised vehicles have been detected
Primary Actor | Video processor
Secondary Actor(s) | -
Trigger | New video footage
Main Scenario | 1.	Recognise vehicles from video footage<br>2.	(opt.) Match recognised vehicles with already known vehicles<br>3.	Recognise illuminated brake lights on each vehicle
Extensions | -
Open Issues | -