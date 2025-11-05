import {Router} from 'express';
import fs from 'fs';
import path from 'path';
import {getThumbnail} from '../controllers/controller.js';
import crypto from "crypto";

const router = Router();

// server files
const videoDir = path.resolve(process.env.VIDEO_DIR);
const thumbnailDir = path.resolve(process.env.THUMBNAIL_DIR);
const results = path.resolve(process.env.RESULT_DIR);

// maven
const JAR_PATH = path.resolve(process.env.JAR_PATH || '../../processor/target/videoProcessor.jar');
const MAVEN_PROJECT_DIR = path.resolve(process.env.MAVEN_PROJECT_DIR || '../../processor');

//get
router.get('/videos', async (req, res) => {
    try {
        const files = await fs.promises.readdir(videoDir);
        res.status(200).json(files);
    } catch (err) {
        console.error(err);
        res.status(500).json({error: 'Error reading video directory'});
    }
})

// just going to use ffmpeg for this since it's part of the javacv anyway and is faster
router.get('/thumbnail/:filename', async (req, res) => {
    const videoDir = path.resolve(process.env.VIDEO_DIR);
    const thumbnailDir = path.resolve(process.env.THUMBNAIL_DIR);
    const {filename} = req.params;
    
    try {
        const thumbnailPath = await getThumbnail(filename, videoDir, thumbnailDir)
        return res.status(200).sendFile(thumbnailPath, { headers: { 'Content-Type': 'image/jpeg' } });
    } catch (err) {
        return res.status(500).json({error: "Error generating thumbnail"})
    }
});

router.get('/process/:jobId/status', (req, res) => {
    //need to differentiate the sends
    
    // res.status(200).json({status: 'processing'});
    // res.status(200).json({status: 'done', result: ""});
    // res.status(200).json({status: 'error', error: 'Error processing video: Unexpected ffmpeg error'});
    // res.status(404).json({error: 'Job ID not found'});
    res.status(500).json({error: 'Error fetching job status'});
});

//post
router.post('/process/:filename', (req, res) => {
    const color = req.query.targetColor;
    const threshold = req.query.threshold;
    if(!color || !threshold) {
        res.status(400).json({error: 'Missing targetColor or threshold query parameter.'});
    }

    //call jar on video

    try {
        const videoID = crypto.randomUUID();

        res.status(202).json({jobId: videoID});
    } catch(err) {
        console.log('Error: ' + err);
        res.status(500).json({error: 'Error starting job'});
    }
})

//put

//patch

//delete

export default router;