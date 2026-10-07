-- Simulated DCS / BHS / Disruption data model (ANSI SQL; runs on H2, MySQL, PostgreSQL, SQLite)
CREATE TABLE flights (
  flight_id     INTEGER PRIMARY KEY,
  flight_no     VARCHAR(8)  NOT NULL,
  origin        CHAR(3)     NOT NULL,
  destination   CHAR(3)     NOT NULL,
  sched_dep     TIMESTAMP   NOT NULL,
  est_dep       TIMESTAMP   NOT NULL,
  status        VARCHAR(12) NOT NULL CHECK (status IN ('SCHEDULED','DELAYED','CANCELLED','CLOSED','DEPARTED')),
  capacity      INTEGER     NOT NULL
);

CREATE TABLE passengers (
  pax_id          INTEGER PRIMARY KEY,
  pnr             CHAR(6)     NOT NULL,
  first_name      VARCHAR(40) NOT NULL,
  last_name       VARCHAR(40) NOT NULL,
  flight_id       INTEGER     NOT NULL REFERENCES flights(flight_id),
  seat_no         VARCHAR(4),
  checkin_status  VARCHAR(12) NOT NULL CHECK (checkin_status IN ('NOT_CHECKED','CHECKED_IN','OFFLOADED')),
  boarding_status VARCHAR(12) NOT NULL CHECK (boarding_status IN ('NOT_BOARDED','BOARDED','NO_SHOW')),
  UNIQUE (flight_id, seat_no)
);

CREATE TABLE bags (
  bag_id     INTEGER PRIMARY KEY,
  bag_tag    CHAR(10)      NOT NULL UNIQUE,   -- 10-digit licence plate
  pax_id     INTEGER       NOT NULL REFERENCES passengers(pax_id),
  flight_id  INTEGER       NOT NULL REFERENCES flights(flight_id),
  weight_kg  DECIMAL(4,1)  NOT NULL CHECK (weight_kg > 0),
  status     VARCHAR(12)   NOT NULL CHECK (status IN ('CHECKED_IN','SORTED','LOADED','OFFLOADED','MISHANDLED'))
);

CREATE TABLE bag_events (
  event_id    INTEGER PRIMARY KEY,
  bag_id      INTEGER     NOT NULL REFERENCES bags(bag_id),
  location    VARCHAR(20) NOT NULL,
  event_type  VARCHAR(12) NOT NULL,
  event_time  TIMESTAMP   NOT NULL
);

CREATE TABLE disruptions (
  disruption_id  INTEGER PRIMARY KEY,
  flight_id      INTEGER     NOT NULL REFERENCES flights(flight_id),
  type           VARCHAR(12) NOT NULL CHECK (type IN ('DELAY','CANCELLATION')),
  reason         VARCHAR(60),
  delay_minutes  INTEGER
);

CREATE TABLE rebookings (
  rebooking_id  INTEGER PRIMARY KEY,
  pax_id        INTEGER     NOT NULL REFERENCES passengers(pax_id),
  old_flight_id INTEGER     NOT NULL REFERENCES flights(flight_id),
  new_flight_id INTEGER     NOT NULL REFERENCES flights(flight_id),
  status        VARCHAR(12) NOT NULL CHECK (status IN ('PENDING','CONFIRMED'))
);
