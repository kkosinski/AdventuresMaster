CREATE TABLE menuItem (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL, parentId INTEGER NOT NULL, isActivity INTEGER NOT NULL);

CREATE TABLE activityDesc (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, activityId INTEGER UNIQUE NOT NULL, className TEXT NOT NULL, hasExtras INTEGER NOT NULL, FOREIGN KEY (activityId) REFERENCES menuItem (id));

CREATE TABLE activityExtras (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, activityId INTEGER NOT NULL, key TEXT NOT NULL, value TEXT NOT NULL, FOREIGN KEY (activityId) REFERENCES activityDesc (activityId) ON DELETE CASCADE);

INSERT INTO menuItem (id, title, parentId, isActivity) VALUES
	(1, 'settings', -1, 0),
	(2, 'games', -1, 0),
	(3, 'gm tools', -1, 0),
	(4, 'philips hue configuration', 1, 1),
	(5, 'join', 2, 1),
	(6, 'my games', 2, 0),
	(7, 'create new game', 6, 1),
	(8, 'existing games', 6, 1),
	(9, 'scenes', 3, 1),
	(10, 'effects', 3, 1);

INSERT INTO activityDesc (activityId, className, hasExtras) VALUES
	(4, 'PhilipsHueConfigActivity', 0),
	(5, 'JoinGameActivity', 0),
	(7, 'CreateNewGameActivity', 0),
	(8, 'ExistingGamesActivity', 0),
	(9, 'BoardPanel', 1),
	(10, 'BoardPanel', 1);

INSERT INTO activityExtras (activityId, key, value)	VALUES 
	(9, 'type', 'scenes'),
	(10, 'type', 'effects');

CREATE TABLE scene (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, inBoard INTEGER NOT NULL, title TEXT NOT NULL, lightId INTEGER NOT NULL, effect INTEGER, music INTEGER, ambience INTEGER, FOREIGN KEY (inBoard) REFERENCES board (id), FOREIGN KEY (effect) REFERENCES audioWithOpts (id), FOREIGN KEY (music) REFERENCES audioWithOpts (id),	FOREIGN KEY (ambience) REFERENCES audioWithOpts (id));

CREATE TABLE audioWithOpts (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, audioFileId INTEGER NOT NULL, volume INTEGER NOT NULL, repeat INTEGER NOT NULL, playAfterEffect INTEGER NOT NULL, FOREIGN KEY (audioFileId) REFERENCES audioFile (id) ON DELETE CASCADE);

CREATE TABLE audioFile (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, path TEXT UNIQUE NOT NULL);

CREATE TABLE board (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, type TEXT NOT NULL, isContentTable INTEGER NOT NULL, parentId INTEGER NOT NULL);