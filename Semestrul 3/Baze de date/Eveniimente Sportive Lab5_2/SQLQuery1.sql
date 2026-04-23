CREATE DATABASE EvenimenteSportiveLAB5;
GO
USE EvenimenteSportiveLAB5;
GO

CREATE TABLE Sporturi (
id_sport INT PRIMARY KEY,
nume VARCHAR(100) NOT NULL, 
descriere VARCHAR(200)
);

CREATE TABLE Locatii (
id_locatie INT PRIMARY KEY,
nume VARCHAR(100) NOT NULL,
adresa VARCHAR(200),
capacitate INT NOT NULL,
contact VARCHAR(200)
);

CREATE TABLE Antrenori (
id_antrenor INT PRIMARY KEY,
nume VARCHAR(100) NOT NULL,
prenume VARCHAR(100) NOT NULL,
contact VARCHAR(200)
);

CREATE TABLE Sponsori (
id_sponsor INT PRIMARY KEY,
nume VARCHAR(100) NOT NULL, 
contact VARCHAR(200),
nivel NVARCHAR(100)
);

CREATE TABLE Echipe (
id_echipa INT PRIMARY KEY,
nume VARCHAR(100) NOT NULL,
tara VARCHAR(100),
id_antrenor INT NOT NULL,
id_sport INT NOT NULL,
FOREIGN KEY (id_antrenor) REFERENCES Antrenori(id_antrenor),
FOREIGN KEY (id_sport) REFERENCES Sporturi(id_sport)
ON DELETE CASCADE
);

CREATE TABLE Atleti (
id_atlet INT PRIMARY KEY,
nume VARCHAR(100) NOT NULL,
prenume VARCHAR(100) NOT NULL,
nationalitate VARCHAR(100),
data_nasterii DATE,
id_echipa INT NOT NULL,
FOREIGN KEY (id_echipa) REFERENCES Echipe(id_echipa)
ON DELETE CASCADE
);

CREATE TABLE Evenimente (
id_eveniment INT PRIMARY KEY,
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
id_programare INT PRIMARY KEY,
start_time TIME NOT NULL,
end_time TIME NOT NULL,
descriere VARCHAR(200),
id_eveniment INT NOT NULL,
FOREIGN KEY (id_eveniment) REFERENCES Evenimente(id_eveniment)
ON DELETE CASCADE
);

CREATE TABLE Inregistrari (
id_inregistrare INT PRIMARY KEY,
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
id_rezultat INT PRIMARY KEY,
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



-- TEMA LAB5 - Tabele folosite: Sporturi, Locatii, Evenimente, Sponsori, SponsorEvenimente (M-M SponsorEvenimente)

------ SPORTURI -------------------------------------

CREATE OR ALTER PROCEDURE Create_Sporturi
	@id INT,
	@Nume VARCHAR(100),
    @Descriere VARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        EXEC dbo.Validate_Sporturi_Create @id, @Nume, @Descriere;
        INSERT INTO Sporturi (id_sport ,nume, descriere)
		VALUES (@id, @Nume, @Descriere);
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Read_Sporturi
	@id INT,
	@Nume VARCHAR(100),
    @Descriere VARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        EXEC dbo.Validate_Sporturi @id, @Nume, @Descriere;		
        SELECT * FROM Sporturi
		WHERE id_sport = @id;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Update_Sporturi
	@id INT,
	@Nume VARCHAR(100),
    @Descriere VARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        EXEC dbo.Validate_Sporturi @id, @Nume, @Descriere;
        UPDATE Sporturi
        SET nume = @Nume, descriere = @Descriere
        WHERE @id = id_sport;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Delete_Sporturi
	@id INT,
	@Nume VARCHAR(100),
    @Descriere VARCHAR(200)

AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        EXEC dbo.Validate_Sporturi @id, @Nume, @Descriere;
        DELETE FROM Sporturi
        WHERE id_sport = @id;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Validate_Sporturi
	@id INT,
    @Nume VARCHAR(100),
    @Descriere VARCHAR(200)
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';

	IF NOT EXISTS (SELECT 1 FROM Sporturi WHERE id_sport = @id)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @Nume IS NULL OR LTRIM(RTRIM(@Nume)) = ''
    BEGIN
        SET @ErrorMessages += 'Numele invalid' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Descriere) > 200
    BEGIN
        SET @ErrorMessages += 'Descrierea invalida!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;

