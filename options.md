JavaCV  
Pros:  
- It's faster because of ffmpeg to pull video data  
- Works on all systems

Cons:  
- It's a wrapper for ffmpeg; it would need to be included  
- Little official documentation, all from tutorials or forums
- Complex setup

Video4j/Jave4j  
Pros:
- Easy to talk OpenCV, for video processing
- Extracts frames as buffered images
- Uses Maven

Cons:
- Linux only
- Sparse documentation

JCodec  
Pros:
- Pure java without dependencies
- Cross platform, works on all systems
- Can extract frames

Cons:
- Only works on H.264 MP4 files
- Bottle necks, slower to process