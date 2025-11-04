import axios from "axios";

const municipalitiesApi = axios.create({
  baseURL: "https://geoapi.pt/",
  headers: {
    "Content-Type": "application/json",
  },
});

export const getMunicipalities = (): Promise<string[]> =>
  municipalitiesApi.get("/municipios").then((response) => response.data);
