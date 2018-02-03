ALTER TABLE generalparametervalue ADD COLUMN referencefilecitation_uuid uuid;
ALTER TABLE generalparametervalue_aud ADD COLUMN referencefilecitation_uuid uuid;

ALTER TABLE generalparametervalue 
	ADD CONSTRAINT fk_efh7xxqv733vwpkslinma1es7 FOREIGN KEY (referencefilecitation_uuid)
      REFERENCES ci_citation (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
