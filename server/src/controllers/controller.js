import {getJob, setJob, deleteJob} from '../repos/repo.js';
import {spawn} from 'child_process';
import { fileURLToPath } from 'url';
import fs from 'fs/promises';
import path from 'path';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const jarPath = path.resolve(__dirname, '../../../processor/target/videoprocessor.jar');

export async function getThumbnail(filename, videoDir, thumbnailDir) {
    try {
        const thumbnailPath = await fetchThumbnail(filename, videoDir, thumbnailDir);
        return thumbnailPath;
    } catch (err) {
        console.error('Unable to fetch thumbnail: ', err);
        throw new Error("Error generating thumbnail");
    }
}

export function startVideoProcessing(filename, color, threshold, jobId, videoDir, resultsDir) {
    const videoPath = path.join(videoDir, filename);
    const outputPath = path.join(resultsDir, filename.replace(/\.[^/.]+$/, '.csv'));
    const jarPath = path.re

    setJob(jobId, {status: 'processing'});

    const javaArgs = [
        '-jar',
        jarPath,       // path to the .jar file
        videoPath,     // args[0] → inputPath
        outputPath,    // args[1] → outputPath
        color,         // args[2] → targetColorInput
        threshold      // args[3] → thresholdInput
    ];

    const javaProcess = spawn('java', javaArgs);

    javaProcess.on('close', (code) => {
        if (code == 0) {
            setJob(jobId, {status: 'done', result: outputPath})
        } else {
            const errMsg = `Error processing video: Exited with code ${code}` 
            setJob(jobId, { status: 'error', error: errMsg });
        }
    });

    javaProcess.on('error', (err) => {
        const errMsg = `Error processing video: ${err.message}`
        setJob(jobId, { status: 'error', error: errMsg });
    });
}

export function checkJob(jobId) {
    return getJob(jobId); // returns {status, resultPath?, error?}
}

export async function fetchThumbnail(videoPath, thumbnailPath) {
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