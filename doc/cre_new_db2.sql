
--FTP配置表
drop table pub_ftpcfg;
create table pub_ftpcfg (
FTPCFG_ID      VARCHAR(10)       not null,
--FTPCFGID   
FTPCFGNAME      VARCHAR(20),
--名称   
IPADR      VARCHAR(20),
--IP   
PORT      VARCHAR(5),
--端口   
USRNAM      VARCHAR(20),
--用户   
USRPWD      VARCHAR(20),
--密码   
OBJDIR      VARCHAR(60),
--目标路径   
OBJFIL      VARCHAR(60),
--目标文件   
LCLDIR      VARCHAR(60),
--本地路径   
LCLFIL      VARCHAR(60),
--本地文件   
MODE      VARCHAR(10)  default 'bin',
--模式   bin or ascii
RMK      VARCHAR(100),
--备注   
TMPFLAG      VARCHAR(1)  default '0'
--临时标志   
);
ALTER TABLE pub_ftpcfg ADD PRIMARY KEY (FTPCFG_ID);


--短信队列
drop table pub_textmsgque;
create table pub_textmsgque (
TEXTMSG_ID      VARCHAR(30)       not null,
--短信ID   
MSG_DATE      VARCHAR(8),
--发送日期   
BATCH_ID      VARCHAR(24),
--批次号   
MOBILE      VARCHAR(15)  default '0',
--手机号   
TEXTCONTENT      VARCHAR(200)  default '0',
--内容   
EXPIREDATE      VARCHAR(14),
--过期日期
MSG_TYPE      VARCHAR(2)  default '0',
--类型   0,定时;1,用户批量,2,单条
FLAG      VARCHAR(2)  default '0',
--发送标志   0、待审;1、通过;2、已发送
TMPFLAG      VARCHAR(1)  default '0'
--临时标志   
receivetype char(10) default '';
--接收人类型 mng,行内;cus,客户
receiver char(60) default '';
--接收人标识
);
ALTER TABLE pub_textmsgque ADD PRIMARY KEY (TEXTMSG_ID);


--定时任务流水表
drop table pub_schedulejnl;
create table pub_schedulejnl (
SCHEDULE_ID      VARCHAR(10)       not null,
--任务ID   
TIME      VARCHAR(14)       not null,
--时间   
YEAR      VARCHAR(4)  default '0',
--年份   
MONTH      VARCHAR(2)  default '0',
--月份   
DAY      VARCHAR(2)  default '0',
--日期   
HOUR      VARCHAR(2)  default '0',
--小时   
MINUTE      VARCHAR(2)  default '0',
--分钟   
FINISH_FLAG      VARCHAR(2)  default '0',
--完成标志   0、未开始;1、执行中;2、成功;3、失败
REMARK      VARCHAR(200)  default '0',
--分钟 
TMPFLAG      VARCHAR(1)  default '0'
--临时标志   
);
ALTER TABLE pub_schedulejnl ADD PRIMARY KEY (SCHEDULE_ID,TIME);


--定时任务表
drop table pub_scheduleinfo;
create table pub_scheduleinfo (
SCHEDULE_ID      VARCHAR(10)       not null,
--任务ID   
SCHEDULE_DESC      VARCHAR(60),
--任务说明   
SCHEDULE_CLASS      VARCHAR(60),
--类名   
SCHEDULE_METHOD      VARCHAR(20),
--方法名   
MONTH      VARCHAR(30)  default '0',
--执行月份   
DAY      VARCHAR(30)  default '0',
--日期   
WEEKDAY      VARCHAR(30)  default '0',
--星期几   
HOUR      VARCHAR(30)  default '0',
--小时   
MINUTE      VARCHAR(30)  default '0',
--分钟   
FREQUENCY      VARCHAR(20)  default '0',
--频率   
FLAG      VARCHAR(2)  default '0',
--生效标志   1、有效;2、无效
FINISH_FLAG      VARCHAR(2)  default '0',
--完成标志   0、未开始;1、执行中;2、成功;3、失败
USERID      VARCHAR(64)  default 'huangchi',
--登录名   
CRE_DATE      VARCHAR(8)  default '20000101',
--建立日期   
END_DATE      VARCHAR(8)  default '30000101',
--结束如期   
ADDINFO      VARCHAR(250)  default '30000101',
--信息 
TMPFLAG      VARCHAR(1)  default '0'
--临时标志 
monflg       char(1) default '0';
--监控标志  0、默认不监控;1、监控 
montyp       char(10) default 'day';
--临时类型 
manager      char(60) default '';  
--管理员
);
ALTER TABLE pub_scheduleinfo ADD PRIMARY KEY (SCHEDULE_ID);


