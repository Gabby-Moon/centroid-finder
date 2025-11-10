import fs from 'fs/promises';
import path from 'path';
import { fetchThumbnailJava } from '../controllers/controller.js';

const jobsMap = new Map();

export async function fetchThumbnailNode(filename, videoDir, thumbnailDir) {
    // Remove any extension when we're looking for thumbnail since it's unclear if we're selecting video or image
    const baseName = path.parse(filename).name; // "moving_cyan_video" from "moving_cyan_video.mp4" or "moving_cyan_video.jpg"
    
    const videoPath = path.resolve(videoDir, filename);
    const thumbnailPath = path.resolve(thumbnailDir, baseName + '.jpg');

    try {
        await fs.access(thumbnailPath); // exists
    } catch {
        try {
            await waitForFile(videoPath); // safely waits only for this job
            await fetchThumbnailJava(videoPath, thumbnailPath);
        } catch (err) {
            console.error("Unable to generate thumbnail", err);
            throw new Error("Unable to generate thumbnail");
        }
    }

    return thumbnailPath;
}

export function setJob(jobId, jobObject) {
    jobsMap.set(jobId, jobObject);
}

export function getJob(jobId) {
    return jobsMap.get(jobId);
}

export function deleteJob(jobId) {
    jobsMap.delete(jobId);
}

async function waitForFile(filePath, interval = 500, timeout = 10000) {
    const start = Date.now();

    return new Promise((resolve, reject) => {
        const check = async () => {
            try {
                await fs.access(filePath);
                resolve(); // File exists
            } catch {
                if (Date.now() - start > timeout) {
                    reject(new Error(`Timeout waiting for file: ${filePath}`));
                } else {
                    setTimeout(check, interval); // schedule next check
                }
            }
        };
        check();
    });
}