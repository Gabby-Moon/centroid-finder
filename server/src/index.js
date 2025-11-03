import express from "express";
import dotenv from 'dotenv';
import path from 'path';
// import processRoutes from './routes/processRoutes.js';

// set up dotenv
dotenv.config();

// set up express
const app = express();
const PORT = process.env.PORT || 3000;

// set up to process requests
app.use(express.json());

// static file locations setup
app.use('/videos', express.static(path.resolve(process.env.VIDEO_DIR)));
app.use('/thumbnails', express.static(path.resolve(process.env.THUMBNAIL_DIR)));
app.use('/results', express.static(path.resolve(process.env.RESULT_DIR)));

// in-memory job tracker
// jobId -> { videoName, status, process }
const jobs = new Map();

// root test
app.get('/', (req, res) => {
  res.send('Server is running!');
});

// Test endpoint to list videos and thumbnails
import fs from 'fs';
app.get('/test-files', (req, res) => {
  try {
    const videoFiles = fs.readdirSync(path.resolve(process.env.VIDEO_DIR));
    const thumbnailFiles = fs.readdirSync(path.resolve(process.env.THUMBNAIL_DIR));

    res.json({
      videos: videoFiles,
      thumbnails: thumbnailFiles,
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Failed to read directories' });
  }
});

// start server
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});