CREATE OR ALTER PROCEDURE Validate_Sporturi_Create
	@id INT,
    @Nume VARCHAR(100),
    @Descriere VARCHAR(200)
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';

	IF EXISTS (SELECT 1 FROM Sporturi WHERE id_sport = @id)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @Nume IS NULL OR LTRIM(RTRIM(@Nume)) = ''
    BEGIN
        SET @ErrorMessages += 'Numele invalid' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Descriere) > 200
    BEGIN
        SET @ErrorMessages += 'Descrierea invalida!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;
------- LOCATII ------------------------------------------------------------------

CREATE OR ALTER PROCEDURE Create_Locatii
	@id INT,
	@Nume VARCHAR(100),
	@Adresa VARCHAR(200),
	@Capacitate INT,
	@Contact VARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        EXEC dbo.Validate_Locatii_Create @id, @Nume, @Adresa, @Capacitate, @Contact;
        INSERT INTO Locatii (id_locatie ,nume, adresa, capacitate, contact)
        VALUES (@id ,@Nume, @Adresa, @Capacitate, @Contact);
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Read_Locatii
	@id INT,
	@Nume VARCHAR(100),
	@Adresa VARCHAR(200),
	@Capacitate INT,
	@Contact VARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        EXEC dbo.Validate_Locatii @id, @Nume, @Adresa, @Capacitate, @Contact;
        SELECT * FROM Locatii
		WHERE id_locatie = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Update_Locatii
	@id INT,
	@Nume VARCHAR(100),
	@Adresa VARCHAR(200),
	@Capacitate INT,
	@Contact VARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        EXEC dbo.Validate_Locatii @id, @Nume, @Adresa, @Capacitate, @Contact;
        UPDATE Locatii
        SET nume = @Nume, adresa = @Adresa, capacitate = @Capacitate, contact = @Contact
        WHERE id_locatie = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Delete_Locatii
	@id INT,
	@Nume VARCHAR(100),
	@Adresa VARCHAR(200),
	@Capacitate INT,
	@Contact VARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        EXEC dbo.Validate_Locatii @id, @Nume, @Adresa, @Capacitate, @Contact;
        DELETE FROM Locatii
        WHERE id_locatie = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Validate_Locatii
	@id INT,
    @Nume VARCHAR(100),
	@Adresa VARCHAR(200),
	@Capacitate INT,
	@Contact VARCHAR(200)
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';

	IF NOT EXISTS (SELECT 1 FROM Locatii WHERE id_locatie = @id)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @Nume IS NULL OR LTRIM(RTRIM(@Nume)) = ''
    BEGIN
        SET @ErrorMessages += 'Numele invalid!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Adresa) > 200
    BEGIN
        SET @ErrorMessages += 'Adresa invalida!' + CHAR(13) + CHAR(10);
    END
    IF @Capacitate <= 0
    BEGIN
        SET @ErrorMessages += 'Capacitatea invalida!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Contact) > 200
    BEGIN
        SET @ErrorMessages += 'Contact invalid!' + CHAR(13) + CHAR(10);
    END

    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;

CREATE OR ALTER PROCEDURE Validate_Locatii_Create
	@id INT,
    @Nume VARCHAR(100),
	@Adresa VARCHAR(200),
	@Capacitate INT,
	@Contact VARCHAR(200)
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';

	IF EXISTS (SELECT 1 FROM Locatii WHERE id_locatie = @id)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @Nume IS NULL OR LTRIM(RTRIM(@Nume)) = ''
    BEGIN
        SET @ErrorMessages += 'Numele invalid!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Adresa) > 200
    BEGIN
        SET @ErrorMessages += 'Adresa invalida!' + CHAR(13) + CHAR(10);
    END
    IF @Capacitate <= 0
    BEGIN
        SET @ErrorMessages += 'Capacitatea invalida!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Contact) > 200
    BEGIN
        SET @ErrorMessages += 'Contact invalid!' + CHAR(13) + CHAR(10);
    END

    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;
