
-- users
ALTER TABLE users ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();

-- friendship
ALTER TABLE friendship ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();

-- meetups
ALTER TABLE meetups ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();

-- meetup_invitations
ALTER TABLE meetup_invitations ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();

-- meetup_participants: created_at, updated_at 모두 추가 (joined_at 은 도메인 필드로 유지)
ALTER TABLE meetup_participants ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT NOW();
ALTER TABLE meetup_participants ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();

-- scrap
ALTER TABLE scrap ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();

-- staged_data
ALTER TABLE staged_data ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();
