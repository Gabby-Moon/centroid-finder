ImageSummaryApp  
Takes in an argument in the commandline and checks the arguments to see if they are correct. Gives error messages for each.  
Changes the hex code color to in integer to read.  
ColorDistanceFinder & ImageBinarizer: 
Makes the png file into a matrix given the variables. 
Then makes the png into a binary array and image.  
Then it writes the new png as 'binarized.png' and gives messages for success and failure.  
ImageGroupFinder & DfsBinaryGroupFinder: 
Then it used DFS to find groups of white pixels. 
And it writes the groups into a csv with size and pixel location.

**2025-10-22 Maven notes**
1. mvn clean <- cleans up old files
2. mvn compile <- compiles .java to .class files
3. mvn test <- runs the junit tests
4. mvn exec:java -Dexec.args="sampleInput/squares.jpg sampleOutput/binarized.png FFA200 164" <- runs the ImageSummaryApp with the arguments
4. mvn exec:java -Dexec.args="sampleInput/input.mp4 sampleOutput/output.csv FFA200 164" <- runs the VideoProcessorApp with the arguments