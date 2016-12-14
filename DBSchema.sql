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

insert into game VALUES(1, 'Sample Game', true, '2016-12-16');
insert into player Values(10, 'Nate', 'Bender', 'RVD', 'computer science', 28.02, 15.43, NOW(), 1, true);
insert into player Values(11, 'Paige', 'Brinks', 'BHT', 'computer science', 22.02, 18.43, NOW(), 1, true);
insert into player Values(12, 'Christiaan', 'Hazlett', 'KHvR', 'computer science', 100.0, 20.0, NOW(), 1, true);
insert into TargetMatches VALUES(1, 1, 10, 11, '2016-12-14', '2016-12-15');
insert into TargetMatches VALUES(2, 1, 11, 12, '2016-12-14', '2016-12-15');



-- DO $$
-- DECLARE
-- 	curPlayerID integer := 10;
-- 	curGameID integer;
-- 	oldTargetID integer;
-- 	newTargetID integer;
-- BEGIN
-- 	SELECT gameID FROM Player INTO curGameID WHERE playerID = curPlayerID;
-- 	SELECT targetID FROM TargetMatches INTO oldTargetID WHERE playerID = curPlayerID AND gameID = curGameID;
-- 	SELECT targetID FROM TargetMatches INTO newTargetID WHERE playerID = oldTargetID AND gameID = curGameID;
--
-- 	UPDATE TargetMatches SET targetID = newTargetID WHERE playerID = curPlayerID AND gameID = curGameID;
-- 	UPDATE Player SET alive = false WHERE playerID = oldTargetID;
-- 	DELETE FROM TargetMatches WHERE playerID = oldTargetID AND gameID = curGameID;
-- END $$

-- DO $$ DECLARE curPlayerID integer := 10; curGameID integer; oldTargetID integer; newTargetID integer; BEGIN SELECT gameID FROM Player INTO curGameID WHERE playerID = curPlayerID; SELECT targetID FROM TargetMatches INTO oldTargetID WHERE playerID = curPlayerID AND gameID = curGameID; SELECT targetID FROM TargetMatches INTO newTargetID WHERE playerID = oldTargetID AND gameID = curGameID; UPDATE TargetMatches SET targetID = newTargetID WHERE playerID = curPlayerID AND gameID = curGameID; UPDATE Player SET alive = false WHERE playerID = oldTargetID; DELETE FROM TargetMatches WHERE playerID = oldTargetID AND gameID = curGameID; END $$

-- SELECT targetID FROM TargetMatches INTO oldTargetID WHERE playerID = curPlayerID AND gameID = curGameID;
--SELECT targetID FROM TargetMatches INTO newTargetID WHERE playerID = oldTargetID AND gameID = curGameID;

--UPDATE TargetMatches SET targetid = (SELECT targetID FROM TargetMatches WHERE playerID = )

-- select * from targetmatches;
