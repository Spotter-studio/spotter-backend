ALTER TABLE shared_post ADD COLUMN ocr_text TEXT;

CREATE TABLE shared_post_image (
    shared_post_id BIGINT       NOT NULL,
    image_url      VARCHAR(1000) NOT NULL,
    CONSTRAINT fk_spi_shared_post FOREIGN KEY (shared_post_id) REFERENCES shared_post (id)
);
