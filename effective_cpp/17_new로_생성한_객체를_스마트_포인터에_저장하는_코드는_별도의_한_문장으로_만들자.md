# 17 : new로 생성한 객체를 스마트 포인터에 저장하는 코드는 별도의 한 문장으로 만들자

---
## 문제인식
함수의 파라미터로 넘어오는 코드를 호출하는 순서는 컴파일러마다 다릅니다.
따라서 아래와 같은 상황에서 자원유출의 가능성이 있습니다.<br>

아래와 같은 함수가 있다고 합시다.

```
int calcPriority();
void processWidget(std::tr1::shared_ptr<Widget> pWidget, int priority);
```

다음과 같이 이용할 수 있습니다.

```
processWidget(std::tr1::shared_ptr<Widget>(new Widget), calcPriority());
```

파라미터로 넘오오는 코드의 호출순서가 어떻게 될까요?

- new Widget
- shared_ptr<Widget>()
- calcPriority()

이러길 바랐겠지만.

- new Widget
- calcPriority()
- shared_ptr<Widget>()

이럴지도 모릅니다.
만일 calcPriority()에서 예외가 발생한다면, 아직 shared_ptr에 담기지 못한 Widget의 객체는 해제되지 못하고 유출되고 맙니다.

## 문제해결
스마트 포인터를 파라미터로 받는 함수가 있는 경우, 코드를 별도의 한 문장으로 분리할 필요가 있습니다.


```
std::tr1::shared_ptr<Widget> widget(new Widget);
processWidget(widget, calcPriority());
```

별도의 문장으로 분리되었으므로 어떤 컴파일러를 사용해도 원하는 호출순서를 다음과 같이 보장할 수 있습니다.

- new Widget
- shared_ptr<Widget>()
- calcPriority()

---
## 정리
- new로 생성한 객체를 스마트 포인터로 넣는 코드는 별도의 한문장으로 만들어야 자원 누출을 막을 수 있습니다.
