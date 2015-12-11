----------------------------------------------------------------
-- This SQL script builds the SQL tables for our Climbing App --
----------------------------------------------------------------
-- @author David Michel ----------------------------------------
-- @version 27 October 2015 ------------------------------------
-- SUBJECT TO CHANGE -------------------------------------------
----------------------------------------------------------------

-- Drop previous versions of the tables if they they exist, in reverse order of foreign keys.
--DROP TABLE IF EXISTS ClimberClimber;
DROP TABLE IF EXISTS Climb;
DROP TABLE IF EXISTS Climber;

-- Create the schema.
CREATE TABLE Climber (
	ID integer PRIMARY KEY, -- Unique identifier, ignore case (i.e. 'davejoshmike')
	userName varchar(25),
	password varchar(50), -- ENCRYPTED. password must be encrypted, no less than 6 characters, case sensitive
	emailAddress varchar(50), -- Email used to login (can be NULL if using facebook?)
	name varchar(50), -- last and first name seperated by a comma
	--myFriends varchar(500), -- store multiple userIDs in one string seperated by commas? i.e. myFriends = 'davejoshmike, cpd5, jam'
	dateCreated timestamp -- Member since: dateCreated
	);

CREATE TABLE Climb (
	ID integer PRIMARY KEY, -- unique identifier
	climberID integer REFERENCES Climber(ID),
	name varchar(50), -- name given by user
	color varchar(50), -- color selected by user (default = 'white')
	difficulty varchar(50), -- i.e. '5.8+', 'V7', or '5.10a'
	type varchar(50), -- i.e. 'boulder' 'top rope'
	notes varchar(500), -- a paragraph of notes (500 character limit)
	--location varchar(50), -- where the climb took place
	time timestamp -- when the climb happened (initially auto-generated, but editable by the user)
	);

-- Friends Table (not implimented)
--CREATE TABLE ClimberClimber (
--	climberID varchar(50) REFERENCES Climber(ID),
--	climbID integer REFERENCES Climb(ID)
--	);

-- Allow users to select data from the tables.
GRANT SELECT ON Climber TO PUBLIC;
GRANT SELECT ON Climb TO PUBLIC;
--GRANT SELECT ON ClimberClimber TO PUBLIC;
	
-- Add sample records.
INSERT INTO Climber VALUES (0, 'Guest', '', 'guest@guest.com', 'Guest, Mr./Mrs.', '2015-10-27 18:10:00');
INSERT INTO Climber VALUES (1, 'cpd5', 'funthings8', 'cpd5@gmail.com', 'Dilley, Chris', '2015-11-18 18:49:00');
INSERT INTO Climber VALUES (2, 'AustinS', 'funthings8', 'austin@gmail.com', 'S, Austin', '2015-11-18 18:50:00');

INSERT INTO Climb VALUES (1, 0, 'route1', 'blue', '5.8+', 'top rope', 'fun climb!!!', '2015-11-18 18:49:00');
INSERT INTO Climb VALUES (2, 0, 'route2', 'green', '5.8+', 'top rope', 'fun climb!!!', '2015-11-18 18:49:01');
INSERT INTO Climb VALUES (3, 0, 'route3', 'yellow', '5.8+', 'top rope', 'fun climb!!!', '2015-11-18 18:49:02');

-- Test queries
-- Lists the names of all climbers and how many days they've been a member
SELECT Climber.name as Name, 
       date_part('day', NOW() - Climber.dateCreated) as Member_days
FROM Climber;

SELECT *
FROM Climber;
