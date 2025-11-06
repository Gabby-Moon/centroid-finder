import fs from 'fs/promises';
import path from 'path';




const jobsMap = new Map();

export async function fetchThumbnail(filename, videoDir, thumbnailDir) {
    // Remove any extension when we're looking for thumbnail since it's unclear if we're selecting video or image
    const baseName = path.parse(filename).name; // "moving_cyan_video" from "moving_cyan_video.mp4" or "moving_cyan_video.jpg"
    
    const videoPath = path.resolve(videoDir, filename);
    const thumbnailPath = path.resolve(thumbnailDir, baseName + '.jpg');

    try {
        await fs.access(thumbnailPath); // exists
    } catch {
        await generateThumbnail(videoPath, thumbnailPath);
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
