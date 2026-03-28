-- Insercion usuarios iniciales
USE recetasdb;
INSERT INTO usuarios (username, password, roles) VALUES ('usuario', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_USER'), ('chef', '$2a$10$8K1p/a0dR1xqM8K3sFjnMOtbSMnbzJxf8kZ6MNPtfBnq7W3Zi.K6G', 'ROLE_USER,ROLE_CHEF'), ('admin', '$2a$10$oI4Y3sqpFDROFj1xL5d4.uJhqYOxMGZMmvKj8CqW.RZsF3z3mhpOi', 'ROLE_USER,ROLE_ADMIN');
