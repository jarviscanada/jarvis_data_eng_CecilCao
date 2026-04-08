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
