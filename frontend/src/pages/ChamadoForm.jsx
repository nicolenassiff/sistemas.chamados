import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { chamadoApi, categoriaApi, tecnicoApi } from '../api/recursosApi.js';
import { clienteApi } from '../api/clienteApi.js';
import { PRIORIDADES, rotulo } from '../constants.js';

const inicial = { titulo: '', descricao: '', prioridade: 'MEDIA', clienteId: '', categoriaId: '', tecnicoId: '' };

// Abrir chamado (POST) e editar chamado (PUT).
export default function ChamadoForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [dados, setDados] = useState(inicial);
  const [clientes, setClientes] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [tecnicos, setTecnicos] = useState([]);
  const [erro, setErro] = useState('');
  const [detalhes, setDetalhes] = useState([]);
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    Promise.all([clienteApi.listar(), categoriaApi.listar(), tecnicoApi.listar()])
      .then(([cl, ca, te]) => { setClientes(cl); setCategorias(ca); setTecnicos(te); })
      .catch((e) => setErro(e.message));
  }, []);

  useEffect(() => {
    if (!id) return;
    chamadoApi.buscar(id)
      .then((c) => setDados({
        titulo: c.titulo, descricao: c.descricao ?? '', prioridade: c.prioridade,
        clienteId: c.clienteId, categoriaId: c.categoriaId, tecnicoId: c.tecnicoId ?? '',
      }))
      .catch((e) => setErro(e.message));
  }, [id]);

  const alterar = (e) => setDados({ ...dados, [e.target.name]: e.target.value });

  const enviar = async (e) => {
    e.preventDefault();
    setErro(''); setDetalhes([]); setSalvando(true);
    const corpo = {
      ...dados,
      clienteId: dados.clienteId ? Number(dados.clienteId) : null,
      categoriaId: dados.categoriaId ? Number(dados.categoriaId) : null,
      tecnicoId: dados.tecnicoId ? Number(dados.tecnicoId) : null,
    };
    try {
      const salvo = id ? await chamadoApi.atualizar(id, corpo) : await chamadoApi.criar(corpo);
      navigate(`/chamados/${salvo.id}`);
    } catch (err) {
      setErro(err.message); setDetalhes(err.detalhes);
    } finally { setSalvando(false); }
  };

  return (
    <section>
      <h1>{id ? 'Editar chamado' : 'Abrir chamado'}</h1>
      <form onSubmit={enviar} noValidate>
        <label>Título <input name="titulo" value={dados.titulo} onChange={alterar} maxLength={100} /></label>
        <label>Descrição <textarea name="descricao" rows={5} value={dados.descricao} onChange={alterar} maxLength={2000} /></label>
        <label>Prioridade <select name="prioridade" value={dados.prioridade} onChange={alterar}>
          {PRIORIDADES.map((p) => <option key={p} value={p}>{rotulo(p)}</option>)}
        </select></label>
        <label>Cliente <select name="clienteId" value={dados.clienteId} onChange={alterar}>
          <option value="">Selecione…</option>
          {clientes.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
        </select></label>
        <label>Categoria <select name="categoriaId" value={dados.categoriaId} onChange={alterar}>
          <option value="">Selecione…</option>
          {categorias.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
        </select></label>
        <label>Técnico (opcional) <select name="tecnicoId" value={dados.tecnicoId} onChange={alterar}>
          <option value="">Sem técnico</option>
          {tecnicos.map((t) => <option key={t.id} value={t.id}>{t.nome}</option>)}
        </select></label>
        {erro && (
          <div className="erro" role="alert">
            <p>{erro}</p>
            {detalhes.length > 0 && <ul>{detalhes.map((d) => <li key={d}>{d}</li>)}</ul>}
          </div>
        )}
        <div className="acoes">
          <button type="submit" className="btn primario" disabled={salvando}>{salvando ? 'Salvando…' : 'Salvar chamado'}</button>
          <Link className="btn" to="/chamados">Cancelar</Link>
        </div>
      </form>
    </section>
  );
}
