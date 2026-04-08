# Introduction

# SQL Queries

###### Table Setup (DDL)

```sql
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
	facid INT NOT NULL,
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
```

###### Question 1: Show all members
```sql
--- Modifying Data
--- Question 1: The club is adding a new facility - a spa. We need to add it into the facilities table.
--- The values are: facid: 9, Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.
INSERT INTO cd.facilities
	(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES
	(9, 'Spa', 20, 30, 100000, 800);


--- Question 2: Let's try adding the spa to the facilities table again. 
--- This time, though, we want to automatically generate the value for the next facid, rather than specifying it as a constant.
--- Add entry: Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.
INSERT INTO cd.facilities
	(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES
	((SELECT MAX(facid) FROM cd.facilities) + 1, 'Spa', 20, 30, 100000, 800);

--- Question 3: Update Tennis Court 2's initialoutlay
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';

--- Question 4: Up the cost of the 2nd tennis court so that it's 10% more than the 1st. Don't use constant values.
UPDATE cd.facilities
SET (membercost, guestcost) = (
  SELECT membercost * 1.1, guestcost * 1.1
  FROM cd.facilities 
  WHERE name = 'Tennis Court 1'
)
WHERE
	name = 'Tennis Court 2';

--- Question 5: Delete all entries from bookings
DELETE FROM cd.bookings;
--- You can also use truncate, but that's a little more dangerous.


--- Question 6: Delete a member from the cd.members table
DELETE FROM cd.members
WHERE memid = 37;

--- Basics
--- Question 7: Produce a list of facilities that charge a fee to members, and that fee is less than 1/50th of the monthly maintenance cost.
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE
	membercost < monthlymaintenance / 50.0 AND
	membercost > 0;


--- Question 8:
SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';


--- Question 9:
SELECT *
FROM cd.facilities
WHERE
	facid IN (1, 5);

--- Question 10:
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate > '2012-09-01';

--- Question 11:
SELECT surname
FROM cd.members
UNION
SELECT name as surname
FROM cd.facilities;

--- JOIN
--- Question 12:
SELECT starttime
FROM cd.bookings
INNER JOIN cd.members
ON bookings.memid = members.memid
WHERE 
	members.firstname = 'David' AND
	members.surname = 'Farrell';

--- Question 13:
SELECT starttime AS start, name
FROM cd.bookings
INNER JOIN cd.facilities
ON bookings.facid = facilities.facid
WHERE
	starttime >= '2012-09-21' AND
	starttime < '2012-09-22' AND
	name LIKE '%Tennis Court%'
ORDER BY start;

--- Question 14:
SELECT
	members.firstname AS memfname,
	members.surname AS memsname, 
	recmem.firstname AS recfname, 
	recmem.surname AS recsname
FROM cd.members
LEFT OUTER JOIN cd.members recmem
ON members.recommendedby = recmem.memid
ORDER BY members.surname, members.firstname;

--- Question 15: Output a list of all members who have recommended another member? 
--- Ensure that there are no duplicates in the list, and that results are ordered by (surname, firstname).
SELECT DISTINCT recmem.firstname, recmem.surname
FROM cd.members
INNER JOIN cd.members recmem
ON members.recommendedby = recmem.memid
ORDER BY recmem.surname, recmem.firstname;

--- Question 16: 
SELECT DISTINCT
	members.firstname || ' ' || members.surname AS member, 
	(SELECT recmem.firstname || ' ' || recmem.surname AS recommender
	 FROM cd.members recmem
	 WHERE recmem.memid = members.recommendedby
	) AS recommender
FROM cd.members
ORDER BY member;

--- Aggregation
--- Question 17:
SELECT recommendedby, COUNT(*)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;

--- Question 18:
SELECT facid, SUM(slots) as "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER BY facid;

--- Question 19:
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE 
	starttime >= '2012-09-01' AND
	starttime < '2012-10-01'
GROUP BY facid
ORDER BY "Total Slots";

--- Question 20:
SELECT facid, EXTRACT(MONTH FROM starttime) AS month, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE
	starttime >= '2012-01-01' AND
	starttime < '2013-01-01'
GROUP BY facid, month
ORDER BY facid, month;

--- Question 21:
SELECT COUNT(DISTINCT memid)
FROM cd.bookings;

--- Question 22:
SELECT surname, firstname, members.memid, MIN(starttime)
FROM cd.members
INNER JOIN cd.bookings
ON members.memid = bookings.memid
WHERE starttime >= '2012-09-01'
GROUP BY members.memid
ORDER BY members.memid;

--- Question 23:
SELECT COUNT(memid) OVER(), firstname, surname
FROM cd.members
ORDER BY joindate;

--- Question 24:
SELECT COUNT(*) OVER(ORDER BY joindate) AS row_number, firstname, surname
FROM cd.members;

--- Question 25:
SELECT facid, total
FROM (
  SELECT facid, SUM(slots) AS total, RANK() OVER(ORDER BY SUM(slots) DESC) AS rank
  FROM cd.bookings
  GROUP BY facid
) subquery
WHERE rank = 1;

--- STRINGS
--- Question 26:
SELECT surname || ', ' || firstname AS name
FROM cd.members;

--- Question 27: Find telephone entries with format '(000)'
SELECT memid, telephone
FROM cd.members
WHERE
	telephone SIMILAR TO '%[()]%'
	
--- Question 28:
SELECT LEFT(surname, 1) AS letter, COUNT(*)
FROM cd.members
GROUP BY letter
ORDER BY letter;

```