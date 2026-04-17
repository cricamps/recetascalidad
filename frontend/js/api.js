/**
 * api.js — Capa de comunicación con el Backend (API REST)
 *
 * SEPARACIÓN DE CAPAS:
 *   Este archivo es la única parte del frontend que conoce la URL del backend.
 *   Todos los componentes de la UI usan estas funciones — nunca fetch() directo.
 *   El backend responde JSON; el frontend nunca accede a la BD directamente.
 */

const API_BASE = 'http://localhost:8080/recetas/api';

// ── TOKEN JWT (almacenado en memoria, no en localStorage por seguridad) ──────
let _token = null;

export function setToken(token) { _token = token; }
export function getToken()      { return _token; }
export function clearToken()    { _token = null; }
export function isLoggedIn()    { return _token !== null; }

// ── HELPER: fetch con cabecera JWT automática ─────────────────────────────────
async function apiFetch(path, options = {}) {
    const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
    if (_token) headers['Authorization'] = `Bearer ${_token}`;

    const response = await fetch(`${API_BASE}${path}`, { ...options, headers });

    if (response.status === 401) {
        clearToken();
        throw new Error('Sesión expirada. Por favor inicia sesión nuevamente.');
    }
    if (!response.ok) {
        const text = await response.text();
        throw new Error(`Error ${response.status}: ${text}`);
    }
    if (response.status === 204) return null;
    return response.json();
}

// ── AUTH ──────────────────────────────────────────────────────────────────────

/**
 * Autenticar usuario y obtener JWT.
 * @param {string} username
 * @param {string} password
 * @returns {{ token, username, roles, expiresIn }}
 */
export async function login(username, password) {
    const data = await apiFetch('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ username, password })
    });
    setToken(data.token);
    return data;
}

export function logout() {
    clearToken();
}

// ── RECETAS ───────────────────────────────────────────────────────────────────

export async function getRecetas()            { return apiFetch('/recetas'); }
export async function getRecetaById(id)       { return apiFetch(`/recetas/${id}`); }
export async function getRecetasPopulares()   { return apiFetch('/recetas/populares'); }
export async function getRecetasRecientes()   { return apiFetch('/recetas/recientes'); }

export async function buscarRecetas({ nombre, tipoCocina, paisOrigen, dificultad, ingrediente } = {}) {
    const params = new URLSearchParams();
    if (nombre)      params.set('nombre', nombre);
    if (tipoCocina)  params.set('tipoCocina', tipoCocina);
    if (paisOrigen)  params.set('paisOrigen', paisOrigen);
    if (dificultad)  params.set('dificultad', dificultad);
    if (ingrediente) params.set('ingrediente', ingrediente);
    return apiFetch(`/recetas/buscar?${params.toString()}`);
}

// ── USUARIO AUTENTICADO ───────────────────────────────────────────────────────
export async function getMe() { return apiFetch('/me'); }
