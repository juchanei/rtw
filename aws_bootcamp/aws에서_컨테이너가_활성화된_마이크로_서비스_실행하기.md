# AWS에서 컨테이너가 활성화된 마이크로 서비스 실행하기

---
Docker/EC2

## 첫 번째 컨테이너 만들기
Docker에 익숙해지기
### 컨테이너란
- 프로세스를 격리한 리눅스 커널 기능
  상자에 물건을 넣기만 하면 물품을 배송할 수 있다.
  내 서비스를 컨테이너에 설치하면 OS와 같은 기반 환경에서 실행가능하다.
  환경을 타지 않는 장점이 있다.
- 스테로이드를 맞은 chroot
  기존의 격리명령보다 더 강력하다.
- 세분화된 리소스 할당
- namespaces, cgroups 활용
  커널 내부의 격리 기능을 이용해서 만들어져있다.

### 가상머신과 비교
컨테이너와 가상머신은 격리되어있다는 점에서 유사하다.

- 가상머신
  - 실서버와 가상서버가 하이퍼바이저 위에 격리되어있다.
  - 호스트OS 위에 하이퍼바이저 위에 가상서버 위에 게스트OS가 올라가있다.
- 도커
  - 호스트OS 위에 바로 사용자서비스가 올라간다.
  - 경량컨테이너

### 이점
- 속도
  가상머신이나 베어 메탈 호스트에 비해 성능 오버헤드가 거의 발생하지 않는다.
- 일관성
  프로덕션 스택과 동일한 환경에서 코드를 테스트 할 수 있다.
- 밀도/리소스 효율성
  도커 여러개를 띄워서 컴퓨팅 리소스 사용률을 높일 수 있다. 특히 CPU.
- 유연성
  컨테이너를 플랫폼 간에 자유롭게 이동할 수 있습니다.
  아직은 리눅스에서만, 하지만 곧 윈도우에서도 가능할 것.

### 도커
도커는 컨테이너와 컨테이너 딜리버리 기능이 포함 된 서버 플랫폼.
주로 웹서비스에 사용된다.
도커파일즈를 이용하면, makefile처럼 도커를 사용하기 위한 규칙들을 선언해서 이미지를 만들 수 있다.<br>

개발 서비스에 붙은 여러가지 라이브러리의 환경 의존성 때문에, 프로덕션 서버에 서비스를 올리기 쉽지 않다.
도커는 서비스와 프로덕션 서버 사이의 어댑터 역할을 하여 연결을 돕는다.

### 컨테이너 생성하기
터미널에서 대화식으로 생성 가능하다.
대화식은 보통 테스트에만 사용하고, 주로 도커파일을 사용한다.
도커허브 레포지토리에는 많이 사용하는 이미지들이 미리 구축되어있다.

### 도커파일 명령
- FROM
  사용할 기본 이미지를 지정한다.
  퍼블릭 이미지 / 사용자 정의 리포지토리
- RUN
  리눅스 명령이 뒤따라온다.
- EXPOSE
  기본적으로 도커는 모든 포트가 막혀있다.
  이 명령으로 원하는 포트를 열어줄 수 있다.
- VOLUME
  컨테이너 사이의 공유하는 이미지가 있을 때 사용한다.
- ENTRYPOINT
  컨테이너를 실행 파일 자체로 취급하여 실행한다.
  init 프로세스와 유사하다?
- CMD
  엔트리포인트에 파라미터를 전달하는데 사용된다.
- COPY
  지금 디렉터리에서 컨테이너로 파일을 복사한다.
- ADD
  지금 디렉토리를 컨테이너에 추가한다.
- ENV
  환경변수를 설정한다.

### 도커 명령
- Docker run <플래그><이미지><프로세스>
  도커 이미지를 실행한다.
- Docker exec <플래그><컨테이너><프로세스>
- Docker stop <컨테이너>
- Docker kill <컨테이너>
- Docker ps
- Docker logs
- Docker stat
- Docker build -t <태그><경로>
  디렉토리 내 도커파일을 읽고, 해당 이미지를 패키징해서 도커허브 계정에 올린다.
- Docker push <사용자이름>/<이미지>:<태그>
  도커허브에 퍼블릭으로 업로드. 퍼블릭은 무료. 프라이빗은 이미지 하나만 무료.
- Docker images
- Docker pull <컨테이너>

