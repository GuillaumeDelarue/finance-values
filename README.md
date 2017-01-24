Finance Values Exercise
=======================

- Everything was done using TDD with ScalaTest and Mockito: Integration tests + tests
- No acceptance test has been written, as they should be written with the business or BA (using Cucumber, etc.)
- YahooFinanceService: I wrapped the provided URL in a simple Source.fromUrl. This has no timeout, on a production environment I would probably use some more advanced library - scalatra, Play, etc? Depending on the choice of the team / architects
- I've used close prices, but this is configurable: currently by changing the hardcoded "Close" value passed to the service
- I've used the arithmetic mean in the 3rd function, there was no details on which type of average to use
- There is a DateProvider (with a SystemDateProvider simple implementation) to allow mocking of the service call using a real value returned by the mocked date provider
- There is one assumption: that the prices returned by the finance service are sorted by date

- Main class: Pricer
- Test: PricerSpec
- Integration tests: YahooFinanceServiceIntegrationSpec


Possible evolutions
===================

- Add acceptance test using Cucumber or similar (This should have been done using BDD)
- Use a more advanced HTTP library
- Make the type of price truly configurable (in application.conf or similar)
- Make it reactive: handle futures from the HTTP layer, maybe use concurrency to compute values faster, etc.
- Make sure the prices are correctly ordere by date, or sort them to be sure
