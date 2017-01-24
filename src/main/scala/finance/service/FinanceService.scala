package finance.service

import java.time.LocalDate

trait FinanceService {
  def downloadOneYearWorthOfPrices(lastDate: java.time.LocalDate, ticker: String): Seq[(LocalDate, Double)]
}

case class YahooFinanceService(usePrice: String) extends FinanceService {

  def downloadOneYearWorthOfPrices(lastDate: java.time.LocalDate, ticker: String): Seq[(LocalDate, Double)] = {
    val data = getPricesAsCsv(lastDate, ticker).getLines()
    val index = data.next.split(",").indexOf(usePrice)

    if (index < 0) throw new IllegalArgumentException(s"This prices file does not contain a column '$usePrice'")

    data.map { line =>
      val columns = line.split(",").map(_.trim)
      LocalDate.parse(columns(0)) -> columns(index).toDouble
    }.toSeq
  }

  private def getPricesAsCsv(today: LocalDate, ticker: String) = {
    val lastYear = today.minusYears(1)
    val url = f"http://real-chart.finance.yahoo.com/table.csv?s=$ticker&a=${lastYear.getMonthValue}&b=${lastYear.getDayOfMonth}&c=${lastYear.getYear}&d=${today.getMonthValue}&e=${today.getDayOfMonth}&f=${today.getYear}&g=d&ignore=.csv"
    scala.io.Source.fromURL(url)
  }
}