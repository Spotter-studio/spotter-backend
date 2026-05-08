ALTER TABLE staged_data RENAME TO shared_post;

ALTER TABLE shared_post ADD COLUMN status INTEGER NOT NULL DEFAULT 0;