----- EVENIMENTE ----------------------------------------
CREATE OR ALTER PROCEDURE Create_Evenimente
	@id INT,
	@Nume NVARCHAR(150),
	@Data DATE,
	@Descriere NVARCHAR(200),
	@Id_locatie INT,
	@Id_sport INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Evenimente_Create @id, @Nume, @Data, @Descriere, @Id_locatie, @Id_sport;
        INSERT INTO Evenimente (id_eveniment, nume, data, descriere, id_locatie, id_sport)
        VALUES (@id, @Nume, @Data, @Descriere, @Id_locatie, @Id_sport);
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Read_Evenimente
	@id INT,
	@Nume NVARCHAR(150),
	@Data DATE,
	@Descriere NVARCHAR(200),
	@Id_locatie INT,
	@Id_sport INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Evenimente @id, @Nume, @Data, @Descriere, @Id_locatie, @Id_sport;
        SELECT * FROM Evenimente 
		WHERE id_eveniment = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Update_Evenimente
	@id INT,
	@Nume NVARCHAR(150),
	@Data DATE,
	@Descriere NVARCHAR(200),
	@Id_locatie INT,
	@Id_sport INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Evenimente @id, @Nume, @Data, @Descriere, @Id_locatie, @Id_sport;
        UPDATE Evenimente
        SET nume = @Nume, data = @Data, descriere = @Descriere, id_locatie = @Id_locatie, id_sport = @Id_sport
        WHERE id_eveniment = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Delete_Evenimente
	@id INT,
	@Nume NVARCHAR(150),
	@Data DATE,
	@Descriere NVARCHAR(200),
	@Id_locatie INT,
	@Id_sport INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Evenimente @id, @Nume, @Data, @Descriere, @Id_locatie, @Id_sport;
        DELETE FROM Evenimente
        WHERE id_eveniment = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Validate_Evenimente
	@id INT,
    @Nume NVARCHAR(150),
	@Data DATE,
	@Descriere NVARCHAR(200),
	@Id_locatie INT,
	@Id_sport INT
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';

	IF NOT EXISTS (SELECT 1 FROM Evenimente WHERE id_eveniment = @id)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @Nume IS NULL OR LTRIM(RTRIM(@Nume)) = ''
    BEGIN
        SET @ErrorMessages += 'Numele invalid!' + CHAR(13) + CHAR(10);
    END
	IF @Data IS NULL
    BEGIN
        SET @ErrorMessages += 'Data invalida!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Descriere) > 200
    BEGIN
        SET @ErrorMessages += 'Descrierea invalida!' + CHAR(13) + CHAR(10);
    END
    IF NOT EXISTS (SELECT 1 FROM Locatii WHERE id_locatie = @Id_locatie)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF NOT EXISTS (SELECT 1 FROM Sporturi WHERE id_sport = @Id_sport)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;

CREATE OR ALTER PROCEDURE Validate_Evenimente_Create
	@id INT,
    @Nume NVARCHAR(150),
	@Data DATE,
	@Descriere NVARCHAR(200),
	@Id_locatie INT,
	@Id_sport INT
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';

	IF EXISTS (SELECT 1 FROM Evenimente WHERE id_eveniment = @id)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @Nume IS NULL OR LTRIM(RTRIM(@Nume)) = ''
    BEGIN
        SET @ErrorMessages += 'Numele invalid!' + CHAR(13) + CHAR(10);
    END
	IF @Data IS NULL
    BEGIN
        SET @ErrorMessages += 'Data invalida!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Descriere) > 200
    BEGIN
        SET @ErrorMessages += 'Descrierea invalida!' + CHAR(13) + CHAR(10);
    END
    IF NOT EXISTS (SELECT 1 FROM Locatii WHERE id_locatie = @Id_locatie)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF NOT EXISTS (SELECT 1 FROM Sporturi WHERE id_sport = @Id_sport)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;

