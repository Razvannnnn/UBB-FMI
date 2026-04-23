CREATE DATABASE EvenimenteSportiveLAB4_3;
GO
USE EvenimenteSportiveLAB4_3;
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
		EXEC dbo.Validate_SponsorEvenimente_Create @Id_sponsor, @Id_eveniment;
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

EXEC Create_Sporturi 20,'da','descriere';
EXEC Read_Sporturi 20,'da','descriere';
EXEC Update_Sporturi 20,'da','descriere';
EXEC Delete_Sporturi 20,'da','descriere';

EXEC Create_Locatii 20,'da','da',30,'contact';
EXEC Read_Locatii 20,'da','da',30,'contact';
EXEC Update_Locatii 20,'da','da',30,'contact';
EXEC Delete_Locatii 20,'da','da',30,'contact';

EXEC Create_Evenimente 20,'da', '11-10-2024', 'descriere', 1, 1;
EXEC Read_Evenimente 20,'da', '11-10-2024', 'descriere', 1, 1;
EXEC Update_Evenimente 20,'da', '11-10-2024', 'descriere', 1, 1;
EXEC Delete_Evenimente 20,'da', '11-10-2024', 'descriere', 1, 1;

EXEC Create_Sponsori 20,'da', 'contact', 'da';
EXEC Read_Sponsori 20,'da', 'contact', 'da';
EXEC Update_Sponsori 20,'da', 'contact', 'da';
EXEC Delete_Sponsori 20,'da', 'contact', 'da';

EXEC Create_SponsorEvenimente 1,8;
EXEC Read_SponsorEvenimente 1,8;
EXEC Update_SponsorEvenimente 1,8;
EXEC Delete_SponsorEvenimente 1,8;

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












--
CREATE OR ALTER PROCEDURE CRUD_Sporturi
    @Nume VARCHAR(100),
    @Descriere VARCHAR(200),
    @NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        EXEC dbo.Validate_Sporturi @Nume, @Descriere;
		DECLARE @i INT = 0;
        WHILE @i < @NoOfRows
        BEGIN
            INSERT INTO Sporturi (nume, descriere)
            VALUES (@Nume, @Descriere);
            SET @i = @i + 1;
        END
        SELECT * FROM Sporturi ORDER BY id_sport;

        UPDATE Sporturi --UPDATE
        SET nume = UPPER(nume)
        WHERE nume = @Nume;
        SELECT * FROM Sporturi ORDER BY id_sport;

        DELETE FROM Sporturi --DELETE
        WHERE nume = UPPER(@Nume);
        SELECT * FROM Sporturi ORDER BY id_sport;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE CRUD_LOCATII
	@Nume VARCHAR(100),
	@Adresa VARCHAR(200),
	@Capacitate INT,
	@Contact VARCHAR(200),
	@NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        EXEC dbo.Validate_Locatii @Nume, @Adresa, @Capacitate, @Contact;
        DECLARE @i INT = 0;
        WHILE @i < @NoOfRows
        BEGIN
            INSERT INTO Locatii (nume, adresa, capacitate, contact)
            VALUES (@Nume, @Adresa, @Capacitate, @Contact);
            SET @i = @i + 1;
        END

        SELECT * FROM Locatii ORDER BY id_locatie;
        UPDATE Locatii -- UPDATE
        SET nume = UPPER(nume)
        WHERE nume = @Nume;
        SELECT * FROM Locatii ORDER BY id_locatie;
        DELETE FROM Locatii -- DELETE
        WHERE nume = UPPER(@Nume);
        SELECT * FROM Locatii ORDER BY id_locatie;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE CRUD_Evenimente
	@Nume NVARCHAR(150),
	@Data DATE,
	@Descriere NVARCHAR(200),
	@Id_locatie INT,
	@Id_sport INT,
	@NoOfRows INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Evenimente @Nume, @Data, @Descriere, @Id_locatie, @Id_sport;

		DECLARE @i INT = 0;
        WHILE @i < @NoOfRows
        BEGIN
            INSERT INTO Evenimente (nume, data, descriere, id_locatie, id_sport)
            VALUES (@Nume, @Data, @Descriere, @Id_locatie, @Id_sport);
            SET @i = @i + 1;
        END

        SELECT * FROM Evenimente ORDER BY id_eveniment;
        UPDATE Evenimente -- UPDATE
        SET nume = UPPER(nume)
        WHERE nume = @Nume;
        SELECT * FROM Evenimente ORDER BY id_eveniment;
        DELETE FROM Evenimente -- DELETE
        WHERE nume = UPPER(@Nume);
        SELECT * FROM Evenimente ORDER BY id_eveniment;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE CRUD_Sponsori
	@Nume VARCHAR(100), 
	@Contact VARCHAR(200),
	@Nivel NVARCHAR(100),
	@NoOfRows INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_Sponsori @Nume, @Contact, @Nivel;

		DECLARE @i INT = 0;
        WHILE @i < @NoOfRows
        BEGIN
            INSERT INTO Sponsori(nume, contact, nivel)
            VALUES (@Nume, @Contact, @Nivel);
            SET @i = @i + 1;
        END
        SELECT * FROM Sponsori ORDER BY id_sponsor;
        UPDATE Sponsori -- UPDATE
        SET nume = UPPER(nume)
        WHERE nume = @Nume;
        SELECT * FROM Sponsori ORDER BY id_sponsor;
        DELETE FROM Sponsori -- DELETE
        WHERE nume = UPPER(@Nume);
        SELECT * FROM Sponsori ORDER BY id_sponsor;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;

CREATE OR ALTER PROCEDURE CRUD_SponsorEvenimente
	@Id_sponsor INT,
	@Id_eveniment INT,
	@NoOfRows INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.Validate_SponsorEvenimente @Id_sponsor, @Id_eveniment;

		DECLARE @i INT = 0;
        WHILE @i < @NoOfRows
        BEGIN
            INSERT INTO SponsorEvenimente(id_sponsor, id_eveniment)
            VALUES (@Id_sponsor, @Id_eveniment);
            SET @i = @i + 1;
        END

        SELECT * FROM SponsorEvenimente ORDER BY id_sponsor;
        UPDATE SponsorEvenimente -- UPDATE
        SET id_sponsor = (SELECT MIN(id_sponsor) FROM Sponsori)
        WHERE id_eveniment = @Id_eveniment AND id_sponsor = @Id_sponsor;
        SELECT * FROM Sponsori ORDER BY id_sponsor;
        DELETE FROM SponsorEvenimente -- DELETE
        WHERE @Id_eveniment = id_eveniment AND @Id_sponsor = (SELECT MIN(id_sponsor) FROM Sponsori);
        SELECT * FROM Sponsori ORDER BY id_sponsor;
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END;