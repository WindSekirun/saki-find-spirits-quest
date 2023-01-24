# saki-find-spirits-quest

사신키우기 온라인 내 '정령 탐험' 에서 내가 가진 정령 점수로 최대한 높은 점수를 계산하는 간단한 프로그램

현재 최고 등급: UUU (7000), 23.01.17 업데이트 https://cafe.naver.com/rookieonline/413396

## 사용방법

파일 다운로드: 

```json
{
  "goalCount": 10,
  "goalMinValue": 20500,
  "pickMinValue": 6800,
  "spiritsList": {
    "common": [200, 300, 350, 400],
    "advanced": [450, 500, 550, 600],
    "hero": [650, 700, 750, 800],
    "legend": [850, 900, 950, 1000],
    "god": [1850, 1900, 1950, 2000],
    "immortal": [1350, 1400, 1350, 1400]
  }
}
```

위 파일을 data.json 등으로 저장하고, 아래 명령어 실행하면 자동으로 계산 실행
```
java -jar saki-find-spirits-quest-1.0.jar data.json
```

결과값은 아래와 같이 표시
```
Found Best value (6.0s)
Spirits Total: 23150
Total: 20700
#1: 6950 - [1950, 1850, 950, 900, 850, 450]
#2: 6950 - [1900, 1350, 1350, 1000, 750, 600]
#3: 6800 - [2000, 1400, 1400, 800, 650, 550]
```

# 변수 구성

* goalCount, goalMinValue
  * 무기/정령/보석 에 대해 최대한 높은 값으로 선택하려고 할 때, 목표로 할 값
  * goalCount: 최종으로 선택할 후보군 갯수 (높을수록 다양한 선택지를 선택)
  * goalMinValue: 후보군의 최소 점수. 무기/정령/보석을 U/UUU/UUU 로 맞추고 싶다면 20000 (UUU=7000, U=6000)
* pickMinValue
  * 탐험 점수에 따라 정령을 선택할 때, 목표로 할 최소 정령값.
  * 만일 최소 U등급을 목표로 한다면 6000
* spiritsList
  * 자신의 정령 점수를 표기
  * 탐험 > 정령 설정의 점수를 일반(common), 고급(advanced), 영웅(hero), 전설(legend), 신(god), 불멸(immortal) 에 4등급 ~ 1등급 순으로 기재
  * 예시로, 신4~신1 전부 9999일 경우 1850, 1900, 1950, 2000