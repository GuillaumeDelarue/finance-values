package finance

import finance.service.{DateProvider, FinanceService, SystemDateProvider, YahooFinanceService}

case class Pricer(financeService: FinanceService, dateProvider: DateProvider) {

  def dailyPrices(ticker: String): Seq[Double] = financeService.downloadOneYearWorthOfPrices(dateProvider.now, ticker).map(_._2)

  def returns(ticker: String): Seq[Double] = dailyPrices(ticker)
    .sliding(2)
    .map(twoDays => (twoDays.head - twoDays(1)) / twoDays(1))
    .toSeq

  def meanReturn(ticker: String): Double = {
    val oneYearReturns = returns(ticker)
    oneYearReturns.foldLeft(0.0)(_ + _) / oneYearReturns.size
  }
}

object Pricer {
  def apply(): Pricer = Pricer(YahooFinanceService("Close"), SystemDateProvider)
}