import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],

  base: '/react/',
  build: {

    outDir: '../src/main/resources/static/react',
    emptyOutDir: true,
    rollupOptions: {
      output: {

        entryFileNames: 'assets/app.js',
        chunkFileNames: 'assets/[name].js',
        assetFileNames: 'assets/app[extname]',
      },
    },
  },
  server: {
    
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
