DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS participations CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    initiator_id BIGINT NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_lat REAL NOT NULL,
    location_lon REAL NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT false,
    participant_limit INTEGER NOT NULL DEFAULT 0,
    request_moderation BOOLEAN NOT NULL DEFAULT true,
    state VARCHAR NOT NULL DEFAULT 'PENDING',
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT category_id_fk FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    CONSTRAINT initiator_id_fk FOREIGN KEY(initiator_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT title_limits CHECK (3 <= length(title) AND length(title) <= 120),
    CONSTRAINT annotation_limits CHECK (20 <= length(annotation) AND length(annotation) <= 2000),
    CONSTRAINT description_limits CHECK (20 <= length(description) AND length(description) <= 7000),
    CONSTRAINT location_lat_limits CHECK (-90 <= location_lat AND location_lat <= 90),
    CONSTRAINT location_lon_limits CHECK (-180 <= location_lon AND location_lon < 180),
    CONSTRAINT events_participant_limit_limits CHECK (participant_limit >= 0),
    CONSTRAINT state_values CHECK (state IN ('PENDING', 'PUBLISHED', 'CANCELED'))
);

CREATE TABLE participations (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT requester_fk FOREIGN KEY(requester_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT event_fk FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT status_values CHECK (status IN ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELED')),
    CONSTRAINT participations_uniqueness UNIQUE (event_id, requester_id)
);

CREATE TABLE compilations (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) UNIQUE NOT NULL,
    pinned BOOLEAN NOT NULL DEFAULT false,

    CONSTRAINT title_limits CHECK (length(title) >= 1)
);

CREATE TABLE compilations_events (
    compilation_id BIGINT,
    events_id BIGINT,

    CONSTRAINT compilation_fk FOREIGN KEY(compilation_id) REFERENCES compilations(id) ON DELETE CASCADE,
    CONSTRAINT events_event_fk FOREIGN KEY(events_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE comments (
     id BIGSERIAL PRIMARY KEY,
     text VARCHAR(2000) NOT NULL,
     author_id BIGINT NOT NULL,
     event_id BIGINT NOT NULL,
     created TIMESTAMP NOT NULL,
     edited TIMESTAMP,  -- if null, then comment was not edited

     CONSTRAINT comments_author_fk FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE CASCADE,
     CONSTRAINT comments_event_fk FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE
 );

-- TODO: create indexes according to database analytics