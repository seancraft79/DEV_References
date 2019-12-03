
# ADB Commands

## Options
```
-b <buffer>	보기 위한 대체 로그 버퍼를 로드합니다(예: events 또는 radio). main, system 및 crash 버퍼 세트가 기본적으로 사용됩니다. 대체 로그 버퍼 보기를 참조하세요.
-c, --clear	선택한 버퍼를 지우고(플러시하고) 종료합니다. 기본 버퍼 세트는 main, system 및 crash입니다. 모든 버퍼를 지우려면 -b all -c를 사용합니다.
-e <expr>, --regex=<expr>	로그 메시지가 <expr>과 일치하는 줄만 출력합니다. 여기서 <expr>은 정규 표현식입니다.
-m <count>, --max-count=<count>	<count>개 줄을 출력한 후 종료합니다. --regex와의 페어링이 필요하지만 자체적으로도 작동합니다.
--print	--regex 및 --max-count와 페어링하면 콘텐츠가 정규 표현식을 우회할 수 있지만, 여전히 올바른 일치 숫자에서 중지합니다.
-d	로그를 화면에 덤프하고 종료합니다.
-f <filename>	로그 메시지 출력을 <filename>에 씁니다. 기본값은 stdout입니다.
-g, --buffer-size	지정된 로그 버퍼의 크기를 출력하고 종료합니다.
-n <count>	순환되는 로그의 최대 수를 <count>로 설정합니다. 기본값은 4입니다. -r 옵션이 필요합니다.
-r <kbytes>	출력의 <kbytes>마다 로그 파일을 순환시킵니다. 기본값은 16입니다. -f 옵션이 필요합니다.
-s	filterspec '*:S'와 동일하며 콘텐츠를 추가하는 filterspec 목록 앞에 두기 위해 사용합니다.
-v <format>	로그 메시지의 출력 형식을 설정합니다. 기본값은 threadtime 형식입니다. 지원되는 형식의 목록은 로그 출력 형식 제어를 참조하세요.
-D, --dividers	각 로그 버퍼 간의 구분선을 출력합니다.
-c	전체 로그를 플러시하고(지우고) 종료합니다.
-t <count>	가장 최근의 줄 수만 출력합니다. 이 옵션은 -d 기능을 포함합니다.
-t '<time>'	지정된 시간 이후 가장 최근의 줄을 출력합니다. 이 옵션은 -d 기능을 포함합니다. 공백이 있는 매개변수에 따옴표를 사용하는 방법에 관한 자세한 내용은 -P 옵션을 참조하세요.

adb logcat -t '01-26 20:52:41.820'

-T <count>	지정된 시간 이후 가장 최근의 줄 수를 출력합니다. 이 옵션은 -d 기능을 포함하지 않습니다.
-T '<time>'	지정된 시간 이후 가장 최근의 줄을 출력합니다. 이 옵션은 -d 기능을 포함하지 않습니다. 공백이 있는 매개변수에 따옴표를 사용하는 방법에 관한 자세한 내용은 -P 옵션을 참조하세요.

adb logcat -t '01-26 20:52:41.820'

-L, -last	마지막 재부팅 전에 로그를 덤프합니다.
-B, --binary	로그를 바이너리로 출력합니다.
-S, --statistics	로그 스패머를 식별하고 타겟팅할 수 있도록 통계를 출력에 포함합니다.
-G <size>	로그 링 버퍼의 크기를 설정합니다. K 또는 M을 끝에 추가하여 킬로바이트나 메가바이트를 표시합니다.
-p, --prune	현재의 허용 목록과 차단 목록을 출력하고(읽고) 다음과 같이 인수를 사용하지 않습니다.

    adb logcat -p
    

-P '<list> ...'
--prune '<list> ...' -P '<white_and_black_list>'	허용 목록과 차단 목록을 작성(설정)하여 특정 목적을 위한 로깅 콘텐츠를 조정합니다. <white> 및 ~<black> 목록 항목의 혼합된 콘텐츠를 제공합니다. 여기서 <white> 또는 <black>은 UID, UID/PID 또는 /PID일 수 있습니다. logcat 통계(logcat -S)의 지침에 따라 다음과 같은 목적으로 허용 목록과 차단 목록을 조정할 수 있습니다.
UID 선택을 통해 특정 로깅 콘텐츠에 가장 긴 수명을 제공합니다.
차단 목록 who(UID) 또는 what(PID)은 logspan을 늘리기 위해 이러한 리소스를 사용합니다. 그러면 진단 중인 문제를 더 잘 볼 수 있습니다.
기본적으로 로깅 시스템은 로그 통계에서 최악의 위반자를 자동으로 차단 목록에 추가하여 새로운 로그 메시지를 위한 공간을 동적으로 만듭니다. 휴리스틱이 고갈되면 시스템은 가장 오래된 항목을 잘라내어 새 메시지를 위한 공간은 만듭니다.

허용 목록을 추가하면 AID(Android Identification Number)가 보호되어 프로세스의 AID 및 GID가 위반자로 선언되는 것을 방지할 수 있으며, 차단 목록을 추가하면 최악의 위반자로 간주되기 전에 공간을 확보할 수 있습니다. 잘라내기를 얼마나 적극적으로 할지 선택할 수 있습니다. 또한 각 로그 버퍼의 가장 오래된 항목에서만 콘텐츠를 삭제하도록 잘라내기를 해제할 수도 있습니다.

따옴표

adb logcat은 따옴표를 유지하지 않으므로 허용 목록과 차단 목록을 지정하기 위한 구문은 다음과 같습니다.


    $ adb logcat -P '"<white_and_blacklist>"'

    or

    adb shell
    $ logcat -P '<white_and_blacklist>'
    

다음 예제는 PID 32676 및 UID 675로 허용 목록을 지정하고, PID 32677 및 UID 897로 차단 목록을 지정합니다. 더 빠른 잘라내기를 위해 차단 목록의 PID 32677에는 가중치가 적용됩니다.


    adb logcat -P '"/32676 675 ~/32677 897"'
    

사용할 수 있는 다른 차단 목록 및 허용 목록 명령어 변형은 다음과 같습니다.


    ~! worst uid blacklist
    ~1000/! worst pid in system (1000)
    

--pid=<pid> ...	지정된 PID의 로그만 출력합니다.
--wrap	두 시간 동안 절전 모드일 때 또는 버퍼가 래핑하려고 할 때 중 하나가 먼저 발생하는 경우 about-to-wrap wakeup을 제공하여 폴링의 효율성을 높입니다.
```

## Filters
```
V: Verbose(가장 낮은 우선순위)
D: Debug
I: Info
W: Warning
E: Error
F: Fatal
S: Silent(가장 높은 순위, 이 경우 아무것도 출력되지 않음)
```

### APK 설치
```
adb install -r <app.apk>
```

### Pull file
```
adb pull /sdcard/BUS/BUS.db .
```

### Push file
```
adb push BUS.db /sdcard/BUS/
```

### 실행중인 프로세스 죽이기
```
busybox ps -ef | grep <프로세스명>
// - PID 확인후 kill
kill -9 <PID>
```

### Get logcat log file
```
adb logcat > logcat.txt
adb logcat -v threadtime > logcat.txt
```