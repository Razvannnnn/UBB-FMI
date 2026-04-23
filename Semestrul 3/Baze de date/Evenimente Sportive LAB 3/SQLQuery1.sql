CREATE DATABASE EvenimenteSportive4;
GO
USE EvenimenteSportive4;
GO

CREATE TABLE Sporturi (
id_sport INT PRIMARY KEY IDENTITY,
nume VARCHAR(100) NOT NULL, 
descriere VARCHAR(200)
);

CREATE TABLE Locatii (
id_locatie INT PRIMARY KEY IDENTITY,
nume VARCHAR(100) NOT NULL,
adresa VARCHAR(200),
capacitate INT NOT NULL,
contact VARCHAR(200)
);

CREATE TABLE Antrenori (
id_antrenor INT PRIMARY KEY IDENTITY,
nume VARCHAR(100) NOT NULL,
prenume VARCHAR(100) NOT NULL,
contact VARCHAR(200)
);

CREATE TABLE Sponsori (
id_sponsor INT PRIMARY KEY IDENTITY,
nume VARCHAR(100) NOT NULL, 
contact VARCHAR(200),
nivel NVARCHAR(100)
);

CREATE TABLE Echipe (
id_echipa INT PRIMARY KEY IDENTITY,
nume VARCHAR(100) NOT NULL,
tara VARCHAR(100),
id_antrenor INT NOT NULL,
id_sport INT NOT NULL,
FOREIGN KEY (id_antrenor) REFERENCES Antrenori(id_antrenor),
FOREIGN KEY (id_sport) REFERENCES Sporturi(id_sport)
);

CREATE TABLE Atleti (
id_atlet INT PRIMARY KEY IDENTITY,
nume VARCHAR(100) NOT NULL,
prenume VARCHAR(100) NOT NULL,
nationalitate VARCHAR(100),
data_nasterii DATE,
id_echipa INT NOT NULL,
FOREIGN KEY (id_echipa) REFERENCES Echipe(id_echipa)
);

CREATE TABLE Evenimente (
id_eveniment INT PRIMARY KEY IDENTITY,
nume NVARCHAR(150) NOT NULL,
data DATE NOT NULL,
descriere NVARCHAR(200),
id_locatie INT NOT NULL,
id_sport INT NOT NULL,
FOREIGN KEY (id_locatie) REFERENCES Locatii(id_locatie),
FOREIGN KEY (id_sport) REFERENCES Sporturi(id_sport)
);

CREATE TABLE Programari (
id_programare INT PRIMARY KEY IDENTITY,
start_time TIME NOT NULL,
end_time TIME NOT NULL,
descriere VARCHAR(200),
id_eveniment INT NOT NULL,
FOREIGN KEY (id_eveniment) REFERENCES Evenimente(id_eveniment)
);

CREATE TABLE Inregistrari (
id_inregistrare INT PRIMARY KEY IDENTITY,
data_inregistrare DATE NOT NULL DEFAULT GETDATE(),
id_eveniment INT NOT NULL,
id_echipa INT NULL,
id_atlet INT NULL,
FOREIGN KEY (id_eveniment) REFERENCES Evenimente(id_eveniment),
FOREIGN KEY (id_echipa) REFERENCES Echipe(id_echipa),
FOREIGN KEY (id_atlet) REFERENCES Atleti(id_atlet),
CHECK (
	(id_atlet IS NOT NULL AND id_echipa IS NULL) OR
	(id_atlet IS NULL AND id_echipa IS NOT NULL) 
	  )
);

CREATE TABLE Rezultate (
id_rezultat INT PRIMARY KEY IDENTITY,
pozitie INT NOT NULL,
scor DECIMAL(10,2),
id_eveniment INT NOT NULL,
id_atlet INT NULL,
id_echipa INT NULL,
FOREIGN KEY (id_eveniment) REFERENCES Evenimente(id_eveniment),
FOREIGN KEY (id_atlet) REFERENCES Atleti(id_atlet),
FOREIGN KEY (id_echipa) REFERENCES Echipe(id_echipa),
CHECK (
	(id_atlet IS NOT NULL AND id_echipa IS NULL) OR
	(id_atlet IS NULL AND id_echipa IS NOT NULL)
	  )
);

