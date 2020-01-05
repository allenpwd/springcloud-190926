### 使用oracle jce补丁
    JDK自带的JCE是有长度限制的版本，如果不更换会报错：java.security.InvalidKeyException: Illegal key size
#### 操作步骤
- 下载jce，jdk8版本下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
- 将里面的jar放到%JAVA_HOME%\jre\lib\security

### 密钥文件
#### 使用jdk中自带的keytool工具生成
```
keytool -genkeypair -alias myAlias -keyalg RSA -dname "CN=Web Server,OU=China,O=gu,L=GuangZhou,S=CZ,C=China" -keypass myKeyPass -keystore config-service.jks -storepass myStorePass -validity 365
```
其中dname各个属性：
- CN：名字与姓氏
- OU：组织单位名称
- O：组织名称
- L：所在城市或区域名称
- C：国家/地区代码
- ST：省/市/自治区名称
#### 查看密钥对信息
```
keytool -v -list -keystore config-service.jks -storepass myStorePass
```


### config加密解密功能的相关端点
- /encrypt/status：查看加密功能状态
- /key：查看密钥
- /encrypt：对请求的body进行加密并返回
- /decrypt：对请求的body进行解密并返回