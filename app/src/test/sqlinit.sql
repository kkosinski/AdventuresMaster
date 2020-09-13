CREATE TABLE menuItem (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	title TEXT NOT NULL,
	parentId INTEGER NOT NULL,
	isActivity INTEGER NOT NULL);

CREATE TABLE activityDesc (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	activityId INTEGER UNIQUE NOT NULL,
	className TEXT NOT NULL,
	packageInfo Text,
	hasExtras INTEGER NOT NULL,
	FOREIGN KEY (activityId) REFERENCES menuItem (id));

CREATE TABLE activityExtras (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	activityId INTEGER NOT NULL,
	key TEXT NOT NULL,
	value TEXT NOT NULL,
	FOREIGN KEY (activityId) REFERENCES activityDesc (activityId) ON DELETE CASCADE);

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

INSERT INTO activityDesc (activityId, className, packageInfo, hasExtras) VALUES
	(4, 'PhilipsHueConfig', 'settings', 0),
	(5, 'JoinGame', 'games', 0),
	(7, 'CreateNewGame', 'games', 0),
	(8, 'ExistingGames', 'games', 0),
	(9, 'BoardPanel', 'tools.gm', 1),
	(10, 'BoardPanel', 'tools.gm', 1);

INSERT INTO activityExtras (activityId, key, value)	VALUES 
	(9, 'type', 'scenes'),
	(10, 'type', 'effects');

CREATE TABLE board (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	name TEXT NOT NULL,
	type TEXT NOT NULL,
	isContentTable INTEGER NOT NULL,
	parentId INTEGER NOT NULL);

CREATE TABLE scene (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
	title TEXT NOT NULL, 
	inBoard INTEGER NOT NULL,
	FOREIGN KEY (inBoard) REFERENCES board (id) ON DELETE CASCADE);

CREATE TABLE audioFile (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 	title TEXT NOT NULL,
 	uri TEXT UNIQUE NOT NULL);

CREATE TABLE audioInScene(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	inScene INTEGER NOT NULL,
	audioFile INTEGER NOT NULL,
	volume INTEGER NOT NULL,
	repeat INTEGER NOT NULL,
	playAfterEffect INTEGER NOT NULL,
	tag TEXT NOT NULL,
	FOREIGN KEY (inScene) REFERENCES scene (id) ON DELETE CASCADE,
	FOREIGN KEY (audioFile) REFERENCES audioFile (id) ON DELETE CASCADE);

CREATE TABLE light(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	brightness INTEGER NOT NULL,
	color INTEGER NOT NULL,
	inScene INTEGER NOT NULL,
	FOREIGN KEY (inScene) REFERENCES scene (id) ON DELETE CASCADE);

CREATE TABLE hueBridge(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	user TEXT,
	url TEXT UNIQUE,
	defaultDevice INTEGER NOT NULL,
	deviceName TEXT);

CREATE TABLE hueBulbs(
	id INTEGER PRIMARY KEY NOT NULL,
	hueBridge INTEGER NOT NULL,
	FOREIGN KEY (hueBridge) REFERENCES hueBridge (id) ON DELETE CASCADE);