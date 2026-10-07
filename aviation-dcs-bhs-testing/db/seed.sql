-- Fictional flights (XY = made-up airline code).
-- Some rows are DELIBERATELY inconsistent ("seeded defects") so the validation queries have something to catch.

INSERT INTO flights VALUES (1,'XY101','AUH','DXB','2026-11-01 09:00:00','2026-11-01 09:00:00','CLOSED',3);
INSERT INTO flights VALUES (2,'XY202','AUH','COK','2026-11-01 14:00:00','2026-11-01 15:30:00','DELAYED',180);
INSERT INTO flights VALUES (3,'XY303','AUH','LHR','2026-11-01 08:00:00','2026-11-01 08:00:00','CANCELLED',200);
INSERT INTO flights VALUES (4,'XY304','AUH','LHR','2026-11-01 20:00:00','2026-11-01 20:00:00','SCHEDULED',200);

-- Flight 1 (closed, capacity 3, but 4 passengers checked in -> seeded defect: overbooking)
INSERT INTO passengers VALUES (1,'AB12CD','Amina','Khan',1,'12A','CHECKED_IN','BOARDED');
INSERT INTO passengers VALUES (2,'EF34GH','Rahul','Nair',1,'12B','CHECKED_IN','BOARDED');
INSERT INTO passengers VALUES (3,'IJ56KL','Sara','Ali',1,'14C','CHECKED_IN','NO_SHOW');
INSERT INTO passengers VALUES (4,'MN78OP','Omar','Hassan',1,'15A','CHECKED_IN','BOARDED');

-- Flight 2 (delayed)
INSERT INTO passengers VALUES (5,'QR90ST','Priya','Menon',2,'3A','CHECKED_IN','NOT_BOARDED');
INSERT INTO passengers VALUES (6,'UV12WX','John','Mathew',2,NULL,'NOT_CHECKED','NOT_BOARDED');

-- Flight 3 (cancelled): pax 7 rebooked to flight 4, pax 8 NOT rebooked -> seeded defect
INSERT INTO passengers VALUES (7,'YZ34AB','Lena','Fischer',3,'7A','OFFLOADED','NOT_BOARDED');
INSERT INTO passengers VALUES (8,'CD56EF','Yusuf','Rahman',3,'7B','CHECKED_IN','NOT_BOARDED');
INSERT INTO rebookings VALUES (1,7,3,4,'CONFIRMED');

-- Bags
INSERT INTO bags VALUES (1,'0229123456',1,1,18.5,'LOADED');
INSERT INTO bags VALUES (2,'0229123457',2,1,22.0,'LOADED');
INSERT INTO bags VALUES (3,'0229123458',3,1,15.0,'LOADED');     -- seeded defect: bag loaded, passenger is a no-show
INSERT INTO bags VALUES (4,'0229123459',4,1,34.5,'LOADED');     -- seeded defect: overweight (> 32 kg)
INSERT INTO bags VALUES (5,'0229123460',5,2,20.0,'SORTED');
INSERT INTO bags VALUES (6,'0229123461',7,4,19.0,'CHECKED_IN');

-- Bag audit trail (bag 3 has no SORTED event -> seeded defect: missing scan)
INSERT INTO bag_events VALUES (1,1,'CHECKIN-DESK','CHECKED_IN','2026-11-01 06:30:00');
INSERT INTO bag_events VALUES (2,1,'SORTER-01','SORTED','2026-11-01 06:45:00');
INSERT INTO bag_events VALUES (3,1,'GATE-A5','LOADED','2026-11-01 08:10:00');
INSERT INTO bag_events VALUES (4,3,'CHECKIN-DESK','CHECKED_IN','2026-11-01 06:40:00');
INSERT INTO bag_events VALUES (5,3,'GATE-A5','LOADED','2026-11-01 08:12:00');

INSERT INTO disruptions VALUES (1,2,'DELAY','Late inbound aircraft',90);
INSERT INTO disruptions VALUES (2,3,'CANCELLATION','Technical issue',NULL);
INSERT INTO bag_events VALUES (6,2,'CHECKIN-DESK','CHECKED_IN','2026-11-01 06:35:00');
INSERT INTO bag_events VALUES (7,2,'SORTER-01','SORTED','2026-11-01 06:50:00');
INSERT INTO bag_events VALUES (8,2,'GATE-A5','LOADED','2026-11-01 08:11:00');
INSERT INTO bag_events VALUES (9,4,'CHECKIN-DESK','CHECKED_IN','2026-11-01 06:42:00');
INSERT INTO bag_events VALUES (10,4,'SORTER-01','SORTED','2026-11-01 06:55:00');
INSERT INTO bag_events VALUES (11,4,'GATE-A5','LOADED','2026-11-01 08:13:00');
