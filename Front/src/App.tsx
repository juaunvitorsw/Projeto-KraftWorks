import { useEffect, useState } from "react";
import { getPeople } from "./service/api";
import type { Person } from "./types/Person";
import "./App.css";

function App() {
  const [people, setPeople] = useState<Person[]>([]);
  const [estado, setEstado] = useState("");
  const [partido, setPartido] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    getPeople()
      .then((data) => {
        setPeople(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message || "Erro ao carregar dados");
        setLoading(false);
      });
  }, []);

  function buscar() {
    setLoading(true);
    setError(null);
    getPeople(estado, partido)
      .then((data) => {
        setPeople(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message || "Erro ao buscar dados");
        setLoading(false);
      });
  }

  return (
    <div className="app-container">
      <div className="header">
        <h1>Pessoas</h1>
        <p className="subtitle">Encontre informações sobre pessoas públicas</p>
      </div>

      {/* FILTROS */}
      <div className="filters-section">
        <div className="filters-grid">
          <input
            className="filter-input"
            placeholder="🔍 Filtrar por Estado"
            value={estado}
            onChange={(e) => setEstado(e.target.value)}
            disabled={loading}
          />

          <input
            className="filter-input"
            placeholder="🏛️ Filtrar por Partido"
            value={partido}
            onChange={(e) => setPartido(e.target.value)}
            disabled={loading}
          />

          <button className="search-btn" onClick={buscar} disabled={loading}>
            {loading ? "Carregando..." : "Buscar"}
          </button>
        </div>
      </div>

      {/* ERRO */}
      {error && (
        <div className="error-message">
          <div className="error-icon">⚠️</div>
          <div className="error-content">
            <h3>Oops! Algo deu errado</h3>
            <p>{error}</p>
            <button className="error-retry-btn" onClick={buscar}>
              Tentar Novamente
            </button>
          </div>
        </div>
      )}

      {/* LISTA */}
      <div className="people-grid">
        {loading ? (
          <div className="loading">Carregando dados...</div>
        ) : people.length > 0 ? (
          people.map((p) => (
            <div key={p.id} className="person-card">
              <div className="card-image-wrapper">
                <img src={p.fotoUrl} alt={p.nome} className="card-image" />
              </div>
              <div className="card-content">
                <h3 className="person-name">{p.nome}</h3>
                <p className="person-cargo">{p.cargo}</p>
                <div className="person-tags">
                  <span className="tag tag-estado">{p.estado}</span>
                  <span className="tag tag-partido">{p.partido}</span>
                </div>
              </div>
            </div>
          ))
        ) : (
          <div className="no-results">Nenhuma pessoa encontrada</div>
        )}
      </div>
    </div>
  );
}

export default App;