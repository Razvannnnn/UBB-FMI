USE BazaDateSGBDTema4_test
GO


BEGIN TRANSACTION
    WAITFOR DELAY '00:00:07';
    
    INSERT INTO Evenimente (nume, data, descriere, id_locatie, id_sport)
    VALUES ('Cupa Fantoma', '2025-12-01', 'Eveniment adaugat in timpul tranzactiei', 1, 1);
COMMIT TRANSACTION;



DELETE FROM Evenimente WHERE nume = 'Cupa Fantoma';
