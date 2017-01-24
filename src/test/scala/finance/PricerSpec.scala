package finance

import java.time.LocalDate

import finance.service.{DateProvider, FinanceService}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, OptionValues, WordSpecLike}

class PricerSpec extends WordSpecLike with Matchers with OptionValues with MockitoSugar {
  private val financeService = mock[FinanceService]
  private val dateProvider = mock[DateProvider]
  private val pricer = Pricer(financeService, dateProvider)
  private val ticker = "GOOG"
  private val now = LocalDate.now
  private val pricesFromService = Seq(
    (now, 1.0),
    (now.minusDays(1), 2.0),
    (now.minusDays(2), 3.0))
  private val return1 = (1.0 - 2.0) / 2.0
  private val return2 = (2.0 - 3.0) / 3.0

  reset(dateProvider, financeService)
  when(dateProvider.now).thenReturn(now)
  when(financeService.downloadOneYearWorthOfPrices(now, ticker)).thenReturn(pricesFromService)

  "Pricer" should {

    /* 1 - 1 year historic prices given a ticker */
    "produce one-year historic prices from today ordered by day" in {
      pricer.dailyPrices(ticker) shouldBe List(1.0, 2.0, 3.0)
    }

    /* 2- daily returns, where return = ( Price_Today – Price_Yesterday)/Price_Yesterday */
    "produce daily returns" in {
      pricer.returns(ticker) shouldBe List(return1, return2)
    }

    /* 3 – 1 year mean returns */
    "produce yearly mean returns" in {
      pricer.meanReturn(ticker) shouldBe (return1 + return2) / 2
    }
  }
}
