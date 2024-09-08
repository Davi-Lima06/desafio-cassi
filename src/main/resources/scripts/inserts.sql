
INSERT INTO categoria (nome_categoria, descricao_categoria, taxa, desconto)
VALUES ('Eletrônicos', 'Produtos eletrônicos', 20, 0),
       ('Alimentos', 'Produtos alimentícios', 5, 0),
       ('Vestuário', 'Roupas e acessórios', 0, 10);

INSERT INTO produto (nome_produto, descricao_produto, preco_base, data_cadastro, categoria_id)
VALUES ('Smartphone', 'Smartphone com 64GB de armazenamento', 299.99, CURRENT_DATE, 1),
       ('Arroz', 'Arroz branco de 5kg', 15.99, '2024-05-30', 2),
       ('Camisa Polo', 'Camisa polo masculina', 39.99, '2024-06-28', 3);