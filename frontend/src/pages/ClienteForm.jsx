import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { clienteApi } from '../api/clienteApi.js';
import { mascaraTelefone, soDigitos, emailValido } from '../constants.js';

const vazio = { nome: '', email: '', telefone: '' };

// Telas 3 e 4 - Novo cliente (POST) e Editar cliente (PUT).
// Sem :id na rota = cadastro; com :id = edição.
export default function ClienteForm() {
  const { id } = useParams();
  const editando = Boolean(id);
  const navigate = useNavigate();

  const [dados, setDados] = useState(vazio);
  const [erro, setErro] = useState('');
  const [detalhes, setDetalhes] = useState([]);
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    if (!editando) return;
    clienteApi.buscar(id)
      .then(({ nome, email, telefone }) => setDados({ nome, email, telefone: mascaraTelefone(telefone ?? '') }))
      .catch((e) => setErro(e.message));
  }, [id, editando]);

  const alterar = (e) => {
    const { name, value } = e.target;
    const novo = name === 'telefone' ? mascaraTelefone(value)
              : name === 'email' ? value.replace(/\s/g, '')
              : value;
    setDados({ ...dados, [name]: novo });
  };

  const enviar = async (e) => {
  e.preventDefault();
  setErro(''); setDetalhes([]);

  if (!emailValido(dados.email)) {
    setErro('Dados inválidos');
    setDetalhes(['email: informe um e-mail válido, como nome@dominio.com']);
    return;
  }

  setSalvando(true);
  try {
    const corpo = { ...dados, email: dados.email.trim(), telefone: soDigitos(dados.telefone) };
    const salvo = editando
      ? await clienteApi.atualizar(id, corpo)
      : await clienteApi.criar(corpo);
    navigate(`/clientes/${salvo.id}`);
  } catch (err) {
    // 400 traz a lista "campo: mensagem"; 409 traz o e-mail duplicado em err.message
    setErro(err.message);
    setDetalhes(err.detalhes);
  } finally {
    setSalvando(false);
  }
};

  return (
    <section>
      <h1>{editando ? 'Editar cliente' : 'Novo cliente'}</h1>
      <form onSubmit={enviar} noValidate>
        <label>Nome
          <input name="nome" value={dados.nome} onChange={alterar} maxLength={100} />
        </label>
        <label>E-mail
          <input name="email" type="email" value={dados.email} onChange={alterar} maxLength={120} />
        </label>
        <label>Telefone
          <input name="telefone" value={dados.telefone} onChange={alterar}
          inputMode="numeric" placeholder="(47) 99999-9999" maxLength={15} />
        </label>

        {erro && (
          <div className="erro" role="alert">
            <p>{erro}</p>
            {detalhes.length > 0 && <ul>{detalhes.map((d) => <li key={d}>{d}</li>)}</ul>}
          </div>
        )}

        <div className="acoes">
          <button className="btn primario" disabled={salvando}>
            {salvando ? 'Salvando…' : 'Salvar cliente'}
          </button>
          <Link className="btn" to="/clientes">Cancelar</Link>
        </div>
      </form>
    </section>
  );
}
