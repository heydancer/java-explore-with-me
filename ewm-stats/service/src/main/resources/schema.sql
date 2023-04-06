CREATE TABLE IF NOT EXISTS hits
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    uri       VARCHAR                                         NOT NULL,
    app       VARCHAR                                         NOT NULL,
    ip        VARCHAR                                         NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE                     NOT NULL
);