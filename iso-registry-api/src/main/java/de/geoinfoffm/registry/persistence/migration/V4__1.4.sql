ALTER TABLE ci_citation
	DROP COLUMN editionDate,
	DROP COLUMN name,
	DROP COLUMN issueIdentification,
	DROP COLUMN page,
	ADD COLUMN editionDate varchar(255),
	ADD COLUMN series_name text,
	ADD COLUMN series_issueIdentification text,
	ADD COLUMN series_page text;
	
ALTER TABLE ci_citation_aud
	DROP COLUMN editionDate,
	DROP COLUMN name,
	DROP COLUMN issueIdentification,
	DROP COLUMN page,
	ADD COLUMN editionDate varchar(255),
	ADD COLUMN series_name text,
	ADD COLUMN series_issueIdentification text,
	ADD COLUMN series_page text;
	
ALTER TABLE ci_citation_date
	DROP COLUMN date_date,
	ADD COLUMN date_date text;
	
ALTER TABLE ci_citation_date_aud
	DROP COLUMN date_date,
	ADD COLUMN date_date text;