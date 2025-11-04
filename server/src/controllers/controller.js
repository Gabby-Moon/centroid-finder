import {fetchThumbnail} from '../repos/repo.js';
import fs from 'fs/promises';
import path from 'path';

export async function getThumbnail(req, res, { videoDir, thumbnailDir }) {
    const { filename } = req.params;

    try {

        const thumbnailPath = await fetchThumbnail(filename, videoDir, thumbnailDir);
        return res.status(200).sendFile(thumbnailPath, { headers: { 'Content-Type': 'image/jpeg' } });

    } catch (err) {
        console.error('Error generating thumbnail:', err);
        return res.status(500).json({ error: "Error generating thumbnail" });
    }
}