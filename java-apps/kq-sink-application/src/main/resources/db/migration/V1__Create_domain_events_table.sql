CREATE TABLE kq_events.domain_events (
    game_id UUID NOT NULL,
    player_id BIGINT NOT NULL,
    event_stream_id INTEGER NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    event_status VARCHAR(50) NOT NULL,
    event_data BYTEA NOT NULL
);

CREATE INDEX idx_game_id_event_stream_id ON domain_events (game_id, event_stream_id);
