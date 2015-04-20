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
	
CREATE TABLE identifieditem_ci_citation
(
  identifieditem_uuid uuid NOT NULL,
  informationsourcecitation_uuid uuid NOT NULL,
  CONSTRAINT identifieditem_ci_citation_pkey PRIMARY KEY (identifieditem_uuid, informationsourcecitation_uuid),
  CONSTRAINT fk_krg9ssrb9kwjpuycprcfapba9 FOREIGN KEY (informationsourcecitation_uuid)
      REFERENCES ci_citation (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_krg9ssrb9kwjpuycprcfapba9 UNIQUE (informationsourcecitation_uuid)
);

CREATE TABLE identifieditem_ci_citation_aud
(
  rev integer NOT NULL,
  identifieditem_uuid uuid NOT NULL,
  informationsourcecitation_uuid uuid NOT NULL,
  revtype smallint,
  CONSTRAINT identifieditem_ci_citation_aud_pkey PRIMARY KEY (rev, identifieditem_uuid, informationsourcecitation_uuid),
  CONSTRAINT fk_qwlebgxtr9a15k19aohg4cn1u FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

