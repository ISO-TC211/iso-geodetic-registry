ALTER TABLE engineeringcrs RENAME COLUMN conversion_uuid TO operation_uuid;
ALTER TABLE engineeringcrs_aud RENAME COLUMN conversion_uuid TO operation_uuid;

ALTER TABLE geodeticcrs RENAME COLUMN conversion_uuid TO operation_uuid;
ALTER TABLE geodeticcrs_aud RENAME COLUMN conversion_uuid TO operation_uuid;

ALTER TABLE projectedcrs RENAME COLUMN conversion_uuid TO operation_uuid;
ALTER TABLE projectedcrs_aud RENAME COLUMN conversion_uuid TO operation_uuid;

ALTER TABLE verticalcrs RENAME COLUMN conversion_uuid TO operation_uuid;
ALTER TABLE verticalcrs_aud RENAME COLUMN conversion_uuid TO operation_uuid;