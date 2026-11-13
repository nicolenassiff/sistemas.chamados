import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { chamadoApi } from '../api/recursosApi.js';
import { clienteApi } from '../api/clienteApi.js';
import { PRIORIDADES, STATUS, rotulo, dataHora } from '../constants.js';

// Lista de chamados com filtros (status, prioridade, clienteId) e paginação.
export default function ChamadoLista() {
  const [filtros, setFiltros] = useState({ status: '', prioridade: '', clienteId: '' });
  const [pagina, setPagina] = useState(0);
  const [dados, setDados] = useState({ content: [], totalPages: 0, totalElements: 0 });
  const [clientes, setClientes] = useState([]);
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(true);

  useEffect(() => { clienteApi.listar().then(setClientes).catch(() => {}); }, []);

  useEffect(() => {
    setCarregando(true);
    chamadoApi.listar({ ...filtros, page: pagina, size: 10, sort: 'dataAbertura,desc' })
      .then((d) => { setDados(d); setErro(''); })
      .catch((e) => setErro(e.message))
      .finally(() => setCarregando(false));
  }, [filtros, pagina]);

  const filtrar = (e) => { setPagina(0); setFiltros({ ...filtros, [e.target.name]: e.target.value }); };

  return (
    <section>
      <div className="cabecalho">
        <h1>Chamados</h1>
        <Link className="btn primario" to="/chamados/novo">Abrir chamado</Link>
      </div>
      <div className="filtros">
        <select name="status" value={filtros.status} onChange={filtrar} aria-label="Status">
          <option value="">Todos os status</option>
          {STATUS.map((s) => <option key={s} value={s}>{rotulo(s)}</option>)}
        </select>
        <select name="prioridade" value={filtros.prioridade} onChange={filtrar} aria-label="Prioridade">
          <option value="">Todas as prioridades</option>
          {PRIORIDADES.map((p) => <option key={p} value={p}>{rotulo(p)}</option>)}
        </select>
        <select name="clienteId" value={filtros.clienteId} onChange={filtrar} aria-label="Cliente">
          <option value="">Todos os clientes</option>
          {clientes.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
        </select>
      </div>
      {erro && <p className="erro">{erro}</p>}
      {carregando ? <p>Carregando…</p> : dados.content.length === 0 ? (
        <p className="vazio">Nenhum chamado encontrado com esses filtros.</p>
      ) : (
        <div className="rolagem">
          <table>
            <thead>
              <tr><th>ID</th><th>Título</th><th>Cliente</th><th>Prioridade</th><th>Status</th><th>Técnico</th><th>Abertura</th></tr>
            </thead>
            <tbody>
              {dados.content.map((c) => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td><Link to={`/chamados/${c.id}`}>{c.titulo}</Link></td>
                  <td>{c.clienteNome}</td>
                  <td>{rotulo(c.prioridade)}</td>
                  <td><span className={`selo ${c.status}`}>{rotulo(c.status)}</span></td>
                  <td>{c.tecnicoNome || 'Sem técnico'}</td>
                  <td>{dataHora(c.dataAbertura)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      <div className="paginacao">
        <button className="btn" disabled={pagina === 0} onClick={() => setPagina(pagina - 1)}>Anterior</button>
        <span>Página {dados.totalPages === 0 ? 0 : pagina + 1} de {dados.totalPages} ({dados.totalElements} chamados)</span>
        <button className="btn" disabled={pagina + 1 >= dados.totalPages} onClick={() => setPagina(pagina + 1)}>Próxima</button>
      </div>
    </section>
  );
}
