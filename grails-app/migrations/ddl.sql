
CREATE TABLE address
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    address_line3 VARCHAR(255),
    address_line4 VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    postal_code VARCHAR(255),
    state VARCHAR(255)
);
CREATE TABLE location
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL
);

CREATE TABLE login
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    account_expired BIT NOT NULL,
    account_locked BIT NOT NULL,
    enabled BIT NOT NULL,
    password VARCHAR(255) NOT NULL,
    password_expired BIT NOT NULL,
    username VARCHAR(255) NOT NULL
);
CREATE TABLE role
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    authority VARCHAR(255) NOT NULL,
    date_created DATETIME NOT NULL,
    is_active BIT NOT NULL,
    last_updated DATETIME NOT NULL
);
CREATE TABLE member
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    account_status VARCHAR(255) NOT NULL,
    current_address_id BIGINT UNSIGNED,
    date_created DATETIME NOT NULL,
    description VARCHAR(255),
    email VARCHAR(255),
    fax VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    gender VARCHAR(255),
    last_name VARCHAR(255),
    last_updated DATETIME NOT NULL,
    login_id BIGINT UNSIGNED NOT NULL,
    member_type VARCHAR(255) NOT NULL,
    permanent_address_id BIGINT UNSIGNED,
    primary_phone VARCHAR(255),
    secondary_phone VARCHAR(255),
    title VARCHAR(255),
    uuid VARCHAR(255),
    FOREIGN KEY (permanent_address_id) REFERENCES address (id),
    FOREIGN KEY (current_address_id) REFERENCES address (id),
    FOREIGN KEY (login_id) REFERENCES login (id)
);
CREATE TABLE registration
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    email_verification_sent_count INT NOT NULL,
    email_verified BIT NOT NULL,
    email_verified_date DATETIME,
    first_name VARCHAR(255) NOT NULL,
    last_email_verification_sent_date DATETIME,
    last_name VARCHAR(255),
    last_phone_verification_sent_date DATETIME,
    phone_number VARCHAR(255) NOT NULL,
    phone_number_verified BIT NOT NULL,
    phone_number_verified_date DATETIME,
    phone_verification_sent_count INT NOT NULL,
    registration_date DATETIME NOT NULL,
    uuid VARCHAR(255),
    verification_attempts INT NOT NULL,
    verification_code VARCHAR(255),
    verification_code_expiery_date DATETIME,
    verification_status VARCHAR(255) NOT NULL,
    tnc_checked BIT NOT NULL,
    last_failed_verification_attempt_date DATETIME,
    member_id BIGINT UNSIGNED DEFAULT NULL,
    FOREIGN KEY (member_id) REFERENCES member(ID)
);
CREATE TABLE doctor
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(165),
    GENDER VARCHAR(165),
    WEBSITE VARCHAR(240),
    PHONE VARCHAR(50),
    FAX VARCHAR(240),
    EMAIL VARCHAR(50),
    DEGREE1 VARCHAR(240),
    UNIV1 VARCHAR(240),
    DEGREE2 VARCHAR(210),
    UNIV2 VARCHAR(210),
    DEGREE3 VARCHAR(240),
    UNIV3 VARCHAR(240),
    DEGREE4 VARCHAR(240),
    UNIV4 VARCHAR(240),
    DEGREE5 VARCHAR(240),
    UNIV5 VARCHAR(240),
    EXPERIENCE VARCHAR(240),
    AWARDS VARCHAR(240),
    LANGUAGE VARCHAR(240),
    VELKARE_VERIFIED TINYINT DEFAULT 0,
    CREATED_BY VARCHAR(240),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY VARCHAR(240),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE hospital
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    HOSPITAL_GEOCODE VARCHAR(240),
    NAME VARCHAR(240) NOT NULL,
    ADDRESS1 VARCHAR(450),
    ADDRESS2 VARCHAR(450),
    ADDRESS3 VARCHAR(450),
    ADDRESS4 VARCHAR(450),
    CITY VARCHAR(450),
    DISTRICT VARCHAR(450),
    STATE VARCHAR(450),
    COUNTRY VARCHAR(360),
    POSTAL_CODE VARCHAR(10),
    WEBSITE VARCHAR(360),
    PHONE VARCHAR(50),
    FAX VARCHAR(450),
    EMAIL VARCHAR(50),
    SPECIALISTS VARCHAR(360),
    VELKARE_VERIFIED TINYINT DEFAULT 0,
    CREATED_BY DECIMAL(12,0),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY DECIMAL(12,0),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT NOT NULL,
    hos_geocode VARCHAR(255) NOT NULL
);
CREATE TABLE lab
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(300),
    ADDRESS1 VARCHAR(900),
    ADDRESS2 VARCHAR(900),
    ADDRESS3 VARCHAR(900),
    ADDRESS4 VARCHAR(900),
    CITY VARCHAR(900),
    DISTRICT VARCHAR(900),
    STATE VARCHAR(900),
    COUNTRY VARCHAR(900),
    POSTAL_CODE VARCHAR(10),
    WEBSITE VARCHAR(900),
    PHONE VARCHAR(50),
    FAX VARCHAR(900),
    EMAIL VARCHAR(50),
    CREATED_BY DECIMAL(12,0),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY DECIMAL(12,0),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT NOT NULL,
    velkare_verified BIT NOT NULL
);


