import { createServer } from 'vite';
import { fileURLToPath } from 'node:url';

const DEFAULT_PORT = 5173;
const configFile = fileURLToPath(new URL('../vite.config.ts', import.meta.url));

async function start() {
  const server = await createServer({
    configFile,
    server: {
      host: '0.0.0.0',
      port: DEFAULT_PORT,
      strictPort: false,
      open: false,
    },
  });

  await server.listen();

  const resolvedUrls = server.resolvedUrls;
  const localUrl = resolvedUrls?.local?.[0] ?? `http://localhost:${DEFAULT_PORT}/`;
  const networkUrl = resolvedUrls?.network?.[0];

  console.log('Frontend IDE launcher started.');
  console.log(`Local:   ${localUrl}`);

  if (networkUrl) {
    console.log(`Network: ${networkUrl}`);
  }
}

start().catch((error) => {
  console.error('Failed to start frontend dev server.');
  console.error(error);
  process.exit(1);
});
