-- Se crea el usuario para la db

-- CREATE USER dbJavaTest IDENTIFIED BY dbpass QUOTA UNLIMITED ON USERS DEFAULT TABLESPACE USERS TEMPORARY TABLESPACE TEMP;

-- GRANT create session TO dbJavaTest;
-- GRANT create table TO dbJavaTest;
-- GRANT create view TO dbJavaTest;
-- GRANT create any trigger TO dbJavaTest;
-- GRANT create any procedure TO dbJavaTest;
-- GRANT create sequence TO dbJavaTest;
-- GRANT create synonym TO dbJavaTest;
-- GRANT CONNECT, RESOURCE TO dbJavaTest;


-- drop table DBJAVATEST.GENDERS;
-- drop table DBJAVATEST.JOBS;
-- drop table DBJAVATEST.EMPLOYEES;
-- drop table DBJAVATEST.EMPLOYEE_WORKED_HOURS;

----------------------------

CREATE TABLE DBJAVATEST.GENDERS (
	ID NUMBER,
	NAME VARCHAR2(255) NOT NULL,
	CONSTRAINT GENDERS_PK PRIMARY KEY (ID)
);

CREATE SEQUENCE DBJAVATEST.GENDERS_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 0 ;

CREATE OR REPLACE TRIGGER DBJAVATEST.GENDERS_TRI 
BEFORE INSERT ON DBJAVATEST.GENDERS 
FOR EACH ROW BEGIN
  SELECT DBJAVATEST.GENDERS_SEQ.NEXTVAL
  INTO   :new.ID
  FROM   dual;
END;

INSERT INTO DBJAVATEST.GENDERS (NAME)
	VALUES ('hombre');
INSERT INTO DBJAVATEST.GENDERS (NAME)
	VALUES ('mujer');
----------------------------
CREATE TABLE DBJAVATEST.JOBS (
	ID NUMBER,
	NAME             VARCHAR2(255) NOT NULL ,
	SALARY               NUMBER(9,2) NOT NULL ,
	CONSTRAINT  JOBS_PK PRIMARY KEY (ID)
);

CREATE SEQUENCE DBJAVATEST.JOBS_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 0 ;

CREATE OR REPLACE TRIGGER DBJAVATEST.JOBS_TRI 
BEFORE INSERT ON DBJAVATEST.JOBS 
FOR EACH ROW BEGIN
  SELECT DBJAVATEST.JOBS_SEQ.NEXTVAL
  INTO   :new.ID
  FROM   dual;
END;

--new data to test
INSERT INTO DBJAVATEST.JOBS (ID,NAME,SALARY)
	VALUES (NULL,'developer',20000);
INSERT INTO DBJAVATEST.JOBS (ID,NAME,SALARY)
	VALUES (NULL,'architech',30000);
INSERT INTO DBJAVATEST.JOBS (ID,NAME,SALARY)
	VALUES (NULL,'devOps',40000);

-------------------------
CREATE TABLE DBJAVATEST.EMPLOYEES (
	ID NUMBER,
	GENDER_ID NUMBER NOT NULL,
	JOB_ID NUMBER NOT NULL,
	NAME VARCHAR2(255) NOT NULL,
	LAST_NAME VARCHAR2(255) NOT NULL,
	BIRTHDATE DATE NOT NULL,
	CONSTRAINT EMPLOYEES_PK PRIMARY KEY (ID),
	CONSTRAINT FK_GENDERS FOREIGN KEY (GENDER_ID) REFERENCES DBJAVATEST.GENDERS(ID),
	CONSTRAINT FK_JOBS FOREIGN KEY (GENDER_ID) REFERENCES DBJAVATEST.JOBS(ID)
);

CREATE SEQUENCE DBJAVATEST.EMPLOYEES_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 0 ;

CREATE OR REPLACE TRIGGER DBJAVATEST.EMPLOYEES_TRI 
BEFORE INSERT ON DBJAVATEST.EMPLOYEES 
FOR EACH ROW BEGIN
  SELECT DBJAVATEST.EMPLOYEES_SEQ.NEXTVAL
  INTO   :new.ID
  FROM   dual;
END;
-------------------------

CREATE TABLE DBJAVATEST.EMPLOYEE_WORKED_HOURS (
	ID NUMBER     NOT NULL,
	EMPLOYEE_ID NUMBER NOT NULL,
	WORKED_HOURS NUMBER NOT NULL,
	WORKED_DATE DATE NOT NULL,
	CONSTRAINT EMPLOYEE_WORKED_HOURS_PK PRIMARY KEY (ID),
	CONSTRAINT FK_EMPLOYEES FOREIGN KEY (EMPLOYEE_ID) REFERENCES DBJAVATEST.EMPLOYEES(ID)
);

CREATE SEQUENCE DBJAVATEST.EMPLOYEE_WORKED_HOURS_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 0 ;

CREATE OR REPLACE TRIGGER DBJAVATEST.EMPLOYEE_WORKED_HOURS_TRI 
BEFORE INSERT ON DBJAVATEST.EMPLOYEE_WORKED_HOURS 
FOR EACH ROW BEGIN
  SELECT DBJAVATEST.EMPLOYEE_WORKED_HOURS_SEQ.NEXTVAL
  INTO   :new.ID
  FROM   dual;
END;

INSERT INTO DBJAVATEST.EMPLOYEE_WORKED_HOURS ( EMPLOYEE_ID,WORKED_HOURS,WORKED_DATE)
	VALUES (1,12,TIMESTAMP '2019-05-03 21:02:44.000000');
INSERT INTO DBJAVATEST.EMPLOYEE_WORKED_HOURS ( EMPLOYEE_ID,WORKED_HOURS,WORKED_DATE)
	VALUES (1,2,TIMESTAMP '2019-01-03 20:02:44.000000');
INSERT INTO DBJAVATEST.EMPLOYEE_WORKED_HOURS ( EMPLOYEE_ID, WORKED_HOURS, WORKED_DATE)
	VALUES(2, 15, TIMESTAMP '2022-05-03 21:02:44.000000');
INSERT INTO DBJAVATEST.EMPLOYEE_WORKED_HOURS ( EMPLOYEE_ID, WORKED_HOURS, WORKED_DATE)
	VALUES(1, 2, TIMESTAMP '2019-05-01 21:02:44.000000');