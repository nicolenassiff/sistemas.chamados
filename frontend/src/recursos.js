import { categoriaApi, tecnicoApi } from './api/recursosApi.js';
import { PERFIS } from './constants.js';

// Configuração dos CRUDs simples (mesma tela serve para os dois recursos).
export const recursos = {
  categorias: {
    api: categoriaApi,
    titulo: 'Categorias',
    singular: 'categoria',
    campos: [
      { name: 'nome', label: 'Nome', max: 100 },
      { name: 'descricao', label: 'Descrição', max: 255 },
    ],
  },
  tecnicos: {
    api: tecnicoApi,
    titulo: 'Técnicos',
    singular: 'técnico',
    campos: [
      { name: 'nome', label: 'Nome', max: 100 },
      { name: 'email', label: 'E-mail', type: 'email', max: 120 },
      { name: 'perfil', label: 'Perfil', opcoes: PERFIS },
    ],
  },
};
