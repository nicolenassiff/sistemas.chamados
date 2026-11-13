import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { recursos } from '../recursos.js';

// Lista genérica: serve para /categorias e /tecnicos.
export default function RecursoLista({ chave }) {
  const { api, titulo, singular, campos } = recursos[chave];
  const [itens, setItens] = useState([]);
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(true);

  const carregar = () => {
    setCarregando(true);
    api.listar()
      .then((d) => { setItens(d); setErro(''); })
      .catch((e) => setErro(e.message))
      .finally(() => setCarregando(false));
  };
  useEffect(carregar, [chave]);

  const excluir = async (item) => {
    if (!window.confirm(`Excluir ${singular} ${item.nome}?`)) return;
    try { await api.excluir(item.id); carregar(); } catch (e) { setErro(e.message); }
  };

  return (
    <section>
      <div className="cabecalho">
        <h1>{titulo}</h1>
        <Link className="btn primario" to={`/${chave}/novo`}>Nova {singular}</Link>
      </div>
      {erro && <p className="erro">{erro}</p>}
      {carregando ? <p>Carregando…</p> : itens.length === 0 ? (
        <p className="vazio">Nenhum registro. Cadastre o primeiro.</p>
      ) : (
        <div className="rolagem">
          <table>
            <thead>
              <tr><th>ID</th>{campos.map((c) => <th key={c.name}>{c.label}</th>)}<th>Ações</th></tr>
            </thead>
            <tbody>
              {itens.map((i) => (
                <tr key={i.id}>
                  <td>{i.id}</td>
                  {campos.map((c) => <td key={c.name}>{i[c.name]}</td>)}
                  <td className="acoes-linha">
                    <Link to={`/${chave}/${i.id}/editar`}>Editar</Link>
                    <button className="link perigo" onClick={() => excluir(i)}>Excluir</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}
