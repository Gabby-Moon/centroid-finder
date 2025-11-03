import {Router} from 'express';
import fs from 'fs';
import path from 'path';

const router = Router();

//get
router.get('/videos', async (req, res) => {
    try {
        const videoDir = path.resolve(process.env.VIDEO_DIR);
        const files = await fs.promises.readdir(videoDir);
        res.status(200).json(files);
    } catch (err) {
        console.error(err);
        res.status(500).json({error: 'Error reading video directory'});
    }
})


//post

//put

//patch

//delete

export default router;