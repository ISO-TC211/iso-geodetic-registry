ALTER TABLE engineeringdatum ALTER COLUMN coordinatereferenceepoch TYPE varchar(255);
ALTER TABLE geodeticdatum ALTER COLUMN coordinatereferenceepoch TYPE varchar(255);
ALTER TABLE verticaldatum ALTER COLUMN coordinatereferenceepoch TYPE varchar(255);
ALTER TABLE engineeringdatum ALTER COLUMN realization_epoch TYPE varchar(255);
ALTER TABLE geodeticdatum ALTER COLUMN realization_epoch TYPE varchar(255);
ALTER TABLE verticaldatum ALTER COLUMN realization_epoch TYPE varchar(255);

ALTER TABLE engineeringdatum_aud ALTER COLUMN coordinatereferenceepoch TYPE varchar(255);
ALTER TABLE geodeticdatum_aud ALTER COLUMN coordinatereferenceepoch TYPE varchar(255);
ALTER TABLE verticaldatum_aud ALTER COLUMN coordinatereferenceepoch TYPE varchar(255);
ALTER TABLE engineeringdatum_aud ALTER COLUMN realization_epoch TYPE varchar(255);
ALTER TABLE geodeticdatum_aud ALTER COLUMN realization_epoch TYPE varchar(255);
ALTER TABLE verticaldatum_aud ALTER COLUMN realization_epoch TYPE varchar(255);