CREATE TABLE SponsorEvenimente (
id_sponsor INT NOT NULL,
id_eveniment INT NOT NULL,
PRIMARY KEY (id_sponsor, id_eveniment),
FOREIGN KEY (id_sponsor) REFERENCES Sponsori(id_sponsor),
FOREIGN KEY (id_eveniment) REFERENCES Evenimente(id_eveniment)
);


INSERT INTO Sporturi (nume, descriere) VALUES
('Fotbal', 'Sport de echipa cu 11 jucatori'),
('Baschet', 'Sport de echipa cu 5 jucatori'),
('Atletism', 'Sport individual'),
('Tenis', 'Sport de echipa sau individual');


INSERT INTO Locatii(nume, adresa, capacitate, contact) VALUES
('Stadion National', 'Bucuresti', 55000, '000-231-4324'),
('Arena Baschet', 'Cluj-Napoca', 8000, '000-221-3467'),
('Sala de atletism', 'Pitesti', 1500, '000-551-4236'),
('Teren de tenis', 'Constanta', 2000, '000-546-7654');

INSERT INTO Antrenori(nume, prenume, contact) VALUES
('Ionescu', 'Mihai', 'mihai.ionescu@gmail.com'),
('Popescu', 'Andrei', 'andrei.popescu@gmail.com'),
('Marinescu', 'Alina', 'alina.marinescu@gmail.com'),
('Georgescu', 'Maria', 'maria.georgescu@gmail.com');


INSERT INTO Sponsori(nume, contact, nivel) VALUES
('BMW Motors', 'contact@bmwmotors.com', 'Platinum'),
('Superbet', 'contact@superbet.com', 'Gold'),
('Bucovina', 'contact@bucovina.com', 'Silver'),
('Timisoreana', 'contact@timisoreana.com', 'Bronze');


INSERT INTO Echipe(nume, tara, id_antrenor, id_sport) VALUES
('Steaua Bucuresti', 'Romania', 1, 1),
('U-Cluj', 'Romania', 2, 2),
('Atletico Pitesti', 'Romania', 3, 3),
('Clubul Tenis CT', 'Romania', 4, 4);

INSERT INTO Atleti(nume, prenume, nationalitate, data_nasterii, id_echipa) VALUES
('Popa', 'Alexandru', 'Romania', '1998-04-10', 1),
('Ionescu', 'Cristian', 'Romania', '2000-07-22', 2),
('Stoica', 'Ana', 'Romania', '1995-11-13', 3),
('Gheorghe', 'Mihnea', 'Romania', '1997-01-05', 4),
('Lebron', 'James', 'USA', '1985-05-12', 2);


INSERT INTO Evenimente(nume, data, descriere, id_locatie, id_sport) VALUES
('Campionatul National', '2024-05-10', 'Competiție de fotbal', 1, 1),
('Turneul de Baschet', '2024-06-15', 'Competiție de baschet', 2, 2),
('Maratonul Pitesti', '2024-07-20', 'Competiție de atletism', 3, 3),
('Open-ul de Tenis', '2024-08-25', 'Competiție de tenis', 4, 4);

INSERT INTO Programari (start_time, end_time, descriere, id_eveniment) VALUES
('09:00', '11:00', 'Meci 1', 1),
('11:30', '13:30', 'Meci 2', 1),
('14:00', '16:00', 'Semifinale', 2),
('16:30', '18:30', 'Finala', 2);

INSERT INTO Inregistrari (data_inregistrare, id_eveniment, id_echipa, id_atlet) VALUES
('2024-05-01', 1, 1, NULL),
('2024-05-01', 1, NULL, 1),
('2024-06-10', 2, 2, NULL),
('2024-07-10', 3, NULL, 3);

INSERT INTO Rezultate (pozitie, scor, id_eveniment, id_atlet, id_echipa) VALUES
(1, 9.8, 1, NULL, 1),
(2, 8.6, 1, NULL, 2),
(1, 10.0, 3, 3, NULL),
(2, 9.5, 3, 4, NULL);


INSERT INTO SponsorEvenimente (id_sponsor, id_eveniment) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 3);


