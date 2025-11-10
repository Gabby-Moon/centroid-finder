import {getJob, setJob, deleteJob, fetchThumbnailNode} from '../repos/repo.js';
import {spawn} from 'child_process';
import { fileURLToPath } from 'url';
import path from 'path';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const jarPath = path.resolve(__dirname, '../../../processor/target/videoprocessor.jar');

export async function getThumbnail(filename, videoDir, thumbnailDir) {
    try {
        const thumbnailPath = await fetchThumbnailNode(filename, videoDir, thumbnailDir);
        return thumbnailPath;
    } catch (err) {
        console.error('Unable to fetch thumbnail: ', err);
        throw new Error("Error generating thumbnail");
    }
}

export function startVideoProcessing(filename, color, threshold, jobId, videoDir, resultsDir) {
    const videoPath = path.join(videoDir, filename);
    const outputPath = path.resolve(resultsDir, filename.replace(/\.[^/.]+$/, '.csv'));

    setJob(jobId, { status: 'processing' });

    const javaArgs = [
        '-jar',
        jarPath,        // fixed: use resolved jarPath
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

export function checkJob(jobId) {
    return getJob(jobId); // returns {status, resultPath?, error?}
}

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