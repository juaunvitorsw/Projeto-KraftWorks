import { useEffect, useState } from "react";
import { getPeople } from "./service/api";
import type { Person } from "./types/Person";

function App() {
  const [people, setPeople] = useState<Person[]>([]);
  const [estado, setEstado] = useState("");
  const [partido, setPartido] = useState("");

  useEffect(() => {
    getPeople().then(setPeople);
  }, []);

  function buscar() {
    getPeople(estado, partido).then(setPeople);
  }

  return (
    <div style={{ padding: 20 }}>
      <h1>Pessoas</h1>

      {/* FILTROS */}
      <div style={{ marginBottom: 20 }}>
        <input
          placeholder="Estado"
          value={estado}
          onChange={(e) => setEstado(e.target.value)}
        />

        <input
          placeholder="Partido"
          value={partido}
          onChange={(e) => setPartido(e.target.value)}
        />

        <button onClick={buscar}>Buscar</button>
      </div>

      {/* LISTA */}
      {people.map((p) => (
        <div key={p.id} style={{ border: "1px solid #ccc", margin: 10, padding: 10 }}>
          <img src={p.fotoUrl} width={80} />
          <h3>{p.nome}</h3>
          <p>{p.cargo}</p>
          <p>{p.estado}</p>
          <p>{p.partido}</p>
        </div>
      ))}
    </div>
  );
}

export default App;