-- 데이터베이스 생성
DO
$do$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'murple') THEN
      CREATE DATABASE murple WITH OWNER postgres;
END IF;
END
$do$;

-- 유저 전화번호 테이블 생성
CREATE SEQUENCE public.user_idx_seq;
ALTER SEQUENCE public.user_idx_seq OWNER TO postgres;

CREATE TABLE public.user_tel (
                                 idx         BIGINT GENERATED ALWAYS AS IDENTITY,
                                 user_idx    BIGINT NOT NULL,
                                 tel         VARCHAR(15) NOT NULL,
                                 label       VARCHAR(1024),
                                 county_code VARCHAR(2) NOT NULL,
                                 is_identity VARCHAR(1) DEFAULT 'N' NOT NULL,
                                 created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
                                 updated_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

COMMENT ON TABLE public.user_tel IS '유저 전화번호';
COMMENT ON COLUMN public.user_tel.idx IS '유저 전화번호 인덱스';
COMMENT ON COLUMN public.user_tel.user_idx IS '유저 인덱스';
COMMENT ON COLUMN public.user_tel.tel IS '전화번호 (Format E164)';
COMMENT ON COLUMN public.user_tel.label IS '설명 (자택, 직장 등)';
COMMENT ON COLUMN public.user_tel.county_code IS '국가코드 (Format ISO 3166-1)';
COMMENT ON COLUMN public.user_tel.is_identity IS '본인인증 여부 (Y: 인증, N: 미인증)';
COMMENT ON COLUMN public.user_tel.created_at IS '생성 시간';
COMMENT ON COLUMN public.user_tel.updated_at IS '수정 시간';

CREATE INDEX user_tel_user_idx_index ON public.user_tel (user_idx);

-- 유저 주소 테이블 생성
CREATE TABLE public.user_addr (
                                  idx        BIGINT GENERATED ALWAYS AS IDENTITY,
                                  user_idx   BIGINT NOT NULL,
                                  addr       VARCHAR(1024) NOT NULL,
                                  label      VARCHAR(1024),
                                  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
                                  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

COMMENT ON TABLE public.user_addr IS '유저 주소';
COMMENT ON COLUMN public.user_addr.idx IS '주소 인덱스';
COMMENT ON COLUMN public.user_addr.user_idx IS '유저 인덱스';
COMMENT ON COLUMN public.user_addr.addr IS '주소';
COMMENT ON COLUMN public.user_addr.label IS '설명 (자택, 직장 등)';
COMMENT ON COLUMN public.user_addr.created_at IS '생성 시간';
COMMENT ON COLUMN public.user_addr.updated_at IS '수정 시간';

CREATE INDEX user_addr_user_idx_index ON public.user_addr (user_idx);

-- 유저 정보 테이블 생성
CREATE TABLE public.user_info (
                                  idx        BIGINT GENERATED ALWAYS AS IDENTITY,
                                  name       VARCHAR(1024) NOT NULL,
                                  age        INTEGER,
                                  sex        VARCHAR(1),
                                  email      VARCHAR(1024),
                                  status     VARCHAR(50) DEFAULT 'ACTIVE' NOT NULL,
                                  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
                                  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

COMMENT ON TABLE public.user_info IS '사용자 기본 정보';
COMMENT ON COLUMN public.user_info.idx IS '유저 인덱스';
COMMENT ON COLUMN public.user_info.name IS '사용자 이름';
COMMENT ON COLUMN public.user_info.age IS '만 나이';
COMMENT ON COLUMN public.user_info.sex IS '성별 (M: 남자, W: 여자)';
COMMENT ON COLUMN public.user_info.email IS '이메일';
COMMENT ON COLUMN public.user_info.status IS '유저 상태 (ACTIVE: 활성화, DELETED: 삭제)';
COMMENT ON COLUMN public.user_info.created_at IS '생성시간';
COMMENT ON COLUMN public.user_info.updated_at IS '수정 시간';

ALTER SEQUENCE public.user_idx_seq OWNED BY public.user_info.idx;
CREATE INDEX user_info_name_index ON public.user_info (name);

-- 한국 성씨 테이블 생성
CREATE TABLE public.korean_last_name (
                                         idx       INTEGER GENERATED ALWAYS AS IDENTITY,
                                         last_name VARCHAR(2) NOT NULL
);

COMMENT ON TABLE public.korean_last_name IS '한국 성씨';
COMMENT ON COLUMN public.korean_last_name.idx IS '인덱스';
COMMENT ON COLUMN public.korean_last_name.last_name IS '성씨';

CREATE INDEX korean_last_name_last_name_index ON public.korean_last_name (last_name);

-- 한국 성씨 데이터 삽입
INSERT INTO korean_last_name (last_name) VALUES
                                             ('가'), ('간'), ('갈'), ('감'), ('강'), ('개'), ('견'), ('경'), ('계'), ('고'), ('곡'),
                                             ('공'), ('곽'), ('관'), ('교'), ('구'), ('국'), ('군'), ('궁'), ('궉'), ('권'), ('근'), ('금'), ('기'), ('길'), ('김'),
                                             ('나'), ('난'), ('남'), ('남궁'), ('낭'), ('내'), ('노'), ('뇌'), ('누'),
                                             ('다'), ('단'), ('담'), ('당'), ('대'), ('도'), ('독고'), ('돈'), ('동'), ('동방'), ('두'), ('등'),
                                             ('류'), ('리'),
                                             ('마'), ('막'), ('만'), ('매'), ('맹'), ('명'), ('모'), ('목'), ('묘'), ('묵'), ('문'), ('미'), ('민'),
                                             ('박'), ('반'), ('방'), ('배'), ('백'), ('범'), ('변'), ('복'), ('봉'), ('부'), ('분'), ('비'), ('빈'), ('빙'),
                                             ('사'), ('삼'), ('상'), ('서'), ('석'), ('선'), ('선우'), ('설'), ('섭'), ('성'), ('소'), ('손'), ('송'), ('수'), ('순'), ('승'), ('시'), ('신'), ('심'),
                                             ('아'), ('안'), ('애'), ('야'), ('양'), ('어'), ('엄'), ('여'), ('연'), ('염'), ('엽'), ('영'), ('예'), ('오'), ('옥'), ('온'), ('옹'), ('왕'), ('요'), ('용'), ('우'), ('운'), ('원'), ('위'), ('유'), ('육'), ('윤'), ('은'), ('음'), ('이'), ('익'), ('인'), ('임'),
                                             ('자'), ('장'), ('저'), ('전'), ('정'), ('제'), ('제갈'), ('조'), ('종'), ('좌'), ('주'), ('준'), ('즙'), ('증'), ('지'), ('진'),
                                             ('차'), ('창'), ('채'), ('척'), ('천'), ('초'), ('총'), ('최'), ('추'), ('춘'),
                                             ('쾌'),
                                             ('탁'), ('탄'), ('태'),
                                             ('파'), ('판'), ('팽'), ('편'), ('평'), ('포'), ('표'), ('풍'), ('피'), ('필'),
                                             ('하'), ('한'), ('함'), ('해'), ('허'), ('현'), ('형'), ('호'), ('홍'), ('화'), ('환'), ('황'), ('황보'), ('후'), ('훈'), ('흥'), ('희');
