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

## Build
The container is able to build the project through it's first base image then makes another base image to run the server, running the installs. Maven isn't required if you are just running the API but needed for modifying and running tests outside of the container.

## Run
To run the container you use ```docker run -p 3000:3000 -v $LOCAL_DRIVE/videos:/videos -v $LOCAL_DRIVE/results:/results -v $LOCAL_DRIVE/thumbnails:/thumbnails ghcr.io/aashgray/salamander:latest```  
The local_drives are folders from your own pc containing the videos and thumbnails folders for the program to read and write to. It runs on port ```3000```.  
**Windows: Use the ```\``` insted for the paths on your own pc but keep ```:/results``` & ```:/thumbnails``` & ```ghcr.io/aashgray/salamander:latest``` the same**

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
The server is an Express API, the folder contains tests and the server code. By default it uses ```3000``` for it's port.

### paths
```/api/videos```: Returns a ```.json``` of all the available videos.
- gives a ```500``` if there is an error reading the directory  

```/thumbnail/:filename```: Returns the thumbnail file of the given video name.
- gives a ```500``` if it can't get the thumbnail

```/process/:jobId/status```: returns the status of the job, can be done with a result, processing, or error with the error returned.
- gives a ```404``` if there is no job with the given ```jobId```
- gives a ```500``` if there was an error finding the job with the given ```jobId```

```/process/:filename```: a post to start a job with the given file name. Needs to have color and threshold as a query parameter within the url.
- gives a ```400``` if color or threshold is missing in the url
- returns a ```202``` when it goes into processing and give the jobId
- gives a ```500``` if there is an error with starting the job

The job itself is async to avoid holding the user up with the long task, and changes the object with it's ```jobId``` to it's status.