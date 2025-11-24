/**
 * Controller utilities for video processing and thumbnail generation.
 *
 * This module coordinates between the repo layer and an external Java
 * processor (videoprocessor.jar). It exposes functions to request
 * thumbnails, start background video processing jobs, check job status,
 * and invoke the Java thumbnail generator directly.
 */

import {getJob, setJob, deleteJob, fetchThumbnailNode} from '../repos/repo.js';
import {spawn} from 'child_process';
import { fileURLToPath } from 'url';
import path from 'path';


// Path to the Java processor JAR. Can be overridden via the JAR_PATH env var.
const jarPath = process.env.JAR_PATH || path.resolve(__dirname, '../../../processor/target/videoprocessor.jar');

/**
 * Fetch or generate a thumbnail for a video using the Node-side repo helper.
 *
 * @param {string} filename - Video file name (e.g. "video.mp4").
 * @param {string} videoDir - Directory where videos are stored.
 * @param {string} thumbnailDir - Directory where thumbnails should be stored.
 * @returns {Promise<string>} Resolves with the thumbnail path when successful.
 * @throws {Error} If thumbnail generation or fetch fails.
 *
 * @example
 * const thumbPath = await getThumbnail('movie.mp4', '/videos', '/thumbnails');
 */
export async function getThumbnail(filename, videoDir, thumbnailDir) {
    try {
        const thumbnailPath = await fetchThumbnailNode(filename, videoDir, thumbnailDir);
        return thumbnailPath;
    } catch (err) {
        console.error('Unable to fetch thumbnail: ', err);
        throw new Error("Error generating thumbnail");
    }
}

/**
 * Start processing a video file by spawning the Java processor.
 *
 * This function sets the job status to "processing", spawns a Java process
 * to run the external jar, and updates the job status to "done" or "error"
 * when the process exits. Side-effects: calls setJob(...) to update job state.
 *
 * @param {string} filename - The video filename to process.
 * @param {number|string} color - Color parameter passed to the processor.
 * @param {number|string} threshold - Threshold parameter passed to the processor.
 * @param {string} jobId - UUID identifier for the processing job (used with setJob/getJob).
 * @param {string} videoDir - Directory containing the source video file.
 * @param {string} resultsDir - Directory to write result CSV files to.
 * @returns {void}
 *
 * @example
 * startVideoProcessing('clip.mp4', 255, 50, 'job-123', '/videos', '/results');
 */
export function startVideoProcessing(filename, color, threshold, jobId, videoDir, resultsDir) {
    const videoPath = path.join(videoDir, filename);
    const outputPath = path.resolve(resultsDir, filename.replace(/\.[^/.]+$/, '.csv'));

    setJob(jobId, { status: 'processing' });

    const javaArgs = [
        '-jar',
        jarPath,
        videoPath,
        outputPath,
        color.toString(),
        threshold.toString()
    ];

    const javaProcess = spawn('java', javaArgs);

    javaProcess.stdout.on('data', (data) => console.log(`Java stdout: ${data}`));
    javaProcess.stderr.on('data', (data) => console.error(`Java stderr: ${data}`));

    javaProcess.on('close', (code) => {
        if (code === 0) {
            const resultUrl = `/results/${filename.replace(/\.[^/.]+$/, '.csv')}`;
            setJob(jobId, { status: 'done', result: resultUrl });
            console.log('Job completed! Result file URL:', resultUrl);
        } else {
            const errMsg = `Error processing video: Exited with code ${code}`;
            setJob(jobId, { status: 'error', error: errMsg });
            console.error(errMsg);
        }
    });

    javaProcess.on('error', (err) => {
        const errMsg = `Error processing video: ${err.message}`;
        setJob(jobId, { status: 'error', error: errMsg });
    });
}

/**
 * Retrieve job state by jobId.
 *
 * @param {string} jobId - Identifier of the job to check.
 * @returns {Promise<any>} The job object as returned by getJob (shape depends on repo implementation).
 *
 * @example
 * const job = await checkJob('job-123');
 */
export function checkJob(jobId) {
    return getJob(jobId);
}

/**
 * Run the Java thumbnail generator directly by spawning the Java process.
 *
 * Resolves when the Java process exits with code 0, rejects on non-zero exit
 * or if the process fails to start.
 *
 * @param {string} videoPath - Full path to the source video file.
 * @param {string} thumbnailPath - Full path where the thumbnail should be written.
 * @returns {Promise<void>} Resolves when thumbnail generation completes successfully.
 *
 * @example
 * await fetchThumbnailJava('/videos/movie.mp4', '/thumbnails/movie.png');
 */
export async function fetchThumbnailJava(videoPath, thumbnailPath) {
 return new Promise((resolve, reject) => {
        const java = spawn('java', ['-jar', jarPath, videoPath, thumbnailPath]);

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
                reject(new Error(`Java process exited with code ${code}.`));
            }
        });

        java.on('error', (err) => {
            reject(new Error(`Failed to start Java process: ${err.message}`));
        });
    });
}