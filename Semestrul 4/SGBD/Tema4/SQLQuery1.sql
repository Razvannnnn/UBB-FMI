CREATE DATABASE BazaDateSGBDTema4
GO
USE BazaDateSGBDTema4
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
ON DELETE CASCADE
);

CREATE TABLE Atleti (
id_atlet INT PRIMARY KEY IDENTITY,
nume VARCHAR(100) NOT NULL,
prenume VARCHAR(100) NOT NULL,
nationalitate VARCHAR(100),
data_nasterii DATE,
id_echipa INT NOT NULL,
FOREIGN KEY (id_echipa) REFERENCES Echipe(id_echipa)
ON DELETE CASCADE
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
ON DELETE CASCADE
);

CREATE TABLE Programari (
id_programare INT PRIMARY KEY IDENTITY,
start_time TIME NOT NULL,
end_time TIME NOT NULL,
descriere VARCHAR(200),
id_eveniment INT NOT NULL,
FOREIGN KEY (id_eveniment) REFERENCES Evenimente(id_eveniment)
ON DELETE CASCADE
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
ON DELETE CASCADE
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


-- Antrenori pentru echipele de Fotbal
INSERT INTO Antrenori(nume, prenume, contact) VALUES
('Dumitrescu', 'Ion', 'ion.dumitrescu@gmail.com'),
('Radu', 'Gabriel', 'gabriel.radu@gmail.com'),
('Constantinescu', 'Daniel', 'daniel.constantinescu@gmail.com'),
('Enache', 'Florin', 'florin.enache@gmail.com');

-- Antrenori pentru echipele de Baschet
INSERT INTO Antrenori(nume, prenume, contact) VALUES
('Vasilescu', 'Marius', 'marius.vasilescu@gmail.com'),
('Dobre', 'Cristian', 'cristian.dobre@gmail.com'),
('Tudor', 'Alexandru', 'alexandru.tudor@gmail.com'),
('Iliescu', 'Stefan', 'stefan.iliescu@gmail.com');

-- Antrenori pentru echipele de Atletism
INSERT INTO Antrenori(nume, prenume, contact) VALUES
('Grigorescu', 'Elena', 'elena.grigorescu@gmail.com'),
('Stanciu', 'Ioana', 'ioana.stanciu@gmail.com'),
('Preda', 'Adrian', 'adrian.preda@gmail.com'),
('Moraru', 'Carmen', 'carmen.moraru@gmail.com');

-- Antrenori pentru echipele de Tenis
INSERT INTO Antrenori(nume, prenume, contact) VALUES
('Petrescu', 'Roxana', 'roxana.petrescu@gmail.com'),
('Barbu', 'Ovidiu', 'ovidiu.barbu@gmail.com'),
('Lazar', 'George', 'george.lazar@gmail.com'),
('Matei', 'Simona', 'simona.matei@gmail.com');

INSERT INTO Echipe(nume, tara, id_antrenor, id_sport) VALUES
('CSM Oradea', 'Romania', 9, 2),
('BC CSU Sibiu', 'Romania', 10, 2),
('Steaua CSM Eximbank', 'Romania', 11, 2),
('U-BT Cluj-Napoca', 'Romania', 12, 2);

INSERT INTO Echipe(nume, tara, id_antrenor, id_sport) VALUES
('CSM Bucuresti', 'Romania', 13, 3),
('CS Universitatea Craiova', 'Romania', 14, 3),
('CSA Steaua', 'Romania', 15, 3),
('CS Rapid', 'Romania', 16, 3);

INSERT INTO Echipe(nume, tara, id_antrenor, id_sport) VALUES
('Bucharest Tennis Club', 'Romania', 17, 4),
('Cluj Tennis Academy', 'Romania', 18, 4),
('Timisoara Tennis Club', 'Romania', 19, 4),
('Constanta Tennis Academy', 'Romania', 20, 4);







CREATE TABLE log_table (
	id INT PRIMARY KEY IDENTITY,
	operation_type VARCHAR(30),
	table_name VARCHAR(50),
	execution_time DATETIME
);

CREATE FUNCTION validate_sponsor_parameters(
	@nume VARCHAR(100),
	@contact VARCHAR(200),
	@nivel NVARCHAR(100)
)
RETURNS VARCHAR(200)
AS
BEGIN
	DECLARE @error VARCHAR(200) = ''

	IF (@nume IS NULL OR LTRIM(RTRIM(@nume)) = '')
		SET @error += 'Nume sponsor invalid. '

	IF (@contact IS NULL OR LTRIM(RTRIM(@contact)) = '')
		SET @error += 'Contact sponsor invalid. '

	IF (@nivel IS NULL OR LTRIM(RTRIM(@nivel)) = '')
		SET @error += 'Nivel sponsor invalid. '

	RETURN @error
END


CREATE FUNCTION validate_event_parameters(
	@nume NVARCHAR(150),
	@data DATE,
	@id_locatie INT,
	@id_sport INT
)
RETURNS VARCHAR(200)
AS
BEGIN
	DECLARE @error VARCHAR(200) = ''

	IF (@nume IS NULL OR LTRIM(RTRIM(@nume)) = '')
		SET @error += 'Nume eveniment invalid. '

	IF (@data IS NULL OR @data < GETDATE())
		SET @error += 'Data evenimentului este în trecut. '

	IF NOT EXISTS (SELECT 1 FROM Locatii WHERE id_locatie = @id_locatie)
		SET @error += 'Locatie inexistenta. '

	IF NOT EXISTS (SELECT 1 FROM Sporturi WHERE id_sport = @id_sport)
		SET @error += 'Sport inexistent. '

	RETURN @error
END



CREATE PROCEDURE insert_sponsor_eveniment_full_rollback (
	@nume_sponsor VARCHAR(100),
	@contact_sponsor VARCHAR(200),
	@nivel NVARCHAR(100),
	@nume_eveniment NVARCHAR(150),
	@data_eveniment DATE,
	@descriere NVARCHAR(200),
	@id_locatie INT,
	@id_sport INT
)
AS
BEGIN
	BEGIN TRAN
	BEGIN TRY
		DECLARE @err_msg VARCHAR(200)

		SET @err_msg = dbo.validate_sponsor_parameters(@nume_sponsor, @contact_sponsor, @nivel)
		IF @err_msg <> ''
			RAISERROR(@err_msg, 14, 1)

		INSERT INTO Sponsori(nume, contact, nivel) VALUES (@nume_sponsor, @contact_sponsor, @nivel)
		INSERT INTO log_table VALUES ('INSERT', 'Sponsori', GETDATE())

		SET @err_msg = dbo.validate_event_parameters(@nume_eveniment, @data_eveniment, @id_locatie, @id_sport)
		IF @err_msg <> ''
			RAISERROR(@err_msg, 14, 1)

		INSERT INTO Evenimente(nume, data, descriere, id_locatie, id_sport)
		VALUES (@nume_eveniment, @data_eveniment, @descriere, @id_locatie, @id_sport)
		INSERT INTO log_table VALUES ('INSERT', 'Evenimente', GETDATE())

		DECLARE @id_sponsor INT = (SELECT MAX(id_sponsor) FROM Sponsori)
		DECLARE @id_eveniment INT = (SELECT MAX(id_eveniment) FROM Evenimente)

		INSERT INTO SponsorEvenimente VALUES (@id_sponsor, @id_eveniment)
		INSERT INTO log_table VALUES ('INSERT', 'SponsorEvenimente', GETDATE())

		COMMIT TRAN
		PRINT 'Transaction committed'
	END TRY
	BEGIN CATCH
		PRINT ERROR_MESSAGE()
		ROLLBACK TRAN
		INSERT INTO log_table VALUES ('ROLLBACK', 'ALL', GETDATE())
		PRINT 'Transaction rolled back'
	END CATCH
END




CREATE PROCEDURE insert_sponsor_eveniment_partial_rollback (
	@nume_sponsor VARCHAR(100),
	@contact_sponsor VARCHAR(200),
	@nivel NVARCHAR(100),
	@nume_eveniment NVARCHAR(150),
	@data_eveniment DATE,
	@descriere NVARCHAR(200),
	@id_locatie INT,
	@id_sport INT
)
AS
BEGIN
	DECLARE @id_sponsor INT = NULL, @id_eveniment INT = NULL
	DECLARE @err_msg VARCHAR(200)

	-- TRANZACTIA 1 - Sponsor
	BEGIN TRAN
	BEGIN TRY
		SET @err_msg = dbo.validate_sponsor_parameters(@nume_sponsor, @contact_sponsor, @nivel)
		IF @err_msg <> ''
			RAISERROR(@err_msg, 14, 1)

		INSERT INTO Sponsori(nume, contact, nivel) VALUES (@nume_sponsor, @contact_sponsor, @nivel)
		SET @id_sponsor = SCOPE_IDENTITY()
		INSERT INTO log_table VALUES ('INSERT', 'Sponsori', GETDATE())
		COMMIT
	END TRY
	BEGIN CATCH
		ROLLBACK
		INSERT INTO log_table VALUES ('ROLLBACK', 'Sponsori', GETDATE())
		RETURN
	END CATCH

	-- TRANZACTIA 2 - Eveniment
	BEGIN TRAN
	BEGIN TRY
		SET @err_msg = dbo.validate_event_parameters(@nume_eveniment, @data_eveniment, @id_locatie, @id_sport)
		IF @err_msg <> ''
			RAISERROR(@err_msg, 14, 1)

		INSERT INTO Evenimente(nume, data, descriere, id_locatie, id_sport)
		VALUES (@nume_eveniment, @data_eveniment, @descriere, @id_locatie, @id_sport)
		SET @id_eveniment = SCOPE_IDENTITY()
		INSERT INTO log_table VALUES ('INSERT', 'Evenimente', GETDATE())
		COMMIT
	END TRY
	BEGIN CATCH
		ROLLBACK
		INSERT INTO log_table VALUES ('ROLLBACK', 'Evenimente', GETDATE())
		RETURN
	END CATCH

	-- TRANZACTIA 3 - Legatura m-n
	BEGIN TRAN
	BEGIN TRY
		INSERT INTO SponsorEvenimente VALUES (@id_sponsor, @id_eveniment)
		INSERT INTO log_table VALUES ('INSERT', 'SponsorEvenimente', GETDATE())
		COMMIT
	END TRY
	BEGIN CATCH
		ROLLBACK
		INSERT INTO log_table VALUES ('ROLLBACK', 'SponsorEvenimente', GETDATE())
	END CATCH
END



EXEC insert_sponsor_eveniment_full_rollback 
  'Red Bull', 'contact@redbull.com', 'Premium',
  'Maraton 2025', '2025-08-20', 'Eveniment anual', 1, 1;

EXEC insert_sponsor_eveniment_partial_rollback 
  'Nike', 'support@nike.com', 'Gold',
  'Cupa Nike 2025', '2025-09-15', 'Eveniment sponsorizat', 1, 1;




-- Nume sponsor invalid
EXEC insert_sponsor_eveniment_full_rollback 
  '', 'invalid@x.com', 'Basic', 'Eveniment Test', '2025-08-20', 'Test', 1, 1;

-- Data eveniment în trecut
EXEC insert_sponsor_eveniment_partial_rollback 
  'Sponsor Test', 'sponsor@test.com', 'Silver', 
  'Eveniment Invalid', '2022-01-01', 'Descriere', 1, 1;
  

SELECT * FROM Sponsori
SELECT * FROM Evenimente
SELECT * FROM SponsorEvenimente

SELECT * FROM log_table