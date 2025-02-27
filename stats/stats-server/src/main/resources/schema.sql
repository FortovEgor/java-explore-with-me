DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits (
    id         BIGSERIAL PRIMARY KEY,
    app        VARCHAR(255),
    uri        VARCHAR(255),
    ip         VARCHAR(15),
    timestamp  TIMESTAMP
);

CREATE INDEX IF NOT EXISTS hits_timestamp_index ON hits (timestamp);  -- btree(default), т.к. сравнения на > / <
CREATE INDEX IF NOT EXISTS hits_uri_index ON hits USING HASH (uri);  -- hash, т.к. только сравнения на ==
CREATE INDEX IF NOT EXISTS hits_app_index ON hits USING HASH (app);  -- hash, т.к. только сравнения на ==