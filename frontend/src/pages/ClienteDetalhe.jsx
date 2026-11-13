import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { clienteApi } from '../api/clienteApi.js';
import { mascaraTelefone } from '../constants.js';

// Tela 5 - Detalhe do cliente (GET /clientes/{id}).
export default function ClienteDetalhe() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [cliente, setCliente] = useState(null);
  const [erro, setErro] = useState('');

  useEffect(() => {
    clienteApi.buscar(id)
      .then(setCliente)
      .catch((e) => setErro(e.status === 404 ? `Cliente ${id} não encontrado.` : e.message));
  }, [id]);

  const excluir = async () => {
    if (!window.confirm(`Excluir o cliente ${cliente.nome}?`)) return;
    try {
      await clienteApi.excluir(id);
      navigate('/clientes');
    } catch (e) {
      setErro(e.message);
    }
  };

  if (erro) {
    return (
      <section>
        <p className="erro">{erro}</p>
        <Link className="btn" to="/clientes">Voltar para a lista</Link>
      </section>
    );
  }
  if (!cliente) return <p>Carregando…</p>;

  return (
    <section>
      <h1>{cliente.nome}</h1>
      <dl>
        <dt>ID</dt><dd>{cliente.id}</dd>
        <dt>E-mail</dt><dd>{cliente.email}</dd>
        <dt>Telefone</dt><dd>{mascaraTelefone(cliente.telefone) || 'Não informado'}</dd>
      </dl>
      <div className="acoes">
        <Link className="btn primario" to={`/clientes/${cliente.id}/editar`}>Editar</Link>
        <button className="btn perigo" onClick={excluir}>Excluir</button>
        <Link className="btn" to="/clientes">Voltar</Link>
      </div>
    </section>
  );
}
