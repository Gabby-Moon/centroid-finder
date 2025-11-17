1. Find a base image that contains Node.js, Maven and JPK
- If can't find with all, figure out what one is easiest to install in the RUN
- Maybe use a slim version of an image
- Set up variables and volumes for the container
2. Test to make sure the server runs
- Use postman for API requests using the server container
- Make sure the video, image, and .csv are able to be changed dynamically

Notes:
- Eclipse/maven has the maven build
- Can use multi-stage build to ensure jar file exists/is newest version and then a second build for node
>> reduces image size

- eclipse 21 JRE Jammy only has "JRE" files (no JDK) so it is smaller than the eclipse build & runs better with javacv than alpine
- Node can be installed separately with apt
- set up working directories for at least thumbnail since it is not mounted but could be?
- results and vidoes also get temp directories in case a volume is not mounted for some reason??

