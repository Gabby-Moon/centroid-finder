Refactoring code (required)
- Getting rid of unused files and commented code
- Separating video processing

Adding test (required)
- Adding test for Thumbnail processing
- Tests for edge casing

Improving error handling (required)
- Thumbnail Error handling
- Validating inputs before passing them in

Writing documentation (required)
- Adding documentation for server and program
- Making a proper README file for the server and file

Improving performance (optional)
- find a way to minify node.js?
- Use parallel processing where possible (e.g., process frames concurrently).

Hardening security (optional)
- Input sanitization: Sanitize all user inputs, especially file names and query parameters, to prevent injection attacks.
- update everything to Java 25 compatibility

Bug fixes (optional)
- clarify if the API route is every route for the Salmander API or only the api/videos route
- Thumbnail hanging without video

Other (optional)
- quiet ffmpeg avutil.AV_LOG_QUIET
    (https://bytedeco.org/javacpp-presets/ffmpeg/apidocs/org/bytedeco/ffmpeg/presets/avutil.html)