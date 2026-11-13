import { NavLink, Route, Routes } from 'react-router-dom';
import Dashboard from './pages/Dashboard.jsx';
import ClienteLista from './pages/ClienteLista.jsx';
import ClienteForm from './pages/ClienteForm.jsx';
import ClienteDetalhe from './pages/ClienteDetalhe.jsx';
import ChamadoLista from './pages/ChamadoLista.jsx';
import ChamadoForm from './pages/ChamadoForm.jsx';
import ChamadoDetalhe from './pages/ChamadoDetalhe.jsx';
import RecursoLista from './pages/RecursoLista.jsx';
import RecursoForm from './pages/RecursoForm.jsx';

export default function App() {
  return (
    <>
      <header className="topo">
        <strong className="marca">Chamados</strong>
        <nav>
          <NavLink to="/" end>Início</NavLink>
          <NavLink to="/chamados">Chamados</NavLink>
          <NavLink to="/clientes">Clientes</NavLink>
          <NavLink to="/categorias">Categorias</NavLink>
          <NavLink to="/tecnicos">Técnicos</NavLink>
        </nav>
      </header>
      <main>
        <Routes>
          <Route path="/" element={<Dashboard />} />

          <Route path="/chamados" element={<ChamadoLista />} />
          <Route path="/chamados/novo" element={<ChamadoForm />} />
          <Route path="/chamados/:id" element={<ChamadoDetalhe />} />
          <Route path="/chamados/:id/editar" element={<ChamadoForm />} />

          <Route path="/clientes" element={<ClienteLista />} />
          <Route path="/clientes/novo" element={<ClienteForm />} />
          <Route path="/clientes/:id" element={<ClienteDetalhe />} />
          <Route path="/clientes/:id/editar" element={<ClienteForm />} />

          <Route path="/categorias" element={<RecursoLista chave="categorias" />} />
          <Route path="/categorias/novo" element={<RecursoForm chave="categorias" />} />
          <Route path="/categorias/:id/editar" element={<RecursoForm chave="categorias" />} />

          <Route path="/tecnicos" element={<RecursoLista chave="tecnicos" />} />
          <Route path="/tecnicos/novo" element={<RecursoForm chave="tecnicos" />} />
          <Route path="/tecnicos/:id/editar" element={<RecursoForm chave="tecnicos" />} />

          <Route path="*" element={<p className="vazio">Página não encontrada.</p>} />
        </Routes>
      </main>
    </>
  );
}
