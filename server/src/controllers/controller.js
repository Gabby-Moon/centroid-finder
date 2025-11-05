import {fetchThumbnail} from '../repos/repo.js';
import fs from 'fs/promises';
import path from 'path';

export async function getThumbnail(filename, videoDir, thumbnailDir) {
    try {
        const thumbnailPath = await fetchThumbnail(filename, videoDir, thumbnailDir);
        return thumbnailPath;
    } catch (err) {
        console.error('Unable to fetch thumbnail: ', err);
        throw new Error("Error generating thumbnail");
    }
}