--1  Atletii nascuti dupa 2000-01-01
SELECT A.nume AS NumeAtlet, A.prenume AS PrenumeAtlet, A.data_nasterii, E.nume AS NumeEchipa, S.nume AS Sport
FROM Atleti A
JOIN Echipe E ON A.id_echipa = E.id_echipa
JOIN Sporturi S ON E.id_sport = S.id_sport
WHERE A.data_nasterii > '2000-01-01';

--2  Evenimente si locatii cu capacitate mai mare de 5000
SELECT E.nume AS NumeEveniment, L.nume AS NumeLocatie, S.nume AS Sport
FROM Evenimente E
JOIN Locatii L ON E.id_locatie = L.id_locatie
JOIN Sporturi S ON E.id_sport = S.id_sport
WHERE L.capacitate > 5000;

--3  Echipele ce au ca antrenor pe cineva cu numele Ionescu
SELECT Echipe.nume AS NumeEchipa, Antrenori.nume AS NumeAntrenor, Antrenori.prenume AS PrenumeAntrenor
FROM Echipe 
JOIN Antrenori ON Echipe.id_antrenor = Antrenori.id_antrenor 
JOIN Sporturi ON Echipe.id_sport = Sporturi.id_sport
WHERE Antrenori.nume = 'Ionescu';

--4  Evenimentele ce au ca sponsor BMW - m-n
SELECT E.nume AS NumeEveniment, S.nume AS NumeSponsor, L.nume AS Locatie
FROM Evenimente E
JOIN SponsorEvenimente SE ON E.id_eveniment = SE.id_eveniment
JOIN Sponsori S ON SE.id_sponsor = S.id_sponsor
JOIN Locatii L ON E.id_locatie = L.id_locatie
WHERE S.nume = 'BMW Motors';

--5  Atletii ce participa la Campionatul National - m-n
SELECT A.nume AS Nume, A.prenume AS Prenume
FROM Atleti A
JOIN Inregistrari I ON A.id_atlet = I.id_atlet
JOIN Evenimente E ON I.id_eveniment = E.id_eveniment
WHERE E.nume = 'Campionatul National';

--6  Nr atleti la fiecare echipa
SELECT E.nume AS NumeEchipa, COUNT(A.id_atlet) AS NumarAtleti, S.nume AS Sport
FROM Echipe E
JOIN Atleti A ON E.id_echipa = A.id_echipa
JOIN Sporturi S ON E.id_sport = S.id_sport
GROUP BY E.nume, S.nume;

--7 Antrenorii ce au mai mult de 1 atlet in echipa
SELECT Antrenori.nume AS NumeAntrenor, Antrenori.prenume AS PrenumeAntrenor, COUNT(A.id_atlet) AS NumarAtleti
FROM Antrenori
JOIN Echipe ON Antrenori.id_antrenor = Echipe.id_antrenor
JOIN Atleti A ON Echipe.id_echipa = A.id_echipa
GROUP BY Antrenori.nume, Antrenori.prenume
HAVING COUNT(A.id_atlet) > 1;


--8  Sporturile care au mai mult de un eveniment
SELECT S.nume AS NumeSport, COUNT(E.id_eveniment) AS NumarEvenimente 
FROM Sporturi S
JOIN Evenimente E ON S.id_sport = E.id_sport 
GROUP BY S.nume		
HAVING COUNT(E.id_eveniment) > 1;

--9  Nationalitatile distincte
SELECT DISTINCT nationalitate 
FROM Atleti;

--10  Locatiile unice
SELECT DISTINCT L.nume AS NumeLocatie 
FROM Locatii L
JOIN Evenimente E ON L.id_locatie = E.id_locatie;


---------------------------

--1  MODIFICARE TIP COLOANA
CREATE PROCEDURE do_procedura_1
AS
BEGIN
    ALTER TABLE Locatii 
    ALTER COLUMN contact NVARCHAR(300)
	PRINT 'Tipul coloanei contact a fost modificat';
END;
GO

CREATE PROCEDURE undo_procedura_1
AS
BEGIN
    ALTER TABLE Locatii 
    ALTER COLUMN contact VARCHAR(200);
	PRINT 'Coloana contact a revenit la tipul initial';
END;
GO

--2  ADAUGARE CONSTRANGERE
CREATE PROCEDURE do_procedura_2
AS
BEGIN
    ALTER TABLE Locatii
    ADD CONSTRAINT DF_Capacitate DEFAULT 1000 FOR capacitate
	PRINT 'Constrangerea a fost adaugata';
