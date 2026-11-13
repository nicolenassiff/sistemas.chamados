export async function request(url, options = {}) {
  let res;
  try {
    res = await fetch(url, {
      headers: { 'Content-Type': 'application/json' },
      signal: AbortSignal.timeout(10000),
      ...options,
    });
  } catch {
    const err = new Error('A API não respondeu em 10 segundos. Confira se o Spring está rodando e se a porta do vite.config.js é a mesma da API.');
    err.status = 0;
    err.detalhes = [];
    throw err;
  }

  if (!res.ok) {
    // O GlobalExceptionHandler devolve { momento, status, erro, detalhes }
    let corpo = {};
    try { corpo = await res.json(); } catch { /* resposta sem corpo */ }
    const err = new Error(corpo.erro || `Erro ${res.status}`);
    err.status = res.status;
    err.detalhes = corpo.detalhes || [];
    throw err;
  }
  return res.status === 204 ? null : res.json();
}

export const comCorpo = (method, body) => ({ method, body: JSON.stringify(body) });
