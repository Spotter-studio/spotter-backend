CREATE TABLE scrap_source_url (
    scrap_id   BIGINT        NOT NULL,
    source_url VARCHAR(1000) NOT NULL,
    CONSTRAINT fk_scrap_source_url_scrap FOREIGN KEY (scrap_id) REFERENCES scrap (id)
);

INSERT INTO scrap_source_url (scrap_id, source_url)
SELECT id, source_url FROM scrap WHERE source_url IS NOT NULL;

ALTER TABLE scrap DROP COLUMN source_url;
