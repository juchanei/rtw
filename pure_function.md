# Algorithm Test와 Pure Function
---
## 함수의 실행 결과
- 아래 함수를 실행 한 결과는 무엇일까?

```
double pi = 3.14;

double area = getCircleArea(3);
```
- 아래의 경우에는 어떤 결과가 나올까?

```
double pi = 3.141592;

double area = getCircleArea(3);
```
- 동일한 함수임에도 불구하고 결과 값은 pi값에 의해 변한다.

## 함수와 외부세계
- Global Function

```
double pi = 3.14;

double getCircleArea(double radious){
    return pi * radious * radious;
}
```
- Memeber Function

```
class Circle {
private:
    double pi;
public:
    Circle(double radious) : pi(3.14), radious(radious) {}

    double getCircleArea(double radious){
        return pi * radious * radious;
    }
}
```
- Inner Function (from Scala)

```
def Circle() {
    val pi = 3.14
    def getCircleArea(radious : Double) {
        pi * radious * radious
    }
}
```
- 함수는 내부 스코프 외부세계와 상호작용 할 수 있다.
    - 즉, 외부세계에 의해 결과 값이 변할 수 있다.

## Pure한 함수
- Parameter를 제외하고는 외부세계로부터 고립된 함수

```
const int add(const int a, const int b){
    return a + b;
}
```
- 외부 세계의 어떤 값도 참조하지 않는다.
- 외부 세계에 어떤 내부 값도 공개하지 않는다.
    - 참조를 리턴하지 않고 값 만을 리턴

- 아래 코드의 실행 결과는 항상 같다.

```
const int threePlusTwo = add(3, 2);
```
- 아래 코드와 같은 역할을 한다.

```
const int threePlusTwo = 5;
```
- Pure한 함수는 상수로 모두 대치될 수 있다.
    - 외부 변화에도 로직을 견고하게 지탱한다.

## Algorithm Test에서의 Pure Function
- 일반적인 Algorithm Test의 코드 스타일

```
int n;
int m;
int k;
int ret;

void sol(){
    //...
    //...
    ret = ...
}

void main(void){
    int testcase;
    scanf("%d\n", &test);
    while(testcase--){
        scanf("%d %d %d\n", &n, &m, &k);
        //...
        sol();
        printf("%d\n", ret);
    }
}
```
- 위 코드는 초보적인 결함이 있다.
    - 전역함수를 testcase 마다 초기화 해주지 않으면 엉뚱한 결과가 나온다.
        - sol()의 로직이 외부세계에 의존적이기 때문
    - 너무도 쉽게 하는 실수.

- 조금이나마 바람직한 코드 스타일

```
const int sol(const int n, const int m, const int k){
    //...
    //...
    return ret;
}

void main(void){
    int testcase;
    scanf("%d\n", &test);
    while(testcase--){
        int n;
        int m;
        int k;
        scanf("%d %d %d\n", &n, &m, &k);
        //...
        printf("%d\n", sol(n, m, k));
    }
}
```

## Pure Function과 Memoization
위 예제의 sol()이 Pure Function 이라면
