# Система тестирования
Приложение для создания и прохождения тестов. В приложении реализовано:
- Создание и редактирование учетной записи с добавлением картинки для аватара
- Создание, редактирование и прохождение тестов
- Вопросы теста могут быть двух форм: radio и checkbox
- В radio вопросе вариант ответа может быть только один, а в checkbox вопросах имеется возможность выбора нескольких вариантов ответа
- Вычисление баллов по прохождению тестов
- Хранение и просмотр результатов прохождения тестов

### Выбранные технологии и библиотеки
<img src="assets/Firebase_Logo.png" style="float: right;" width="350">

1. Для серверной части приложения был использован сервис облачных услуг Firebase, а конкретно: 
	- Firebase Authorization для авторизации
	- Firebase Realtime database для хранение данных
	- Firebase Storage для хранения изображений
	- Библиотека Fresco для загрузки изображений
2. Для навигации между фрагментами используется технология Navigation component
3. Для взаимодействия между UI и данными используется архитектура MVVM

### Граф фрагментов приложения
![Fragments graph](assets/graph.png "Fragments graph")

### Диаграмма классов моделей
![Model classes](assets/model_classes.png "Model classes")

### Видео, демонстрирующее создание и прохождение теста
<img src="assets/testing_system_video.gif" width="280">
