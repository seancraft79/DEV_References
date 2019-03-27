
# Install Docker in Ubuntu


### ���پ� ���ʴ�� �Է�

```
sudo apt update

sudo apt install apt-transport-https ca-certificates curl software-properties-common

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"

sudo apt update

apt-cache policy docker-ce
```

### ������ �� �Է��ϰ� ���� ���� �޼���
```
docker-ce:
  Installed: (none)
  Candidate: 18.06.1~ce~3-0~ubuntu
  Version table:
     18.06.1~ce~3-0~ubuntu 500
        500 https://download.docker.com/linux/ubuntu bionic/stable amd64 Packages
     18.06.0~ce~3-0~ubuntu 500
        500 https://download.docker.com/linux/ubuntu bionic/stable amd64 Packages
     18.03.1~ce~3-0~ubuntu 500
        500 https://download.docker.com/linux/ubuntu bionic/stable amd64 Packages
```

### Docker ��ġ
```
sudo apt install docker-ce
```

### ���������� �۵��ϴ��� Ȯ��
```
sudo systemctl status docker
```


### Docker-compose ��ġ
##### ��ġ���� Ȯ��
```
docker-compose -v
```

##### ��ġ
```
apt install docker-compose
```
