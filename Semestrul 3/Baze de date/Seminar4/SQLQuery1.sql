CREATE DATABASE Seminar
USE Seminar
GO

CREATE TABLE Sectiuni (
	cod_s INT PRIMARY KEY IDENTITY,
	nume VARCHAR(100),
	descriere VARCHAR(200)
)

CREATE TABLE Atractii (
	cod_a INT PRIMARY KEY IDENTITY,
	nume VARCHAR(100),
	descriere VARCHAR(200),
	varsta_min INT NOT NULL,
	cod_s INT,
	FOREIGN KEY (cod_s) REFERENCES Sectiuni(cod_s) ON DELETE CASCADE

);

CREATE TABLE Categorii (
	cod_c INT PRIMARY KEY IDENTITY,
	nume VARCHAR(100)
)

CREATE TABLE Vizitatori (
	cod_v INT PRIMARY KEY IDENTITY,
	nume VARCHAR(100),
	email NVARCHAR(100),
	cod_c INT,
	FOREIGN KEY(cod_c) REFERENCES Categorii(cod_c) ON DELETE CASCADE

)

CREATE TABLE Note (
	cod_a INT,
	cod_v INT,
	PRIMARY KEY(cod_a, cod_v),
	FOREIGN KEY(cod_a) REFERENCES Atractii(cod_a) ON DELETE CASCADE,
	FOREIGN KEY(cod_v) REFERENCES Vizitatori(cod_v) ON DELETE CASCADE
)

ALTER TABLE Note
ADD nota INT

--POPULARE TABELE

INSERT INTO Sectiuni (nume, descriere) VALUES
('Aventura', 'Activitati extreme'),
('Relaxare', 'Zona linistita'),
('Copii', 'Jocuri si distractii'),
('Cultura', 'Expozitii si muzee');

INSERT INTO Atractii (nume, descriere, varsta_min, cod_s) VALUES
('Tir', 'Tir cu arcul', 12, 1),
('Plimbare', 'Zona verde', 0, 2),
('Tobogan', 'Distractie acvatica', 5, 3),
('Expozitie', 'Picturi celebre', 10, 4);

INSERT INTO Categorii (nume) VALUES
('Adult'),
('Familie'),
('Copii');

INSERT INTO Vizitatori (nume, email, cod_c) VALUES
('Ion Popescu', 'ion.popescu@email.com', 1),
('Maria Ionescu', 'maria.ionescu@email.com', 2),
('Andrei Vasile', 'andrei.vasile@email.com', 3),
('Ana Mihai', 'ana.mihai@email.com', 3);

INSERT INTO Note (cod_a, cod_v, nota) VALUES
(1, 1, 9),
(2, 2, 8),
(3, 3, 10),
(4, 4, 7);

--FUNCTII

CREATE FUNCTION ExistaCategorie(@nume VARCHAR(100))
RETURNS BIT AS
BEGIN
IF(EXISTS(SELECT * FROM Categorii WHERE nume=@nume))
	RETURN 1;
RETURN 0;
END;
GO

PRINT dbo.ExistaCategorie('Adult')


CREATE FUNCTION Prob1(@nume VARCHAR(100))
RETURNS INT AS
BEGIN
	DECLARE @cod INT
	IF(EXISTS(SELECT cod_c FROM Categorii WHERE nume=@nume))
		SELECT @cod = cod_c
		FROM Categorii
		WHERE nume=@nume

		RETURN @cod
	RETURN 0;
END;
GO

PRINT dbo.Prob1('Adult')

DROP FUNCTION dbo.Prob1




CREATE FUNCTION NoteAtractii(@email NVARCHAR(200))
RETURNS @NoteAtractii TABLE (atractie VARCHAR(100), email NVARCHAR(200), nota REAL, tip_evaluare VARCHAR(10)) AS
BEGIN
	INSERT INTO @NoteAtractii (atractie, nota, email)
	SELECT A.nume, N.nota, V.email FROM Atractii A
	INNER JOIN Note N ON A.cod_a=N.cod_a
	INNER JOIN Vizitatori V on V.cod_c=N.cod_v
	WHERE email=@email;
	UPDATE @NoteAtractii SET tip_evaluare='pozitiva' WHERE nota >= 5.0;
	UPDATE @NoteAtractii SET tip_evaluare='negativa' WHERE nota < 5.0;
	RETURN;
END;
GO

--dbo.NoteAtractii('ion.popescu@email.com');



CREATE FUNCTION CodCategorie(@nume VARCHAR(70))
RETURNS INT AS
BEGIN 
		DECLARE @cod_c INT;
		SELECT @cod_c = cod_c
		FROM Categorii
		WHERE nume = @nume
 
		if @cod_c IS NULL
			Return 0 
		Return @cod_c
END
GO
 
PRINT dbo.CodCategorie('cevaNeexistent');


CREATE TRIGGER EliminareCategorie
ON Categorii
INSTEAD OF DELETE
AS
BEGIN
	RAISERROR( 'Operatie interzisa', 16, 1)
END
GO

CREATE VIEW vw_PensionariCopii
AS
SELECT C.cod_c, C.nume
FROM Categorii C
WHERE C.nume = 'pensionari' OR C.nume = 'copii';
GO

CREATE VIEW vw_IncepeCuC
AS
SELECT S.nume FROM Sectiuni S WHERE nume like 'C%' 
GO

CREATE FUNCTION Prob5(@char VARCHAR(1))
RETURNS TABLE
AS
RETURN 
(
	SELECT *
	FROM Sectiuni
	WHERE LEN(nume) >= 2 AND nume like '%_'+@char
)
GO

CREATE VIEW vw_Prob6
AS
SELECT 
	Vizitatori.nume AS Nume,
	Note.nota AS Nota,
	Atractii.varsta_min AS Varsta
	INNER JOIN Atractii A