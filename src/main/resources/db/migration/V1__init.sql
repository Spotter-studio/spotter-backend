CREATE TABLE category (
    id   INTEGER      NOT NULL,
    name VARCHAR(50)  NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE users (
    id              BIGSERIAL    NOT NULL,
    name            VARCHAR(50)  NOT NULL,
    email           VARCHAR(100) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    preference_doc  VARCHAR(2000) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE location (
    id                BIGSERIAL       NOT NULL,
    latitude          DECIMAL(10, 7)  NOT NULL,
    longitude         DECIMAL(10, 7)  NOT NULL,
    category_id       INTEGER         NOT NULL,
    naver_place_id    VARCHAR(100),
    name              VARCHAR(255)    NOT NULL,
    address           VARCHAR(500),
    total_scrap_count INTEGER         NOT NULL DEFAULT 0,
    created_at        TIMESTAMP       NOT NULL,
    updated_at        TIMESTAMP,
    CONSTRAINT pk_location PRIMARY KEY (id),
    CONSTRAINT fk_location_category FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE friendship (
    id          BIGSERIAL NOT NULL,
    user_id     BIGINT    NOT NULL,
    friend_id   BIGINT    NOT NULL,
    status      INTEGER   NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    accepted_at TIMESTAMP,
    CONSTRAINT pk_friendship PRIMARY KEY (id),
    CONSTRAINT uk_friendship_user_friend UNIQUE (user_id, friend_id),
    CONSTRAINT fk_friendship_user   FOREIGN KEY (user_id)   REFERENCES users (id),
    CONSTRAINT fk_friendship_friend FOREIGN KEY (friend_id) REFERENCES users (id)
);

CREATE TABLE meetups (
    id               BIGSERIAL    NOT NULL,
    host_id          BIGINT       NOT NULL,
    location_id      BIGINT       NOT NULL,
    title            VARCHAR(200) NOT NULL,
    description      TEXT,
    meetup_at        TIMESTAMP    NOT NULL,
    max_participants INTEGER      NOT NULL DEFAULT 2,
    status           INTEGER      NOT NULL,
    created_at       TIMESTAMP    NOT NULL,
    visibility       INTEGER      NOT NULL,
    CONSTRAINT pk_meetups PRIMARY KEY (id),
    CONSTRAINT fk_meetups_host     FOREIGN KEY (host_id)     REFERENCES users (id),
    CONSTRAINT fk_meetups_location FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE TABLE meetup_invitations (
    id           BIGSERIAL NOT NULL,
    invited_by   BIGINT    NOT NULL,
    user_id      BIGINT    NOT NULL,
    meetup_id    BIGINT    NOT NULL,
    status       INTEGER   NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    responded_at TIMESTAMP,
    CONSTRAINT pk_meetup_invitations PRIMARY KEY (id),
    CONSTRAINT uk_meetup_invitations_meetup_user UNIQUE (meetup_id, user_id),
    CONSTRAINT fk_meetup_invitations_invited_by FOREIGN KEY (invited_by) REFERENCES users (id),
    CONSTRAINT fk_meetup_invitations_user       FOREIGN KEY (user_id)    REFERENCES users (id),
    CONSTRAINT fk_meetup_invitations_meetup     FOREIGN KEY (meetup_id)  REFERENCES meetups (id)
);

CREATE TABLE meetup_participants (
    id        BIGSERIAL NOT NULL,
    user_id   BIGINT    NOT NULL,
    meetup_id BIGINT    NOT NULL,
    joined_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_meetup_participants PRIMARY KEY (id),
    CONSTRAINT uk_meetup_participants_meetup_user UNIQUE (meetup_id, user_id),
    CONSTRAINT fk_meetup_participants_user   FOREIGN KEY (user_id)   REFERENCES users (id),
    CONSTRAINT fk_meetup_participants_meetup FOREIGN KEY (meetup_id) REFERENCES meetups (id)
);

CREATE TABLE scrap (
    id           BIGSERIAL    NOT NULL,
    user_id      BIGINT       NOT NULL,
    location_id  BIGINT       NOT NULL,
    source_url   VARCHAR(1000),
    source_type  VARCHAR(20),
    visit_count  INTEGER      NOT NULL DEFAULT 0,
    created_at   TIMESTAMP    NOT NULL,
    CONSTRAINT pk_scrap PRIMARY KEY (id),
    CONSTRAINT uk_scrap_user_location UNIQUE (user_id, location_id),
    CONSTRAINT fk_scrap_user     FOREIGN KEY (user_id)     REFERENCES users (id),
    CONSTRAINT fk_scrap_location FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE TABLE staged_data (
    id          BIGSERIAL    NOT NULL,
    user_id     BIGINT       NOT NULL,
    source_url  VARCHAR(1000),
    source_type VARCHAR(20),
    created_at  TIMESTAMP    NOT NULL,
    CONSTRAINT pk_staged_data PRIMARY KEY (id),
    CONSTRAINT fk_staged_data_user FOREIGN KEY (user_id) REFERENCES users (id)
);
