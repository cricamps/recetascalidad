/**
 * api.test.js — Pruebas unitarias del módulo api.js
 *
 * Estas pruebas verifican la lógica del frontend de forma aislada,
 * sin depender del backend real. Se usa Jest con jsdom y fetch simulado.
 *
 * Cobertura: gestión de tokens JWT, construcción de requests HTTP,
 * manejo de errores, renovación automática (refresh token).
 */

import {
    login,
    logout,
    getToken,
    setToken,
    clearToken,
    isLoggedIn,
    getRecetas,
    getRecetaById,
    buscarRecetas,
    getMe
} from '../api.js';

// ── Setup: mock global de fetch ───────────────────────────────────────────────
beforeEach(() => {
    jest.clearAllMocks();
    clearToken();
    sessionStorage.clear();
    global.fetch = jest.fn();
});

// ── Helpers ───────────────────────────────────────────────────────────────────
function mockFetch(status, body) {
    global.fetch = jest.fn().mockResolvedValue({
        status,
        ok: status >= 200 && status < 300,
        json:  () => Promise.resolve(body),
        text:  () => Promise.resolve(JSON.stringify(body)),
        statusText: 'Error'
    });
}

function mockFetchSequence(...responses) {
    global.fetch = jest.fn();
    responses.forEach(r => {
        global.fetch.mockResolvedValueOnce({
            status: r.status,
            ok: r.status >= 200 && r.status < 300,
            json:  () => Promise.resolve(r.body),
            text:  () => Promise.resolve(JSON.stringify(r.body)),
            statusText: 'Error'
        });
    });
}

// ═══════════════════════════════════════════════════════════════════════════════
// 1. GESTIÓN DE TOKENS
// ═══════════════════════════════════════════════════════════════════════════════

describe('Gestión de tokens JWT', () => {

    test('isLoggedIn() retorna false cuando no hay token', () => {
        expect(isLoggedIn()).toBe(false);
    });

    test('setToken() activa isLoggedIn()', () => {
        setToken('mi.token.jwt');
        expect(isLoggedIn()).toBe(true);
        expect(getToken()).toBe('mi.token.jwt');
    });

    test('clearToken() limpia el token', () => {
        setToken('token123');
        clearToken();
        expect(isLoggedIn()).toBe(false);
        expect(getToken()).toBeNull();
    });

    test('logout() limpia tokens y sessionStorage', () => {
        setToken('token123');
        sessionStorage.setItem('jwt_token', 'token123');
        logout();
        expect(isLoggedIn()).toBe(false);
        expect(sessionStorage.getItem('jwt_token')).toBeNull();
    });
});

// ═══════════════════════════════════════════════════════════════════════════════
// 2. LOGIN
// ═══════════════════════════════════════════════════════════════════════════════

describe('login()', () => {

    test('login exitoso guarda accessToken y retorna datos', async () => {
        mockFetch(200, {
            accessToken: 'access.jwt.token',
            refreshToken: 'refresh.jwt.token',
            username: 'admin',
            roles: '[ROLE_USER, ROLE_ADMIN]'
        });

        const data = await login('admin', 'admin123');

        expect(data.accessToken).toBe('access.jwt.token');
        expect(data.username).toBe('admin');
        expect(isLoggedIn()).toBe(true);
        expect(getToken()).toBe('access.jwt.token');
    });

    test('login llama al endpoint /auth/login con método POST', async () => {
        mockFetch(200, { accessToken: 'tok', refreshToken: 'ref', username: 'u', roles: '' });

        await login('usuario', 'pass123');

        expect(fetch).toHaveBeenCalledWith(
            expect.stringContaining('/auth/login'),
            expect.objectContaining({ method: 'POST' })
        );
    });

    test('login envía username y password en el body JSON', async () => {
        mockFetch(200, { accessToken: 'tok', refreshToken: 'ref', username: 'u', roles: '' });

        await login('testuser', 'testpass');

        const body = JSON.parse(fetch.mock.calls[0][1].body);
        expect(body.username).toBe('testuser');
        expect(body.password).toBe('testpass');
    });

    test('login con credenciales incorrectas lanza error y no activa sesión', async () => {
        mockFetch(401, { error: 'Credenciales incorrectas' });

        await expect(login('admin', 'wrong')).rejects.toThrow();
        expect(isLoggedIn()).toBe(false);
    });
});

// ═══════════════════════════════════════════════════════════════════════════════
// 3. CABECERA JWT EN PETICIONES AUTENTICADAS
// ═══════════════════════════════════════════════════════════════════════════════

