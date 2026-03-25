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

  const response = await fetch(url);
  return response.json();
}