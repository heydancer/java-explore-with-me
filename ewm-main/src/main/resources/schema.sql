CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR UNIQUE NOT NULL,
    email VARCHAR UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title  VARCHAR,
    pinned BOOLEAN
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION

);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title              VARCHAR NOT NULL,
    annotation         VARCHAR NOT NULL,
    description        VARCHAR NOT NULL,
    state              VARCHAR NOT NULL,
    paid               BOOLEAN NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    confirmed_requests BIGINT,
    participant_limit  BIGINT,
    created            TIMESTAMP WITHOUT TIME ZONE,
    published          TIMESTAMP WITHOUT TIME ZONE,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    category_id        BIGINT REFERENCES categories (id),
    initiator_id       BIGINT REFERENCES users (id) ON DELETE CASCADE,
    location_id        BIGINT REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS compilation_event
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id       BIGINT REFERENCES events (id) ON DELETE CASCADE,
    compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status       VARCHAR                     NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id     BIGINT REFERENCES events (id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text      VARCHAR(3000)               NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated   TIMESTAMP WITHOUT TIME ZONE,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    event_id  BIGINT REFERENCES events (id) ON DELETE CASCADE
);