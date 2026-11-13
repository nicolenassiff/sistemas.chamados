import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

const api = 'http://localhost:8081'; 
const rota = {
  target: api,
  bypass: (req) => (req.headers.accept?.includes('text/html') ? '/index.html' : undefined),
};

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/clientes': rota,
      '/categorias': rota,
      '/tecnicos': rota,
      '/chamados': rota,
    },
  },
});