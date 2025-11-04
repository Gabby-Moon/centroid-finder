 // TODO: Fix calling ThumbnailGenerator in file correctly
 /**
 *  Java stderr: Error: Could not find or load main class io.github.GabbyMoon.centroidfinder.ThumbnailsProcessingApp
 *   Caused by: java.lang.ClassNotFoundException: io.github.GabbyMoon.centroidfinder.ThumbnailsProcessingApp
 *    ❌ Thumbnail generation failed: Error: Java process exited with code 1
 *       at ChildProcess.<anonymous> (file:///C:/Users/Blaze/SDEV334/server%20project/centroid-finder/server/src/repos/repo.js:46:24)
 *       at ChildProcess.emit (node:events:524:28)
 *       at maybeClose (node:internal/child_process:1101:16)
 *       at ChildProcess._handle.onexit (node:internal/child_process:304:5)
 */

import path from 'path';
import { fileURLToPath } from 'url';
import { generateThumbnail } from '../src/repos/repo.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Paths relative to centroid-finder/processor
const videoPath = path.resolve(__dirname, '../src/static/videos/moving_cyan_video.mp4');
const thumbnailPath = path.resolve(__dirname, '../src/static/thumbnails/moving_cyan_video.jpg');

generateThumbnail(videoPath, thumbnailPath)
  .then(() => {
    console.log('✅ Thumbnail generated successfully.');
  })
  .catch((err) => {
    console.error('❌ Thumbnail generation failed:', err);
  });
