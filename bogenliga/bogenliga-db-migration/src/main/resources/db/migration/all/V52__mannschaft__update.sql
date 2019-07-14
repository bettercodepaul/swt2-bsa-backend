-- zur mannschaft wird noch ein sortierkriterium abgelegt, dass für den Fall des Gleichstands von match und Satzpunkte
-- die Sortierung bestimmt, später auch über einen Dialog zu setzen sein soll.

ALTER TABLE mannschaft
    ADD COLUMN
        -- an auto-incremented unique attribute, for easier internal access
        mannschaft_sortierung DECIMAL(2, 0) NOT NULL DEFAULT 0

;