### 실습
https://events-aws.qwiklab.com/
juchanei@naver.com l

https://hub.docker.com/
juchanei l

- aws console에 접속
- pem, ppk 파일을 받아서 git-bash, putty로 ec2에 접속
  >ssh -i yourkey.pem ec2-user@<your-EC2-instance-IP-address>
  >docker info

  정상적으로 도커 정보가 출력되면 성공
- 도커허브에서 우분투이미지를 받아 도커에 컨테이너로 올린다.
  >docker pull ubuntu


```
Exercise 1 - Building and Running Your First Container

==================================================================================================================


==================================================================================================================
7. SSH into the instance
==================================================================================================================

ssh -i yourkey.pem ec2-user@<your-EC2-instance-IP-address>

==================================================================================================================
8. Log into Docker Hub
==================================================================================================================

docker login

==================================================================================================================
9. Confirm you've been logged into Docker Hub
==================================================================================================================

docker info

==================================================================================================================
10. Pull a stock ubuntu container
==================================================================================================================

docker pull ubuntu

==================================================================================================================
11. Run the container in interactive mode
==================================================================================================================

docker run -p 80:80 -it ubuntu /bin/bash

==================================================================================================================
12. Explore the container shell. Try installing apache and starting the process
==================================================================================================================

apt-get install -y apache2

sudo /etc/init.d/apache2 start  

==================================================================================================================
13. Drop out of the container shell
==================================================================================================================

exit

==================================================================================================================
14. Clone a sample application from github
==================================================================================================================

git clone https://github.com/awslabs/ecs-demo-php-simple-app

==================================================================================================================
15. Switch into the newly cloned directory
==================================================================================================================

cd ecs-demo-php-simple-app

==================================================================================================================
16. Inspect the Docker file
==================================================================================================================

cat Dockerfile

==================================================================================================================
17. Build the docker image
==================================================================================================================

docker build -t <UserName>/php-demo-app:v1 .

==================================================================================================================
18. List our images
==================================================================================================================

docker images

==================================================================================================================
19. Run the container in detached mode
==================================================================================================================

docker run -dp 80:80 <UserName>/php-demo-app:v1

==================================================================================================================
21. Inspect your running container
==================================================================================================================

docker ps

==================================================================================================================
22. Exec into your running container to get a shell
==================================================================================================================

docker exec -it <container_id> /bin/bash

==================================================================================================================
22. Explore the shell of your conainter
==================================================================================================================

env
exit

==================================================================================================================
23. Terminate your running container
==================================================================================================================

docker kill <container_ID>

==================================================================================================================
24. Push your image to Docker Hub
==================================================================================================================

docker push <UserName>/php-demo-app:v1
```


## AWS 기반 마이크로 서비스 개요
### 마이크로 서비스
모놀리딕 아키텍처
- 이점
  - 간편한 개발
  - 간편한 배포
  - 간편한 확장
  - 일관된 또는 단일 기술
- 단점
  - 코드유지관리 어려움
  - 테스트가 어려움
  - 전체 시스템을 확장해야 함
  - 위 문제로 출시 기간이 길어짐
  - 재사용할 수 없음
  - 팀이 거대해서 확장하기 어려움

마이크로 서비스
>
언어의 구애를 받지 않은 API를 통해 서로 통신하는 독립 된 여러 작은 프로세스로 복잡한 애플리케이션을 구성하는 소프트웨어 아키텍처 스타일을 말합니다. 이러한 서비스는 작고, 결합 해제되어있으며, 작언 작업에 집중합니다.
-위키피디아

- 이점
  - 탄력성
    작은 요소로 나뉘어있어 확장의 탄력성이 있음
  - 복원력
  - 미니멀
  - 자족형
    다른 외부서비스의 자원/데이터베이스를 이용하지 않음.
    자신의 데이터는 스스로 숨겨서 사용
    외부에 노출할 데이터는 API로 제공
  - 기술에 구애받지 않음
  - 독립적인 반복 파이프라인
  - 명확한 관점
    코드의 범위가 해당 서비스에 한정되므로 단편적 이해가 쉬움
  - 분산되어있음
  - 상태 비저장

마이크로서비스는 민첩성

### 핵심 개념
- 소결합
- 뛰어난 응집력
- 한정된 컨텍스트 노출
- 초점 : 데이터가 아니라 비즈니스 기능
- 중첩된 컨텍스트

