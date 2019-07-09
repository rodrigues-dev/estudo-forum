INSERT INTO USUARIO(nome, email, senha) VALUES('Aluno','aluno@email.com', '123456');

INSERT INTO CURSO(nome, categoria) VALUES('Spring Boot', 'Programação');
INSERT INTO CURSO(nome, categoria) VALUES('HTML 5', 'Front-end');

INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida', 'Erro ao criar o projeto', '2019-05-05T18:00:00','NAO_RESPONDIDO','1','1');
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 2', 'Erro no POM', '2018-05-05T18:00:00','NAO_RESPONDIDO','1','1');
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 3', 'Erro na tag HTML 5', '2017-05-05T18:00:00','NAO_RESPONDIDO','1','2');