------ SPONSORI ------------------------------------------------
CREATE OR ALTER PROCEDURE Create_Sponsori
	@id INT,
	@Nume VARCHAR(100), 
	@Contact VARCHAR(200),
	@Nivel NVARCHAR(100)
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Sponsori_Create @id, @Nume, @Contact, @Nivel;
        INSERT INTO Sponsori(id_sponsor, nume, contact, nivel)
        VALUES (@id, @Nume, @Contact, @Nivel);
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Read_Sponsori
	@id INT,
	@Nume VARCHAR(100), 
	@Contact VARCHAR(200),
	@Nivel NVARCHAR(100)
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Sponsori @id, @Nume, @Contact, @Nivel;
        SELECT * FROM Sponsori
		WHERE id_sponsor = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Update_Sponsori
	@id INT,
	@Nume VARCHAR(100), 
	@Contact VARCHAR(200),
	@Nivel NVARCHAR(100)
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Sponsori @id, @Nume, @Contact, @Nivel;
        UPDATE Sponsori
        SET nume = @Nume, contact = @Contact, nivel = @Nivel
        WHERE id_sponsor = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Delete_Sponsori
	@id INT,
	@Nume VARCHAR(100), 
	@Contact VARCHAR(200),
	@Nivel NVARCHAR(100)
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Sponsori @id, @Nume, @Contact, @Nivel;
        DELETE FROM Sponsori
        WHERE id_sponsor = @id
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Validate_Sponsori
	@id INT,
    @Nume VARCHAR(100), 
	@Contact VARCHAR(200),
	@Nivel NVARCHAR(100)
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';
	IF NOT EXISTS (SELECT 1 FROM Sponsori WHERE id_sponsor = @id)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @Nume IS NULL OR LTRIM(RTRIM(@Nume)) = ''
    BEGIN
        SET @ErrorMessages += 'Numele invalid!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Contact) > 200
    BEGIN
        SET @ErrorMessages += 'Contact invalid!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Nivel) > 100
    BEGIN
        SET @ErrorMessages += 'Nivelul invalid!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;

CREATE OR ALTER PROCEDURE Validate_Sponsori_Create
	@id INT,
    @Nume VARCHAR(100), 
	@Contact VARCHAR(200),
	@Nivel NVARCHAR(100)
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';
	IF EXISTS (SELECT 1 FROM Sponsori WHERE id_sponsor = @id)
    BEGIN
        SET @ErrorMessages += 'ID invalid!' + CHAR(13) + CHAR(10);
    END
    IF @Nume IS NULL OR LTRIM(RTRIM(@Nume)) = ''
    BEGIN
        SET @ErrorMessages += 'Numele invalid!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Contact) > 200
    BEGIN
        SET @ErrorMessages += 'Contact invalid!' + CHAR(13) + CHAR(10);
    END
    IF LEN(@Nivel) > 100
    BEGIN
        SET @ErrorMessages += 'Nivelul invalid!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;

--------- SponsorEvenimente ------------------------------------
CREATE OR ALTER PROCEDURE Create_SponsorEvenimente
	@Id_sponsor INT,
	@Id_eveniment INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_SponsorEvenimente @Id_sponsor, @Id_eveniment;
        INSERT INTO SponsorEvenimente(id_sponsor, id_eveniment)
        VALUES (@Id_sponsor, @Id_eveniment);
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Read_SponsorEvenimente
	@Id_sponsor INT,
	@Id_eveniment INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_SponsorEvenimente @Id_sponsor, @Id_eveniment;
        SELECT * FROM SponsorEvenimente
		WHERE id_sponsor = @Id_sponsor AND id_eveniment = @Id_eveniment
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Update_SponsorEvenimente
	@Id_sponsor INT,
	@Id_eveniment INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_SponsorEvenimente @Id_sponsor, @Id_eveniment;
        UPDATE SponsorEvenimente
        SET id_sponsor = (SELECT MIN(id_sponsor) FROM Sponsori)
        WHERE id_eveniment = @Id_eveniment AND id_sponsor = @Id_sponsor;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Delete_SponsorEvenimente
	@Id_sponsor INT,
	@Id_eveniment INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_SponsorEvenimente @Id_sponsor, @Id_eveniment;
        DELETE FROM SponsorEvenimente
        WHERE @Id_eveniment = id_eveniment AND @Id_sponsor = (SELECT MIN(id_sponsor) FROM Sponsori);
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE Validate_SponsorEvenimente
    @Id_sponsor INT,
	@Id_eveniment INT
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';
    IF NOT EXISTS (SELECT 1 FROM Sponsori WHERE id_sponsor = @Id_sponsor)
    BEGIN
        SET @ErrorMessages += 'ID sponsor invalid!' + CHAR(13) + CHAR(10);
    END
    IF NOT EXISTS (SELECT 1 FROM Evenimente WHERE id_eveniment = @Id_eveniment)
    BEGIN
        SET @ErrorMessages += 'ID eveniment invalid!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;

