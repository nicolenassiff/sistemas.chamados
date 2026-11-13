import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { clienteApi } from '../api/clienteApi.js';

// Tela 1 - Início: resumo dos clientes e atalhos.
export default function Dashboard() {
  const [clientes, setClientes] = useState([]);
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    clienteApi.listar()
      .then(setClientes)
      .catch(() => setErro('Não foi possível conectar à API em http://localhost:8080. Confira se o Spring Boot está rodando.'))
      .finally(() => setCarregando(false));
  }, []);

  const recentes = [...clientes].reverse().slice(0, 5);

  return (
    <section>
      <h1>Início</h1>
      {erro && <p className="erro">{erro}</p>}
      {carregando ? <p>Carregando…</p> : !erro && (
        <>
          <p className="resumo">
            <span className="numero">{clientes.length}</span>{' '}
            {clientes.length === 1 ? 'cliente cadastrado' : 'clientes cadastrados'}
          </p>
          <div className="acoes">
            <Link className="btn primario" to="/clientes/novo">Cadastrar cliente</Link>
            <Link className="btn" to="/clientes">Ver todos</Link>
          </div>
          <h2>Últimos cadastros</h2>
          {recentes.length === 0 ? (
            <p className="vazio">Nenhum cliente ainda. Cadastre o primeiro para começar.</p>
          ) : (
            <ul className="lista">
              {recentes.map((c) => (
                <li key={c.id}>
                  <Link to={`/clientes/${c.id}`}>{c.nome}</Link>
                  <span>{c.email}</span>
                </li>
              ))}
            </ul>
          )}
        </>
      )}
    </section>
  );
}
