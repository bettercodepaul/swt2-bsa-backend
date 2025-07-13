-- Clear tablet schusszettel sessions to remove stale data from old initialization logic
-- This ensures all sessions are re-created with the enhanced initialization logic

-- Clear all existing tablet sessions
-- The admin component will re-create them with correct state detection
DELETE FROM schusszettel_tablet_session;

-- Reset the sequence to start from 1 again
ALTER SEQUENCE schusszettel_tablet_session_id_seq RESTART WITH 1;