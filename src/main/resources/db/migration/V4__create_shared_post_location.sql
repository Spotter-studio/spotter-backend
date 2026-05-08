CREATE TABLE shared_post_location (
    shared_post_id BIGINT NOT NULL,
    location_id    BIGINT NOT NULL,
    CONSTRAINT pk_shared_post_location PRIMARY KEY (shared_post_id, location_id),
    CONSTRAINT fk_spl_shared_post FOREIGN KEY (shared_post_id) REFERENCES shared_post (id),
    CONSTRAINT fk_spl_location    FOREIGN KEY (location_id)    REFERENCES location (id)
);
