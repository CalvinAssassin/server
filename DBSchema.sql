-- ###Current proposed design by cdh24 on Dec 6

DROP TABLE IF EXISTS Player CASCADE;
CREATE TABLE Player (
	playerID SERIAL PRIMARY KEY,
	firstName varchar(50),
	lastName varchar(50),
	residence varchar(50),
	major varchar(50),
	latitude float,
	longitude float,
	locUpdateTime timestamp,
	gameID integer REFERENCES Game(gameID),
	alive boolean
	);

DROP TABLE IF EXISTS Game CASCADE;
CREATE TABLE Game (
	gameID SERIAL PRIMARY KEY,
	gameName varchar(50),
	inPlay boolean,
	startDate date
	);

insert into game Values(1, 'Test Game', true, '2016-12-4');
insert into player Values(104, 'Nate', 'Bender', 'RVD', 'computer science', 28.02, 15.43, NOW(), 1, true);







-- -- Drop previous versions of the tables if they they exist, in reverse order of foreign keys.
-- DROP TABLE IF EXISTS Player CASCADE;
-- DROP TABLE IF EXISTS PlayerGame CASCADE;
-- DROP TABLE IF EXISTS Game CASCADE;
-- DROP TABLE IF EXISTS PlayerMatchUp CASCADE;
-- DROP TABLE IF EXISTS PlayerLocation CASCADE;
--
--
--
-- CREATE TABLE Player (
-- 	playerID SERIAL PRIMARY KEY,
-- 	firstName varchar(50),
-- 	lastName varchar(50),
-- 	residence varchar(50),
-- 	major varchar(50)
-- 	);
--
--
-- CREATE TABLE Game (
-- 	gameID SERIAL PRIMARY KEY,
-- 	gameName varchar(50),
-- 	inPlay boolean,
-- 	creatorID integer REFERENCES Player(playerID),
-- 	startDate date,
-- 	endDate date,
-- 	currentRound integer
-- 	);
-- --
-- INSERT INTO Player(playerID, firstName, lastName, residence, major) VALUES(100, 'Joe', 'Smith', 'NVW', 'computer science');
-- INSERT INTO Game(gameName, inPlay, creatorID, startDate, endDate) VALUES('Cool Game', TRUE, 100, '2016-11-20', '2016-11-27');
-- --
-- -- CREATE TABLE PlayerGame (
-- -- 	playerGameID integer PRIMARY KEY,
-- -- 	gameID integer REFERENCES Game(gameID),
-- -- 	playerID integer REFERENCES Player(playerID),
-- -- 	stillAlive boolean
-- -- 	);
-- --
-- --
-- -- CREATE TABLE PlayerMatchUp (
-- -- 	playerMatchID integer PRIMARY KEY,
-- -- 	gameID integer REFERENCES Game(gameID),
-- -- 	assassinID integer REFERENCES Player(playerID),
-- -- 	targetID integer REFERENCES Player(playerID), -- target
-- -- 	targetAlive boolean
-- -- 	);
-- --
-- -- CREATE TABLE PlayerLocation (
-- -- 	playerLocationID integer PRIMARY KEY,
-- -- 	playerID integer REFERENCES Player(playerID),
-- -- 	gameID integer REFERENCES Game(gameID),
-- -- 	latitude float,
-- -- 	longitude float,
-- -- 	time timestamp
-- -- 	);
-- --
-- -- -- Allow users to select data from the tables.
-- -- GRANT SELECT ON Game TO PUBLIC;
-- -- GRANT SELECT ON Player TO PUBLIC;
-- -- GRANT SELECT ON PlayerGame TO PUBLIC;
-- -- GRANT SELECT ON PlayerMatchUp TO PUBLIC;
-- -- GRANT SELECT ON PlayerLocation TO PUBLIC;
-- --
-- -- -- INSERT INTO Player VALUES ('Jesse', 'Hulse', 'SE', 'computer science');
-- -- -- INSERT INTO Player VALUES ('First', 'Last', 'RVD', 'art');
-- -- -- INSERT INTO Player VALUES ('Christiaan', 'Hazlett', 'KHvR', 'history');
-- -- --
-- -- -- INSERT INTO Game VALUES (1, 'tournement1', true, 1, '2016-06-27 08:00:00', 3);
-- --
-- --
-- -- INSERT INTO PlayerGame VALUES (1, 1, 1, true);
-- -- INSERT INTO PlayerGame VALUES (2, 1, 2, true);
-- -- INSERT INTO PlayerGame VALUES (3, 1, 3, true);
-- -- INSERT INTO PlayerGame VALUES (4, 1, 4, false);
-- --
-- -- INSERT INTO PlayerMatchUp VALUES (1, 1, 1, 2, true);
-- -- INSERT INTO PlayerMatchUp VALUES (2, 1, 2, 3, true);
-- -- INSERT INTO PlayerMatchUp VALUES (3, 1, 3, 1, true);
-- --
-- -- INSERT INTO PlayerLocation VALUES (1, 1, 1, 42.9634, 85.6681);
-- -- INSERT INTO PlayerLocation VALUES (2, 2, 1, 43.1254, 84.9874);
-- -- INSERT INTO PlayerLocation VALUES (3, 3, 1, 41.6512, 87.2231);
-- -- --
-- -- -- -- get target of player1
-- -- -- SELECT targetID FROM PlayerMatchUp
-- -- -- WHERE assassinID = 1;
-- -- --
-- -- -- -- get game rankings - everyone who is still in game 1
-- -- -- SELECT userName FROM Player, PlayerGame
-- -- -- WHERE PlayerGame.stillAlive = TRUE
-- -- -- AND PlayerGame.playerID = Player.playerID
-- -- -- AND PlayerGame.GameID = 1;
-- -- --
-- -- -- -- get people who are dead in game
-- -- -- SELECT userName FROM Player, PlayerGame
-- -- -- WHERE PlayerGame.stillAlive = FALSE
-- -- -- AND PlayerGame.playerID = Player.playerID
-- -- -- AND PlayerGame.GameID = 1;
