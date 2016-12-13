DROP TABLE IF EXISTS Game CASCADE;
DROP TABLE IF EXISTS Player CASCADE;
DROP TABLE IF EXISTS TargetMatches CASCADE;

CREATE TABLE Game (
	gameID SERIAL PRIMARY KEY,
	gameName varchar(50),
	inPlay boolean,
	startDate date
);

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

CREATE TABLE TargetMatches (
	matchID SERIAL PRIMARY KEY,
	gameID integer REFERENCES Game(gameID),
	playerID integer REFERENCES Player(playerID),
	targetID integer REFERENCES Player(playerID),
	targetStartTime timestamp,
	targetTimeoutTime timestamp
);

insert into game VALUES(1, 'Test Game', true, '2016-12-4');
insert into player Values(10, 'Nate', 'Bender', 'RVD', 'computer science', 28.02, 15.43, NOW(), 1, true);
insert into player Values(11, 'Paige', 'Brinks', 'BHT', 'computer science', 22.02, 18.43, NOW(), 1, true);
insert into TargetMatches VALUES(1, 1, 10, 11, '2016-12-10', '2016-12-11');
-- select * from player;
-- select * from TargetMatches;

--SELECT matchID, gameID, playerID, targetID, targetStartTime, targetTimeoutTime FROM TargetMatches WHERE TargetMatches.playerID = 10 AND TargetMatches.gameID = 1 LIMIT 1;
SELECT DATEDIFF(day, targetStartTime, targetTimeoutTime) as DIFF FROM TargetMatches WHERE TargetMatches.playerID = 10 AND TargetMatches.gameID = 1
