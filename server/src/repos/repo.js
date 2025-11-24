/**
 * Repository module for managing video processing jobs and thumbnail generation.
 *
 * Provides functions to:
 * - Fetch existing thumbnails or generate new ones for video files.
 * - Track job states (e.g., pending, processing, completed, failed) in an in-memory map.
 *
 * Thumbnail generation is performed via an external Java process (videoprocessor.jar).
 * Requires Java to be installed and accessible in the runtime environment.
 */
import fs from 'fs/promises';
import path from 'path';
import { fetchThumbnailJava } from '../controllers/controller.js';

/**
 * In-memory map to store job states.
 * Key: jobId (string) - UUID identifier for the job.
 * Value: jobObject (object) - can contain any relevant information about the job's state.
 */
const jobsMap = new Map();


/**
 * Retrieves exisiting thumbnail image or begins generation process for  thumbnail image for a video file.
 *
 *
 * @param {string} jobId - Unique identifier for the job.
 * @param {string} filename - Name of the video file to process.
 * @param {string} videoDir - Directory containing the video files.
 * @param {string} thumbnailDir - Directory to store generated thumbnails.
 * @returns {Promise<string>} Resolves with the path to the generated thumbnail.
 */

export async function fetchThumbnailNode(filename, videoDir, thumbnailDir) {
    const baseName = path.parse(filename).name;
    
    const videoPath = path.resolve(videoDir, filename);
    const thumbnailPath = path.resolve(thumbnailDir, baseName + '.jpg');

    try {
        await fs.access(thumbnailPath);
    } catch {
        try {
            // Wait for the video file to be accessible before generating the thumbnail
            await waitForFile(videoPath);
            await fetchThumbnailJava(videoPath, thumbnailPath);

        } catch (err) {
            console.error(`Error generating thumbnail for ${filename}:`, err);
            console.error(`Video path: ${videoPath}`);
            console.error(`Thumbnail path: ${thumbnailPath}`);
            console.error(`Using JAR: ${process.env.JAR_PATH || 'default path'}`);
            throw new Error("Unable to generate thumbnail");
        }
    }

    return thumbnailPath;
}

/**
 * Adds or updates a job state for a given jobId.
 * 
 * @param {string} jobId - Unique identifier for the job.
 * @param {object} jobObject - Object containing job state (e.g. { status: 'processing' }).
 */
export function setJob(jobId, jobObject) {
    jobsMap.set(jobId, jobObject);
}

/**
 * Retrieves the job state for a given jobId.
 * 
 * @param {string} jobId - Unique identifier for the job.
 * @returns {object} The job state object associated with the jobId.
 */
export function getJob(jobId) {
    return jobsMap.get(jobId);
}

/** 
 * Deletes a job state for a given jobId.
 * 
 * @param {string} jobId - Unique identifier for the job.
 */
export function deleteJob(jobId) {
    jobsMap.delete(jobId);
}

/** 
 * Utility function to wait for a file to be readable before proceeding.
 * Only necessary on a Windows build, where other processes may interfere with file access.
 * 
 * @param {string} filePath - Path to the file to wait for.
 * @param {number} interval - Time in milliseconds between checks (default: 500ms).
 * @param {number} timeout - Maximum time in milliseconds to wait before rejecting (default: 10000ms).
 * @returns {Promise<void>} Resolves when the file exists, or rejects on timeout.
*/
async function waitForFile(filePath, interval = 500, timeout = 5000) {
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