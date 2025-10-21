INSERT INTO role (nome) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER');

INSERT INTO usuario (nome, email, senha) VALUES
    ('Administrador', 'admin@mytraining.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id
FROM usuario u, role r
WHERE u.email = 'admin@mytraining.com'
AND r.nome = 'ROLE_ADMIN';