import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { recursos } from '../recursos.js';
import { rotulo, emailValido } from '../constants.js';

// Formulário genérico (POST e PUT) para categorias e técnicos.
export default function RecursoForm({ chave }) {
  const { api, singular, campos } = recursos[chave];
  const { id } = useParams();
  const navigate = useNavigate();
  const inicial = Object.fromEntries(campos.map((c) => [c.name, c.opcoes ? c.opcoes[0] : '']));
  const [dados, setDados] = useState(inicial);
  const [erro, setErro] = useState('');
  const [detalhes, setDetalhes] = useState([]);
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    if (!id) return;
    api.buscar(id)
      .then((r) => setDados(Object.fromEntries(campos.map((c) => [c.name, r[c.name] ?? '']))))
      .catch((e) => setErro(e.message));
  }, [id, chave]);

  const alterar = (e) => {
    const { name, value, type } = e.target;
    setDados({ ...dados, [name]: type === 'email' ? value.replace(/\s/g, '') : value });
  };

  const enviar = async (e) => {
    e.preventDefault();
    setErro(''); setDetalhes([]);

    const campoEmail = campos.find((c) => c.type === 'email');
    if (campoEmail && !emailValido(dados[campoEmail.name])) {
      setErro('Dados inválidos');
      setDetalhes(['email: informe um e-mail válido, como nome@dominio.com']);
      return;
    }

    setSalvando(true);
    try {
      if (id) await api.atualizar(id, dados); else await api.criar(dados);
      navigate(`/${chave}`);
    } catch (err) {
      setErro(err.message); setDetalhes(err.detalhes || []);
    } finally {
      setSalvando(false);
    }
  };

  return (
    <section>
      <h1>{id ? `Editar ${singular}` : `Nova ${singular}`}</h1>
      <form onSubmit={enviar} noValidate>
        {campos.map((c) => (
          <label key={c.name}>{c.label}
            {c.opcoes ? (
              <select name={c.name} value={dados[c.name]} onChange={alterar}>
                {c.opcoes.map((o) => <option key={o} value={o}>{rotulo(o)}</option>)}
              </select>
            ) : (
              <input name={c.name} type={c.type || 'text'} value={dados[c.name]} onChange={alterar} maxLength={c.max} />
            )}
          </label>
        ))}
        {erro && (
          <div className="erro" role="alert">
            <p>{erro}</p>
            {detalhes.length > 0 && <ul>{detalhes.map((d) => <li key={d}>{d}</li>)}</ul>}
          </div>
        )}
        <div className="acoes">
          <button className="btn primario" disabled={salvando}>{salvando ? 'Salvando…' : 'Salvar'}</button>
          <Link className="btn" to={`/${chave}`}>Cancelar</Link>
        </div>
      </form>
    </section>
  );
}