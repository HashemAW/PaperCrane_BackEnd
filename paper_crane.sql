SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;


CREATE TABLE public.token (
    token_id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    tokenType VARCHAR(10) NOT NULL,
    revoked BOOLEAN DEFAULT false,
    expired BOOLEAN DEFAULT false,
    user_id integer NOT NULL
);

ALTER TABLE public.token OWNER TO pc;


CREATE TABLE public.client (
    user_id integer NOT NULL,
    client_name character varying(50) NOT NULL,
    company_name character varying(50) NOT NULL
);


ALTER TABLE public.client OWNER TO pc;


CREATE TABLE public.employee (
    user_id integer NOT NULL,
    employee_name character varying(50) NOT NULL,
    employee_role character(1) NOT NULL,
    CONSTRAINT employee_employee_role CHECK ((employee_role = ANY (ARRAY['C'::bpchar, 'D'::bpchar, 'A'::bpchar])))
);


ALTER TABLE public.employee OWNER TO pc;


CREATE TABLE public.employee_project_bridge (
    user_id integer NOT NULL,
    project_lead_id integer NOT NULL,
    project_id integer NOT NULL
);


ALTER TABLE public.employee_project_bridge OWNER TO pc;


CREATE TABLE public.project (
    project_id integer NOT NULL,
    client_id integer NOT NULL,
    project_lead_id integer NOT NULL,
    project_description character varying(500) NOT NULL
);


ALTER TABLE public.project OWNER TO pc;


CREATE TABLE public.proposals (
    client_name character varying(50) NOT NULL,
    client_email character varying(100) NOT NULL,
    proposal_id integer NOT NULL,
    user_id integer,
    status character varying(20) NOT NULL,
    date date DEFAULT now() NOT NULL,
    file_names character varying(300)[],
    links character varying(700)[],
    CONSTRAINT status_ck CHECK (((status)::text = ANY ((ARRAY['pending'::character varying, 'processed'::character varying])::text[])))
);


ALTER TABLE public.proposals OWNER TO pc;


COMMENT ON TABLE public.proposals IS 'receiving proposals from potential customers and pending or processed proposals';


COMMENT ON COLUMN public.proposals.status IS 'there are ''pending'' and ''processed'' status';


ALTER TABLE public.proposals ALTER COLUMN proposal_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.proposals_proposal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


CREATE TABLE public.repository (
    repo_id integer NOT NULL,
    project_id integer NOT NULL,
    client_id integer NOT NULL,
    file character varying[] NOT NULL,
    link character varying[] NOT NULL,
    description character varying(500) NOT NULL
);


ALTER TABLE public.repository OWNER TO pc;


COMMENT ON TABLE public.repository IS 'for admins and clients';


ALTER TABLE public.repository ALTER COLUMN repo_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.repository_repo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


CREATE TABLE public.task (
    task_id integer NOT NULL,
    project_id integer NOT NULL,
    task_name character varying(25),
    description character varying(500) NOT NULL,
    deadline date NOT NULL,
    start_date date NOT NULL,
    date_completed date,
    expected_work_hours double precision NOT NULL,
    progress_in_work_hours double precision DEFAULT 0,
    is_complete boolean DEFAULT false
);


ALTER TABLE public.task OWNER TO pc;


CREATE TABLE public.time_off (
    time_off_id integer NOT NULL,
    employee_id integer NOT NULL,
    start_date date NOT NULL,
    end_date date,
    status character(1) NOT NULL,
    reason character varying(500) NOT NULL
);


ALTER TABLE public.time_off OWNER TO pc;


CREATE TABLE public.user_account (
    user_id integer NOT NULL,
    user_role character varying(20) NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    CONSTRAINT user_email CHECK (((email)::text ~~ '%_@__%.__%'::text))
);


ALTER TABLE public.user_account OWNER TO pc;


CREATE TABLE public.video_training (
    video_id integer NOT NULL,
    video_project_id integer NOT NULL,
    video_link character varying(300) NOT NULL,
    video_description character varying(500) NOT NULL,
    video_type integer NOT NULL,
    video_thumbnail character varying(200),
    video_role character varying[] NOT NULL
);


ALTER TABLE public.video_training OWNER TO pc;


COMMENT ON COLUMN public.video_training.video_type IS 'type 1 is for training, type 2 is for offboarding ';


COMMENT ON COLUMN public.video_training.video_thumbnail IS 'photos are in url';


COMMENT ON COLUMN public.video_training.video_role IS 'all employees, developers and designers';


CREATE TABLE public.worked_hours (
    user_id integer NOT NULL,
    project_id integer NOT NULL,
    worked_hours_id integer NOT NULL,
    hours integer NOT NULL,
    date date DEFAULT now() NOT NULL,
    task_id integer NOT NULL
);


ALTER TABLE public.worked_hours OWNER TO pc;


COMMENT ON TABLE public.worked_hours IS 'worked hours from employees'' daily logs, and its shared with admins';


ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (user_id);


ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (user_id);


ALTER TABLE ONLY public.employee_project_bridge
    ADD CONSTRAINT employee_project_bridge_pkey PRIMARY KEY (user_id);


ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (project_id);


ALTER TABLE ONLY public.proposals
    ADD CONSTRAINT proposals_pkey PRIMARY KEY (proposal_id);


ALTER TABLE ONLY public.repository
    ADD CONSTRAINT repository_pkey PRIMARY KEY (repo_id);


ALTER TABLE public.time_off
    ADD CONSTRAINT status CHECK ((status = ANY (ARRAY['A'::bpchar, 'R'::bpchar, 'P'::bpchar]))) NOT VALID;


ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (task_id);


ALTER TABLE ONLY public.time_off
    ADD CONSTRAINT time_off_pkey PRIMARY KEY (time_off_id);


ALTER TABLE ONLY public.user_account
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


ALTER TABLE ONLY public.video_training
    ADD CONSTRAINT video_training_pkey PRIMARY KEY (video_id);


ALTER TABLE ONLY public.worked_hours
    ADD CONSTRAINT worked_hours_pkey PRIMARY KEY (worked_hours_id);


ALTER TABLE ONLY public.repository
    ADD CONSTRAINT client_id_fk FOREIGN KEY (client_id) REFERENCES public.client(user_id);


ALTER TABLE ONLY public.time_off
    ADD CONSTRAINT employee_id FOREIGN KEY (employee_id) REFERENCES public.employee(user_id);


ALTER TABLE ONLY public.worked_hours
    ADD CONSTRAINT project_id_fk FOREIGN KEY (project_id) REFERENCES public.project(project_id);


ALTER TABLE ONLY public.repository
    ADD CONSTRAINT project_id_fk FOREIGN KEY (project_id) REFERENCES public.project(project_id);


ALTER TABLE ONLY public.task
    ADD CONSTRAINT project_id_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id) NOT VALID;


ALTER TABLE ONLY public.worked_hours
    ADD CONSTRAINT task_id_fk FOREIGN KEY (task_id) REFERENCES public.task(task_id);


ALTER TABLE ONLY public.proposals
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public.user_account(user_id);


ALTER TABLE ONLY public.worked_hours
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public.employee(user_id);

ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_fkey FOREIGN KEY (user_id) REFERENCES public.user_account(user_id) ON DELETE CASCADE;
