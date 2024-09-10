
INSERT INTO categoria (nome_categoria, descricao_categoria, taxa, desconto)
VALUES ('Eletrônicosos teste', 'Produtos eletrônicos teste', 20, 0),
       ('Alimentos  teste', 'Produtos alimentícios  teste', 5, 0),
       ('Vestuário  teste', 'Roupas e acessórios  teste', 0, 10);

INSERT INTO produto (nome_produto, descricao_produto, preco_base, data_cadastro, categoria_id)
VALUES ('Smartphonees teste', 'Smartphone com 64GB de armazenamento  teste', 299.99, CURRENT_DATE, 1),
       ('Arroz  teste', 'Arroz branco de 5kg  teste', 15.99, '2024-05-30', 2),
       ('Camisa Polo  teste', 'Camisa polo masculina  teste', 39.99, '2024-06-28', 3);