CREATE TABLE appointment
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    booking_date DATETIME NOT NULL,
    cancel_reason VARCHAR(255),
    cancelled_by_user_id BIGINT UNSIGNED,
    cancelled_date DATETIME,
    confirmed_by_user_id BIGINT UNSIGNED,
    confirmed_date DATETIME,
    from_time DATETIME NOT NULL,
    doctor_id BIGINT UNSIGNED,
    hospital_id BIGINT UNSIGNED,
    lab_id BIGINT UNSIGNED,
    member_id BIGINT UNSIGNED NOT NULL,
    notes VARCHAR(255),
    send_reminder_email BIT NOT NULL,
    status VARCHAR(255) NOT NULL,
    to_time DATETIME NOT NULL,
    type VARCHAR(255) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (doctor_id) REFERENCES doctor (id),
    FOREIGN KEY (lab_id) REFERENCES lab(id),
    FOREIGN KEY (hospital_id) REFERENCES hospital(id)
);

CREATE TABLE doctor_hospital
(
    SERVICE_PROVIDER_ID INT PRIMARY KEY NOT NULL,
    DOCTOR_ID BIGINT UNSIGNED NOT NULL,
    HOSPITAL_ID BIGINT UNSIGNED NOT NULL,
    SERVICE_HOURS_FROM TIME,
    SERVICE_HOURS_TO TIME,
    VELKARE_VERIFIED TINYINT DEFAULT 0,
    CREATED_BY DECIMAL(12,0),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY DECIMAL(12,0),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT NOT NULL,
    FOREIGN KEY (DOCTOR_ID) REFERENCES doctor (ID),
    FOREIGN KEY (HOSPITAL_ID) REFERENCES hospital (ID)
);

CREATE TABLE speciality
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(100),
    VELKARE_VERIFIED TINYINT DEFAULT 0,
    CREATED_BY DECIMAL(12,0),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY DECIMAL(12,0),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT NOT NULL
);

CREATE TABLE doctor_speciality
(
    DOCTOR_ID BIGINT UNSIGNED NOT NULL,
    SPECIALITY_ID BIGINT UNSIGNED NOT NULL,
    VELKARE_VERIFIED TINYINT DEFAULT 0,
    CREATED_BY DECIMAL(12,0),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY DECIMAL(12,0),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT NOT NULL,
    FOREIGN KEY (DOCTOR_ID) REFERENCES doctor (ID),
    FOREIGN KEY (SPECIALITY_ID) REFERENCES speciality (ID)
);

CREATE TABLE package
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(300),
    COST DECIMAL(12,0),
    CREATED_BY DECIMAL(12,0),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY DECIMAL(12,0),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT NOT NULL
);
CREATE TABLE packages
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    cost VARCHAR(255) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    creation_date VARCHAR(255) NOT NULL,
    last_update_date VARCHAR(255) NOT NULL,
    last_updated_by VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);
CREATE TABLE test
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(300),
    COST DECIMAL(12,2),
    CREATED_BY DECIMAL(12,0),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY DECIMAL(12,0),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT DEFAULT 0 NOT NULL
);

CREATE TABLE lab_package_test
(
    LAB_ID BIGINT UNSIGNED,
    PACKAGE_ID BIGINT UNSIGNED,
    TEST_ID BIGINT UNSIGNED,
    LAB_PACKAGE_COST DECIMAL(12,2),
    VELKARE_PACKAGE_COST DECIMAL(12,2),
    CREATED_BY DECIMAL(12,0),
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY DECIMAL(12,0),
    LAST_UPDATE_DATE DATETIME,
    version BIGINT NOT NULL,
    FOREIGN KEY (LAB_ID) REFERENCES lab (ID),
    FOREIGN KEY (PACKAGE_ID) REFERENCES package (ID),
    FOREIGN KEY (TEST_ID) REFERENCES test (ID)
);

