# Git 명령어


### Add Remote
```
git add remote <URL>
git remote add origin https://knum@bitbucket.org/knum/knum9test.git
```


### Change Remote URL
````
git remote set-url origin git@github.com:seancraft79/DEV_References.git
```


### Show remote url
```
git remote -v
```


### SSH
```
ssh-keygen
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

### Start the ssh-agent in the background
```
eval 'ssh-agent'
eval $(ssh-agent -s)
```


### Add your SSH private key to the ssh-agent
```
ssh-add privatekey
ssh-add ~/.ssh/keyname
```


### Git config credential
```
git config credential.username 'username'
```


### Git credential helper
```
git config --global credential.helper wincred
```


### Config 확인
```
git config --list
```


### Config
```
git config --global user.name “Your name”
git config --global user.email “Your email address”
```


### Branch 보기
```
// 지역, 원격 모든 브랜치 보기   
git branch -a

// 원격브랜치ㅣ 보기
git branch -r
```


### Branch 만들기
```
git checkout -b <Branch>
```


### Branch Merge
```
git merge <Branch>
git merge --no-commit <Branch>
```


### Delete local branch
```
--delete    git branch -d <Branch>
--force     git branch -D <Branch>
```


### Delete remote branch
```
git push origin --delete <Branch>
```


### Reset commit to REF(commit reference or SHA)
```
git reset --hard REF
```

### Reset just last commit
```
git reset --hard HEAD^
```

### Reset just last 2 commits
```
git reset --hard HEAD~2
```

### Set tracking
```
git branch -u test1 origin/test1
```


### Push
```
-u (a.k.a.  --set-upstream)
git push -u origin <Branch>
git push origin localBranchName:remoteBranchName
```


### Pull
```
git pull origin <Branch>
```


### Clone by branch name
```
git clone -b <branch_name> --single-branch <저장소 URL>
```


### Unstage all
```
git reset
```


### submodule 추가
1. parent, module 둘다 git repository 여야 됨
2. module 폴더 에서 일반적으로 git clone
3. add submodule
```
	git submodule add <repository> <destination folder>
```

### submodule 클론
git submodule update --init --recursive
git clone --recursive <project url>


### submodule 로그 필터링
git filter-branch


# Git log
### 출력 log 갯수 제한
```
git log -(n)
git log -2 --pretty=oneline
```

### 특정 날짜 이전/이후 commit 만 조회
```
git log --since[after,until,before]
git log --since="2017-01-29"
```

### 특정 author/commitor 의 commit 만 조회
```
git log --author[committer]
git log --author=unknown
```

### 특정 경로(폴더or파일)의 변경사항에 대해서만 조회
```
git log -- [path1] [path2] ...
git log -p -- ./w.txt ./v.txt ./work
```


# ISSUE 에러
remote: Permission to seancraft79/DEV_Snippets.git denied to shleemr.
-> git config credential.username 'seancraft79'
-> 으로 적당한 username 넣어줘야 됨
