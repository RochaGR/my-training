-- NÃO use IDs explícitos - deixe o banco gerar automaticamente
INSERT INTO role (nome) VALUES ('ROLE_ADMIN');
INSERT INTO role (nome) VALUES ('ROLE_USER');

-- Inserir usuários de teste SEM IDs
INSERT INTO usuario (nome, email, senha) VALUES
                                             ('Admin Test', 'admin@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
                                             ('User Test', 'user@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
                                             ('João Silva', 'joao@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
                                             ('Maria Santos', 'maria@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
                                             ('Pedro Costa', 'pedro@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Associar roles aos usuários (use subqueries para encontrar os IDs)
INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuario u, role r WHERE u.email = 'admin@test.com' AND r.nome = 'ROLE_ADMIN';

INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuario u, role r WHERE u.email = 'admin@test.com' AND r.nome = 'ROLE_USER';

INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuario u, role r WHERE u.email = 'user@test.com' AND r.nome = 'ROLE_USER';

INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuario u, role r WHERE u.email = 'joao@test.com' AND r.nome = 'ROLE_USER';

INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuario u, role r WHERE u.email = 'maria@test.com' AND r.nome = 'ROLE_USER';

INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuario u, role r WHERE u.email = 'pedro@test.com' AND r.nome = 'ROLE_USER';

-- Inserir treinos de teste (usando subqueries para usuários)
INSERT INTO treino (data_hora, tipo, duracao_min, observacoes, distancia_km, usuario_id)
SELECT '2024-01-15 07:00:00', 'CORRIDA', 30, 'Treino matinal leve', 5.0, u.id
FROM usuario u WHERE u.email = 'user@test.com';

INSERT INTO treino (data_hora, tipo, duracao_min, observacoes, distancia_km, usuario_id)
SELECT '2024-01-16 18:00:00', 'MUSCULACAO', 60, 'Treino de pernas', NULL, u.id
FROM usuario u WHERE u.email = 'user@test.com';

INSERT INTO treino (data_hora, tipo, duracao_min, observacoes, distancia_km, usuario_id)
SELECT '2024-01-17 07:30:00', 'CICLISMO', 45, 'Treino intervalado', 15.0, u.id
FROM usuario u WHERE u.email = 'user@test.com';

INSERT INTO treino (data_hora, tipo, duracao_min, observacoes, distancia_km, usuario_id)
SELECT '2024-01-18 19:00:00', 'CORRIDA', 40, 'Treino de velocidade', 8.0, u.id
FROM usuario u WHERE u.email = 'joao@test.com';

INSERT INTO treino (data_hora, tipo, duracao_min, observacoes, distancia_km, usuario_id)
SELECT '2024-01-19 08:00:00', 'MUSCULACAO', 50, 'Treino de superiores', NULL, u.id
FROM usuario u WHERE u.email = 'joao@test.com';

INSERT INTO treino (data_hora, tipo, duracao_min, observacoes, distancia_km, usuario_id)
SELECT '2024-01-20 07:00:00', 'CORRIDA', 35, 'Corrida regenerativa', 6.0, u.id
FROM usuario u WHERE u.email = 'maria@test.com';

INSERT INTO treino (data_hora, tipo, duracao_min, observacoes, distancia_km, usuario_id)
SELECT '2024-01-21 17:00:00', 'CICLISMO', 60, 'Treino de resistência', 20.0, u.id
FROM usuario u WHERE u.email = 'maria@test.com';

INSERT INTO treino (data_hora, tipo, duracao_min, observacoes, distancia_km, usuario_id)
SELECT '2024-01-22 06:30:00', 'CORRIDA', 50, 'Long run', 12.0, u.id
FROM usuario u WHERE u.email = 'pedro@test.com';

-- Inserir exercícios de teste (usando subqueries para treinos)
INSERT INTO exercicio (nome, series, repeticoes, carga_kg, observacoes, treino_id)
SELECT 'Agachamento', 4, 12, 80.0, 'Descer até paralelo', t.id
FROM treino t
WHERE t.observacoes = 'Treino de pernas' AND t.duracao_min = 60;

INSERT INTO exercicio (nome, series, repeticoes, carga_kg, observacoes, treino_id)
SELECT 'Leg Press', 3, 15, 150.0, 'Amplitude completa', t.id
FROM treino t
WHERE t.observacoes = 'Treino de pernas' AND t.duracao_min = 60;

INSERT INTO exercicio (nome, series, repeticoes, carga_kg, observacoes, treino_id)
SELECT 'Extensora', 3, 12, 40.0, 'Controlar a descida', t.id
FROM treino t
WHERE t.observacoes = 'Treino de pernas' AND t.duracao_min = 60;

INSERT INTO exercicio (nome, series, repeticoes, carga_kg, observacoes, treino_id)
SELECT 'Supino Reto', 4, 10, 60.0, 'Barra até o peito', t.id
FROM treino t
WHERE t.observacoes = 'Treino de superiores' AND t.duracao_min = 50;

INSERT INTO exercicio (nome, series, repeticoes, carga_kg, observacoes, treino_id)
SELECT 'Desenvolvimento', 3, 12, 30.0, 'Halteres', t.id
FROM treino t
WHERE t.observacoes = 'Treino de superiores' AND t.duracao_min = 50;

INSERT INTO exercicio (nome, series, repeticoes, carga_kg, observacoes, treino_id)
SELECT 'Remada Curvada', 4, 10, 50.0, 'Costas retas', t.id
FROM treino t
WHERE t.observacoes = 'Treino de superiores' AND t.duracao_min = 50;

-- Inserir desafios de teste (usando subqueries para usuários)
INSERT INTO desafio (titulo, descricao, data_inicio, data_fim, objetivo_valor, progresso_atual, unidade, status, usuario_id)
SELECT 'Desafio 100km', 'Correr 100km em janeiro', '2024-01-01', '2024-01-31', 100.0, 45.0, 'KM', 'PENDENTE', u.id
FROM usuario u WHERE u.email = 'user@test.com';

INSERT INTO desafio (titulo, descricao, data_inicio, data_fim, objetivo_valor, progresso_atual, unidade, status, usuario_id)
SELECT 'Treinar 20 dias', 'Treinar pelo menos 20 dias no mês', '2024-01-01', '2024-01-31', 20.0, 12.0, 'REPETICOES', 'PENDENTE', u.id
FROM usuario u WHERE u.email = 'user@test.com';

INSERT INTO desafio (titulo, descricao, data_inicio, data_fim, objetivo_valor, progresso_atual, unidade, status, usuario_id)
SELECT 'Desafio 50km Ciclismo', 'Pedalar 50km em uma semana', '2024-01-15', '2024-01-21', 50.0, 50.0, 'KM', 'CONCLUIDO', u.id
FROM usuario u WHERE u.email = 'joao@test.com';


INSERT INTO desafio (titulo, descricao, data_inicio, data_fim, objetivo_valor, progresso_atual, unidade, status, usuario_id)
SELECT 'Musculação 3x semana', 'Treinar musculação 12 vezes no mês', '2024-01-01', '2024-01-31', 12.0, 8.0, 'REPETICOES', 'PENDENTE', u.id
FROM usuario u WHERE u.email = 'maria@test.com';

INSERT INTO desafio (titulo, descricao, data_inicio, data_fim, objetivo_valor, progresso_atual, unidade, status, usuario_id)
SELECT 'Desafio Cancelado', 'Este desafio foi cancelado', '2024-01-01', '2024-01-15', 30.0, 5.0, 'KM', 'CANCELADO', u.id
FROM usuario u WHERE u.email = 'pedro@test.com';