### 의사 결정 사항
- 동기식 / 비동기식
- RPC / REST
- 오케스트레이션 / 코리오그래피

### 조직에 의미하는 것
- 배포 및 테스트 자동화를 도입
- 장애와 결함을 수용
- 비즈니스 기능을 위주로 응집
  - 작은 팀 - Two Pizza Team
  - 자율적인 주인

### 도커와 마이크로 서비스
- 하나의 어플리케이션이 여러 컨테이너 서비스로 구성됨
- 프로그래밍 언어에 구애받지 않음
- 변경이 불가능한 서버, 하나의 큰 인터페이스
- 하나의 이미지, 하나의 버전, 하나의 아티팩트

ECS가 컨테이너 클러스터를 관리/스케줄링.

### 실습
마이크로 서비스는 시스템 전체의 관점에서 볼 때 너무 복잡하다.
ec2 가상서버 위에 도커가 올라가있는 상태.
ecs는 복잡한 시스템을 관리하는 서비스.
마이크로서비스 100개 이상의 규모에서 ecs를 사용<br>

ecs 핵심구성요소
- 테스크
  짧은 서비스에 적합
  ec2위에 돌아가는 컨테이너를 작업정의로 함
  - 컨테이너 정의
    실행 될 컨테이너, 도커파일과 유사, json으로 되어있음
  - 볼륨 정의
    볼륨은 컨테이너 사이에 공유할 데이터
- 서비스
  오래동작하는 서비스에 적합
  elb를 앞단에 두고 컨테이너에 로드밸런싱
  - ec2 인스턴스 실행
  - 작업 정의 생성
  - 서비스 생성
  - vpc에 컨테이너가 돌아감
- 클러스터
  - 리전, 리소스풀, 컨테이너 인스턴스를 그룹화, 작게 시작하여 동적으로 확장 가능
  - 오토스케일링을 해두면, 부하에 따라 알아서 인스턴스 수를 늘린다.
  - ecs 람다
  - 스팟인스턴스
    경매형식으로 서버를 싸게 더 붙일 수 있다.
    CI/CD에 적합


## 컨테이너 기반 애플리케이션 확장하기?????


## 컨테이너 기반 마이크로 서비스를 위한 지속적 전달 파이프라인
### 소프트웨어 개발 수명 주기
- 전달 파이프라인
  - 빌드 > 테스트 > 릴리스
- 피드백 루프
  - 계획 < 모니터링

데브옵스 : 이 수명주기를 가속화하는 효율성

### 전달 파이프라인의 목표
- 소프트웨어 배포 자동화
  - aws 엘라스틱 빈스톡
  - aws 코드디플로이
  - aws 옵스웍스
- 릴리스 프로세스 자동화
  - aws 코드파이프라인

### 실습

