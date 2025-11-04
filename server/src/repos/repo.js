import fs from 'fs/promises';
import path from 'path';
import {spawn} from 'child_process';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);



export async function fetchThumbnail(filename, videoDir, thumbnailDir) {
    // Remove any extension when we're looking for thumbnail since it's unclear if we're selecting video or image
    const baseName = path.parse(filename).name; // "moving_cyan_video" from "moving_cyan_video.mp4" or "moving_cyan_video.jpg"
    
    const videoPath = path.resolve(videoDir, filename);
    const thumbnailPath = path.resolve(thumbnailDir, baseName + '.jpg');

    try {
        await fs.stat(thumbnailPath); // exists
    } catch {
        await generateThumbnail(videoPath, thumbnailPath);
    }

    return thumbnailPath;
}

export async function generateThumbnail(videoPath, thumbnailPath) {
 return new Promise((resolve, reject) => {
        const jarPath = path.resolve(__dirname, '../processor/target/videoprocessor.jar');
        const mainClass = 'io.github.GabbyMoon.centroidfinder.ThumbnailsProcessingApp';

        const java = spawn('java', ['-cp', jarPath, mainClass, videoPath, thumbnailPath]);

        java.stdout.on('data', (data) => {
            console.log(`Java stdout: ${data}`);
        });

        java.stderr.on('data', (data) => {
            console.error(`Java stderr: ${data}`);
        });

        java.on('close', (code) => {
            if (code === 0) {
                resolve();
            } else {
                reject(new Error(`Java process exited with code ${code}`));
            }
        });

        java.on('error', (err) => {
            reject(new Error(`Failed to start Java process: ${err.message}`));
        });
    });

}

