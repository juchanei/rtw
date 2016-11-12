# Range Library
---

## Motivation
### Range based for
- 과거

```
std::vector<int> v{0, 1, 2, 3, 4};

for(std::vector<int>::iterator it = v.begin(); it != v.end(); it.next()) {
    //
}
```

- 조금 전

```
std::vector<int> v{0, 1, 2, 3, 4};

for(auto it = v.begin(); it != v.end(); it.next()) {
    //
}
```

- 요즘

```
std::vector<int> v{0, 1, 2, 3, 4};

for(auto it : v) {
    //
}
```

### Range based algorithm?
- Range based for는 편리하다.
- for 말고 다른 것은?
    - 아직.

```
std::vector<int> v{0, 4, 2, 3, 1};

std::sort(v.begin(), v.end());
```

## Range?
- Range는 Range based Library
### 편의성

- Range based algorithm

```
// 정렬
std::vector<int> v{0, 4, 2, 3, 1};

std::sort(v.begin(), v.end());
```
```
// 정렬
std::vector<int> v{0, 4, 2, 3, 1};

ranges::sort(v);
// [0, 1, 2, 3, 4]
```

- 선언과 동시에 초기화

```
// vector v의 짝수번째 element만 얻기
std::vector<int> v{0, 1, 2, 3, 4};
std::vector<int> newv;

for (auto it = v.begin(); it != v.end(); it += 2) {
    newv.push_back(*it);
}
```
```
// vector v의  짝수번째 element만 Range로 얻기
std::vector<int> v{0, 1, 2, 3, 4};
std::vector<int> newv = v | view::stride(2);
// [0, 1, 4]
```

- 출력
```
// elements 출력
std::vector<int> v{9, 0, 0, 5, 2, 6}
for (auto it : v) {
    std::cout << *it << " ";
}
// 9 0 0 5 2 6
```
```
// elements 출력
std::vector<int> v{9, 0, 0, 5, 2, 6}
std::cout << view::all(v) << std::endl;
// [9, 0, 0, 5, 2, 6]
```

### 조합성 Pipline
- 쉬운 예

```
// 0, 1, 2, 3 이 반복되는 수열 15개 얻기
std::vector<int> v(15);
for (int i = 0; i < v.size(); ++i){
    v[i] = i % 4;
}
// 먼저 메모리 공간을 생각하고 구현을 고려, 컴퓨터적인 사고
```
```
// 0, 1, 2, 3 이 반복되는 수열 15개 얻기
std::vector<int> v;
v = view::iota(0, 3) |
    view::cycle |
    view::take(15);

// [0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2]
```

- 어려운 예

```
// vector의 요소들을 3개씩 모아서 합한 값 얻기
std::vector<int> v{0, 1, 2, 3, 4, 5, 6, 7, 8};

std::vector<int> newv;
for (auto it = v.begin(); it != v.end(); it+=3) {
    int sum = 0;
    for (auto it2 = it; it2 < it + 3; ++it2) {
        sum += *it2;
    }
    newv.push_back(sum);
}

// 3 12 21
```
```
// vector의 요소들을 3개씩 모아서 합한 값 얻기
std::vector<int> v{0, 1, 2, 3, 4, 5, 6, 7, 8};

auto quotient = [](int a, int b) { return a / 3 == b / 3; };
auto sum = [](auto range){ ranges::accumulate(range, 0); };

std::vector<int> newv = v |
                        view::group_by(quotient) |
                        view::transform(sum);
// [[0, 1, 2], [3, 4, 5], [6, 7, 8]]
// [3, 12, 21]
```

- 더 어려운 예

```
// 짝수번째 element만 sort하기
std::vector<int> v{2, 3, 9, 0, 6, 1, 7};

// ???
// 감도 안온다.
```
```
// 짝수번째 element만 sort하기
std::vector<int> v{2, 3, 9, 0, 6, 1, 7};

v = v |
    view::stride(2) |
    action::sort;

// [2, 3, 6, 0, 7, 1, 9]
```

### Lazy Evaluation

```
// 0에서 8까지 3개씩 모아서 합한 값의 vector 얻기
std::vector<int> v{0, 1, 2, 3, 4, 5, 6, 7, 8};

std::vector<int> newv;
for (auto it = v.begin(); it != v.end(); it+=3) {
    int sum = 0;
    for (auto it2 = it; it2 < it + 3; ++it2) {
        sum += *it2;
    }
    newv.push_back(sum);
}
```
```
// 0에서 ???까지 3개씩 모아서 합한 값 100 얻기

// ...

```

- Range의 Lazy Evaluation
```
// 0에서 ???까지 3개씩 모아서 합한 값 100 얻기
std::vector<int> newv = view::iota(0) |
                        view::group_by(quotient) |
                        view::transform(sum) |
                        view::take(100);

// view::iota(0)은 무한수열을 의미
// 실제로 필요한 수만 Lazy 하게 계산된다.
```
