/**
 * Express router for handling video processing and thumbnail generation routes.
 *
 * This module defines the API endpoints for:
 * - Fetching video file names
 * - Generating and retrieving thumbnails
 * - Starting video processing jobs
 */
import {Router} from 'express';
import fs from 'fs';
import path from 'path';
import {getThumbnail, startVideoProcessing, checkJob} from '../controllers/controller.js';
import crypto from "crypto";
import { fileURLToPath } from 'url';

const router = Router();
const videoDir = process.env.VIDEO_DIRECTORY;
const thumbnailDir = process.env.THUMBNAIL_DIRECTORY;
const resultsDir = process.env.RESULTS_DIRECTORY;
const jarPath = process.env.JAR_PATH;

/**
 * GET /api/videos
 * 
 * Fetches a list of video file names from the video directory.
 */
router.get('/api/videos', async (req, res) => {
    try {
        const files = await fs.promises.readdir(videoDir);
        res.status(200).json(files);
    } catch (err) {
        console.error(err);
        res.status(500).json({error: 'Error reading video directory'});
    }
})

/**
 * GET /thumbnail/:filename
 * 
 * Retrieves or generates a thumbnail image for a given video file.
 */
router.get('/thumbnail/:filename', async (req, res) => {
    const {filename} = req.params;
    
    try {
        const thumbnailPath = await getThumbnail(filename, videoDir, thumbnailDir);
        return res.status(200).sendFile(thumbnailPath, { headers: { 'Content-Type': 'image/jpeg' } });
    } catch (err) {
        return res.status(500).json({error: "Error generating thumbnail"})
    }
});

/**
 * GET /process/:jobId/status
 * 
 * Retrieves the status of a video processing job by its jobId.
 */
router.get('/process/:jobId/status', (req, res) => {
    try {
        const job = checkJob(req.params.jobId);
        if (!job) {
            return res.status(404).json({error: 'Job ID not found'});
        }

        switch (job.status) {
            case 'processing':
                return res.status(200).json({ status: 'processing' });

            case 'done':
                return res.status(200).json({ status: 'done', result: job.result });

            case 'error':
                return res.status(200).json({ status: 'error', error: job.error || 'Unknown error' });

            default:
                return res.status(500).json({ error: 'Error fetching job status' });
        }

    } catch (err) {
        res.status(500).json({error: 'Error fetching job status'});
    }
});

//post
/** 
 * POST /process/:filename
 * 
 * Starts processing a video file with specified parameters.
 * Expects query parameters: targetColor and threshold.
 */
router.post('/process/:filename', (req, res) => {
    const color = req.query.targetColor;
    const threshold = req.query.threshold;
    const {filename} = req.params

    if(!color || !threshold) {
        return res.status(400).json({error: 'Missing targetColor or threshold query parameter.'});
    }

    try {
        const jobId = crypto.randomUUID();    
        startVideoProcessing(filename, color, threshold, jobId, videoDir, resultsDir);
        return res.status(202).json({ jobId });
    } catch(err) {
        console.log('Error: ' + err);
        return res.status(500).json({error: 'Error starting job'});
    }
})

//put

//patch

//delete

export default router;