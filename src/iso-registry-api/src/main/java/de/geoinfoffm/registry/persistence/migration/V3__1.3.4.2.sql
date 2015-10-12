ALTER TABLE registryuser
	DROP COLUMN confirmationToken,
	ADD COLUMN confirmationToken uuid;

ALTER TABLE registryuser_aud
	DROP COLUMN confirmationToken,
	ADD COLUMN confirmationToken uuid;