CREATE OR ALTER PROCEDURE Validate_SponsorEvenimente_Create
    @Id_sponsor INT,
	@Id_eveniment INT
AS
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';
    IF EXISTS (SELECT 1 FROM Sponsori WHERE id_sponsor = @Id_sponsor)
    BEGIN
        SET @ErrorMessages += 'ID sponsor invalid!' + CHAR(13) + CHAR(10);
    END
    IF EXISTS (SELECT 1 FROM Evenimente WHERE id_eveniment = @Id_eveniment)
    BEGIN
        SET @ErrorMessages += 'ID eveniment invalid!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END
    RETURN 1;
END;

-------------  EXEC  -------------------------------------------

SELECT * FROM Sporturi;

EXEC Create_Sporturi 20,'da','descriere';
EXEC Read_Sporturi 20,'da','descriere';
EXEC Update_Sporturi 20,'da2123','descriere';
EXEC Delete_Sporturi 20,'da2123','descriere';

SELECT * FROM Locatii;

EXEC Create_Locatii 20,'da','da',30,'contact';
EXEC Read_Locatii 20,'da','da',30,'contact';
EXEC Update_Locatii 20,'da22','da',30,'contact';
EXEC Delete_Locatii 20,'da22','da',30,'contact';

SELECT * FROM Evenimente

EXEC Create_Evenimente 20, 'da', '11-10-2024', 'descriere', 1, 1;
EXEC Read_Evenimente 20,'da', '11-10-2024', 'descriere', 1, 1;
EXEC Update_Evenimente 20,'da22', '11-10-2024', 'descriere', 1, 1;
EXEC Delete_Evenimente 20,'da', '11-10-2024', 'descriere', 1, 1;

SELECT * FROM Sponsori;

EXEC Create_Sponsori 20,'da', 'contact', 'da';
EXEC Read_Sponsori 20,'da', 'contact', 'da';
EXEC Update_Sponsori 20,'da22', 'contact', 'da';
EXEC Delete_Sponsori 20,'da22', 'contact', 'da';

SELECT * FROM SponsorEvenimente
SELECT * FROM Sponsori
SELECT

EXEC Create_SponsorEvenimente 2,3;
EXEC Read_SponsorEvenimente 2,3;
EXEC Update_SponsorEvenimente 3,2;
EXEC Delete_SponsorEvenimente 2,3;

------------- VIEWS  -----------------------------------------

CREATE OR ALTER VIEW ViewEvenimenteLocatii AS
SELECT 
    e.id_eveniment,
    e.nume AS eveniment_nume,
    e.data AS eveniment_data,
    e.descriere AS eveniment_descriere,
    l.nume AS locatie_nume,
    l.adresa AS locatie_adresa,
    l.capacitate AS locatie_capacitate
FROM 
    Evenimente e
WITH (INDEX (idx_Evenimente_id_locatie))
JOIN 
    Locatii l ON e.id_locatie = l.id_locatie
WHERE 
    e.id_locatie = l.id_locatie;

-----------------------------------------------

CREATE OR ALTER VIEW View_SponsoriEvenimente AS
SELECT 
    sp.id_sponsor,
    sp.nume AS nume_sponsor,
    sp.nivel AS nivel_sponsor,
    e.id_eveniment,
    e.nume AS nume_eveniment,
    e.data AS data_eveniment
FROM 
    Sponsori sp
WITH (INDEX (idx_Sponsori_id_sponsor))
JOIN 
    SponsorEvenimente se ON sp.id_sponsor = se.id_sponsor
JOIN 
    Evenimente e ON se.id_eveniment = e.id_eveniment;


SELECT * FROM ViewEvenimenteLocatii
SELECT * FROM View_SponsoriEvenimente

------------------ INDEX --------------------

CREATE NONCLUSTERED INDEX idx_Evenimente_id_locatie ON Evenimente (id_locatie);
CREATE NONCLUSTERED INDEX idx_Sponsori_id_sponsor ON Sponsori (id_sponsor);

DROP INDEX idx_Evenimente_id_locatie ON Evenimente
DROP INDEX idx_Sponsori_id_sponsor ON Sponsori

