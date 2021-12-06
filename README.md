# EnglishWordStudyBot
## Telegram java bot for OOP course

## Развертывание на heroku:
1. Сделать форк репозитория
2. Клонировать репозиторий к себе
3. Зарегистрироваться на heroku :) и скачать Heroku CLI :) 
4. Перейти в консоль:
    1. __heroku create <app_name>__
    2. __heroku login__
    3. __heroku git:clone -a <app_name>__
    4. __cd <app_name>__
    5. __добавить в config vars botName (имя бота), dbName (имя монго бд), extKey (ключ к бд), GRADLE_TASK=stage, token(токен для бота), uriMongoDB(uri mongo db)__
    6. __git push heroku main__ (можно указать флаг -f перед heroku, тогда будет enable automatic deploys mode для ветки __main__)
5. Отдыхаем

Contributors:
- https://github.com/iz1xxxKatka
- https://github.com/ninaprokopova
- https://github.com/PeterMotorniy
