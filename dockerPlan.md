1. Find a base image that contains Node.js, Maven and JPK
- If can't find with all, figure out what one is easiest to install in the RUN
- Maybe use a slim version of an image
- Set up variables and volumes for the container
2. Test to make sure the server runs
- Use postman for API requests using the server container
- Make sure the video, image, and .csv are able to be changed dynamically