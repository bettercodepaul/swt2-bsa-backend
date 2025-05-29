-- Add missing BasicDAO audit columns to schusszettel_tablet_session table
-- These columns are required for BasicDAO compatibility
-- Note: This will not fail anymore in development where V25 already created these columns, since we used IF NOT EXISTS
ALTER TABLE schusszettel_tablet_session
    ADD COLUMN IF NOT EXISTS created_at_utc TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS created_by BIGINT NOT NULL DEFAULT -1,
    ADD COLUMN IF NOT EXISTS last_modified_at_utc TIMESTAMP NULL,
    ADD COLUMN IF NOT EXISTS last_modified_by BIGINT NULL,
    ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;

-- Create missing indexes for performance
CREATE INDEX IF NOT EXISTS idx_tablet_session_token ON schusszettel_tablet_session(token);
CREATE INDEX IF NOT EXISTS idx_tablet_session_lookup ON schusszettel_tablet_session(wettkampf_id, team_id);