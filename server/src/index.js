/**
 * Main server entry point for the video processing application.
 *
 * This module sets up the Express server, defines API routes, and starts listening
 * on the specified port. It serves static files for videos, thumbnails, and results.
 *
 * Environment variables:
 * - PORT: Port number to listen on (default: 3000)
 * - VIDEO_DIRECTORY: Directory where video files are stored
 * - THUMBNAIL_DIRECTORY: Directory where thumbnail images are stored
 * - RESULTS_DIRECTORY: Directory where processing results are stored
 * - JAR_PATH: Path to the videoprocessor.jar file
 * - Requires Java to be installed and accessible in the runtime environment.
 *
 * API Endpoints:
 * - GET /api/videos: List available video files
 * - GET /thumbnail/:filename: Fetch or generate a thumbnail for a video
 * - GET /process/:jobId/status: Get the status of a video processing job
 * - POST /process/:filename: Start processing a video file with parameters
 * - Serves static files from VIDEO_DIRECTORY, THUMBNAIL_DIRECTORY, and RESULTS_DIRECTORY
 *
 * Example usage:
 * - Start the server: `node src/index.js`
 * - Fetch video list: `GET http://localhost:3000/api/videos`
 * - Get thumbnail: `GET http://localhost:3000/thumbnail/video.mp4`
 * - Start processing: `POST http://localhost:3000/process/video.mp4?targetColor=255&threshold=50`
 * - Check job status: `GET http://localhost:3000/process/job-123/status`
 */
import 'dotenv/config'
import express from "express";
import path from 'path';
import routes from './routes/router.js';
import cors from 'cors';

// set up express
const app = express();
const PORT = process.env.PORT || 3000;

// set up to process requests
app.use(express.json());
app.use(cors());
app.use('/', routes);
app.use('/routes', express.static('/routes'))

// static file locations setup
app.use('/videos', express.static(path.resolve(process.env.VIDEO_DIRECTORY)));
app.use('/thumbnails', express.static(path.resolve(process.env.THUMBNAIL_DIRECTORY)));
app.use('/results', express.static(path.resolve(process.env.RESULTS_DIRECTORY)));

// root test location
app.get('/', (req, res) => {
  res.status(200).send('Server is running!');
});

// start server
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});