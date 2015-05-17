ALTER TABLE concatenatedoperationitem
	ADD COLUMN accuracy integer;
	
ALTER TABLE concatenatedoperationitem_aud
	ADD COLUMN accuracy integer;

ALTER TABLE passthroughoperationitem
	ADD COLUMN accuracy integer;
	
ALTER TABLE passthroughoperationitem_aud
	ADD COLUMN accuracy integer;
	
ALTER TABLE conversionitem
	ADD COLUMN accuracy integer;
	
ALTER TABLE conversionitem_aud
	ADD COLUMN accuracy integer;
	
ALTER TABLE transformationitem
	ADD COLUMN accuracy integer;
	
ALTER TABLE transformationitem_aud
	ADD COLUMN accuracy integer;

DROP TABLE singleoperationitem;
DROP TABLE singleoperationitem_aud;