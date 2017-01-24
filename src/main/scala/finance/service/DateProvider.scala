package finance.service

import java.time.LocalDate

trait DateProvider {
  def now: LocalDate
}

object SystemDateProvider extends DateProvider {
  override def now: LocalDate = LocalDate.now
}