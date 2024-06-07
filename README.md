# NIDA
![20240323_213657 (1)](https://github.com/Matrix-Username/Nida/assets/59887239/9bfb3977-b9c6-47cc-b660-96de13ef0561)

### Nida - це легка та потужна бібліотека для пентесту та реверс-інжинірингу Android додатків прямо під час виконання

Інструмент дозволяє проводити аналіз і пентест там, де класичні засоби безсилі, інструмент дозволяє отримати дані, які приховані за лаштунками, легко орієнтуватися в обфусфікованих класах, логувати виконання методів, їх аргументи, проливати світло на методи, що викликаються через рефлексію, складати карту виколення коду і гнути додаток під себе.



## NIDA API

### Додайте залежність у Gradle

1. Завантажте актуальний .jar файл у [Releases](https://github.com/Matrix-Username/Nida/releases)
2. Підключіть залежність у Gradle `implementation(files("/your/path/nida.jar"))`

### Проведіть розгортання Nida
`NidaAPI.initialize(/*Your Context */getApplicationContext());`


### Профілювання методів

````
 NidaAPI.profile(Toast.class.getDeclaredMethod("show"), new ProfileListener() {
 @Override
 public void onProfileComplete(ProfileData profileData) {
 Log.i("NidaProfiler", profileData.toString());
 }
 });
````

у цьому прикладі ми профільуємо метод show у класі Toast, після виконання отримуємо ProfileData який містить

- getExecutionTime()
- getUsedMemory()
- getMember()
- getCurrentThread()

### Відключення методів

При використанні цього методу цiль перестає виконувати логіку

`NidaAPI.disable(Toast.class.getDeclaredMethod("show"));`

### Трасування методів

`NidaAPI.trace(Toast.class.getDeclaredMethod("show"));`

для перегляду трасувальних даних необхідно активувати UI панель (Активація панелі та що входить до трасувальних даних див. нижче)

### Активація панелі UI


Application клас
`NidaMain.startMyEngineFromApplication(this);`

або

Activity клас
`NidaMain.startMyEngine(this);`

***

## NEIP

При створенні Nida була задіяна концепція NEIP (Nida Easy Inject Policy), завдяки ній процес інжектування вийшов неймовірно простий, а бібліотека легкою, менше 1МБ. Концепція повністю забороняє використання сторонніх бібліотек при розробці, щоб уникнути збільшення розміру та конфліктів класів при інжекції у додаток. Суть легкого інжектування зводиться до того, що всі файли підтримки бібліотеки упаковані в один файл .dex, зображення, шрифти, .so файли нативної підтримки, все це було упаковано в один файл, навіть дизайн панелі був повністю побудований на Java.

![Untitled 21_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/9654289e-f503-4590-82d0-d21a4743e4b5)


## Три способи інжектування



### Пряме інжектування
1. Додавання dex файлу Nida у програму
![Untitled 12_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/d19ff506-b7cd-41f6-93b6-08ac35839182)
2. Виклик стартового методу Nida у Smali
![Untitled 13_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/bae1f99b-dff6-43a5-a09b-fcf0d38f8e9a)

Для Activity класу
`invoke-static {p0}, Lnida/mmp/main/NidaMain;->startMyEngine(Landroid/app/Activity;)V`

Або якщо хочете викликати з Application
`invoke-static {p0}, Lnida/mmp/main/NidaMain;->startMyEngineFromApplication(Landroid/app/Application;)V`

### Nida Launcher
Дозволяє запускати потрібну програму у віртуальному контейнері, встановлюючи міст із сервісом Nidа. Не вимагає прямого втручання у додаток, зберігаючи його цілісність.
![Screenshot_2024-03-29-11-06-30-676_nida launcher](https://github.com/Matrix-Username/Nida/assets/59887239/0710d6be-3ef4-4740-9777-fadde58a0b0e)


![Untitled 11_page-0001 (1)](https://github.com/Matrix-Username/Nida/assets/59887239/99a0f6be-fce4-487e-8072-12e2e246867d)


### Патч odex файлу (Вимагає Root доступу)
Можна використовувати різні патчери, наприклад, Odex Patcher




# При запуску

При запуску програми з'являється плаваюче вікно, яке супроводжуватиме вас у кожній Activity
![Untitled 2_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/940460c0-a3e8-4fdd-bf94-4956850c3370)

Ось як виглядає головне меню Nida:

![Untitled 3_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/861ba80f-930a-436e-9f2e-ee0412868ee9)

# Classes inspector

![Untitled 4_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/4bd9dbb9-bbc2-42a0-8431-b2ef555b816b)

Тут ви побачите всі класи, які присутні в додатку. У списку є системні класи Android SDK & JDK.
Nida уважно стежить за динамічним підвантаженням коду з dex, тому автоматично додає класи, яких немає у додатку до списку. Не важливо, якими класами ви хочете маніпулювати - системними або звичайними, всі вони рівні перед Nida. Особливу увагу було приділено UI частини Nida та швидкому орієнтуванню за класами. Для багатозадачності була зроблена система вкладок: в одній вкладці переглядаємо члени класу, в іншій трасувальні дані - все просто.


***

При відкритті класу ми бачимо всі його нутрощі: поля, конструктори, методи.

![Untitled 5_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/fef83429-7516-47d6-ad03-a4ccf92075a8)

Огляд виконаний у стилі IDE від JetBrains

# Взаємодія

Розглянемо по порядку взаємодію з членами класу

## Поля
Nida автоматично підвантажуватиме статичні поля

При натисканні на полі можна вибрати пункт Set та динамічно задати його значення

![Untitled 6_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/8fa0e3aa-d3f7-4959-a5c7-e25031f42dab)

Якщо поле має примітивний тип, можна вписати дані вручну, щоб Nida їх спарсила. Якщо поле залежить від певного об'єкта, ми можемо вибрати його з будь-якого класу привівши ініціалізацію.

![Untitled 9_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/1809606b-6dc8-4bbb-9be0-37117b243b5a)

## Методи

При натисканні методу можна вибрати відразу кілька пунктів


![Untitled 14_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/5f7128ef-f2ed-4775-a197-a11df3cad511)

### Trace method

Логується кожен виклик методу, в дані входять:
  - Назва методу
  - Час виконання
  - Аргументи, які отримав метод
  - Результат виконання методу
  - Повний StackTrace методу

У додаткових опціях трасування можна вибрати збереження аргументів або результату виконання методу в пам'ять Nida. Збережені об'єкти будуть доступні для повторного використання та взаємодії.


![Untitled 8_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/115b1309-6118-478d-95be-4a60fb763143)

У списку StackTrace можна перейти на будь-який клас і метод, звідки здійснювався виклик.

### Inject

Доступно три опції інжектування:

  - Before method call (Виконує виклик методу, що інжектується, до виклику цiлi)
  - After method call (Виконує виклик методу, що інжектується після виклику цiлi)
  - Replace method call (Заміна значення методу, що повертається, доступна мапіпуляція аргументами методу)

Розглянемо всі варіанти:

1. Можна виконати наш Inject метод усередині цільового. Наприклад, підключивши свій dex file, вибрати клас, метод, провівши ініціалізацію.

![Untitled 17_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/c765372b-9e8f-45f0-8751-81979d53b783)

***

2. Заміна об'єкта, що повертається цільовим методом.


![Untitled 16_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/4f206b01-c9c9-481e-a668-ce379c5eef10)

3. Можна спарсити примітивний об'єкт, або вибрати вручну з іншого класу. Якщо потрібно, можна перехопити і змінити аргументи, що передаються методу.

### Invoke method

![Untitled 15_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/8b01c131-5111-48a2-87a6-11f1bb284223)

4. Можна викликати метод N кількість разів, є можливість збереження повернутого значення в пам'ять Nida.


***

# Add executables

У цьому розділі ви можете додавати свої dex файли, Nida автоматично проіндексує їх та включить до складу програми.

![Untitled 18_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/cd2b77d6-1dcd-42ef-b7ae-a9180a404fe8)



***
# Reflection usage

Показує до яких методів і звідки додаток звертався через рефлексію

![Untitled 19_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/642954b0-1112-47cf-a6b6-e79571e12505)

***

# View editor

Повноцінне редагування розмітки активності.
Після натискання на будь-який елемент екрана відкривається панель з інформацією про елемент, у списку є атрибути, які можна динамічно змінювати.

![Untitled 20_page-0001](https://github.com/Matrix-Username/Nida/assets/59887239/2c53ab15-bf28-469e-9a8f-d4799e6382f0)

# Подальші плани на проект:

  - Підтримка трасування .so файлів
  - Режим де-обфускації імен класів
  - Розширення функцій View editor
  - Трасування мережі, емулювання вхідних та вихідних запитів
  - Динамічне редагування smali з використанням кодогенерації
