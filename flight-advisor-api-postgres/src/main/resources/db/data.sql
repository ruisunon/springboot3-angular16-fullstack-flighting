---- Inserting data
------ Add an admin user
------ Password is: Admin1234
INSERT INTO USERS (user_uuid, first_name, last_name, username, password,account_enabled, email)
VALUES (gen_random_uuid (), 'ry', 'sum', 'admin@traveladvisor.com',
        '$$$secrets%%%',true,'bob@doe.com');
INSERT INTO users (username, password, user_role, first_name, last_name, email)
VALUES ('bob', '$2a$10$esolmUvFZDqSIE744dU5V.5dPxBk0.xzjDXe7Gim4tou7DXYBLa4q', 'USER', 'Bob', 'Doe', 'bodb@doe.com');
INSERT INTO users (username, password, user_role, first_name, last_name, email)
VALUES ('alice', '$2a$10$esolmUvFZDqSIE744dU5V.5dPxBk0.xzjDXe7Gim4tou7DXYBLa4q', 'ADMIN', 'Alice', 'Doe', 'alice@doe.com');
COMMIT;
------ Add user authorities
INSERT INTO AUTHORITY (user_id, authority) VALUES (1,'ADMIN');
INSERT INTO AUTHORITY (user_id, authority) VALUES (1,'CLIENT');

------ Add a country
INSERT INTO COUNTRY (NAME)
VALUES ('Egypt');

------ Add a city
INSERT INTO CITY (COUNTRY_ID, NAME, DESCRIPTION)
VALUES (1, 'Cairo', 'My home town.');

------ Add a city comments
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Nice city to visit 1', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 2', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 3', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 4', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 5', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 6', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 7', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 8', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 9', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 10', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 11', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 12', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 13', 1);
INSERT INTO CITY_COMMENT (CITY_ID, DESCRIPTION, USER_ID)
VALUES (1, 'Great city to visit 14', 1);

------ Add airports
INSERT INTO AIRPORT( AIRPORT_ID, NAME, CITY_ID, COUNTRY_ID, CITY, COUNTRY
                   , IATA, ICAO, LATITUDE, LONGITUDE, ALTITUDE, TIMEZONE, DST, TZ, TYPE
                   , DATA_SOURCE)
VALUES (111111111, 'XXX Airport', 1, 1, 'Goroka 11111', 'Papua New Guinea 11', 'XXX', 'XXXA',
        -6.081689834590001, 145.391998291, 5282, 10, 'U', 'Pacific/Port_Moresby', 'airport',
        'OurAirports');

INSERT INTO AIRPORT( AIRPORT_ID, NAME, CITY_ID, COUNTRY_ID, CITY, COUNTRY, IATA
                   , ICAO, LATITUDE, LONGITUDE, ALTITUDE, TIMEZONE, DST, TZ, TYPE, DATA_SOURCE)
VALUES (222222222, 'YYY Airport', 1, 1, 'Goroka 22222', 'Papua New Guinea 22', 'YYY', 'YYYA',
        76.5311965942, -68.7032012939, 5282, 10, 'U', 'Pacific/Port_Moresby', 'airport',
        'OurAirports');

INSERT INTO AIRPORT( AIRPORT_ID, NAME, CITY_ID, COUNTRY_ID, CITY, COUNTRY, IATA
                   , ICAO, LATITUDE, LONGITUDE, ALTITUDE, TIMEZONE, DST, TZ, TYPE, DATA_SOURCE)
VALUES (333333333, 'ZZZ Airport', 1, 1, 'Goroka 33333', 'Papua New Guinea 33', 'ZZZ', 'ZZZA',
        61.1604995728, -45.4259986877, 5282, 10, 'U', 'Pacific/Port_Moresby', 'airport',
        'OurAirports');

------ Add routes
INSERT INTO ROUTE( AIRLINE_CODE, AIRLINE_ID, SOURCE_AIRPORT, SOURCE_AIRPORT_ID
                 , DESTINATION_AIRPORT, DESTINATION_AIRPORT_ID, CODE_SHARE, STOPS, EQUIPMENT, PRICE)
VALUES ('2B', 410, 'XXX', 111111111, 'YYY', 222222222, false, 0, 'CR2', 95.87);

INSERT INTO ROUTE( AIRLINE_CODE, AIRLINE_ID, SOURCE_AIRPORT, SOURCE_AIRPORT_ID
                 , DESTINATION_AIRPORT, DESTINATION_AIRPORT_ID, CODE_SHARE, STOPS, EQUIPMENT, PRICE)
VALUES ('2B', 410, 'YYY', 222222222, 'ZZZ',333333333, false, 0, 'CR2', 98.25);