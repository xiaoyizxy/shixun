-- 老人信息表
CREATE TABLE IF NOT EXISTS `elder` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50),
    `age` INT,
    `gender` VARCHAR(10),
    `phone` VARCHAR(20),
    `address` VARCHAR(200),
    `care_level` VARCHAR(50),
    `nurse` VARCHAR(50),
    `status` VARCHAR(20) DEFAULT 'active',
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- 护理人员表
CREATE TABLE IF NOT EXISTS `staff` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50),
    `title` VARCHAR(50),
    `phone` VARCHAR(20),
    `dept` VARCHAR(50),
    `schedule` VARCHAR(20),
    `rating` DOUBLE DEFAULT 4.0,
    `cert` VARCHAR(100),
    `status` VARCHAR(20) DEFAULT 'active',
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- 预约表
CREATE TABLE IF NOT EXISTS `appointment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `elder_name` VARCHAR(50),
    `elder_phone` VARCHAR(20),
    `elder_age` INT,
    `elder_address` VARCHAR(200),
    `service_type` VARCHAR(50),
    `appointment_date` VARCHAR(20),
    `appointment_time` VARCHAR(20),
    `staff_name` VARCHAR(50),
    `remark` VARCHAR(500),
    `status` VARCHAR(20) DEFAULT 'pending',
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- 护理服务项目表
CREATE TABLE IF NOT EXISTS `nursing_service` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100),
    `category` VARCHAR(50),
    `price` DECIMAL(10,2),
    `unit` VARCHAR(20),
    `duration` VARCHAR(20),
    `description` VARCHAR(500),
    `tags` VARCHAR(200),
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- 服务订单表
CREATE TABLE IF NOT EXISTS `service_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_no` VARCHAR(30),
    `elder_name` VARCHAR(50),
    `service` VARCHAR(100),
    `staff` VARCHAR(50),
    `date` VARCHAR(20),
    `amount` DECIMAL(10,2),
    `status` VARCHAR(20) DEFAULT 'pending',
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- 居家病床表
CREATE TABLE IF NOT EXISTS `bed` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `bed_no` VARCHAR(20),
    `elder_name` VARCHAR(50),
    `age` INT,
    `care_level` VARCHAR(50),
    `nurse` VARCHAR(50),
    `check_in_date` VARCHAR(20),
    `status` VARCHAR(20) DEFAULT 'vacant',
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- 上门巡诊表
CREATE TABLE IF NOT EXISTS `visit` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `elder_name` VARCHAR(50),
    `age` INT,
    `symptom` VARCHAR(200),
    `doctor` VARCHAR(50),
    `visit_date` VARCHAR(20),
    `time_slot` VARCHAR(20),
    `status` VARCHAR(20) DEFAULT 'upcoming',
    `diagnosis` VARCHAR(500),
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- 数据字典表
CREATE TABLE IF NOT EXISTS `dict` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(50),
    `name` VARCHAR(100),
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- 数据字典项表
CREATE TABLE IF NOT EXISTS `dict_item` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `dict_id` BIGINT,
    `label` VARCHAR(100),
    `value` VARCHAR(100),
    `sort` INT DEFAULT 1,
    `status` VARCHAR(20) DEFAULT 'active',
    `create_time` DATETIME,
    `update_time` DATETIME
);

-- ===== 插入测试数据 =====

-- 老人数据
INSERT INTO `elder` (`name`, `age`, `gender`, `phone`, `address`, `care_level`, `nurse`, `status`, `create_time`, `update_time`) VALUES
('张奶奶', 78, '女', '138****6789', '朝阳区建国路88号', '一级护理', '王护士', 'active', NOW(), NOW()),
('李爷爷', 82, '男', '139****8901', '海淀区中关村1号', '特级护理', '赵护士', 'active', NOW(), NOW()),
('刘奶奶', 75, '女', '137****0123', '东城区王府井大街', '二级护理', '张护士', 'active', NOW(), NOW()),
('陈爷爷', 85, '男', '136****3456', '西城区金融街5号', '一级护理', '李护士', 'inactive', NOW(), NOW()),
('周奶奶', 71, '女', '135****7890', '丰台区方庄路2号', '二级护理', '孙护士', 'active', NOW(), NOW()),
('吴爷爷', 80, '男', '134****2345', '大兴区亦庄路6号', '三级护理', '郑护士', 'active', NOW(), NOW()),
('郑奶奶', 73, '女', '133****6789', '通州区新华大街', '一级护理', '钱护士', 'active', NOW(), NOW()),
('冯爷爷', 77, '男', '132****0123', '昌平区回龙观镇', '二级护理', '杨护士', 'active', NOW(), NOW());

-- 护理人员数据
INSERT INTO `staff` (`name`, `title`, `phone`, `dept`, `schedule`, `rating`, `cert`, `status`, `create_time`, `update_time`) VALUES
('王护士', '主管护师', '158****1234', '内科', '白班', 4.9, '执业护士证', 'active', NOW(), NOW()),
('赵护士', '护师', '159****2345', '外科', '白班', 4.7, '执业护士证', 'active', NOW(), NOW()),
('张护士', '主管护师', '157****3456', '内科', '夜班', 4.8, '执业护士证', 'active', NOW(), NOW()),
('李护士', '护士', '156****4567', '康复科', '白班', 4.6, '执业护士证', 'active', NOW(), NOW()),
('孙护士', '护师', '155****5678', '老年科', '白班', 4.5, '执业护士证', 'leave', NOW(), NOW()),
('郑护士', '护士', '154****6789', '内科', '白班', 4.4, '执业护士证', 'active', NOW(), NOW()),
('钱护士', '主管护师', '153****7890', '急诊', '夜班', 4.9, '执业护士证', 'active', NOW(), NOW()),
('杨护士', '护师', '152****8901', '康复科', '白班', 4.3, '执业护士证', 'active', NOW(), NOW());

-- 护理服务数据
INSERT INTO `nursing_service` (`name`, `category`, `price`, `unit`, `duration`, `description`, `tags`, `create_time`, `update_time`) VALUES
('压疮护理', 'basic', 198, '次', '60分钟', '专业压疮评估、清创换药、护理指导', '热门', NOW(), NOW()),
('静脉输液', 'basic', 150, '次', '45分钟', '遵医嘱静脉输液、输液观察及护理', NULL, NOW(), NOW()),
('肌肉注射', 'basic', 80, '次', '20分钟', '肌肉/皮下注射、药物注射护理', NULL, NOW(), NOW()),
('更换尿管', 'special', 220, '次', '40分钟', '留置尿管更换、尿道口护理、膀胱冲洗', NULL, NOW(), NOW()),
('更换胃管', 'special', 220, '次', '40分钟', '留置胃管更换、鼻饲护理指导', NULL, NOW(), NOW()),
('PICC维护', 'special', 260, '次', '50分钟', 'PICC导管冲管封管、敷料更换、观察记录', '推荐', NOW(), NOW()),
('康复训练', 'rehab', 180, '次', '60分钟', '偏瘫康复、关节活动度训练、日常功能训练', NULL, NOW(), NOW()),
('中医按摩', 'rehab', 160, '次', '45分钟', '专业中医手法按摩、穴位推拿、经络调理', '热门', NOW(), NOW()),
('术后换药', 'basic', 120, '次', '30分钟', '术后伤口评估、换药、敷料更换', NULL, NOW(), NOW()),
('生活护理', 'life', 300, '天', '8小时', '饮食照料、清洁卫生、翻身拍背、心理关怀', NULL, NOW(), NOW());

-- 病床数据
INSERT INTO `bed` (`bed_no`, `elder_name`, `age`, `care_level`, `nurse`, `check_in_date`, `status`, `create_time`, `update_time`) VALUES
('A-101', '张奶奶', 78, '一级护理', '王护士', '2026-03-15', 'occupied', NOW(), NOW()),
('A-102', '李爷爷', 82, '特级护理', '赵护士', '2026-01-20', 'occupied', NOW(), NOW()),
('A-103', '刘奶奶', 75, '二级护理', '张护士', '2026-04-10', 'occupied', NOW(), NOW()),
('A-104', NULL, 0, NULL, NULL, NULL, 'vacant', NOW(), NOW()),
('A-105', '陈爷爷', 85, '一级护理', '李护士', '2026-02-28', 'occupied', NOW(), NOW()),
('A-106', NULL, 0, NULL, NULL, NULL, 'vacant', NOW(), NOW()),
('A-107', '周奶奶', 71, '二级护理', '孙护士', '2026-04-22', 'occupied', NOW(), NOW()),
('A-108', '吴爷爷', 80, '三级护理', '郑护士', '2026-05-10', 'occupied', NOW(), NOW());

-- 服务订单数据
INSERT INTO `service_order` (`order_no`, `elder_name`, `service`, `staff`, `date`, `amount`, `status`, `create_time`, `update_time`) VALUES
('FW20260501001', '张奶奶', '压疮护理', '王护士', '2026-05-23', 198, 'completed', NOW(), NOW()),
('FW20260501002', '李爷爷', '静脉输液', '赵护士', '2026-05-23', 150, 'completed', NOW(), NOW()),
('FW20260502001', '刘奶奶', '康复训练', '李护士', '2026-05-24', 180, 'in-progress', NOW(), NOW()),
('FW20260502002', '陈爷爷', 'PICC维护', '张护士', '2026-05-24', 260, 'pending', NOW(), NOW()),
('FW20260503001', '周奶奶', '中医按摩', '孙护士', '2026-05-25', 160, 'pending', NOW(), NOW()),
('FW20260503002', '吴爷爷', '更换尿管', '郑护士', '2026-05-25', 220, 'pending', NOW(), NOW()),
('FW20260504001', '郑奶奶', '生活护理', '钱护士', '2026-05-25', 300, 'cancelled', NOW(), NOW()),
('FW20260505001', '冯爷爷', '术后换药', '杨护士', '2026-05-26', 120, 'pending', NOW(), NOW());

-- 巡诊数据
INSERT INTO `visit` (`elder_name`, `age`, `symptom`, `doctor`, `visit_date`, `time_slot`, `status`, `diagnosis`, `create_time`, `update_time`) VALUES
('赵爷爷', 76, '高血压复查', '王医生', '2026-05-20', '09:00', 'completed', '血压控制良好', NOW(), NOW()),
('钱奶奶', 82, '腰腿疼痛', '李医生', '2026-05-21', '10:30', 'completed', '腰椎退行性病变', NOW(), NOW()),
('孙爷爷', 69, '术后复查', '王医生', '2026-05-25', '14:00', 'upcoming', NULL, NOW(), NOW()),
('周奶奶', 73, '胸闷不适', '刘医生', '2026-05-26', '09:30', 'upcoming', NULL, NOW(), NOW()),
('吴爷爷', 88, '慢病管理', '李医生', '2026-05-27', '15:00', 'upcoming', NULL, NOW(), NOW()),
('郑奶奶', 71, '头晕乏力', '王医生', '2026-05-18', '11:00', 'completed', '贫血、低血压', NOW(), NOW());

-- 数据字典
INSERT INTO `dict` (`code`, `name`, `create_time`, `update_time`) VALUES
('care_level', '护理等级', NOW(), NOW()),
('service_type', '服务类型', NOW(), NOW()),
('order_status', '订单状态', NOW(), NOW()),
('staff_title', '护理人员职称', NOW(), NOW());

INSERT INTO `dict_item` (`dict_id`, `label`, `value`, `sort`, `status`, `create_time`, `update_time`) VALUES
(1, '特级护理', 'special', 1, 'active', NOW(), NOW()),
(1, '一级护理', 'level1', 2, 'active', NOW(), NOW()),
(1, '二级护理', 'level2', 3, 'active', NOW(), NOW()),
(1, '三级护理', 'level3', 4, 'active', NOW(), NOW()),
(2, '上门护理', 'nursing', 1, 'active', NOW(), NOW()),
(2, '上门巡诊', 'visit', 2, 'active', NOW(), NOW()),
(2, '康复理疗', 'rehab', 3, 'active', NOW(), NOW()),
(2, '打针输液', 'injection', 4, 'active', NOW(), NOW()),
(2, '生活照料', 'care', 5, 'active', NOW(), NOW()),
(3, '待接单', 'pending', 1, 'active', NOW(), NOW()),
(3, '服务中', 'in_progress', 2, 'active', NOW(), NOW()),
(3, '已完成', 'completed', 3, 'active', NOW(), NOW()),
(3, '已取消', 'cancelled', 4, 'active', NOW(), NOW()),
(4, '主任护师', 'chief', 1, 'active', NOW(), NOW()),
(4, '副主任护师', 'deputy_chief', 2, 'active', NOW(), NOW()),
(4, '主管护师', 'supervisor', 3, 'active', NOW(), NOW()),
(4, '护师', 'senior', 4, 'active', NOW(), NOW()),
(4, '护士', 'junior', 5, 'active', NOW(), NOW());

-- 预约数据
INSERT INTO `appointment` (`elder_name`, `elder_phone`, `elder_age`, `elder_address`, `service_type`, `appointment_date`, `appointment_time`, `staff_name`, `remark`, `status`, `create_time`, `update_time`) VALUES
('刘奶奶', '137****0123', 75, '东城区王府井大街', 'nursing', '2026-05-23', '09:00-10:00', '王护士', NULL, 'completed', NOW(), NOW()),
('陈爷爷', '136****3456', 85, '西城区金融街5号', 'rehab', '2026-05-24', '14:00-15:00', '李护士', NULL, 'upcoming', NOW(), NOW()),
('周奶奶', '135****7890', 71, '丰台区方庄路2号', 'visit', '2026-05-22', '10:00-11:00', '赵医生', NULL, 'completed', NOW(), NOW());
