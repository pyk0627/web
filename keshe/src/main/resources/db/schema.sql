-- app_user用来存系统用户账号
CREATE TABLE IF NOT EXISTS app_user (

-- BIGINT表示64位整数，NOT NULL表示这一列不能为空，插入时必须有值
-- AUTO_INCREMENT，自增，不填值时，系统自动分配下一个编号
    id            BIGINT       NOT NULL AUTO_INCREMENT,

-- VARCHAR(50)表示可变长字符串，最多50个字符    
    username      VARCHAR(50)  NOT NULL,
    
    password_hash VARCHAR(100) NOT NULL,

-- username是登录唯一表示账号（不可重复），display_name是昵称（可重复）
    display_name  VARCHAR(50)  NOT NULL,
    
    email         VARCHAR(100) NOT NULL,
    phone         VARCHAR(20)  NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',

-- enabled表示用户状态 TINYINT(1)表示启用，0表示停用
    enabled       TINYINT(1)   NOT NULL DEFAULT 1,

-- created_at表示创建时间，默认为数据库的当前时间
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY (id),

-- UNIQUE KEY 表示唯一索引，值不能重复，KEY表示普通索引，只是加速查询
-- 索引名字：uk_user_username
-- 索引建在username列上
    UNIQUE KEY uk_user_username (username),
    
    UNIQUE KEY uk_user_email (email),
    KEY idx_user_role (role)

-- 存储引擎：InnoDB  默认字符集：utf8mb4  
-- 排序规则：按Unicode规则排序，ci:对大小写不敏感
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 建立表用来存植物档案
CREATE TABLE IF NOT EXISTS crop (

    id              BIGINT       NOT NULL AUTO_INCREMENT,
    chinese_name    VARCHAR(100) NOT NULL,
    english_name    VARCHAR(150) NULL,
    latin_name      VARCHAR(150) NULL,
    alias           VARCHAR(200) NULL,
    family          VARCHAR(100) NOT NULL,
    genus           VARCHAR(100) NOT NULL,
    features        TEXT         NULL,
    distribution    TEXT         NULL,
    cultivation     TEXT         NULL,
    uses            TEXT         NULL,
    image_url       VARCHAR(255) NULL,
    reference_title VARCHAR(200) NULL,
    reference_url   VARCHAR(500) NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_crop_chinese_name (chinese_name),
    KEY idx_crop_family (family),
    KEY idx_crop_genus (genus),
    KEY idx_crop_name (chinese_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS oil_record (
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    crop_id               BIGINT       NOT NULL,
    collection_unit       VARCHAR(100) NULL,
    test_unit             VARCHAR(100) NULL,
    test_part             VARCHAR(100) NULL,
    origin                VARCHAR(150) NULL,
    oil_content           DECIMAL(6,3) NULL,
    iodine_value          DECIMAL(8,3) NULL,
    acid_value            DECIMAL(8,3) NULL,
    saponification_value  DECIMAL(8,3) NULL,
    c12                   DECIMAL(6,3) NULL,
    c14                   DECIMAL(6,3) NULL,
    c16                   DECIMAL(6,3) NULL,
    c16_1                 DECIMAL(6,3) NULL,
    c18                   DECIMAL(6,3) NULL,
    c18_1                 DECIMAL(6,3) NULL,
    c18_2                 DECIMAL(6,3) NULL,
    c18_3                 DECIMAL(6,3) NULL,
    c20                   DECIMAL(6,3) NULL,
    c20_1                 DECIMAL(6,3) NULL,
    remark                VARCHAR(200) NULL,
    created_at            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_oil_crop (crop_id),
    KEY idx_oil_content (oil_content),
    UNIQUE KEY uk_oil_sample (crop_id, collection_unit, test_unit, test_part, origin, oil_content),
    CONSTRAINT fk_oil_crop FOREIGN KEY (crop_id) REFERENCES crop (id) ON DELETE CASCADE,
    CONSTRAINT ck_oil_content CHECK (oil_content IS NULL OR (oil_content >= 0 AND oil_content <= 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS favorite (
    user_id    BIGINT   NOT NULL,
    crop_id    BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, crop_id),
    KEY idx_favorite_crop (crop_id),
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_favorite_crop FOREIGN KEY (crop_id) REFERENCES crop (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
