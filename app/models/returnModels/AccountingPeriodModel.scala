package models.returnModels

import java.time.LocalDate

import play.api.libs.json.{Json, JsonValidationError, Reads, __}

import scala.util.Try

case class AccountingPeriodModel(startDate: LocalDate, endDate: LocalDate)

object AccountingPeriodModel {

  private def readDate(field: String): Reads[LocalDate] = (__ \ field).read[String]
    .filter(JsonValidationError("Date must be in ISO Date format YYYY-MM-DD"))(_.matches(raw"(\d{4})-(\d{2})-(\d{2})"))
    .filter(JsonValidationError("Date must be a valid date"))(str => Try(LocalDate.parse(str)).isSuccess)
    .map(LocalDate.parse)

  implicit val writes = Json.writes[AccountingPeriodModel]
}