# GitBook CLI 이용 시작하기

##### Node.js 버전확인
[Node.js](https://nodejs.org)
```
$ node -v
```

##### Install gitbook-cli
```
$ npm install gitbook-cli -g
$ gitbook install
```

##### GitBook 만들기
```
$ gitbook init ./<BookName>
```

##### Local 환경에서 호스팅
```
$ gitbook build
$ gitbook serve   // http://localhost:4000 에서 호스팅
```

### YouTube Plug-in 사용하기
1. root path 에 book.json  추가
	[GitBook YouTube Plugin](https://www.npmjs.com/package/gitbook-plugin-youtube)
2. youtube plugin 추가
```
{
    "plugins": ["youtube"]
}
```

3. Install
```
$ gitbook install
```

4. 원하는 곳에 Embed
```
Take a look at this video:
 
{% youtube src="https://www.youtube.com/watch?v=9bZkp7q19f0" %}{% endyoutube %}
```