CREATE DATABASE EvenimenteSportiveLAB4;
GO
USE EvenimenteSportiveLAB4;
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
ON DELETE CASCADE;
);

CREATE TABLE Atleti (
id_atlet INT PRIMARY KEY IDENTITY,
nume VARCHAR(100) NOT NULL,
prenume VARCHAR(100) NOT NULL,
nationalitate VARCHAR(100),
data_nasterii DATE,
id_echipa INT NOT NULL,
FOREIGN KEY (id_echipa) REFERENCES Echipe(id_echipa)
ON DELETE CASCADE;
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
ON DELETE CASCADE;
);

CREATE TABLE Programari (
id_programare INT PRIMARY KEY IDENTITY,
start_time TIME NOT NULL,
end_time TIME NOT NULL,
descriere VARCHAR(200),
id_eveniment INT NOT NULL,
FOREIGN KEY (id_eveniment) REFERENCES Evenimente(id_eveniment)
ON DELETE CASCADE;
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
ON DELETE CASCADE;
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

--------------------------------------------------

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunTables_Tables]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestRunTables] DROP CONSTRAINT FK_TestRunTables_Tables
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestTables_Tables]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestTables] DROP CONSTRAINT FK_TestTables_Tables
GO
 
if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunTables_TestRuns]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestRunTables] DROP CONSTRAINT FK_TestRunTables_TestRuns
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunViews_TestRuns]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestRunViews] DROP CONSTRAINT FK_TestRunViews_TestRuns
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestTables_Tests]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestTables] DROP CONSTRAINT FK_TestTables_Tests
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestViews_Tests]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestViews] DROP CONSTRAINT FK_TestViews_Tests
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunViews_Views]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestRunViews] DROP CONSTRAINT FK_TestRunViews_Views
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestViews_Views]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestViews] DROP CONSTRAINT FK_TestViews_Views
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[Tables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [Tables]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestRunTables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestRunTables]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestRunViews]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestRunViews]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestRuns]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestRuns]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestTables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestTables]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestViews]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestViews]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[Tests]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [Tests]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[Views]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [Views]
GO

