--- Connect to the database.
\c cd

--- Create tbe members table
CREATE TABLE IF NOT EXISTS PUBLIC.members (
	memid INT NOT NULL,
	surname VARCHAR(200) NOT NULL,
	firstname VARCHAR(200) NOT NULL,
	address VARCHAR(300) NOT NULL,
	zipcode INT NOT NULL,
	telephone VARCHAR(20) NOT NULL,
	recommendedby INT,
	joindate timestamp NOT NULL,
	CONSTRAINT members_pk PRIMARY KEY (memid),
	CONSTRAINT fk_members_recommendedby FOREIGN KEY (memid)
		REFERENCES cd.members(memid) ON DELETE SET NULL
);

--- Create the bookings table
CREATE TABLE IF NOT EXISTS PUBLIC.bookings (
	bookid INT NOT NULL,
	facid INT,
	memid INT,
	starttime timestamp,
	slots INT,
	CONSTRAINT bookings_pk PRIMARY KEY (bookid),
	CONSTRAINT fk_bookings_facilities FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
	CONSTRAINT fk_bookings_members FOREIGN KEY (memid) REFERENCES cd.members(memid)
);


--- Create the facilities table
CREATE TABLE IF NOT EXISTS PUBLIC.facilities (
	facid INT NOT NULL,
	name VARCHAR(100) NOT NULL,
	membercost NUMERIC NOT NULL,
	guestcost NUMERIC NOT NULL,
	initialoutlay NUMERIC NOT NULL,
	monthlymaintentence NUMBERIC NOT NULL,
	CONSTRAINT facilities_pk PRIMARY KEY (facid)
);