```
ECS Microservices: Lab 3 - Continuous Delivery Pipelines for ECS Microservices (Linux)

==================================================================================================================

Using this command reference.

==================================================================================================================


1. Locate the section you need. Each section in this file matches a section in the lab instructions.

2. Do NOT enable the Word Wrap feature in Windows Notepad or the text editor you use to view this file.

++++1. Task: Setup the Jenkins CI Server ++++

==================================================================================================================
1.6 Setup the Jenkins ECR repository
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices/jenkins/

./setup-jenkins.sh

==================================================================================================================
1.7 Build the Jenkins Docker Image and Push to ECR
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices/jenkins/

AWS_ACCOUNT_ID=$(aws ecr describe-repositories --region us-east-1 --query 'repositories[?repositoryName == `jenkins`].registryId' --output text)

docker build -t myjenkins .

docker tag myjenkins $AWS_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/jenkins

aws ecr get-login --region us-east-1 | sh

docker push $AWS_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/jenkins

==================================================================================================================
1.8 Build the Jenkins Docker Image and Push to ECR
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices/jenkins/deploy

eb deploy

++++2. Task: Setup the Microservices delivery pipelines ++++

==================================================================================================================
2.1 Create the Portal Microservice Pipeline
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices

aws cloudformation create-stack --stack-name PortalPipelineStack --template-body file://microservice-pipeline.template \
  --capabilities CAPABILITY_IAM --parameters ParameterKey=MicroserviceName,ParameterValue=Portal \
  ParameterKey=BaseStackName,ParameterValue=$(aws cloudformation describe-stacks --output text --query 'Stacks[?starts_with(Description, `Lab 3 Base CloudFormation template`) == `true`].StackName')


==================================================================================================================
2.3 Create the Stocks Microservice Pipeline
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices

aws cloudformation create-stack --stack-name StocksPipelineStack --template-body file://microservice-pipeline.template \
  --capabilities CAPABILITY_IAM --parameters ParameterKey=MicroserviceName,ParameterValue=Stocks \
  ParameterKey=BaseStackName,ParameterValue=$(aws cloudformation describe-stacks --output text --query 'Stacks[?starts_with(Description, `Lab 3 Base CloudFormation template`) == `true`].StackName')

==================================================================================================================
2.4 Create the Weather Microservice Pipeline
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices

aws cloudformation create-stack --stack-name WeatherPipelineStack --template-body file://microservice-pipeline.template \
  --capabilities CAPABILITY_IAM --parameters ParameterKey=MicroserviceName,ParameterValue=Weather \
  ParameterKey=BaseStackName,ParameterValue=$(aws cloudformation describe-stacks --output text --query 'Stacks[?starts_with(Description, `Lab 3 Base CloudFormation template`) == `true`].StackName')


++++3. Task: Deploy the Weather Microservice ++++

==================================================================================================================
3.3 Deploy the Weather Microservice
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices

deploy-microservice.sh weather

++++4. Task: Deploy the Portal Microservice ++++

==================================================================================================================
4.1 Update the Portal Docker config file
==================================================================================================================

WEATHER_ENDPOINT=$(aws cloudformation describe-stacks --query 'Stacks[].Outputs[?OutputKey==`WeatherEndpoint`].OutputValue' --output text)
STOCKS_ENDPOINT=$(aws cloudformation describe-stacks --query 'Stacks[].Outputs[?OutputKey==`StocksEndpoint`].OutputValue' --output text)

cd ~/lab-3-continuous-deployment/microservices/portal
sed -i 's/{WEATHER_ENDPOINT}/'${WEATHER_ENDPOINT}'/g' Dockerrun.aws.json
sed -i 's/{STOCKS_ENDPOINT}/'${STOCKS_ENDPOINT}'/g' Dockerrun.aws.json

==================================================================================================================
4.4 Deploy the Portal Microservice
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices

deploy-microservice.sh portal

++++5. Task: Deploy the Stocks Microservice ++++

==================================================================================================================
5.1 Deploy the Stocks Microservice
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices

deploy-microservice.sh stocks

++++6. Task: Test the entire solution ++++

==================================================================================================================
6.1 Test the Weather Microservice
==================================================================================================================

WEATHER_ENDPOINT=$(aws cloudformation describe-stacks --query 'Stacks[].Outputs[?OutputKey==`WeatherEndpoint`].OutputValue' --output text)

curl -u admin:password ${WEATHER_ENDPOINT}/weather/Seattle | jq '.'

==================================================================================================================
6.2 Test the Stocks Microservice
==================================================================================================================

STOCKS_ENDPOINT=$(aws cloudformation describe-stacks --query 'Stacks[].Outputs[?OutputKey==`StocksEndpoint`].OutputValue' --output text)

curl -u admin:password ${STOCKS_ENDPOINT}/stocks/AMZN | jq '.'

==================================================================================================================
6.3 Test the Portal Microservice
==================================================================================================================

aws cloudformation describe-stacks --query 'Stacks[].Outputs[?OutputKey==`PortalURL`].OutputValue' --output text

++++7. Task: Update Microservices config and push new version ++++

==================================================================================================================
7.3 Deploy the Weather and Portal microservices
==================================================================================================================

cd ~/lab-3-continuous-deployment/microservices

deploy-microservice.sh weather

deploy-microservice.sh portal

++++8. Task: Add Test step to the microservice pipeline ++++

==================================================================================================================
8.3 Get the Weather microservice endpoint
==================================================================================================================

aws cloudformation describe-stacks --query 'Stacks[].Outputs[?OutputKey==`WeatherEndpoint`].OutputValue' --output text

==================================================================================================================
8.4 Get the Jenkins Provider name
==================================================================================================================

aws cloudformation describe-stacks --query 'Stacks[].Outputs[?OutputKey==`JenkinsProviderName`].OutputValue' --output text

==================================================================================================================
8.5 Create the Weather microservice test command for Jenkins job
==================================================================================================================

-----------------------------------------------------------------------------
#!/bin/bash

# Test out the Weather microservice end point
result=$(curl -s -u admin:password http://${WEATHER_ENDPOINT}/weather/London,uk)
printf "Result from query is:\n$result\n"

# Test returned city name is "London"
WEATHER_CITY=$(echo $result | jq -r '.city')
if [ "$WEATHER_CITY" != "London" ]; then
  echo "Result not what was expected"; exit 1
fi

# Test returned symbol is "AMZN"
WEATHER_COUNTRY=$(echo $result | jq -r '.country')
if [ "$WEATHER_COUNTRY" != "GB" ]; then
  echo "Result not what was expected"; exit 1
fi

# Test returned price is a real floating number
WEATHER_TEMP=$(echo $result | jq -r '.temp')
if ! [[ $WEATHER_TEMP =~ ^[+-]?[0-9]+\.?[0-9]*$ ]]; then
  echo "Result not what was expected"; exit 1
fi
-----------------------------------------------------------------------------

==================================================================================================================
8.12 Get the Stocks microservice endpoint
==================================================================================================================

aws cloudformation describe-stacks --query 'Stacks[].Outputs[?OutputKey==`StocksEndpoint`].OutputValue' --output text

==================================================================================================================
8.13 Create the Stocks microservice test command for Jenkins job
==================================================================================================================

-----------------------------------------------------------------------------
#!/bin/bash

# Test out the Stocks microservice end point
result=$(curl -s -u admin:password http://${STOCKS_ENDPOINT}/stocks/AMZN)
printf "Result from query is:\n$result\n"

# Test returned name is "Amazon.com, Inc."
STOCK_NAME=$(echo $result | jq -r '.name')
if [ "$STOCK_NAME" != "Amazon.com, Inc." ]; then
  echo "Result not what was expected"; exit 1
fi

# Test returned symbol is "AMZN"
STOCK_SYMBOL=$(echo $result | jq -r '.symbol')
if [ "$STOCK_SYMBOL" != "AMZN" ]; then
  echo "Result not what was expected"; exit 1
fi

# Test returned price is a real floating number
STOCK_PRICE=$(echo $result | jq -r '.price')
if ! [[ $STOCK_PRICE =~ ^[+-]?[0-9]+\.?[0-9]*$ ]]; then
  echo "Result not what was expected"; exit 1
fi
-----------------------------------------------------------------------------

짤 2016 Amazon Web Services, Inc. or its affiliates. All rights reserved.
```