--通讯录信息表
drop table contactsinfo;
create table contactsinfo (
OWNER_EMP      VARCHAR(20)       not null,
--员工编号   
PK_EMP      VARCHAR(30)       not null,
--通讯录员工编号   
USERID      VARCHAR(64),
--登录名   
EMPNAME      VARCHAR(30)  default '0',
--姓名   
BUSINESS_PHONE      VARCHAR(30)  default '0',
--办公电话   
MOBILE      VARCHAR(40)  default '0',
--手机号   
DEPT_NAME      VARCHAR(60)  default '0',
--所属部门名称   
JOB_DESC      VARCHAR(100)  default '0',
--职务描述   
GENDER_DESC      VARCHAR(10)  default '0',
--性别名称   
LAST_VISIT_DATE      VARCHAR(8),
--上次访问日期   
VISIT_TIMES      VARCHAR(10),
--访问次数   
CONSTACTS_FLAG      VARCHAR(1)  default '0',
--标志   0、待审;1、有效;2、无效
CONSTACTS_DESC      VARCHAR(200)  default '0',
--备注   
TMPFLAG      VARCHAR(1)  default '0'
--临时标志   
);
ALTER TABLE contactsinfo ADD PRIMARY KEY (OWNER_EMP,PK_EMP);


--拼音对照表信息表
drop table para_pinyin;
create table para_pinyin (
CHNCODE      VARCHAR(2)       not null,
--中文字符   
PINYINCODE      VARCHAR(30),
--拼音   
PINYINFIRST      VARCHAR(2)
--拼音首字母   
);
ALTER TABLE para_pinyin ADD PRIMARY KEY (CHNCODE);


--分行信息表
drop table branchinfo;
create table branchinfo (
PK_CORP      VARCHAR(10)       not null,
--分行号   
PK_FATHER_CORP      VARCHAR(10),
--上级分行号   
PK_CORPTYPE      VARCHAR(20),
--分行类型   
UNITCODE      VARCHAR(20)  default '0',
--分行码   
UNITNAME      VARCHAR(60)  default '0',
--分行名称   
UNITSHORTNAME      VARCHAR(60)  default '0',
--分行简称   
BRANCH_NO      VARCHAR(6)  default '0',
--分行号   
BRANCH_FLAG      VARCHAR(1)  default '0',
--生效标志   0、待审;1、有效;2、无效
CRE_DATE      VARCHAR(8)  default '20000101',
--建立日期   
END_DATE      VARCHAR(8)  default '30000101',
--结束如期   
TMPFLAG      VARCHAR(1)  default '0'
--临时标志   
);
ALTER TABLE branchinfo ADD PRIMARY KEY (PK_CORP);


--部门信息表
drop table deptinfo;
create table deptinfo (
PK_DEPT      VARCHAR(20)       not null,
--代码   
PK_CORP      VARCHAR(10),
--分行号   
DEPT_CODE      VARCHAR(10),
--部门编号   
DEPT_NAME      VARCHAR(60)  default '0',
--部门名称   
DEPT_SHORT_NAME      VARCHAR(10)  default '0',
--部门简称   
PK_FATHER_DEPT      VARCHAR(20)  default '0',
--上级部门代码   
DEPT_SEQ      VARCHAR(500)  default '0',
--部门序号   
DEPT_KIND      VARCHAR(20)  default '0',
--类型   
DEPT_NO      VARCHAR(6)  default '0',
--部门号   
BELONG_DEPT_NO      VARCHAR(6)  default '0',
--上级部门号   
BRANCH_NO      VARCHAR(6)  default '0',
--分行号   
DEPT_FLAG      VARCHAR(1)  default '0',
--生效标志   0、待审;1、有效;2、无效
CRE_DATE      VARCHAR(8)  default '20000101',
--建立日期   
END_DATE      VARCHAR(8)  default '30000101',
--结束如期   
TMPFLAG      VARCHAR(1)  default '0'
--临时标志   
);
ALTER TABLE deptinfo ADD PRIMARY KEY (PK_DEPT);


