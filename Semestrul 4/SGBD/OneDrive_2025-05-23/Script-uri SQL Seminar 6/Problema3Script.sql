USE master
GO
IF(EXISTS(SELECT * FROM sys.databases WHERE name='Problema3'))
	DROP DATABASE Problema3;
GO
CREATE DATABASE Problema3;
GO
USE Problema3;
GO
CREATE TABLE Producatori
(cod_p INT PRIMARY KEY IDENTITY,
nume_p VARCHAR(100),
website VARCHAR(100)
);
CREATE TABLE Biscuiti
(cod_b INT PRIMARY KEY IDENTITY,
nume_b VARCHAR(100),
nr_calorii INT,
pret REAL,
cod_p INT FOREIGN KEY REFERENCES Producatori(cod_p)
ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE Tari
(cod_t INT PRIMARY KEY IDENTITY,
nume_t VARCHAR(100)
);
CREATE TABLE Clienti
(cod_c INT PRIMARY KEY IDENTITY,
nume_c VARCHAR(100),
cod_t INT FOREIGN KEY REFERENCES Tari(cod_t)
ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE Note
(cod_b INT FOREIGN KEY REFERENCES Biscuiti(cod_b)
ON UPDATE CASCADE ON DELETE CASCADE,
cod_c INT FOREIGN KEY REFERENCES Clienti(cod_c)
ON UPDATE CASCADE ON DELETE CASCADE,
nota INT,
CONSTRAINT pk_Note PRIMARY KEY (cod_b, cod_c)
);
--Producatori
INSERT INTO Producatori (nume_p, website) VALUES 
('Milka','www.milka.com');
INSERT INTO Producatori (nume_p, website) VALUES
('Belvita','www.belvita.ro');
INSERT INTO Producatori (nume_p, website) VALUES
('Petit Beurre','www.petitbeurre.com');
--Biscuiti Milka
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti1 Milka',100,5,1);
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti2 Milka',120,7,1);
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti3 Milka',200,12,1);
--Biscuiti Belvita
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti4 Belvita',70,5,2);
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti5 Belvita',56,7,2);
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti6 Belvita',101,9,2);
--Biscuiti Petit Beurre
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti7 Petit Beurre',80,5,3);
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti8 Petit Beurre',95,8,3);
INSERT INTO Biscuiti (nume_b,nr_calorii,pret,cod_p) VALUES
('biscuiti9 Petit Beurre',89,2,3);
--Tari
INSERT INTO Tari (nume_t) VALUES ('Romania');
INSERT INTO Tari (nume_t) VALUES ('Spania');
INSERT INTO Tari (nume_t) VALUES ('Portugalia');
--Clienti
INSERT INTO Clienti (nume_c, cod_t) VALUES ('client1',1);
INSERT INTO Clienti (nume_c, cod_t) VALUES ('client2',2);
INSERT INTO Clienti (nume_c, cod_t) VALUES ('client3',3);
--Note
INSERT INTO Note (cod_b, cod_c, nota) VALUES (1,1,10);
INSERT INTO Note (cod_b, cod_c, nota) VALUES (2,2,8);
INSERT INTO Note (cod_b, cod_c, nota) VALUES (3,3,9);
