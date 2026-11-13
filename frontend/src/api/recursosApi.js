import { request, comCorpo } from './http.js';

function crud(base) {
  return {
    listar: () => request(base),
    buscar: (id) => request(`${base}/${id}`),
    criar: (dados) => request(base, comCorpo('POST', dados)),
    atualizar: (id, dados) => request(`${base}/${id}`, comCorpo('PUT', dados)),
    excluir: (id) => request(`${base}/${id}`, { method: 'DELETE' }),
  };
}

export const categoriaApi = crud('/categorias');
export const tecnicoApi = crud('/tecnicos');

export const chamadoApi = {
  ...crud('/chamados'),
  listar: (filtros = {}) => {
    const limpos = Object.fromEntries(Object.entries(filtros).filter(([, v]) => v !== '' && v != null));
    return request('/chamados?' + new URLSearchParams(limpos));
  },
  atribuir: (id, tecnicoId) => request(`/chamados/${id}/atribuir`, comCorpo('PATCH', { tecnicoId })),
  alterarStatus: (id, status) => request(`/chamados/${id}/status`, comCorpo('PATCH', { status })),
  comentarios: (id) => request(`/chamados/${id}/comentarios`),
  comentar: (id, dados) => request(`/chamados/${id}/comentarios`, comCorpo('POST', dados)),
  porCliente: (clienteId, page = 0) => request(`/clientes/${clienteId}/chamados?page=${page}&size=10`),
};
