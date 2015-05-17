ALTER TABLE epsgisomapping
	ALTER COLUMN isocode DROP NOT NULL;
	
ALTER TABLE epsgisomapping
	ADD COLUMN isouuid uuid;
	
ALTER TABLE ONLY epsgisomapping
    DROP CONSTRAINT uk_rj6338g473ygmddvreal3y6oc;

ALTER TABLE ONLY epsgisomapping
    ADD CONSTRAINT uk_rj6338g473ygmddvreal3y6oc UNIQUE (isouuid);
