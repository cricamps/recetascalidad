/**
 * api.js — Capa de comunicación con el Backend (API REST)
 *
 * ┌──────────────────────────────────────────────────────────────────────┐
 * │  SEPARACIÓN DE CAPAS                                                 │
 * │  Este es el único archivo del frontend que conoce la URL del         │
 * │  backend. Todos los módulos de la UI importan funciones de aquí.     │
 * │  NUNCA se usa fetch() directamente fuera de este archivo.            │
 * │  El frontend NO accede a la BD, NO contiene lógica de negocio.       │
 * └──────────────────────────────────────────────────────────────────────┘
 *
 * Flujo de autenticación:
 *   1. login()           → POST /api/auth/login        → { accessToken, refreshToken }
 *   2. cada petición     → header Authorization: Bearer <accessToken>
 *   3. token expirado    → POST /api/auth/refresh-token → { accessToken }
 *   4. logout()          → limpia tokens en memoria y sessionStorage
 */

const API_BASE = 'http://localhost:8080/recetas/api';

// ── ALMACENAMIENTO DE TOKENS EN MEMORIA ──────────────────────────────────────
// Los tokens se guardan en variables de módulo (no en localStorage)
// para reducir la superficie de ataque XSS.
let _accessToken  = null;
let _refreshToken = null;

export function setToken(token)         { _accessToken  = token; }
export function setRefreshToken(token)  { _refreshToken = token; }
export function getToken()              { return _accessToken; }
export function clearToken()            { _accessToken = null; _refreshToken = null; }
export function isLoggedIn()            { return _accessToken !== null; }

// ── HELPER PRINCIPAL ─────────────────────────────────────────────────────────
/**
 * Realiza una petición HTTP al backend con cabecera JWT automática.
 * Si el servidor responde 401 y existe un refreshToken, intenta renovar
 * el accessToken automáticamente antes de reintentar la petición.
 *
 * @param {string} path    - Ruta relativa, ej: '/recetas'
 * @param {object} options - Opciones de fetch (method, body, headers)
 * @returns {Promise<any>} - Respuesta JSON parseada, o null si 204
 */
async function apiFetch(path, options = {}) {
    const response = await _fetchWithAuth(path, options);

    // 401 con refresh token disponible → intentar renovación automática
    if (response.status === 401 && _refreshToken) {
        const renewed = await _tryRefresh();
        if (renewed) {
            // Reintentar la petición original con el nuevo accessToken
            const retry = await _fetchWithAuth(path, options);
            return _parseResponse(retry);
        }
    }

    return _parseResponse(response);
}

async function _fetchWithAuth(path, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {})
    };
    if (_accessToken) {
        headers['Authorization'] = `Bearer ${_accessToken}`;
    }
    return fetch(`${API_BASE}${path}`, { ...options, headers });
}

async function _parseResponse(response) {
    if (response.status === 401) {
        clearToken();
        throw new Error('Sesión expirada. Por favor inicia sesión nuevamente.');
    }
    if (!response.ok) {
        const text = await response.text().catch(() => response.statusText);
        throw new Error(`Error ${response.status}: ${text}`);
    }
    if (response.status === 204) return null;
    return response.json();
}

/**
 * Intenta renovar el accessToken usando el refreshToken almacenado.
 * @returns {boolean} true si la renovación fue exitosa
 */
async function _tryRefresh() {
    try {
        const res = await fetch(`${API_BASE}/auth/refresh-token`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken: _refreshToken })
        });
        if (!res.ok) { clearToken(); return false; }
        const data = await res.json();
        _accessToken = data.accessToken;
        sessionStorage.setItem('jwt_token', _accessToken);
        return true;
    } catch {
        clearToken();
        return false;
    }
}

// ── AUTH ──────────────────────────────────────────────────────────────────────

/**
 * Autenticar usuario y obtener tokens JWT.
 * Llama a POST /api/auth/login en el backend (Spring Security).
 *
 * @param {string} username
 * @param {string} password
 * @returns {{ accessToken, refreshToken, username, roles, expiresIn }}
 */
export async function login(username, password) {
    const data = await apiFetch('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ username, password })
    });
    _accessToken  = data.accessToken;
    _refreshToken = data.refreshToken;
    return data;
}

export function logout() {
    clearToken();
    sessionStorage.removeItem('jwt_token');
    sessionStorage.removeItem('jwt_refresh');
}

// Restaurar sesión desde sessionStorage al cargar el módulo
const _savedAccess  = sessionStorage.getItem('jwt_token');
const _savedRefresh = sessionStorage.getItem('jwt_refresh');
if (_savedAccess)  _accessToken  = _savedAccess;
if (_savedRefresh) _refreshToken = _savedRefresh;

// ── RECETAS ───────────────────────────────────────────────────────────────────

/** Lista todas las recetas */
export async function getRecetas() {
    return apiFetch('/recetas');
}

/** Detalle de una receta por ID */
export async function getRecetaById(id) {
    return apiFetch(`/recetas/${id}`);
}

/** Recetas marcadas como populares */
export async function getRecetasPopulares() {
    return apiFetch('/recetas/populares');
}

/** Recetas más recientes (últimas 6) */
export async function getRecetasRecientes() {
    return apiFetch('/recetas/recientes');
}

/**
 * Buscar recetas con filtros opcionales.
 * Todos los parámetros son opcionales.
 *
 * @param {{ nombre?, tipoCocina?, paisOrigen?, dificultad?, ingrediente? }} filtros
 * @returns {Promise<RecetaEntity[]>}
 */
export async function buscarRecetas({
    nombre,
    tipoCocina,
    paisOrigen,
    dificultad,
    ingrediente
} = {}) {
    const params = new URLSearchParams();
    if (nombre)      params.set('nombre',      nombre);
    if (tipoCocina)  params.set('tipoCocina',  tipoCocina);
    if (paisOrigen)  params.set('paisOrigen',  paisOrigen);
    if (dificultad)  params.set('dificultad',  dificultad);
    if (ingrediente) params.set('ingrediente', ingrediente);
    const query = params.toString();
    return apiFetch(`/recetas/buscar${query ? '?' + query : ''}`);
}

// ── USUARIO AUTENTICADO ───────────────────────────────────────────────────────

/**
 * Obtiene los datos del usuario actualmente autenticado.
 * Requiere token JWT válido.
 * @returns {{ username, roles }}
 */
export async function getMe() {
    return apiFetch('/me');
}