CREATE TABLE [Tables] (
	[TableID] [int] IDENTITY (1, 1) NOT NULL ,
	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [TestRunTables] (
	[TestRunID] [int] NOT NULL ,
	[TableID] [int] NOT NULL ,
	[StartAt] [datetime] NOT NULL ,
	[EndAt] [datetime] NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [TestRunViews] (
	[TestRunID] [int] NOT NULL ,
	[ViewID] [int] NOT NULL ,
	[StartAt] [datetime] NOT NULL ,
	[EndAt] [datetime] NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [TestRuns] (
	[TestRunID] [int] IDENTITY (1, 1) NOT NULL ,
	[Description] [nvarchar] (2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,
	[StartAt] [datetime] NULL ,
	[EndAt] [datetime] NULL 
) ON [PRIMARY]
GO

CREATE TABLE [TestTables] (
	[TestID] [int] NOT NULL ,
	[TableID] [int] NOT NULL ,
	[NoOfRows] [int] NOT NULL ,
	[Position] [int] NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [TestViews] (
	[TestID] [int] NOT NULL ,
	[ViewID] [int] NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [Tests] (
	[TestID] [int] IDENTITY (1, 1) NOT NULL ,
	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [Views] (
	[ViewID] [int] IDENTITY (1, 1) NOT NULL ,
	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 
) ON [PRIMARY]
GO

ALTER TABLE [Tables] WITH NOCHECK ADD 
	CONSTRAINT [PK_Tables] PRIMARY KEY  CLUSTERED 
	(
		[TableID]
	)  ON [PRIMARY] 
GO

ALTER TABLE [TestRunTables] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestRunTables] PRIMARY KEY  CLUSTERED 
	(
		[TestRunID],
		[TableID]
	)  ON [PRIMARY] 
GO

ALTER TABLE [TestRunViews] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestRunViews] PRIMARY KEY  CLUSTERED 
	(
		[TestRunID],
		[ViewID]
	)  ON [PRIMARY] 
GO

ALTER TABLE [TestRuns] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestRuns] PRIMARY KEY  CLUSTERED 
	(
		[TestRunID]
	)  ON [PRIMARY] 
GO

ALTER TABLE [TestTables] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestTables] PRIMARY KEY  CLUSTERED 
	(
		[TestID],
		[TableID]
	)  ON [PRIMARY] 
GO

ALTER TABLE [TestViews] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestViews] PRIMARY KEY  CLUSTERED 
	(
		[TestID],
		[ViewID]
	)  ON [PRIMARY] 
GO

ALTER TABLE [Tests] WITH NOCHECK ADD 
	CONSTRAINT [PK_Tests] PRIMARY KEY  CLUSTERED 
	(
		[TestID]
	)  ON [PRIMARY] 
GO

ALTER TABLE [Views] WITH NOCHECK ADD 
	CONSTRAINT [PK_Views] PRIMARY KEY  CLUSTERED 
	(
		[ViewID]
	)  ON [PRIMARY] 
GO

ALTER TABLE [TestRunTables] ADD 
	CONSTRAINT [FK_TestRunTables_Tables] FOREIGN KEY 
	(
		[TableID]
	) REFERENCES [Tables] (
		[TableID]
	) ON DELETE CASCADE  ON UPDATE CASCADE ,
	CONSTRAINT [FK_TestRunTables_TestRuns] FOREIGN KEY 
	(
		[TestRunID]
	) REFERENCES [TestRuns] (
		[TestRunID]
	) ON DELETE CASCADE  ON UPDATE CASCADE 
GO

ALTER TABLE [TestRunViews] ADD 
	CONSTRAINT [FK_TestRunViews_TestRuns] FOREIGN KEY 
	(
		[TestRunID]
	) REFERENCES [TestRuns] (
		[TestRunID]
	) ON DELETE CASCADE  ON UPDATE CASCADE ,
	CONSTRAINT [FK_TestRunViews_Views] FOREIGN KEY 
	(
		[ViewID]
	) REFERENCES [Views] (
		[ViewID]
	) ON DELETE CASCADE  ON UPDATE CASCADE 
GO

ALTER TABLE [TestTables] ADD 
	CONSTRAINT [FK_TestTables_Tables] FOREIGN KEY 
	(
		[TableID]
	) REFERENCES [Tables] (
		[TableID]
	) ON DELETE CASCADE  ON UPDATE CASCADE ,
	CONSTRAINT [FK_TestTables_Tests] FOREIGN KEY 
	(
		[TestID]
	) REFERENCES [Tests] (
		[TestID]
	) ON DELETE CASCADE  ON UPDATE CASCADE 
GO

ALTER TABLE [TestViews] ADD 
	CONSTRAINT [FK_TestViews_Tests] FOREIGN KEY 
	(
		[TestID]
	) REFERENCES [Tests] (
		[TestID]
	),
	CONSTRAINT [FK_TestViews_Views] FOREIGN KEY 
	(
		[ViewID]
	) REFERENCES [Views] (
		[ViewID]
	)
GO


-------------------------------------------------

INSERT INTO Tables (Name) VALUES ('Locatii'), ('Evenimente'), ('Sporturi'), ('SponsorEvenimente');

SELECT * FROM Tables

-- SELECT pe o tabela
CREATE OR ALTER VIEW V_Locatii AS
SELECT 
    id_locatie, 
    nume AS nume_locatie, 
    capacitate,
	contact
FROM Locatii;
GO

--SELECT pe 2 tabele
CREATE OR ALTER VIEW V_Evenimente_Sporturi AS
SELECT 
    e.id_eveniment, 
    e.nume AS nume_eveniment, 
    e.data AS data_eveniment, 
    s.nume AS sport, 
    s.descriere AS descriere_sport
FROM Evenimente e
JOIN Sporturi s 
ON e.id_sport = s.id_sport;
GO

--SELECT pe minim 2 tabele si GROUP BY
CREATE OR ALTER VIEW V_Sumar_Sponsori AS
SELECT 
    l.id_locatie, 
    l.nume AS locatie_eveniment, 
    COUNT(DISTINCT se.id_sponsor) AS numar_sponsori,
    COUNT(DISTINCT e.id_eveniment) AS numar_evenimente
FROM SponsorEvenimente se
JOIN Evenimente e ON se.id_eveniment = e.id_eveniment
JOIN Locatii l ON e.id_locatie = l.id_locatie
GROUP BY 
    l.id_locatie, 
    l.nume;
GO


SELECT * FROM V_Locatii

SELECT * FROM V_Evenimente_Sporturi

SELECT * FROM V_Sumar_Sponsori


-- Adaugam view-urile in tabela Views
INSERT INTO Views VALUES
	('V_Locatii'), 
	('V_EvenimenteSporturi'), 
	('V_Sponsori');
GO


SELECT * FROM Views;

-- Adaugam testele in tabela Tests
INSERT INTO Tests VALUES
	('Test_Locatii_10'),
	('Test_EvenimenteSporturi_7'),
	('Test_Sponsori_10')
GO

SELECT * FROM Tests;

-- Legatura intre teste si tabel

INSERT INTO TestTables (TestID, TableID, NoOfRows, Position) VALUES
	(1, 1, 10, 1), -- 1 tabela Locatii
	(2, 2, 10, 1), -- 2 tabela Evenimente
    (2, 3, 10, 2), -- 3 Tabela Sporturi
    (3, 4, 10, 1), -- 4 Tabela SponsoriEvenimente
    (3, 2, 10, 2),
	(3, 1, 10, 3);
GO

SELECT * FROM TestTables

-- Legatura intre teste si view-uri
INSERT INTO TestViews (TestID, ViewID) VALUES
	(1,1),
	(2,2),
	(3,3);
GO

SELECT * FROM TestViews

-- Procedurile de inserare

-- Inserare in tabela Locatii
CREATE OR ALTER PROCEDURE insert_test_Locatii
@NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @nume_locatie NVARCHAR(100);
    DECLARE @adresa NVARCHAR(200);
    DECLARE @capacitate INT;
    DECLARE @contact NVARCHAR(200);
    DECLARE @n INT = 0;
    DECLARE @last_id INT = (SELECT MAX(id_locatie) FROM Locatii)

    -- loop pt a insera NoOfRows in Locatii
    WHILE @n < @NoOfRows
    BEGIN
        SET @nume_locatie = 'Locatie TEST ' + CONVERT(VARCHAR(10), @last_id);
        SET @adresa = 'Adresa Test ' + CONVERT(VARCHAR(10), @last_id);
        SET @capacitate = 100 + @n;
        SET @contact = 'Contact Test ' + CONVERT(VARCHAR(10), @last_id);

        -- Insert locatia in tabela
        INSERT INTO Locatii (nume, adresa, capacitate, contact)
        VALUES (@nume_locatie, @adresa, @capacitate, @contact);

        -- id-ul ultimului rand introdus
        SET @last_id = @last_id + 1;

        -- contorul
        SET @n = @n + 1;
    END

    PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @NoOfRows) + ' locații in Locatii.';
END
GO

-- Inserare in tabela Evenimente
CREATE OR ALTER PROCEDURE insert_test_Evenimente
@NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @nume_eveniment NVARCHAR(150);
    DECLARE @data_eveniment DATE;
    DECLARE @descriere NVARCHAR(200);
    DECLARE @id_locatie INT;
    DECLARE @id_sport INT;
    DECLARE @n INT = 0;
    DECLARE @last_id INT = (SELECT MAX(id_eveniment) FROM Evenimente)

    WHILE @n < @NoOfRows
    BEGIN
        SET @nume_eveniment = 'Eveniment TEST ' + CONVERT(VARCHAR(10), @last_id);
        SET @data_eveniment = DATEADD(DAY, @n, '2024-01-01');
        SET @descriere = 'Descriere eveniment TEST ' + CONVERT(VARCHAR(10), @last_id);
        SET @id_locatie = 1 + (@n % 5);
        SET @id_sport = 1 + (@n % 3);

        INSERT INTO Evenimente (nume, data, descriere, id_locatie, id_sport)
        VALUES (@nume_eveniment, @data_eveniment, @descriere, @id_locatie, @id_sport);

        SET @last_id = @last_id + 1;

        SET @n = @n + 1;
    END

    PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @NoOfRows) + ' evenimente in Evenimente.';
