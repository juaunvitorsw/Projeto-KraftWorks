import { useEffect, useState } from "react";
import { getPeople } from "./service/api";
import type { Person } from "./types/Person";

function App() {
  const [people, setPeople] = useState<Person[]>([]);

  useEffect(() => {
    getPeople().then(setPeople);
  }, []);

  return (
    <div style={{ padding: 20 }}>
      <h1>Pessoas</h1>

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