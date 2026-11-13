export const PRIORIDADES = ['BAIXA', 'MEDIA', 'ALTA', 'CRITICA'];
export const STATUS = ['ABERTO', 'EM_ATENDIMENTO', 'AGUARDANDO_CLIENTE', 'FECHADO'];
export const PERFIS = ['ADMIN', 'TECNICO'];

export const rotulo = (v) => (v ? v.replace('_', ' ').toLowerCase().replace(/^./, (c) => c.toUpperCase()) : '—');
export const dataHora = (v) => (v ? new Date(v).toLocaleString('pt-BR') : '—');

export const soDigitos = (v) => (v || '').replace(/\D/g, '').slice(0, 11);

export function mascaraTelefone(valor) {
  const d = soDigitos(valor);
  if (d.length === 0) return '';
  if (d.length <= 2) return `(${d}`;
  if (d.length <= 6) return `(${d.slice(0, 2)}) ${d.slice(2)}`;
  if (d.length <= 10) return `(${d.slice(0, 2)}) ${d.slice(2, 6)}-${d.slice(6)}`;
  return `(${d.slice(0, 2)}) ${d.slice(2, 7)}-${d.slice(7)}`;
}

export const emailValido = (v) =>
  /^[^\s@]+@([^\s@.]+\.)+[^\s@.]{2,}$/.test((v || '').trim());