# JKS error

### Create keystore 시도시 아래와 같은 error 발생
```
Key was created with errors:
Warning:
JKS 키 저장소는 고유 형식을 사용합니다. "keytool -importkeystore -srckeystore D:\SYSGEN\Acount\AndroidKeyStore\sysgenandroidkey.jks -destkeystore D:\SYSGEN\Acount\AndroidKeyStore\sysgenandroidkey.jks -deststoretype pkcs12"를 사용하는 산업 표준 형식인 PKCS12로 이전하는 것이 좋습니다.
```

### 해결
```
// 1. keytool 이 있는 폴더로 이동
C:\Program Files\Android\Android Studio\jre\bin


// 2. Open command window


// 3. Change the keystore format
keytool -importkeystore -srckeystore your_keystore_destination\your_keystore_file.jks -destkeystore your_keystore_destination\your_keystore_file.jks -deststoretype pkcs12
keytool -importkeystore -srckeystore D:\SYSGEN\Acount\AndroidKeyStore\sysgenandroidkey.jks -destkeystore D:\SYSGEN\Acount\AndroidKeyStore\sysgenandroidkey.jks -deststoretype pkcs12


// 4. Command result 
sysgenandroidkey 별칭에 대한 항목이 성공적으로 임포트되었습니다.
임포트 명령 완료: 성공적으로 임포트된 항목은 1개, 실패하거나 취소된 항목은 0개입니다.

Warning:
"D:\SYSGEN\Acount\AndroidKeyStore\sysgenandroidkey.jks"을(를) Non JKS/JCEKS(으)로 이전했습니다. JKS 키 저장소가 "D:\SYSGEN\Acount\AndroidKeyStore\sysgenandroidkey.jks.old"(으)로 백업되었습니다.
```