END
GO

-- Inserare in tabela Sporturi
CREATE OR ALTER PROCEDURE insert_test_Sporturi
@NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @nume VARCHAR(100);
    DECLARE @descriere VARCHAR(200);
	DECLARE @n INT = 0;
    DECLARE @last_id INT = (SELECT MAX(id_sport) FROM Sporturi)

    WHILE @n < @NoOfRows
    BEGIN
        SET @nume = 'Sport TEST ' + CONVERT(VARCHAR(10), @last_id);
        SET @descriere = 'Descriere sport TEST ' + CONVERT(VARCHAR(10), @last_id);

        INSERT INTO Sporturi(nume, descriere)
        VALUES (@nume, @descriere);

        SET @last_id = @last_id + 1;

        SET @n = @n + 1;
    END

    PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @NoOfRows) + ' sporturi in Sporturi.';
END
GO

-- Inserare in tabela SponsorEvenimente
CREATE OR ALTER PROCEDURE insert_test_SponsorEvenimente
@NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @id_sponsor INT;
    DECLARE @id_eveniment INT;
    DECLARE @n INT = 0;
    DECLARE @last_id INT = (SELECT MAX(id_sponsor) FROM Sponsori)

    WHILE @n < @NoOfRows
    BEGIN
        SET @id_sponsor = 1 + (@last_id % 5);
        SET @id_eveniment = 1 + (@n % 10);

        INSERT INTO SponsorEvenimente (id_sponsor, id_eveniment)
        VALUES (@id_sponsor, @id_eveniment);

        SET @last_id = @last_id + 1;

        SET @n = @n + 1;
    END

    PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @NoOfRows) + ' sponsori pentru evenimente.';