--员工信息表
drop table staffinfo;
create table staffinfo (
PK_EMP      VARCHAR(20)       not null,
--员工编号   
USERID      VARCHAR(64),
--登录名   
PK_CORP      VARCHAR(10),
--分行号   
PK_PSNCL      VARCHAR(20),
--员工类别   
EMPCODE      VARCHAR(20)  default '0',
--工号   
EMPNAME      VARCHAR(30)  default '0',
--姓名   
PID      VARCHAR(18)  default '0',
--身份证号   
BUSINESS_PHONE      VARCHAR(40)  default '0',
--办公电话   
MOBILE      VARCHAR(40)  default '0',
--手机号   
EMAIL      VARCHAR(60)  default '0',
--邮箱   
DEPT_ID      VARCHAR(20)  default '0',
--所属部门   
DEPT_NAME      VARCHAR(60)  default '0',
--所属部门名称   
DEPT_NO      VARCHAR(6)  default '0',
--部门号   
BRANCH_NO      VARCHAR(6)  default '0',
--分行号   
JOB_ID      VARCHAR(20)  default '0',
--职务编号   
JOB_DESC      VARCHAR(100)  default '0',
--职务描述   
PK_JOB_LEVEL      VARCHAR(20)  default '0',
--职务级别   
GENDER_ID      VARCHAR(20)  default '0',
--性别编号   
GENDER_DESC      VARCHAR(10)  default '0',
--性别名称   
BIRTHDAY      VARCHAR(10),
--生日   
WORK_DATE      VARCHAR(10),
--工作日期   
BOC_STARTDATE      VARCHAR(10),
--进行日期   
BOCCORP_STARTDATE      VARCHAR(10),
--进分行日期   
OUT_DATE      VARCHAR(10),
--离职日期   
USER_FLAG      VARCHAR(1)  default '0',
--生效标志   0、待审;1、有效;2、无效
USER_DESC      VARCHAR(500)  default '0',
--个人说明   
TMPFLAG      VARCHAR(1)  default '0'
--临时标志   
);
ALTER TABLE staffinfo ADD PRIMARY KEY (PK_EMP);


--已核销资产小类表
drop table AssDetTypTbl;
create table AssDetTypTbl (
AssTyp      CHAR(4),
--核销资产大类   
AssNam      VARCHAR(100),
--核销资产大类   
AssDetTyp      CHAR(4)       not null,
--核销资产小类   
AssDetNam      VARCHAR(100),
--核销资产大类   
BankNo      CHAR(6)       not null,
--分行号   
TmpFlg      CHAR(1)  default '0'
--备用标志   备用标志
);
ALTER TABLE AssDetTypTbl ADD PRIMARY KEY (AssDetTyp,BankNo);


--财政大额交易数据表
drop table fin_pro_bigjnl;
create table fin_pro_bigjnl (
Logno       CHAR(14)       not null,
--流水号 
Tckno       CHAR(12)       not null,
--传票号
ActDat      CHAR(8)       not null,
--交易日期  
en_name1      VARCHAR(100)  default '',
--一级预算单位   
en_name       VARCHAR(100)  default '',
--基层预算单位   
mk_name       VARCHAR(100)  default '',
--资金性质  
payee_account_no      VARCHAR(42)  default '',
--收款人账号 
payee_account_name      VARCHAR(100)  default '',
--收款人   
payee_account_bank      VARCHAR(100)  default '',
--收款人银行   
txnamt      int  default '0',
--交易金额
pay_summary_name      VARCHAR(300)  default '',
--附言   
TMPFLAG      VARCHAR(1)  default '0'
--临时标志   
);
ALTER TABLE fin_pro_bigjnl ADD PRIMARY KEY (Logno);
create index ni1_fin_pro_bigjnl on fin_pro_bigjnl(ActDat,TxnAmt);