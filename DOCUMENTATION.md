# Centroid Finder
Used for finding the center of an object in a video and giving a ```.csv``` for it's movemnt. Uses a sever API to get information from the java end.

## Requires
- Maven
- Docker
- JDK for Maven
- [The DockerImage](https://github.com/users/AAshGray/packages/container/package/salamander)

## Overview
Running the ```ghcr.io/aashgray/salamander:latest``` to start the docker container will run the API. The API calls the ```.jar``` file for processing of the video and making the ```.csv```.  
The project is being used as the back-end for a Salamander tracker.

## Processor
The processor contains the java code and a pom.xml for Maven to be able to run.  
Contains tests for each of the ```.java``` files.  
The program looks at the length of the arguments past in to determine what part of the program it runs. Either going to the video processing or the thumbnail processing.

### Video Processing
The video processing uses JavaCV for processing the frame data.  
It takes in the video path, output path for csv, hex color, and threshold in the commandline and throws different exceptions for each error. It passes each frame into processing to get the center object in the video, it give a frame each second from the video.

### Thumbnail Processing
The thumbnail processing gets a video path then an output path for the image.  
It gets a frame from the video and makes a ```.jpg``` from it for a thumbnail to be used for the API.  
It is much smaller due to the small task compared to the video processor.

## Server