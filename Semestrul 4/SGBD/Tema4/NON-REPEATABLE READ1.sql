USE BazaDateSGBDTema4_test
GO



-- THREAD 1
INSERT INTO Sporturi(nume, descriere)
VALUES ('Racheta', 'Sport cu mingea');

BEGIN TRANSACTION
    WAITFOR DELAY '00:00:07';
    UPDATE Sporturi
    SET descriere = 'Sport de interior'
    WHERE nume = 'Racheta';
COMMIT TRANSACTION;
