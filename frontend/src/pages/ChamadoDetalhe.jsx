import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { chamadoApi, tecnicoApi } from '../api/recursosApi.js';
import { STATUS, rotulo, dataHora } from '../constants.js';

// Detalhe do chamado: dados, atribuir técnico (PATCH), alterar status (PATCH) e comentários.
export default function ChamadoDetalhe() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [chamado, setChamado] = useState(null);
  const [comentarios, setComentarios] = useState([]);
  const [tecnicos, setTecnicos] = useState([]);
  const [tecnicoId, setTecnicoId] = useState('');
  const [status, setStatus] = useState('');
  const [novo, setNovo] = useState({ autor: '', texto: '' });
  const [erro, setErro] = useState('');
  const [detalhes, setDetalhes] = useState([]);

  const aplicar = (c) => { setChamado(c); setStatus(c.status); setTecnicoId(c.tecnicoId ?? ''); };

  useEffect(() => {
    Promise.all([chamadoApi.buscar(id), chamadoApi.comentarios(id), tecnicoApi.listar()])
      .then(([c, co, te]) => { aplicar(c); setComentarios(co); setTecnicos(te); })
      .catch((e) => setErro(e.status === 404 ? `Chamado ${id} não encontrado.` : e.message));
  }, [id]);

  // Executa uma ação e mostra o erro da API (ex.: 409 em chamado fechado).
  const executar = async (fn) => {
    setErro(''); setDetalhes([]);
    try { await fn(); } catch (e) { setErro(e.message); setDetalhes(e.detalhes || []); }
  };

  const atribuir = () => executar(async () => aplicar(await chamadoApi.atribuir(id, Number(tecnicoId))));
  const mudarStatus = () => executar(async () => aplicar(await chamadoApi.alterarStatus(id, status)));
  const comentar = (e) => {
    e.preventDefault();
    executar(async () => {
      const criado = await chamadoApi.comentar(id, novo);
      setComentarios([...comentarios, criado]);
      setNovo({ ...novo, texto: '' });
    });
  };
  const excluir = async () => {
    if (!window.confirm('Excluir este chamado?')) return;
    executar(async () => { await chamadoApi.excluir(id); navigate('/chamados'); });
  };

  if (!chamado) {
    return erro ? (
      <section><p className="erro">{erro}</p><Link className="btn" to="/chamados">Voltar</Link></section>
    ) : <p>Carregando…</p>;
  }

  return (
    <section>
      <div className="cabecalho">
        <h1>#{chamado.id} {chamado.titulo}</h1>
        <span className={`selo ${chamado.status}`}>{rotulo(chamado.status)}</span>
      </div>
      {erro && (
        <div className="erro" role="alert">
          <p>{erro}</p>
          {detalhes.length > 0 && <ul>{detalhes.map((d) => <li key={d}>{d}</li>)}</ul>}
        </div>
      )}
      <dl>
        <dt>Descrição</dt><dd>{chamado.descricao || 'Sem descrição'}</dd>
        <dt>Cliente</dt><dd><Link to={`/clientes/${chamado.clienteId}`}>{chamado.clienteNome}</Link></dd>
        <dt>Categoria</dt><dd>{chamado.categoriaNome}</dd>
        <dt>Prioridade</dt><dd>{rotulo(chamado.prioridade)}</dd>
        <dt>Técnico</dt><dd>{chamado.tecnicoNome || 'Sem técnico'}</dd>
        <dt>Abertura</dt><dd>{dataHora(chamado.dataAbertura)}</dd>
        <dt>Fechamento</dt><dd>{dataHora(chamado.dataFechamento)}</dd>
      </dl>

      <div className="painel">
        <label>Atribuir técnico
          <select value={tecnicoId} onChange={(e) => setTecnicoId(e.target.value)}>
            <option value="">Selecione…</option>
            {tecnicos.map((t) => <option key={t.id} value={t.id}>{t.nome}</option>)}
          </select>
        </label>
        <button className="btn" disabled={!tecnicoId} onClick={atribuir}>Atribuir</button>
        <label>Status
          <select value={status} onChange={(e) => setStatus(e.target.value)}>
            {STATUS.map((s) => <option key={s} value={s}>{rotulo(s)}</option>)}
          </select>
        </label>
        <button className="btn" onClick={mudarStatus}>Alterar status</button>
      </div>

      <div className="acoes">
        <Link className="btn primario" to={`/chamados/${chamado.id}/editar`}>Editar</Link>
        <button className="btn perigo" onClick={excluir}>Excluir</button>
        <Link className="btn" to="/chamados">Voltar</Link>
      </div>

      <h2>Comentários</h2>
      {comentarios.length === 0 ? <p className="vazio">Nenhum comentário ainda.</p> : (
        <ul className="lista comentarios">
          {comentarios.map((c) => (
            <li key={c.id}><strong>{c.autor}</strong> <span>{dataHora(c.data)}</span><p>{c.texto}</p></li>
          ))}
        </ul>
      )}
      <form onSubmit={comentar} noValidate>
        <label>Autor
          <input value={novo.autor} maxLength={100} onChange={(e) => setNovo({ ...novo, autor: e.target.value })} />
        </label>
        <label>Comentário
          <textarea rows={3} value={novo.texto} maxLength={1000} onChange={(e) => setNovo({ ...novo, texto: e.target.value })} />
        </label>
        <div className="acoes"><button className="btn primario">Adicionar comentário</button></div>
      </form>
    </section>
  );
}
