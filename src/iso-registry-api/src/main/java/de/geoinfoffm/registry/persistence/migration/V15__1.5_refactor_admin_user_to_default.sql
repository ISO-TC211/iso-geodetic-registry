-- admin@example.org change to "geodetic.register@ribose.com" , reset password "a"
-- this is to reset as Ribose required on May 24 2019

UPDATE "public"."registryuser"
SET "emailaddress" = 'geodetic.register2@ribose.com',
    "passwordhash" = '$2a$12$w8AoYGP2UxTsnrajt0UJ6eExjm1AD/IG2RD87wx5qoXkKXiCEgYT6'
WHERE "emailaddress" = 'admin@example.org';
