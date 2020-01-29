package models.returnModels.abbreviatedReturnModel

import models.returnModels._
import play.api.libs.json.Json

case class UkCompanyModel(companyName: CompanyNameModel,
                          ctutr: UTRModel,
                          consenting: Boolean)

object UkCompanyModel {
  implicit val writes = Json.writes[UkCompanyModel]
}