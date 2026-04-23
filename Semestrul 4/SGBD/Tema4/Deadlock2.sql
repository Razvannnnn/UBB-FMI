USE BazaDateSGBDTema4_test
GO

-- THREAD 2
SET DEADLOCK_PRIORITY HIGH;

BEGIN TRANSACTION;
    PRINT 'THREAD 2: update Antrenori...'
    UPDATE Antrenori 
    SET contact = 'deadlock@thread2.com' 
    WHERE id_antrenor = 1;

    WAITFOR DELAY '00:00:05';

    PRINT 'THREAD 2: update Echipe...'
    UPDATE Echipe 
    SET tara = 'Franta' 
    WHERE id_echipa = 1;

COMMIT TRANSACTION;


SELECT * FROM Antrenori
SELECT * FROM Echipe