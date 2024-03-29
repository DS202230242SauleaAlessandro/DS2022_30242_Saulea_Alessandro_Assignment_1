PGDMP             	        
    z            energy    14.5 (Debian 14.5-1.pgdg110+1)    15.0     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                        1262    16384    energy    DATABASE     q   CREATE DATABASE energy WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE energy;
                root    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                root    false                       0    0    SCHEMA public    ACL     Q   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;
                   root    false    4            �            1259    16385    device    TABLE     �   CREATE TABLE public.device (
    id bytea NOT NULL,
    address character varying(255),
    description character varying(255),
    max_consumption integer,
    user_id bytea,
    CONSTRAINT device_max_consumption_check CHECK ((max_consumption >= 0))
);
    DROP TABLE public.device;
       public         heap    root    false    4            �            1259    16393    measurement    TABLE     �   CREATE TABLE public.measurement (
    "timestamp" timestamp without time zone NOT NULL,
    consumption integer,
    device_id bytea NOT NULL,
    CONSTRAINT measurement_consumption_check CHECK ((consumption >= 0))
);
    DROP TABLE public.measurement;
       public         heap    root    false    4            �            1259    16401    user    TABLE     �   CREATE TABLE public."user" (
    id bytea NOT NULL,
    is_admin boolean,
    name character varying(255),
    password character varying(255),
    username character varying(255)
);
    DROP TABLE public."user";
       public         heap    root    false    4            i           2606    16392    device device_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.device
    ADD CONSTRAINT device_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.device DROP CONSTRAINT device_pkey;
       public            root    false    209            k           2606    16400    measurement measurement_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.measurement
    ADD CONSTRAINT measurement_pkey PRIMARY KEY (device_id, "timestamp");
 F   ALTER TABLE ONLY public.measurement DROP CONSTRAINT measurement_pkey;
       public            root    false    210    210            m           2606    16407    user user_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public."user" DROP CONSTRAINT user_pkey;
       public            root    false    211            o           2606    16413 '   measurement fkfk341lu27m89eohc71wnwf8bt    FK CONSTRAINT     �   ALTER TABLE ONLY public.measurement
    ADD CONSTRAINT fkfk341lu27m89eohc71wnwf8bt FOREIGN KEY (device_id) REFERENCES public.device(id);
 Q   ALTER TABLE ONLY public.measurement DROP CONSTRAINT fkfk341lu27m89eohc71wnwf8bt;
       public          root    false    209    210    3177            n           2606    16408 "   device fkk92m2qj36vn62ctp5pgbt4982    FK CONSTRAINT     �   ALTER TABLE ONLY public.device
    ADD CONSTRAINT fkk92m2qj36vn62ctp5pgbt4982 FOREIGN KEY (user_id) REFERENCES public."user"(id);
 L   ALTER TABLE ONLY public.device DROP CONSTRAINT fkk92m2qj36vn62ctp5pgbt4982;
       public          root    false    209    3181    211           