import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { clienteApi } from '../api/clienteApi.js';
import { mascaraTelefone } from '../constants.js';

// Tela 2 - Lista de clientes (GET /clientes) com exclusão (DELETE /clientes/{id}).
export default function ClienteLista() {
  const [clientes, setClientes] = useState([]);
  const [busca, setBusca] = useState('');
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(true);

  const carregar = () => {
    setCarregando(true);
    clienteApi.listar()
      .then((dados) => { setClientes(dados); setErro(''); })
      .catch((e) => setErro(e.message))
      .finally(() => setCarregando(false));
  };

  useEffect(carregar, []);

  const excluir = async (c) => {
    if (!window.confirm(`Excluir o cliente ${c.nome}?`)) return;
    try {
      await clienteApi.excluir(c.id);
      carregar();
    } catch (e) {
      setErro(e.message);
    }
  };

  const filtrados = clientes.filter((c) =>
    `${c.nome} ${c.email}`.toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <section>
      <div className="cabecalho">
        <h1>Clientes</h1>
        <Link className="btn primario" to="/clientes/novo">Novo cliente</Link>
      </div>
      <input
        className="busca"
        placeholder="Buscar por nome ou e-mail"
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
      />
      {erro && <p className="erro">{erro}</p>}
      {carregando ? <p>Carregando…</p> : filtrados.length === 0 ? (
        <p className="vazio">Nenhum cliente encontrado.</p>
      ) : (
        <div className="rolagem">
          <table>
            <thead>
              <tr><th>ID</th><th>Nome</th><th>E-mail</th><th>Telefone</th><th>Ações</th></tr>
            </thead>
            <tbody>
              {filtrados.map((c) => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td>{c.nome}</td>
                  <td>{c.email}</td>
                  <td>{mascaraTelefone(c.telefone) || '—'}</td>
                  <td className="acoes-linha">
                    <Link to={`/clientes/${c.id}`}>Ver</Link>
                    <Link to={`/clientes/${c.id}/editar`}>Editar</Link>
                    <button className="link perigo" onClick={() => excluir(c)}>Excluir</button>
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
