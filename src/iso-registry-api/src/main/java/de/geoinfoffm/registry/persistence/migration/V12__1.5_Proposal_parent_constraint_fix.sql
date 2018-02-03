ALTER TABLE proposal DROP CONSTRAINT fk_4on5n216wmt470pwa75nh0woy;
ALTER TABLE ONLY proposal ADD CONSTRAINT fk_4on5n216wmt470pwa75nh0woy FOREIGN KEY (parent_uuid) REFERENCES proposal(uuid);
