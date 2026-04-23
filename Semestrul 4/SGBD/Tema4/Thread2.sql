USE BazaDateSGBDTema4_test
GO

CREATE TABLE log_table (
    actiune VARCHAR(20),
    tabela VARCHAR(100),
    timestamp DATETIME
);


SELECT * FROM Sporturi

-- Transaction 1
CREATE OR ALTER PROCEDURE dirty_reads_1 AS
BEGIN 
	BEGIN TRY
		BEGIN TRAN
		UPDATE Sporturi SET nume = 'Sport Dirty' WHERE id_sport = 1
		INSERT INTO log_table VALUES ('UPDATE', 'Sporturi', CURRENT_TIMESTAMP)
		WAITFOR DELAY '00:00:05'
		ROLLBACK TRAN
		PRINT 'Transaction rollbacked'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		PRINT 'Transaction failed'
	END CATCH
END

-- Transaction 2
CREATE OR ALTER PROCEDURE dirty_reads_2 AS
BEGIN
	--SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED  --Solution
	BEGIN TRY
		BEGIN TRAN
		SELECT * FROM Sporturi
		INSERT INTO log_table VALUES('SELECT', 'Sporturi', CURRENT_TIMESTAMP)
		WAITFOR DELAY '00:00:10'
		SELECT * FROM Sporturi
		INSERT INTO log_table VALUES('SELECT', 'Sporturi', CURRENT_TIMESTAMP)
		COMMIT TRAN
		PRINT 'Transaction committed'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		PRINT 'Error in dirty_reads_2'
	END CATCH
END


EXEC dirty_reads_2

SELECT * FROM Sporturi;
SELECT * FROM log_table WHERE tabela = 'Sporturi';

-----------------------------------------------------------------------------------------------------------------

-- Transaction 1 (modifies during other transaction)
CREATE OR ALTER PROCEDURE non_repeatable_reads_1 AS
BEGIN
	BEGIN TRY
		BEGIN TRAN
		WAITFOR DELAY '00:00:05'
		UPDATE Sporturi SET nume = 'Sport Modified' WHERE id_sport = 1
		INSERT INTO log_table VALUES ('UPDATE', 'Sporturi', CURRENT_TIMESTAMP)
		COMMIT TRAN
		PRINT 'Transaction committed'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		PRINT 'Transaction rollbacked'
	END CATCH
END

-- Transaction 2
CREATE OR ALTER PROCEDURE non_repeatable_reads_2 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED
	--SET TRANSACTION ISOLATION LEVEL REPEATABLE READ --Solution
	BEGIN TRY
		BEGIN TRAN
		SELECT * FROM Sporturi
		INSERT INTO log_table VALUES('SELECT', 'Sporturi', CURRENT_TIMESTAMP)
		WAITFOR DELAY '00:00:10'
		SELECT * FROM Sporturi
		INSERT INTO log_table VALUES('SELECT', 'Sporturi', CURRENT_TIMESTAMP)
		COMMIT TRAN
		PRINT 'Transaction committed'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		PRINT 'Transaction rollbacked'
	END CATCH
END


EXEC non_repeatable_reads_2

UPDATE Sporturi SET nume = 'Sport' WHERE id_sport = 1


SELECT * FROM Sporturi;
SELECT * FROM log_table WHERE tabela = 'Sporturi';

--------------------------------------------------------------------------------------------------------

-- Transaction 1 (inserts new row during other transaction)
CREATE OR ALTER PROCEDURE phantom_reads_1 AS
BEGIN
	BEGIN TRY
		BEGIN TRAN
		WAITFOR DELAY '00:00:05'
		INSERT INTO Sporturi (nume, descriere) VALUES ('Phantom Sport', 'Inserted during phantom test')
		INSERT INTO log_table VALUES ('INSERT', 'Sporturi', CURRENT_TIMESTAMP)
		COMMIT TRAN
		PRINT 'Transaction committed'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		PRINT 'Transaction rollbacked'
	END CATCH
END

