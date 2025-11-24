import 'dotenv/config'
import express from "express";
import path from 'path';
import routes from './routes/router.js';

// set up express
const app = express();
const PORT = process.env.PORT || 3000;

// set up to process requests
app.use(express.json());
app.use('/', routes);

// static file locations setup
app.use('/videos', express.static(path.resolve(process.env.VIDEO_DIRECTORY)));
app.use('/thumbnails', express.static(path.resolve(process.env.THUMBNAIL_DIRECTORY)));
app.use('/results', express.static(path.resolve(process.env.RESULTS_DIRECTORY)));

// root test location
app.get('/', (req, res) => {
  res.status(200).send('Server is running!');
});

// start server
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});