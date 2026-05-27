import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/login': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/find': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/elder': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/staff': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/bed': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/nursing-service': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/visit': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/order': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/appointment': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/user': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/dict': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/statistics': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/ai': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