END
GO

----- Procedurile de stergere -----

GO
CREATE OR ALTER PROCEDURE del_test_Locatii
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM Locatii
END
GO

GO
CREATE OR ALTER PROCEDURE del_test_Evenimente
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM Evenimente
END
GO

GO
CREATE OR ALTER PROCEDURE del_test_Sporturi
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM Sporturi
END
GO

GO
CREATE OR ALTER PROCEDURE del_test_SponsorEvenimente
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM SponsorEvenimente
END
GO

EXECUTE del_test_Locatii;
EXECUTE del_test_Evenimente;
EXECUTE del_test_Sporturi;
EXECUTE del_test_SponsorEvenimente;

-- Proceduri generale

-- Procedura de inserare
GO
CREATE OR ALTER PROCEDURE insert_test
@idTest INT
AS
BEGIN
	DECLARE @numeTest NVARCHAR(50) = (SELECT T.Name FROM Tests T WHERE T.TestID = @idTest);
	DECLARE @numeTabela NVARCHAR(50);
	DECLARE @NoOfRows INT;
	DECLARE @procedura NVARCHAR(50);

	DECLARE cursorTab CURSOR FORWARD_ONLY FOR
		SELECT Tab.Name, Test.NoOfRows FROM TestTables Test
		INNER JOIN Tables Tab ON Test.TableID = Tab.TableID
		WHERE Test.TestID = @idTest
		ORDER BY Test.Position;
	OPEN cursorTab;

	FETCH NEXT FROM cursorTab INTO @numeTabela, @NoOfRows;
	WHILE (@numeTest NOT LIKE N'Test_' + @numeTabela + N'_' + CONVERT(NVARCHAR(10), @NoOfRows)) AND (@@FETCH_STATUS = 0)
	BEGIN
		SET @procedura = N'insert_test_' + @numeTabela;
		EXECUTE @procedura @NoOfRows;
		FETCH NEXT FROM cursorTab INTO @numeTabela, @NoOfRows;
	END

	SET @procedura = N'insert_test_' + @numeTabela;
	EXECUTE @procedura @NoOfRows;

	CLOSE cursorTab;
	DEALLOCATE cursorTab;
END

EXECUTE insert_test 3;

SELECT * FROM Locatii

SELECT * FROM Tests
-- Procedura de stergere
GO
CREATE PROCEDURE delete_test
@idTest INT
AS
BEGIN
	DECLARE @numeTest NVARCHAR(50) = (SELECT T.Name FROM Tests T WHERE T.TestID = @idTest);
	DECLARE @numeTabela NVARCHAR(50);
	DECLARE @NoOfRows INT;
	DECLARE @procedura NVARCHAR(50);

	DECLARE cursorTab CURSOR FORWARD_ONLY FOR
		SELECT Tab.Name, Test.NoOfRows FROM TestTables Test
		INNER JOIN Tables Tab ON Test.TableID = Tab.TableID
		WHERE Test.TestID = @idTest
		ORDER BY Test.Position DESC;
	OPEN cursorTab;

	FETCH NEXT FROM cursorTab INTO @numeTabela, @NoOfRows;
	WHILE (@numeTest NOT LIKE N'Test_' + @numeTabela + N'_' + CONVERT(NVARCHAR(10), @NoOfRows)) AND (@@FETCH_STATUS = 0)
	BEGIN
		SET @procedura = N'del_test_' + @numeTabela;
		EXECUTE @procedura;
		FETCH NEXT FROM cursorTab INTO @numeTabela, @NoOfRows;
	END

	SET @procedura = N'del_test_' + @numeTabela;
	EXECUTE @procedura;

	CLOSE cursorTab;
	DEALLOCATE cursorTab;
END

EXECUTE delete_test 1

-- DELETE TOATE DATELE DIN TABELE