import type { Person } from "../types/Person";

export async function getPeople(): Promise<Person[]> {
  const response = await fetch("http://localhost:8080/people");
  return response.json();
}