CREATE TABLE member_role
(
    role_id BIGINT UNSIGNED NOT NULL,
    member_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (role_id, member_id),
    FOREIGN KEY (role_id) REFERENCES role (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
CREATE TABLE oauth_access_token
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    authentication LONGBLOB NOT NULL,
    authentication_json VARCHAR(4096),
    authentication_key VARCHAR(255) NOT NULL,
    client_id VARCHAR(255) NOT NULL,
    expiration DATETIME,
    refresh_token VARCHAR(255),
    token_type VARCHAR(255) NOT NULL,
    username VARCHAR(255),
    value VARCHAR(255) NOT NULL
);
CREATE TABLE oauth_access_token_scope
(
    oauth_access_token_id BIGINT UNSIGNED NOT NULL,
    scope_string VARCHAR(255),
    FOREIGN KEY (oauth_access_token_id) REFERENCES oauth_access_token (id)
);
CREATE TABLE oauth_authorization_code
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    authentication LONGBLOB NOT NULL,
    code VARCHAR(255) NOT NULL
);
CREATE TABLE oauth_client
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    access_token_validity_seconds INT,
    client_id VARCHAR(255) NOT NULL,
    client_secret VARCHAR(255),
    namespace VARCHAR(255),
    refresh_token_validity_seconds INT
);
CREATE TABLE oauth_client_additional_information
(
    additional_information BIGINT,
    additional_information_idx VARCHAR(255),
    additional_information_elt VARCHAR(255) NOT NULL
);
CREATE TABLE oauth_client_authorities
(
    oauth_client_id BIGINT UNSIGNED,
    authorities_string VARCHAR(255),
    FOREIGN KEY (oauth_client_id) REFERENCES oauth_client (id)
);
CREATE TABLE oauth_client_authorized_grant_types
(
    oauth_client_id BIGINT UNSIGNED,
    authorized_grant_types_string VARCHAR(255),
    FOREIGN KEY (oauth_client_id) REFERENCES oauth_client (id)
);
CREATE TABLE oauth_client_redirect_uris
(
    oauth_client_id BIGINT UNSIGNED,
    redirect_uris_string VARCHAR(255),
    FOREIGN KEY (oauth_client_id) REFERENCES oauth_client (id)
);
CREATE TABLE oauth_client_resource_ids
(
    oauth_client_id BIGINT UNSIGNED,
    resource_ids_string VARCHAR(255),
    FOREIGN KEY (oauth_client_id) REFERENCES oauth_client (id)
);
CREATE TABLE oauth_client_scopes
(
    oauth_client_id BIGINT UNSIGNED,
    scopes_string VARCHAR(255),
    FOREIGN KEY (oauth_client_id) REFERENCES oauth_client (id)
);
CREATE TABLE oauth_refresh_token
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    authentication LONGBLOB NOT NULL,
    value VARCHAR(255) NOT NULL
);

CREATE TABLE pending_email_confirmation
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    confirmation_event VARCHAR(80),
    confirmation_token VARCHAR(80) NOT NULL,
    email_address VARCHAR(80) NOT NULL,
    timestamp DATETIME NOT NULL,
    user_token VARCHAR(500)
);
CREATE TABLE social_connection
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    access_token VARCHAR(512) NOT NULL,
    deleted BIT NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    expire_time BIGINT,
    image_url VARCHAR(255),
    login_id BIGINT UNSIGNED NOT NULL,
    profile_url VARCHAR(255),
    provider_id VARCHAR(255) NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,
    rank BIGINT,
    refresh_token VARCHAR(255),
    secret VARCHAR(255),
    uid BIGINT NOT NULL,
    FOREIGN KEY (login_id) REFERENCES login (id)
);
CREATE TABLE subscription_plan
(
    ID BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL
);

CREATE TABLE medical_record_type
(
    id BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    enabled BIT DEFAULT b'1' NOT NULL
);

CREATE TABLE media
(
    id BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    public_media BIT DEFAULT b'0' NOT NULL,
    content_type VARCHAR(255) NULL,
    size INT(10) NOT NULL
);

CREATE TABLE media_data
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    file_data LONGBLOB NOT NULL,
    media_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (media_id) REFERENCES media (id)
);


