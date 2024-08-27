<img src="http://javaops.ru/static/img/logo/javaops_30.png" width="223"/>

<h2>Курс: Микросервисы, Docker, Kafka<br>
Spring Cloud, реактивный стек (<a href="https://javaops.ru/view/cloudjava">CloudJava</a>)</h2>

-----------------------------------------------------------------
<h3>Menu Aggregate Service</h3>
<h4>Предоставляет REST API для получения агрегированных данных о блюдах, отзывах к ним и их рейтингах.<br>
Доступно всем пользователям.</h4>
<ul class="mt-2">
    <li class="mt-2"><i>GET /v1/menu-aggregate/{menuId}?sort={sort}&from={from}&size={size}</i> - получить информацию о блюде с отзывами. 
Список отзывов пагинирован и отсортирован по дате создания (DATE_ASC, DATE_DESC)</li>
    <li class="mt-2"><i>GET /v1/menu-aggregate?category={category}&sort={sort}</i> - получить информацию о блюдах из указанной категории. 
Блюда отсортированы или по алфавиту(AZ, ZA) или по цене (PRICE_ASC, PRICE_DESC) или по дате создания (DATE_ASC, DATE_DESC) или по рейтингу (RATE_ASC, RATE_DESC). Информация по каждому блюду включает в себя рейтинг блюда, полученный из Review Service</li>
</ul>
<br>
Данные запрашиваются из других сервисов, своей базы у сервиса данных нет. 

