# NewsAgregatorBot
## перед началом работы нужно создать папку resources в src/main/ и создать файлы и добавиьт в них записи: 
  1) _application.properties_
    spring.datasource.url = jdbc:postgresql://localhost:5432/aggregator
  spring.datasource.username = ваше имя пользователя
  spring.datasource.password = пароль от базы
  в моем случае БД называется aggregator
  3) _sources.properties_
     список ресурсов(rss ленты, к которым обращается приложение для получения новостей) Добавил это еще в БД, поэтому можно запрашивать их из бд:
            cnbc.economy = https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=20910258
            cnbc.finance = https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=10000664
            cnbc.technology = https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=19854910
            cnbc.politics = https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=10000113
            cnbc.energy = https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=19836768
            cnbc.business = https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=44877279
            cnbc.earnings = https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=15839135
            reuters.businessAndFinance = https://www.reutersagency.com/feed/?best-topics=business-finance&post_type=best
            reuters.deals = https://www.reutersagency.com/feed/?best-topics=deals&post_type=best
            reuters.politics = https://www.reutersagency.com/feed/?best-topics=political-general&post_type=best
            reuters.tech = https://www.reutersagency.com/feed/?best-topics=tech&post_type=best
            reuters.foreignExchangeAndFixedIncome = https://www.reutersagency.com/feed/?best-sectors=foreign-exchange-fixed-income&post_type=best
            reuters.economy = https://www.reutersagency.com/feed/?best-sectors=economy&post_type=best
            reuters.commoditiesAndEnergy = https://www.reutersagency.com/feed/?best-sectors=commodities-energy&post_type=best
  5) _values.properties_
        bot.token=ваш токен бота
        bot.name=название вашего бота
