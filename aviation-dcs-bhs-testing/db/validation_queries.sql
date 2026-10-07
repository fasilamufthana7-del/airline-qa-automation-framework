-- Each query should return ZERO rows on healthy data. Any row returned = a defect to log.

-- V1 [DCS-06] Overbooking: checked-in passengers exceed flight capacity
SELECT f.flight_no, f.capacity, COUNT(*) AS checked_in
FROM flights f JOIN passengers p ON p.flight_id = f.flight_id
WHERE p.checkin_status = 'CHECKED_IN'
GROUP BY f.flight_id, f.flight_no, f.capacity
HAVING COUNT(*) > f.capacity;

-- V2 [DCS-02] Duplicate seat assignment on the same flight
SELECT flight_id, seat_no, COUNT(*) AS pax_count
FROM passengers
WHERE seat_no IS NOT NULL
GROUP BY flight_id, seat_no
HAVING COUNT(*) > 1;

-- V3 [DCS-03] Checked-in passenger without a seat
SELECT pax_id, pnr, last_name FROM passengers
WHERE checkin_status = 'CHECKED_IN' AND seat_no IS NULL;

-- V4 [DCS-04] Boarded passenger who never checked in
SELECT pax_id, pnr, last_name FROM passengers
WHERE boarding_status = 'BOARDED' AND checkin_status <> 'CHECKED_IN';

-- V5 [BHS-04] Passenger-bag reconciliation: bag LOADED but passenger did not board
SELECT b.bag_tag, p.pnr, p.last_name, p.boarding_status
FROM bags b JOIN passengers p ON p.pax_id = b.pax_id
WHERE b.status = 'LOADED' AND p.boarding_status <> 'BOARDED';

-- V6 [BHS-02] Bag over the 32 kg limit
SELECT bag_tag, weight_kg FROM bags WHERE weight_kg > 32;

-- V7 [BHS-01] Bag tag is not exactly 10 digits
SELECT bag_tag FROM bags
WHERE LENGTH(bag_tag) <> 10 OR bag_tag GLOB '*[^0-9]*';  -- GLOB is SQLite; in MySQL use: bag_tag NOT REGEXP '^[0-9]{10}$'

-- V8 [BHS-05] LOADED bag with no SORTED scan in its audit trail (missing scan)
SELECT b.bag_tag
FROM bags b
WHERE b.status = 'LOADED'
  AND NOT EXISTS (SELECT 1 FROM bag_events e WHERE e.bag_id = b.bag_id AND e.event_type = 'SORTED');

-- V9 [DIS-02] Cancelled flight: passenger neither rebooked nor offloaded
SELECT p.pnr, p.last_name, f.flight_no
FROM passengers p JOIN flights f ON f.flight_id = p.flight_id
WHERE f.status = 'CANCELLED'
  AND NOT EXISTS (SELECT 1 FROM rebookings r WHERE r.pax_id = p.pax_id);

-- V10 [DIS-01] Delayed flight where estimated departure was not updated
SELECT flight_no FROM flights
WHERE status = 'DELAYED' AND est_dep <= sched_dep;

-- V11 [DIS-03] Rebooked passenger whose bag is still tagged to the old flight
SELECT b.bag_tag, r.old_flight_id, r.new_flight_id
FROM rebookings r JOIN bags b ON b.pax_id = r.pax_id
WHERE r.status = 'CONFIRMED' AND b.flight_id = r.old_flight_id;