-- Transaction 2
CREATE OR ALTER PROCEDURE phantom_reads_2 AS
BEGIN
	--SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	SET TRANSACTION ISOLATION LEVEL SERIALIZABLE  --Solution
	BEGIN TRY
		BEGIN TRAN
		SELECT * FROM Sporturi
		INSERT INTO log_table VALUES('SELECT', 'Sporturi', CURRENT_TIMESTAMP)
		WAITFOR DELAY '00:00:10'
		SELECT * FROM Sporturi
		INSERT INTO log_table VALUES('SELECT', 'Sporturi', CURRENT_TIMESTAMP)
		COMMIT TRAN
		PRINT 'Transaction committed'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		PRINT 'Transaction rollbacked'
	END CATCH
END


EXEC phantom_reads_2



SELECT * FROM Sporturi WHERE nume = 'Phantom Sport';
SELECT * FROM log_table WHERE tabela = 'Sporturi';

DELETE FROM Sporturi WHERE nume = 'Phantom Sport';

------------------------------------------------------------------------------------------------------------

-- Transaction 1 (Sponsori → Sporturi)
CREATE OR ALTER PROCEDURE deadlock_1 AS
BEGIN
	DECLARE @error INT
	BEGIN TRY
		SET DEADLOCK_PRIORITY HIGH

		BEGIN TRAN
		UPDATE Sponsori SET nume = 'Deadlock 1' WHERE id_sponsor = 1
		INSERT INTO log_table VALUES ('UPDATE', 'Sponsori', CURRENT_TIMESTAMP)
		WAITFOR DELAY '00:00:05'
		UPDATE Sporturi SET nume = 'Deadlock 1' WHERE id_sport = 1
		INSERT INTO log_table VALUES ('UPDATE', 'Sporturi', CURRENT_TIMESTAMP)
		COMMIT TRAN
		PRINT 'Transaction committed in deadlock_1'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SET @error = ERROR_NUMBER()
		IF @error = 1205
		BEGIN
			PRINT 'Deadlock in deadlock_1, retrying...'
			WAITFOR DELAY '00:00:01'
			EXEC deadlock_1
		END
		ELSE
		BEGIN
			PRINT 'Error in deadlock_1: ' + ERROR_MESSAGE()
		END
	END CATCH
END

-- Transaction 2 (Sporturi → Sponsori)
CREATE OR ALTER PROCEDURE deadlock_2 AS
BEGIN
	DECLARE @error INT
	BEGIN TRY
		BEGIN TRAN
		UPDATE Sporturi SET nume = 'Deadlock 2' WHERE id_sport = 1
		INSERT INTO log_table VALUES ('UPDATE', 'Sporturi', CURRENT_TIMESTAMP)
		WAITFOR DELAY '00:00:05'
		UPDATE Sponsori SET nume = 'Deadlock 2' WHERE id_sponsor = 1
		INSERT INTO log_table VALUES ('UPDATE', 'Sponsori', CURRENT_TIMESTAMP)
		COMMIT TRAN
		PRINT 'Transaction committed in deadlock_2'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SET @error = ERROR_NUMBER()
		IF @error = 1205
		BEGIN
			PRINT 'Deadlock in deadlock_2, retrying...'
			WAITFOR DELAY '00:00:01'
			EXEC deadlock_2
		END
		ELSE
		BEGIN
			PRINT 'Error in deadlock_2: ' + ERROR_MESSAGE()
		END
	END CATCH
END


EXEC deadlock_2


SELECT * FROM Sporturi;
SELECT * FROM Sponsori;
SELECT * FROM log_table WHERE tabela IN ('Sporturi', 'Sponsori');

--------------------------------------------------------------------------

CREATE OR ALTER PROCEDURE deadlock_11 AS
BEGIN
	--SET DEADLOCK_PRIORITY HIGH
	BEGIN TRAN
	UPDATE Sponsori set nume = 'Deadlock trans 1' WHERE id_sponsor = 1
	WAITFOR DELAY '00:00:05'
	UPDATE Sporturi set nume = 'Deadlock trans 1' WHERE id_sport = 1
	COMMIT TRAN
	PRINT 'Transaction commited'
END

CREATE OR ALTER PROCEDURE deadlock_22 AS
BEGIN
	BEGIN TRAN
	UPDATE Sporturi set nume = 'Deadlock trans 2' WHERE id_sport = 1
	WAITFOR DELAY '00:00:05'
	UPDATE Sponsori set nume = 'Deadlock trans 2' WHERE id_sponsor = 1
	COMMIT TRAN
	PRINT 'Transaction commited'
END

EXEC deadlock_22