describe('Cabecera JWT automática', () => {

    test('petición con token incluye Authorization: Bearer', async () => {
        setToken('mi.token.valido');
        mockFetch(200, []);

        await getRecetas();

        const headers = fetch.mock.calls[0][1].headers;
        expect(headers['Authorization']).toBe('Bearer mi.token.valido');
    });

    test('petición sin token no incluye cabecera Authorization', async () => {
        clearToken();
        mockFetch(200, []);

        await getRecetas();

        const headers = fetch.mock.calls[0][1].headers;
        expect(headers['Authorization']).toBeUndefined();
    });
});

// ═══════════════════════════════════════════════════════════════════════════════
// 4. ENDPOINTS DE RECETAS
// ═══════════════════════════════════════════════════════════════════════════════

describe('getRecetaById()', () => {

    test('llama al endpoint /recetas/{id} correcto', async () => {
        setToken('token');
        mockFetch(200, { id: 1, nombre: 'Cazuela' });

        const receta = await getRecetaById(1);

        expect(receta.nombre).toBe('Cazuela');
        expect(fetch).toHaveBeenCalledWith(
            expect.stringContaining('/recetas/1'),
            expect.any(Object)
        );
    });
});

describe('buscarRecetas()', () => {

    test('sin filtros llama a /recetas/buscar', async () => {
        setToken('token');
        mockFetch(200, []);

        await buscarRecetas();

        expect(fetch).toHaveBeenCalledWith(
            expect.stringContaining('/recetas/buscar'),
            expect.any(Object)
        );
    });

    test('con filtros incluye parámetros en la URL', async () => {
        setToken('token');
        mockFetch(200, []);

        await buscarRecetas({ nombre: 'cazuela', tipoCocina: 'Tradicional' });

        const url = fetch.mock.calls[0][0];
        expect(url).toContain('nombre=cazuela');
        expect(url).toContain('tipoCocina=Tradicional');
    });

    test('filtros undefined no aparecen en la URL', async () => {
        setToken('token');
        mockFetch(200, []);

        await buscarRecetas({ nombre: 'sopa', paisOrigen: undefined });

        const url = fetch.mock.calls[0][0];
        expect(url).toContain('nombre=sopa');
        expect(url).not.toContain('paisOrigen');
    });
});

// ═══════════════════════════════════════════════════════════════════════════════
// 5. MANEJO DE ERRORES HTTP
// ═══════════════════════════════════════════════════════════════════════════════

describe('Manejo de errores HTTP', () => {

    test('respuesta 404 lanza error con código de estado', async () => {
        setToken('token');
        mockFetch(404, { message: 'Not found' });

        await expect(getRecetaById(999)).rejects.toThrow('404');
    });

    test('respuesta 500 lanza error con código de estado', async () => {
        setToken('token');
        mockFetch(500, { message: 'Internal server error' });

        await expect(getRecetas()).rejects.toThrow('500');
    });

    test('respuesta 401 sin refresh token lanza error de sesión expirada', async () => {
        setToken('token.expirado');
        global.fetch = jest.fn().mockResolvedValue({
            status: 401,
            ok: false,
            json: () => Promise.resolve({ error: 'Unauthorized' }),
            text: () => Promise.resolve('Unauthorized'),
            statusText: 'Unauthorized'
        });

        await expect(getMe()).rejects.toThrow('Sesión expirada');
        expect(isLoggedIn()).toBe(false);
    });
});

// ═══════════════════════════════════════════════════════════════════════════════
// 6. RENOVACIÓN AUTOMÁTICA DE TOKEN (REFRESH)
// ═══════════════════════════════════════════════════════════════════════════════

describe('Renovación automática de JWT', () => {

    test('401 con refreshToken válido renueva y reintenta la petición', async () => {
        setToken('token.expirado');
        sessionStorage.setItem('jwt_refresh', 'refresh.valido');

        // Secuencia: 1) 401 original, 2) refresh OK, 3) reintento OK
        mockFetchSequence(
            { status: 401, body: { error: 'Unauthorized' } },
            { status: 200, body: { accessToken: 'token.nuevo', username: 'admin' } },
            { status: 200, body: [{ id: 1, nombre: 'Cazuela' }] }
        );

        const result = await getRecetas();

        expect(fetch).toHaveBeenCalledTimes(3);
        expect(result).toHaveLength(1);
        expect(result[0].nombre).toBe('Cazuela');
    });
});