SELECT * FROM Evenimente
ORDER BY id_locatie


INSERT INTO Sporturi (id_sport ,nume, descriere) VALUES
(1,'Fotbal', 'Sport de echipa cu 11 jucatori'),
(2,'Baschet', 'Sport de echipa cu 5 jucatori'),
(3,'Atletism', 'Sport individual'),
(4,'Tenis', 'Sport de echipa sau individual');


INSERT INTO Locatii(id_locatie,nume, adresa, capacitate, contact) VALUES
(1,'Stadion National', 'Bucuresti', 55000, '000-231-4324'),
(2,'Arena Baschet', 'Cluj-Napoca', 8000, '000-221-3467'),
(3,'Sala de atletism', 'Pitesti', 1500, '000-551-4236'),
(4,'Teren de tenis', 'Constanta', 2000, '000-546-7654');


INSERT INTO Sponsori(id_sponsor,nume, contact, nivel) VALUES
(1,'BMW Motors', 'contact@bmwmotors.com', 'Platinum'),
(2,'Superbet', 'contact@superbet.com', 'Gold'),
(3,'Bucovina', 'contact@bucovina.com', 'Silver'),
(4,'Timisoreana', 'contact@timisoreana.com', 'Bronze');


INSERT INTO Echipe(id_echipa,nume, tara, id_antrenor, id_sport) VALUES
(1,'Steaua Bucuresti', 'Romania', 1, 1),
(2,'U-Cluj', 'Romania', 2, 2),
(3,'Atletico Pitesti', 'Romania', 3, 3),
(4,'Clubul Tenis CT', 'Romania', 4, 4);

INSERT INTO Antrenori(id_antrenor,nume, prenume, contact) VALUES
(1,'Ionescu', 'Mihai', 'mihai.ionescu@gmail.com'),
(2,'Popescu', 'Andrei', 'andrei.popescu@gmail.com'),
(3,'Marinescu', 'Alina', 'alina.marinescu@gmail.com'),
(4,'Georgescu', 'Maria', 'maria.georgescu@gmail.com');

INSERT INTO Atleti(id_atlet,nume, prenume, nationalitate, data_nasterii, id_echipa) VALUES
(1,'Popa', 'Alexandru', 'Romania', '1998-04-10', 1),
(2,'Ionescu', 'Cristian', 'Romania', '2000-07-22', 2),
(3,'Stoica', 'Ana', 'Romania', '1995-11-13', 3),
(4,'Gheorghe', 'Mihnea', 'Romania', '1997-01-05', 4),
(5,'Lebron', 'James', 'USA', '1985-05-12', 2);


INSERT INTO Evenimente(id_eveniment,nume, data, descriere, id_locatie, id_sport) VALUES
(1,'Campionatul National', '2024-05-10', 'Competiție de fotbal', 1, 1),
(2,'Turneul de Baschet', '2024-06-15', 'Competiție de baschet', 2, 2),
(3,'Maratonul Pitesti', '2024-07-20', 'Competiție de atletism', 3, 3),
(4,'Open-ul de Tenis', '2024-08-25', 'Competiție de tenis', 4, 4);

INSERT INTO Programari (id_programare,start_time, end_time, descriere, id_eveniment) VALUES
(1,'09:00', '11:00', 'Meci 1', 1),
(2,'11:30', '13:30', 'Meci 2', 1),
(3,'14:00', '16:00', 'Semifinale', 2),
(4,'16:30', '18:30', 'Finala', 2);

INSERT INTO Inregistrari (id_inregistrare,data_inregistrare, id_eveniment, id_echipa, id_atlet) VALUES
(1,'2024-05-01', 1, 1, NULL),
(2,'2024-05-01', 1, NULL, 1),
(3,'2024-06-10', 2, 2, NULL),
(4,'2024-07-10', 3, NULL, 3);

INSERT INTO Rezultate (id_rezultat,pozitie, scor, id_eveniment, id_atlet, id_echipa) VALUES
(1,1, 9.8, 1, NULL, 1),
(2,2, 8.6, 1, NULL, 2),
(3,1, 10.0, 3, 3, NULL),
(4,2, 9.5, 3, 4, NULL);


INSERT INTO SponsorEvenimente (id_sponsor, id_eveniment) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 3);