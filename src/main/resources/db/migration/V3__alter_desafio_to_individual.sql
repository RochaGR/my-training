-- Remove a tabela de relacionamento ManyToMany
DROP TABLE IF EXISTS usuario_desafio;

-- Adiciona coluna usuario_id na tabela desafio
ALTER TABLE desafio ADD COLUMN usuario_id BIGINT;

-- Define a coluna como NOT NULL após migrar os dados existentes
-- Primeiro, vamos associar os desafios existentes ao primeiro usuário (se existir)
UPDATE desafio SET usuario_id = (SELECT id FROM usuario LIMIT 1) WHERE usuario_id IS NULL;

-- Agora define como NOT NULL e adiciona a foreign key
ALTER TABLE desafio ALTER COLUMN usuario_id SET NOT NULL;
ALTER TABLE desafio ADD CONSTRAINT fk_desafio_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE;

-- Adiciona índice para melhor performance
CREATE INDEX idx_desafio_usuario ON desafio(usuario_id);

