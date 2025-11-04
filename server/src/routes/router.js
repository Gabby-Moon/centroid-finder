import {Router} from 'express';
import fs from 'fs';
import path from 'path';
import {getThumbnail} from '../controllers/controller.js';

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
router.get('/thumbnail/:filename', (req, res) => {
    const videoDir = path.resolve(process.env.VIDEO_DIR);
    const thumbnailDir = path.resolve(process.env.THUMBNAIL_DIR);

    getThumbnail(req, res, { videoDir, thumbnailDir });
});

//post

//put

//patch

//delete

export default router;