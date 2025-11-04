
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, role_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

CREATE TABLE desafio (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_inicio DATE,
    data_fim DATE,
    objetivo_valor DOUBLE PRECISION,
    progresso_atual DOUBLE PRECISION DEFAULT 0,
    unidade VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE usuario_desafio (
    usuario_id BIGINT NOT NULL,
    desafio_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, desafio_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (desafio_id) REFERENCES desafio(id) ON DELETE CASCADE
);

CREATE TABLE treino (
    id BIGSERIAL PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    duracao_min INTEGER,
    observacoes TEXT,
    distancia_km DOUBLE PRECISION,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE exercicio (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    series INTEGER,
    repeticoes INTEGER,
    carga_kg DOUBLE PRECISION,
    observacoes VARCHAR(255),
    treino_id BIGINT NOT NULL,
    FOREIGN KEY (treino_id) REFERENCES treino(id) ON DELETE CASCADE
);

CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_treino_usuario ON treino(usuario_id);
CREATE INDEX idx_treino_data ON treino(data_hora);
CREATE INDEX idx_exercicio_treino ON exercicio(treino_id);
CREATE INDEX idx_desafio_status ON desafio(status);