END;
GO

CREATE PROCEDURE undo_procedura_2
AS
BEGIN
    ALTER TABLE Locatii
    DROP CONSTRAINT DF_Capacitate
	PRINT 'Constrangerea a fost eliminata';
END;
GO

--3   CREARE TABELA NOUA SI STERGERE
CREATE PROCEDURE do_procedura_3 
AS
BEGIN
    CREATE TABLE DetaliiEvenimente (
        id_detaliu INT PRIMARY KEY IDENTITY,
        id_eveniment INT NOT NULL,
        descriere NVARCHAR(200),
        data_adaugare DATE DEFAULT GETDATE()
    );
	PRINT 'Noua tabela DetaliiEvenimente a fost creata';
END;
GO

CREATE PROCEDURE undo_procedura_3
AS
BEGIN
    DROP TABLE IF EXISTS DetaliiEvenimente;
	PRINT 'Tabela DetaliiEvenimente a fost eliminata';
END;
GO

--4    ADAUGARE CAMP NOU
CREATE PROCEDURE do_procedura_4 
AS
BEGIN
    ALTER TABLE DetaliiEvenimente 
    ADD organizator NVARCHAR(50);
	PRINT 'In tabela DetaliiEvenimente a fost adaugat campul organizator';
END;
GO

CREATE PROCEDURE undo_procedura_4
AS
BEGIN
    ALTER TABLE DetaliiEvenimente 
    DROP COLUMN organizator;
	PRINT 'In tabela DetaliiEvenimente a fost eliminat campul organizator';
END;
GO

--5    ADAUGARE SI STERGERE FOREIGN KEY
CREATE PROCEDURE do_procedura_5 AS
BEGIN
    ALTER TABLE DetaliiEvenimente
    ADD CONSTRAINT FK_DetaliiEveniment_Evenimente
    FOREIGN KEY (id_eveniment) REFERENCES Evenimente(id_eveniment);
	PRINT 'A fost adauga FK in tabela DetaliiEvenimnte';
END;
GO

CREATE PROCEDURE undo_procedura_5 AS
BEGIN
    ALTER TABLE DetaliiEvenimente
    DROP CONSTRAINT FK_DetaliiEveniment_Evenimente;
	PRINT 'A fost eliminat FK in tabela DetaliiEvenimnte';
END;
GO

------ MAIN 
DROP TABLE IF EXISTS VersiuneDB
CREATE TABLE VersiuneDB (
	versiuneNr int
);

INSERT INTO VersiuneDB VALUES(0)


SELECT * FROM VersiuneDB

GO
CREATE OR ALTER PROCEDURE main @versiune INT
AS
BEGIN
		
		IF @versiune < 0 OR @versiune > 5
		BEGIN
			PRINT 'Versiune invalida!'
			RETURN
		END

		DECLARE @versiune_curenta AS INT
		SET @versiune_curenta = (SELECT versiuneNr FROM VersiuneDB)

		IF @versiune = @versiune_curenta
		BEGIN
			PRINT 'Versiunea este deja cea curenta!'
			RETURN
		END

		DECLARE @proc VARCHAR(20)
		DECLARE @proc_undo VARCHAR(20)

		DECLARE @ok AS INT
		SET @ok = 0

		DELETE FROM VersiuneDB
		INSERT INTO VersiuneDB(versiuneNr) VALUES (@versiune)

		WHILE(@versiune_curenta < @versiune)
			BEGIN
				SET @ok = @ok + 1
				SET @versiune_curenta = @versiune_curenta + 1
				SET @proc = 'do_procedura_' + CAST(@versiune_curenta AS VARCHAR(10))
				PRINT 'Se executa ' + @proc
				EXEC @proc
			END

		IF(@ok > 0)
		BEGIN
			RETURN
		END

		WHILE(@versiune_curenta > @versiune)
			BEGIN
				SET @proc_undo = 'undo_procedura_' + CAST(@versiune_curenta AS VARCHAR(10))
				PRINT 'Se executa ' + @proc_undo
				EXEC @proc_undo
				SET @versiune_curenta = @versiune_curenta - 1
			END

END;
GO


exec main 0
exec main 1
exec main 2
exec main 3
exec main 4
exec main 5
exec main 1000










