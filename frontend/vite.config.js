import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // fejlesztés közben (npm run dev) az /api kéréseket a Spring Boot kapja meg
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