CREATE TABLE medical_record
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    member_id BIGINT UNSIGNED NOT NULL,
    appointment_id BIGINT UNSIGNED,
    deleted BIT NOT NULL,
    deleted_date DATETIME,
    media_id BIGINT UNSIGNED,
    notes VARCHAR(5000),
    record_type_id BIGINT UNSIGNED NOT NULL,
    upload_date DATETIME NOT NULL,
    record_date DATE,
    FOREIGN KEY (record_type_id) REFERENCES medical_record_type (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (appointment_id) REFERENCES appointment (id),
    FOREIGN KEY (media_id) REFERENCES media (id)
);
CREATE TABLE lab_location
(
    ID BIGINT UNSIGNED NOT NULL,
    LOCATION_ID BIGINT NOT NULL,
    LOCATION VARCHAR(240),
    ADDRESS1 VARCHAR(240),
    ADDRESS2 VARCHAR(240),
    CITY VARCHAR(240),
    DISTRICT VARCHAR(240),
    STATE VARCHAR(240),
    COUNTRY VARCHAR(240),
    POSTAL_CODE VARCHAR(10),
    WEBSITE VARCHAR(360),
    PHONE VARCHAR(50),
    FAX VARCHAR(240),
    EMAIL VARCHAR(50),
    CREATED_BY INT,
    CREATION_DATE DATETIME,
    LAST_UPDATED_BY INT,
    LAST_UPDATE_DATE DATETIME,
    IMAGE_LOCATION VARCHAR(240)
);



CREATE INDEX IDX_FK_APPOINTMENT_MEMBER_ID ON appointment (member_id);
CREATE INDEX IDX_LAB_PACKAGE_TEST_LAB_ID ON lab_package_test (LAB_ID);
CREATE UNIQUE INDEX UK_IDX_LOGIN_USERNAME ON login (username);
CREATE INDEX IDX_FK_MEMBER_PERMANENT_ADDRESS_ID ON member (permanent_address_id);
CREATE INDEX IDX_FK_MEMBER_CURRENT_ADDRESS_ID  ON member (current_address_id);
CREATE INDEX IDX_FK_MEMBER_LOGIN_ID  ON member (login_id);
CREATE INDEX IDX_FK_MEMBER_ROLE_MEMBER_ID  ON member_role (member_id);
CREATE UNIQUE INDEX IDX_UK_OAUTH_ACCESS_TOKEN_VALUE ON oauth_access_token (value);
CREATE UNIQUE INDEX IDX_UK_OAUTH_ACCESS_TOKEN_AUTHENTICATION_KEY  ON oauth_access_token (authentication_key);
CREATE INDEX IDX_FK_OAUTH_ACCESS_TOKEN_SCOPE_OAUTH_ACCESS_TOKEN_ID ON oauth_access_token_scope (oauth_access_token_id);
CREATE UNIQUE INDEX IDX_UK_OAUTH_AUTHORIZATION_CODE_CODE  ON oauth_authorization_code (code);
CREATE UNIQUE INDEX IDX_UK_OAUTH_CLIENT_CLIENT_ID  ON oauth_client (client_id);
CREATE INDEX IDX_FK_OAUTH_CLIENT_AUTHORITIES_OAUTH_CLIENT_ID  ON oauth_client_authorities (oauth_client_id);
CREATE INDEX IDX_FK_OAUTH_CLIENT_AUTHORIZED_GRANT_TYPES_OAUTH_CLIENT_ID  ON oauth_client_authorized_grant_types (oauth_client_id);
CREATE INDEX IDX_FK_OAUTH_CLIENT_REDIRECT_URIS_OAUTH_CLIENT_ID  ON oauth_client_redirect_uris (oauth_client_id);
CREATE INDEX IDX_FK_OAUTH_CLIENT_RESOURCE_IDS_OAUTH_CLIENT_ID  ON oauth_client_resource_ids (oauth_client_id);
CREATE INDEX IDX_FK_OAUTH_CLIENT_SCOPES_OAUTH_CLIENT_ID ON oauth_client_scopes (oauth_client_id);
CREATE UNIQUE INDEX IDX_UK_OAUTH_REFRESH_TOKEN_VALUE  ON oauth_refresh_token (value);
CREATE UNIQUE INDEX IDX_UK_PENDING_EMAIL_CONFIRMATION_CONFIRMATION_TOKEN  ON pending_email_confirmation (confirmation_token);
CREATE INDEX IDX_PENDING_EMAIL_CONFIRMATION_TIMESTAMP ON pending_email_confirmation (timestamp);
CREATE INDEX IDX_PENDING_EMAIL_CONFIRMATION_CONFIRMATION_TOKEN  ON pending_email_confirmation (confirmation_token);
CREATE UNIQUE INDEX IDX_UK_REGISTRATION_EMAIL  ON registration (email);
CREATE INDEX IDX_FK_REGISTRATION_MEMBER_ID  ON registration (member_id);
CREATE INDEX IDX_FK_SOCIAL_CONNECTION_LOGIN_ID  ON social_connection (login_id);


ALTER TABLE DOCTOR ADD IMAGE_LOCATION VARCHAR(240);
ALTER TABLE HOSPITAL ADD IMAGE_LOCATION VARCHAR(240);
ALTER TABLE LAB ADD IMAGE_LOCATION VARCHAR(240);