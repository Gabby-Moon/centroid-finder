# Planning
1. Main (ex: SalamanderTrackingApp.java)
- INPUT: the command line arguments with the file location, the color, and distance
- Orchestrates the entire process
- Initializes components and triggers execution
- OUTPUT: N/A (Calls the output writer at the end)
2. Producer (ex: VideoFrameExtractor.java)
- INPUT: the command line arguments with the file location
- Loads frames into a list from the video file
- 1 frame every 60 seconds
- if the frame "fails" to read for some reason, a "null" buffered image should be added to preserve timestamp placeholder
- OUTPUT: LinkedList of ImageFrame (ImageFrame: int seconds since video start + Buffered Image image)
> ***ImageFrame***
> BufferedImage image
> double timestampInSeconds (Num seconds since beginning of video)
3. Consumer (ex: GroupDataExtractor.java ? (I don't know what to name these things))
- INPUT: The Linked List of ImageFrames, color, threshold (from command line args)
- Iterates over each frame
- Calls the frame processor (BinaryGroupFinder) for each Buffered Image
- OUTPUT: A List of FrameData it found in the images
> ***FrameGroupData***
> int frameIndex - timeStampInSeconds
> Group largestGroup - the largest group data including Centroid and pixel size. This would be from Group.java
> Consider other data which we might associate with the frame?
4. BinarizingImageGroupFinder (Called from the consumer)
- INPUT: 
-- Building instance of EuclidianColorDistance (requires color, threshold)
-- Instance of DistanceImageBinarizer (No input)
- Find the connected groups for a single frame
Output: Sorted List<Groups> to return .get(0) (largest group) to store in the FrameData
5. FrameDataWriter (Frame Group Processor?)
INPUT: List of FrameGroupData, inputCSV name/path from main args
- Creates and outputs the CSV of each frame in the specified format
- reads though the FrameGroupData list to output 
> Output format will be : Timestamp, Group .getX, Group .getY
> null buffered images (failed framegrabs) or null group data (no salamander) should be written as "timestamp, -1, -1" per instructions
OUTPUT: CSV written to hard disc in specified location/format


# Wave 4: Validating
The jar executable was created and ran on a generated video file with a cyan block moving diagonally across a black background. The x and y coordinates of the group Coordinate moved as the timestamps changed, showing that it was tracking movement more or less as expected. 

For test color, we sampled from the original project and expanded the threshold until it grabbed most what we perceived as the salamander image, which also showed movement across the frame. 

With the way the frames are sampled, there is a pattern with frames where Group data returned from group finder does not find a large enough group for groups to have any data after enough frames are processed. There does not seem to be a correlation to certain image frames, as this happens even with a long enough generated video which should not have color issues.

For color and threshold it could be improved with image processing or set-up to make sure the salamander is always a distinct color from the background. 