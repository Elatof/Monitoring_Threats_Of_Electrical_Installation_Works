CREATE TABLE IF NOT EXISTS coordinate
(
    id              SERIAL PRIMARY KEY,
    lat             DOUBLE precision,
    lon             DOUBLE precision,
    part_of_country VARCHAR(32) NOT NULL,

    UNIQUE (lat, lon)
);

CREATE TABLE IF NOT EXISTS weather_condition
(
    id                  SERIAL PRIMARY KEY,
    weather_id          DOUBLE precision NOT NULL,
    main                VARCHAR(32) NOT NULL,
    description         VARCHAR(32) NOT NULL,
    icon                VARCHAR(32) NOT NULL,
    wind_speed          DOUBLE precision NOT NULL,
    threat_level     INT NOT NULL,
    coordinate_id       INT NOT NULL,
    date_time           TIMESTAMP NOT NULL,

    CONSTRAINT fk_coordinate
        FOREIGN KEY (coordinate_id)
            REFERENCES coordinate (id)
);

CREATE TABLE IF NOT EXISTS lightning_coordinate
(
    id                  SERIAL PRIMARY KEY,
    lat                 DOUBLE precision,
    lon                 DOUBLE precision,
    date_time           TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS company
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(32) NOT NULL UNIQUE,
    logo_path  VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_info
(
    id            SERIAL PRIMARY KEY,
    first_name    VARCHAR(32)  NOT NULL,
    second_name   VARCHAR(32)  NOT NULL,
    is_admin      INT          NOT NULL,
    password      VARCHAR(155) NOT NULL,
    email         VARCHAR(32),
    phone         VARCHAR(32),
    day_of_birth  DATE       ,
    title         VARCHAR(32)  NOT NULL,
    company_id    INT NOT NULL,

    CONSTRAINT fk_company
        FOREIGN KEY (company_id)
            REFERENCES company (id),

    UNIQUE (first_name, second_name)
);

CREATE TABLE IF NOT EXISTS alert_info
(
    id            SERIAL PRIMARY KEY,
    date_time     TIMESTAMP NOT NULL,
    user_id       INT NOT NULL,

    CONSTRAINT fk_user_info
        FOREIGN KEY (user_id)
            REFERENCES user_info (id)
);







