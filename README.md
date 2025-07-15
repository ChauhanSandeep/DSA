# 📘 Java 8 Syntax Cheatsheet for Interviews

A quick-reference guide to essential Java 8 (and below) syntaxes that make your code cleaner, faster to write, and more expressive during technical interviews.

---

## 📚 Collections & Maps

``` java
map.putIfAbsent(key, new ArrayList<>());
map.putIfAbsent(key, k -> new ArrayList<>()).add(value);
map.computeIfAbsent(key, k -> new ArrayList<>());
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
map.computeIfPresent(key, (k, v) -> v + 1);
map.merge(key, 1, Integer::sum);
list.sort(Comparator.naturalOrder());
list.sort(Comparator.reverseOrder());
Collections.sort(list);
Collections.reverse(list);
Collections.frequency(list, element);
```

---

## 🌀 Streams & Lambdas

``` java
list.stream().filter(x -> x > 0).collect(Collectors.toList());
list.stream().map(String::toUpperCase).collect(Collectors.toList());
list.stream().flatMap(List::stream).collect(Collectors.toList());
list.stream().distinct().collect(Collectors.toList());
list.stream().sorted().collect(Collectors.toList());
list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
list.stream().sorted(Comparator.comparing(Foo::getBar)).collect(Collectors.toList());
list.stream().anyMatch(x -> x > 5);
list.stream().allMatch(x -> x != null);
list.stream().noneMatch(String::isEmpty);
list.stream().reduce(0, Integer::sum);
list.stream().max(Integer::compareTo);
list.stream().min(Integer::compareTo);
```

---

## 📦 Collectors

``` java
Collectors.toList();
Collectors.toSet();
Collectors.toMap(k -> k, v -> v);
Collectors.toMap(Item::getKey, Item::getValue, Integer::sum);
Collectors.groupingBy(Foo::getType);
Collectors.joining(", ");
```

---

## 🔐 Optional

``` java
Optional.ofNullable(value).orElse(defaultValue);
Optional.ofNullable(value).ifPresent(val -> doSomething(val));
Optional.ofNullable(value).map(String::trim).orElse("default");
```

---

## 🔧 Functional Interfaces

``` java
Predicate<String> isEmpty = String::isEmpty;
Function<String, Integer> length = String::length;
Consumer<String> printer = System.out::println;
```

---

## 🔁 ForEach & Method References

``` java
list.forEach(System.out::println);
map.forEach((k, v) -> System.out.println(k + ": " + v));
```

---

## 🧮 Arrays & List Helpers

``` java
Arrays.asList(1, 2, 3);
new HashSet<>(list);
Arrays.sort(arr);
Arrays.stream(arr).sum();
```

---

## ✨ Tips

- Prefer `Streams` for readability, but use loops when performance or clarity demands it.
- Use `Optional` to write null-safe code.
- `computeIfAbsent`, `merge`, and `Collectors.groupingBy()` are **frequent interview favorites**.

---

Made with ☕ and 💻 by Sandeep