CREATE TABLE kq_auth.players (
    player_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    CHECK (LENGTH(username) > 4)
);

CREATE INDEX idx_username ON players (username);