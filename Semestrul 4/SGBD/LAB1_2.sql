CREATE DATABASE [Lab1224-2SGBD2025];
GO
USE [Lab1224-2SGBD2025];


CREATE TABLE Motociclete (
	cod_m INT PRIMARY KEY IDENTITY,
	model VARCHAR(100),
	cc INT,
	cai_putere INT,
	greutate INT,
	producator VARCHAR(100),
	tip VARCHAR(100),
	tip_permis VARCHAR(100)
);

INSERT INTO Motociclete (model, cc, cai_putere, greutate, producator, tip, tip_permis) VALUES 
('Yamaha YZF', 125, 15, 140, 'YAMAHA', 'Sport', 'A1'), 
('BMW R1250 GS', 1254, 136, 249, 'BMW', 'Adventure', 'A'), 
('Ducati Panigale V4', 1103, 214, 198, 'Ducati', 'Sport', 'A'), 
('Yamaha MT-07', 689, 74, 184, 'Yamaha', 'Naked', 'A2'), 
('Honda CBR500R', 471, 47, 192, 'Honda', 'Sport', 'A2'), 
('Kawasaki Ninja ZX-6R', 636, 130, 196, 'Kawasaki', 'Sport', 'A'), 
('Suzuki GSX-R1000', 999, 199, 202, 'Suzuki', 'Sport', 'A'), 
('Harley Davidson Iron 883', 883, 50, 256, 'Harley Davidson', 'Cruiser', 'A'), 
('Triumph Street Triple', 765, 123, 189, 'Triumph', 'Naked', 'A'), 
('KTM 390 Duke', 373, 44, 149, 'KTM', 'Naked', 'A2'), 
('Aprilia Tuono V4 1100', 1077, 175, 209, 'Aprilia', 'Naked', 'A');