import fs from 'fs/promises';
import path from 'path';
import { fetchThumbnailJava } from '../controllers/controller.js';

const jobsMap = new Map();

export async function fetchThumbnailNode(filename, videoDir, thumbnailDir) {
    const baseName = path.parse(filename).name;
    
    const videoPath = path.resolve(videoDir, filename);
    const thumbnailPath = path.resolve(thumbnailDir, baseName + '.jpg');

    try {
        await fs.access(thumbnailPath);
    } catch {
        try {
            await waitForFile(videoPath);
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
                resolve();
            } catch {
                if (Date.now() - start > timeout) {
                    reject(new Error(`Timeout waiting for file: ${filePath}`));
                } else {
                    setTimeout(check, interval);
                }
            }
        };
        check();
    });
}