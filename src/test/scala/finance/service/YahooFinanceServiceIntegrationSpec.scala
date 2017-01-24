package finance.service

import java.time.LocalDate

import org.scalatest.{Matchers, OptionValues, WordSpecLike}

class YahooFinanceServiceIntegrationSpec extends WordSpecLike with Matchers with OptionValues {
  private val lastDate = LocalDate.now
  private val ticker = "GOOG"

  "YahooFinanceService" should {

    "retrieve prices from Yahoo financial data" in {
      val service = YahooFinanceService("Close")
      val prices = service.downloadOneYearWorthOfPrices(lastDate, ticker)
      prices.size should be > 200
      prices.size should be < 366
    }

    "produce different prices when a different price type is used" in {
      val closePrices = YahooFinanceService("Close").downloadOneYearWorthOfPrices(lastDate, ticker)
      val openPrices = YahooFinanceService("Open").downloadOneYearWorthOfPrices(lastDate, ticker)

      closePrices.size shouldBe openPrices.size
      closePrices should not be openPrices
    }

    "throw an exception when price type is invalid" in {
      val invalidPriceType = "Invalid"
      val service = YahooFinanceService(invalidPriceType)
      val expectedException = intercept[IllegalArgumentException](service.downloadOneYearWorthOfPrices(lastDate, ticker))
      expectedException.getMessage shouldBe s"This prices file does not contain a column '$invalidPriceType'"
    }
  }
}
