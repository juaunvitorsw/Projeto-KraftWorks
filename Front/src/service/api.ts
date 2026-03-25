import type { Person } from "../types/Person";

export async function getPeople(
  estado?: string,
  partido?: string
): Promise<Person[]> {

  let url = "http://localhost:8080/people";

  const params = new URLSearchParams();

  if (estado) params.append("estado", estado);
  if (partido) params.append("partido", partido);

  if (params.toString()) {
    url += `?${params.toString()}`;
  }

  try {
    const response = await fetch(url);
    
    if (!response.ok) {
      throw new Error(`Erro ${response.status}: ${response.statusText}`);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof TypeError) {
      throw new Error("Não foi possível conectar ao servidor. Verifique se o backend está ativo.");
    }
    throw error;
  }
}