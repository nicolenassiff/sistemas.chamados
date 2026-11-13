import { request, comCorpo } from './http.js';

// Rotas: POST/GET /clientes, GET/PUT/DELETE /clientes/{id}
const BASE = '/clientes';

export const clienteApi = {
  listar: () => request(BASE),
  buscar: (id) => request(`${BASE}/${id}`),
  criar: (dados) => request(BASE, comCorpo('POST', dados)),
  atualizar: (id, dados) => request(`${BASE}/${id}`, comCorpo('PUT', dados)),
  excluir: (id) => request(`${BASE}/${id}`, { method: 'DELETE' }),
};
