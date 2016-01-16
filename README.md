[![Build Status](https://travis-ci.com/idstein/ADAM.svg?token=QTWdMaqXyCvwHbkPxMwB&branch=master)](https://travis-ci.com/idstein/ADAM)

## Synopsis

The Advanced Driving Assistance on a Mobile is a solution to assist while driving with a commodity mobile device.

This system operates within city/overland traffic boundaries.


At the top of the file there should be a short introduction and/ or overview that explains **what** the project is. This description should match descriptions added for package managers (Gemspec, package.json, etc.)

## Requirements

### Functionality

Driving

1. Show a warning if the distance is below the average break distance (including human reaction and actual breaking time) (may require further calibration or knowledge of break effectiveness)
2. Inform with a warning if a lane departure is expected to happen within the next x(e.g. 5 ) seconds
3. Show which lane is about to be crossed

Traffic and flow information

4. Give an indication if the cars in front are about to break by detecting the brakelight
5. Detect and show speed signs (including city signs, end of speed limits) and show the detected speed limit
6. Detect priority in traffic by showing the direction which has priority  

Dashcam

7. Record video footage in case of a major incident
8. Display last detected information such as speed, cars in front, detected signs and lanes

### Non-functional requirements
1. The solution should run on a commodity Android device with at least 4 CPU cores and a GPU with OpenGL ES 2.x support, support OpenCV runtime, at least 512 MB RAM, back-facing camera with at least FullHD resolution, sustainable charging (run on power-source with battery charging)
2. The solution should run in almost real-time (no longer than the average humand reaction time) to detect objects/situations
3. The system shall not record any data and must withdraw video footage after processing
4. No car signs should be disclosed and any detected signs should be anonymoused while showing the captured video
5. The system shall reach a detection ratio of at least 50% for lanes, 75% for traffic signs, 50% for brakelight, 75% for lane departure
6. The solution requires a steady power connection to bridge longer driving sessions




[Use case diagramm](https://www.lucidchart.com/invitations/accept/7bd9d298-3531-4e64-993f-ee86e93b487c)

## Code Example

Show what the library does as concisely as possible, developers should be able to figure out **how** your project solves their problem by looking at the code example. Make sure the API you are showing off is obvious, and that your code is short and concise.

## Motivation

A short description of the motivation behind the creation and maintenance of the project. This should explain **why** the project exists.

## Installation

Provide code examples and explanations of how to get the project.

## API Reference

Depending on the size of the project, if it is small and simple enough the reference docs can be added to the README. For medium size to larger projects it is important to at least provide a link to where the API reference docs live.

## Tests

Describe and show how to run the tests with code examples.

## Contributors

Let people know how they can dive into the project, include important links to things like issue trackers, irc, twitter accounts if applicable.

## License

A short snippet describing the license (MIT, Apache, etc.)
