
CREATE TABLE categoria (
                           id_categoria BIGINT PRIMARY KEY AUTO_INCREMENT,
                           nome_categoria VARCHAR(255) NOT NULL,
                           descricao_categoria VARCHAR(255),
                           taxa DECIMAL(10, 2),
                           desconto DECIMAL(10, 2)
);

CREATE TABLE produto (
                         id_produto BIGINT PRIMARY KEY AUTO_INCREMENT,
                         nome_produto VARCHAR(255) NOT NULL,
                         descricao_produto VARCHAR(255),
                         preco_base DECIMAL(10, 2) NOT NULL,
                         data_cadastro DATE NOT NULL,
                         categoria_id BIGINT,
                         CONSTRAINT fk_categoria
                             FOREIGN KEY (categoria_id) REFERENCES categoria(id_categoria)
);