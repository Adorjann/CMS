SET GLOBAL time_zone = '+2:00';

 INSERT INTO user_profile (id, deleted, e_mail, profile_user_name, password, name, last_name, role)
              VALUES (1,false, 'miroslav@maildrop.cc','miroslav','$2y$12$NH2KM2BJaBl.ik90Z1YqAOjoPgSd0ns/bF.7WedMxZ54OhWQNNnh6','Miroslav','Simicee','ADMIN');
 INSERT INTO user_profile (id,deleted, e_mail, profile_user_name, password, name, last_name, role)
              VALUES (2,false,'tamara@maildrop.cc','tamara','$2y$12$DRhCpltZygkA7EZ2WeWIbewWBjLE0KYiUO.tHDUaJNMpsHxXEw9Ky','Tamara','Milosavljevic','KORISNIK');
 INSERT INTO user_profile (id,deleted, e_mail, profile_user_name, password, name, last_name, role)
              VALUES (3,false,'petar@maildrop.cc','petar','$2y$12$i6/mU4w0HhG8RQRXHjNCa.tG2OwGSVXb0GYUnf8MZUdeadE4voHbC','Petar','Jovic','KORISNIK');

