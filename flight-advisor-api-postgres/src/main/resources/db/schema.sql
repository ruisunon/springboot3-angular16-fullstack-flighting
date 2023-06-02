-- Dropping tables
DROP TABLE IF EXISTS route;
DROP TABLE IF EXISTS airport;
DROP TABLE IF EXISTS city_comment;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS country;
DROP TABLE IF EXISTS authority;
DROP TABLE IF EXISTS users;
DROP TYPE IF EXISTS auth_status;
DROP TYPE IF EXISTS dsp_type;

-- Start - Spring Security tables definition
---- Start - User table definition

CREATE TABLE IF NOT EXISTS users
(
    id                BIGSERIAL           PRIMARY KEY,
    user_uuid         UUID UNIQUE,
    first_name        CHARACTER VARYING(100)         NOT NULL,
    last_name         CHARACTER VARYING(100)         NOT NULL,
    username          VARCHAR(255) UNIQUE NOT NULL,
    password          VARCHAR(255) NOT NULL,
    user_role         VARCHAR(50),
    email             VARCHAR(100) NOT NULL UNIQUE,
    avatar            VARCHAR,
    account_enabled BOOLEAN NOT NULL DEFAULT true,
    account_expired BOOLEAN NOT NULL DEFAULT false,
    account_locked BOOLEAN NOT NULL DEFAULT false,
    credentials_expired BOOLEAN NOT NULL DEFAULT false,
    use_2fa BOOLEAN NOT NULL DEFAULT false,
    secret            VARCHAR,
    refresh_token     UUID UNIQUE,
    token_expiry_date timestamp
);
---- End - User table definition

---- Start - Authorities table definition
CREATE TYPE auth_status AS ENUM (
  'ADMIN',
  'CLIENT',
  'MANAGER'
);

CREATE TABLE IF NOT EXISTS authority
(
    user_id   BIGINT     NOT NULL,
    authority auth_status NOT NULL,

    CONSTRAINT authority_pk
        PRIMARY KEY (user_id, authority),

    CONSTRAINT authority_user_fk
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);
---- End - Authorities table definition

-- End - Spring Security tables definition

-- Start - Country, City, airports, and routes tables definition
CREATE TABLE IF NOT EXISTS country
(
    id   BIGSERIAL PRIMARY KEY,
    name CHARACTER VARYING(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS city
(
    id          BIGSERIAL  PRIMARY KEY,
    name        CHARACTER VARYING(100)  NOT NULL,
    description CHARACTER VARYING(100)          ,
    country_id  INT                     NOT NULL,

    CONSTRAINT city_country_fk
        FOREIGN KEY (country_id)
            REFERENCES country (id)
);

CREATE TABLE IF NOT EXISTS city_comment
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    city_id     BIGINT                  NOT NULL,
    user_id     INT                     NOT NULL,
    description CHARACTER VARYING(1000) NOT NULL,
    created_at  TIMESTAMP  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP,

    CONSTRAINT comment_city_fk
        FOREIGN KEY (city_id)
            REFERENCES city (id),

    CONSTRAINT comment_user_fk
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

CREATE TYPE dsp_type AS ENUM (
  'E','A','S','O','Z','N','U'
);


CREATE TABLE IF NOT EXISTS airport
(
    airport_id  BIGSERIAL PRIMARY KEY        NOT NULL,
    name        CHARACTER VARYING(255)       NOT NULL,
    city_id     BIGINT                     NOT NULL,
    country_id  BIGINT                     NOT NULL,
    city        CHARACTER VARYING(100)             NOT NULL,
    country     CHARACTER VARYING(100)             NOT NULL,
    iata        CHAR(3),
    icao        CHAR(4),
    latitude    NUMERIC(12, 6)                     NOT NULL,
    longitude   NUMERIC(12, 6)                     NOT NULL,
    altitude    INT,
    timezone    NUMERIC(3, 1),
    dst         dsp_type  NOT NULL,
    tz          CHARACTER VARYING(50),
    type        CHARACTER VARYING(50)              NOT NULL,
    data_source CHARACTER VARYING(255)             NOT NULL,

    CONSTRAINT airport_city_fk
        FOREIGN KEY (city_id)
            REFERENCES city (id),

    CONSTRAINT airport_country_fk
        FOREIGN KEY (country_id)
            REFERENCES country (id)
);

COMMENT ON COLUMN airport.airport_id IS 'Identifier for this airport.';
COMMENT ON COLUMN airport.name IS 'Name of airport.';
-- COMMENT ON COLUMN airport.city_id IS 'Main city served by airport.';
-- COMMENT ON COLUMN airport.country_id IS 'Country or territory where airport is located.';
COMMENT ON COLUMN airport.iata IS '3-letter IATA code. Null if not assigned/unknown.';
COMMENT ON COLUMN airport.icao IS '4-letter ICAO code. Null if not assigned.';
COMMENT ON COLUMN airport.latitude IS 'Decimal degrees, usually to six significant digits.';
COMMENT ON COLUMN airport.longitude IS 'Decimal degrees, usually to six significant digits.';
COMMENT ON COLUMN airport.altitude IS 'In feet.';
COMMENT ON COLUMN airport.timezone IS 'Hours offset from UTC.';
COMMENT ON COLUMN airport.dst IS 'Daylight savings time. One of E (Europe), A (US/Canada), S (South America), O(Australia), Z (New Zealand), N (None) or U (Unknown).';
COMMENT ON COLUMN airport.tz IS 'Timezone in "tz" (Olson) format, eg. "America/Los_Angeles".';
COMMENT ON COLUMN airport.type IS 'Type of the airport.';
COMMENT ON COLUMN airport.data_source IS 'Source of this data.';


CREATE TABLE IF NOT EXISTS route
(
    airline_code           CHAR(3),
    airline_id             BIGINT,
    source_airport         CHAR(4),
    source_airport_id      BIGINT,
    destination_airport    CHAR(4),
    destination_airport_id BIGINT,
    code_share             BOOLEAN,
    stops                  INT,
    equipment              CHARACTER VARYING(100),
    price                  NUMERIC(6, 3),


    CONSTRAINT route_pk
        PRIMARY KEY (source_airport, destination_airport),

    CONSTRAINT route_source_airport_fk
        FOREIGN KEY (source_airport_id)
            REFERENCES airport (airport_id),

    CONSTRAINT route_destination_airport_fk
        FOREIGN KEY (destination_airport_id)
            REFERENCES airport (airport_id)
);

COMMENT ON COLUMN route.airline_code IS '2-letter (IATA) or 3-letter (ICAO) code of the airline.';
COMMENT ON COLUMN route.airline_id IS 'Identifier for airline.';
COMMENT ON COLUMN route.source_airport IS '3-letter (IATA) or 4-letter (ICAO) code of the source airport.';
COMMENT ON COLUMN route.source_airport_id IS 'Identifier for source airport.';
COMMENT ON COLUMN route.destination_airport IS '3-letter (IATA) or 4-letter (ICAO) code of the destination airport.';
COMMENT ON COLUMN route.destination_airport_id IS 'Unique OpenFlights identifier for destination airport.';
COMMENT ON COLUMN route.code_share IS '"Y" if this flight is a code-share, empty otherwise.';
COMMENT ON COLUMN route.stops IS 'Number of stops on this flight ("0" for direct).';
COMMENT ON COLUMN route.equipment IS '3-letter codes for plane type(s) generally used on this flight, separated by spaces.';
COMMENT ON COLUMN route.price IS 'Flight cost';

-- End - Country, City, airports, and routes tables definition