## 컨테이너 기반 마이크로 서비스를 위한 서비스 검색
- consul
- weave

### 실습
```
ECS Microservices: Lab 4 - Extending ECS Microservices (Linux)

==================================================================================================================

Using this command reference.

==================================================================================================================


1. Locate the section you need. Each section in this file matches a section in the lab instructions.

2. Replace items in angle brackets - < > - with appropriate values. For example, in this command you would replace the value - <KEYPAIR> - (including the angle brackets) with the Keypair value of the lab:

ecs-cli up --keypair <KEYPAIR>. You can also use find and replace to change bracketed parameters in bulk.

3. Do NOT enable the Word Wrap feature in Windows Notepad or the text editor you use to view this file.


++++1. Task: Create the Consul ECS Clusters and Deploy Consul Server and Agent tasks ++++

==================================================================================================================
1.5 Create the Consul Server
==================================================================================================================

cd ~/lab-4-service-discovery/consul/agents/ecs-consul-server

./setup-consul-server.sh

ecs-cli compose --project-name consul-server service up

==================================================================================================================
1.6 Create the ECS cluster and deploy Consul agent
==================================================================================================================

cd ~/lab-4-service-discovery/consul/agents/ecs-consul-agent

./setup-consul-agent.sh

ecs-cli compose --project-name consul-agent service up

ecs-cli compose --project-name consul-agent service scale 2

==================================================================================================================
1.7 Get the Consul Server Public IP address
==================================================================================================================

aws ec2 describe-instances --instance-ids $(aws ecs describe-container-instances --container-instances $(aws ecs list-container-instances --cluster consul-server --query 'containerInstanceArns[]' --output text) --cluster consul-server --query 'containerInstances[].ec2InstanceId' --output text) --query 'Reservations[].Instances[].PublicIpAddress' --output text

++++2. Task: Build and Deploy the Weather microservice ++++

==================================================================================================================
2.1 Build and Deploy the Weather microservice
==================================================================================================================

cd ~/lab-4-service-discovery/consul/microservices

./build_and_publish_microservice.sh weather

cd ~/lab-4-service-discovery/consul/microservices/weather

sed -i 's/{API_KEY}/'<YOUR_API_KEY>'/g' docker-compose.yml

ecs-cli compose --project-name consul-weather service up

++++3. Task: Build and Deploy the Stocks microservice ++++

==================================================================================================================
3.1 Build and Deploy the Stocks microservice
==================================================================================================================

cd ~/lab-4-service-discovery/consul/microservices

./build_and_publish_microservice.sh stocks

cd ~/lab-4-service-discovery/consul/microservices/stocks

ecs-cli compose --project-name consul-stocks service up

++++4. Task: Build and Deploy the Portal microservice ++++

==================================================================================================================
4.1 Build and Deploy the Portal microservice
==================================================================================================================

cd ~/lab-4-service-discovery/consul/microservices

./build_and_publish_microservice.sh portal

cd ~/lab-4-service-discovery/consul/microservices/portal

ecs-cli compose --project-name consul-portal service up

++++6. Task: Query the Consul server DNS and HTTP interfaces ++++

==================================================================================================================
6.2 Query the Consul agent DNS service
==================================================================================================================

docker run -it tutum/dnsutils dig -t SRV weather.service.consul

docker run -it tutum/dnsutils dig -t SRV stocks.service.consul

==================================================================================================================
6.3 Query the Consul agent HTTP service
==================================================================================================================

sudo yum install -y jq

docker run -it tutum/curl curl consul-agent:8500/v1/catalog/nodes | jq '.'

==================================================================================================================
6.4 Query the Consul agent for all services
==================================================================================================================

docker run -it tutum/curl curl consul-agent:8500/v1/catalog/services | jq '.'

==================================================================================================================
6.5 Query the Consul agent for weather service
==================================================================================================================

docker run -it tutum/curl curl consul-agent:8500/v1/catalog/service/weather | jq '.'

docker run -it tutum/curl curl consul-agent:8500/v1/catalog/service/stocks | jq '.'

++++7. Task: Interact with the Consul Key/Value store ++++

==================================================================================================================
7.1 Update password of Stocks service via Consul Key/Value store
==================================================================================================================

docker run -it tutum/curl curl -v -X PUT -d 'Password123' consul-agent:8500/v1/kv/stocks/config/stocks_password

==================================================================================================================
7.2 View values stored in Consul Key/Value store
==================================================================================================================

docker run -it tutum/curl curl consul-agent:8500/v1/kv/stocks/conf?recurse | jq '.'

++++8. Task: Use Weave for Service Discovery ++++

==================================================================================================================
8.1 Setup the Weave environment
==================================================================================================================

cd ~/lab-4-service-discovery/weave

./setup-weave.sh

++++9. Task: Build and Deploy the Weather microservice ++++

==================================================================================================================
9.1 Build and Deploy the Weather microservice
==================================================================================================================

cd ~/lab-4-service-discovery/weave/microservices/weather

sed -i 's/{API_KEY}/'<YOUR_API_KEY>'/g' docker-compose.yml

cd ~/lab-4-service-discovery/weave/microservices

./build_and_publish_microservice.sh weather

cd ~/lab-4-service-discovery/weave/microservices/weather

ecs-cli compose --project-name weave-weather service scale 2

++++10. Task: Build and Deploy the Stocks microservice ++++

==================================================================================================================
10.1 Build and Deploy the Stocks microservice
==================================================================================================================

cd ~/lab-4-service-discovery/weave/microservices

./build_and_publish_microservice.sh stocks

cd ~/lab-4-service-discovery/weave/microservices/stocks

ecs-cli compose --project-name weave-stocks service scale 2

++++11. Task: Build and Deploy the Portal microservice ++++

==================================================================================================================
11.1 Build and Deploy the Portal microservice
==================================================================================================================

cd ~/lab-4-service-discovery/weave/microservices

./build_and_publish_microservice.sh portal

짤 2016 Amazon Web Services, Inc. or its affiliates. All rights reserved.
```
