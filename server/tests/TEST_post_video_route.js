const baseUrl = 'http://localhost:3000/api';
const filename = 'moving_cyan_video.mp4';
const targetColor = '00FFFF'; // RRGGBB hex without #
const threshold = 50;
const pollInterval = 2000; // milliseconds

// Build the URL for starting the job
const startUrl = `${baseUrl}/process/${filename}?targetColor=${targetColor}&threshold=${threshold}`;

async function startJob() {
  try {
    const response = await fetch(startUrl, { method: 'POST' });
    const data = await response.json();

    if (!data.jobId) {
      console.error('No jobId returned from POST:', data);
      return;
    }

    const jobId = data.jobId;
    console.log('Job started. Captured jobId:', jobId);

    // Start polling the status
    await pollJobStatus(jobId);
  } catch (err) {
    console.error('Error starting job:', err);
  }
}

async function pollJobStatus(jobId) {
  const statusUrl = `${baseUrl}/process/${jobId}/status`;

  const intervalId = setInterval(async () => {
    try {
      const response = await fetch(statusUrl);

      if (response.status === 404) {
        clearInterval(intervalId);
        console.error('Job ID not found.');
        return;
      }

      if (!response.ok) {
        clearInterval(intervalId);
        const errData = await response.json().catch(() => ({}));
        console.error('Error fetching job status:', errData.error || response.statusText);
        return;
      }

      const data = await response.json();

      switch (data.status) {
        case 'processing':
          console.log('Job is still processing...');
          break;
        case 'done':
          clearInterval(intervalId);
          console.log('Job completed! Result file:', data.result);
          break;
        case 'error':
          clearInterval(intervalId);
          console.error('Job failed:', data.error);
          break;
        default:
          clearInterval(intervalId);
          console.error('Unknown status:', data);
          break;
      }
    } catch (err) {
      clearInterval(intervalId);
      console.error('Error polling job status:', err);
    }
  }, pollInterval);
}

// Start everything
startJob();