USE BazaDateSGBDTema4_test
GO

-- THREAD 1
SET DEADLOCK_PRIORITY LOW;

BEGIN TRANSACTION;
    PRINT 'THREAD 1: update Echipe...'
    UPDATE Echipe 
    SET tara = 'Spania' 
    WHERE id_echipa = 1;

    WAITFOR DELAY '00:00:05';

    PRINT 'THREAD 1: update Antrenori...'
    UPDATE Antrenori 
    SET contact = 'deadlock@thread1.com' 
    WHERE id_antrenor = 1;

COMMIT TRANSACTION;

SELECT * FROM Antrenori
SELECT * FROM Echipe
