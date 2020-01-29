package models.returnModels.fullReturn

import models.returnModels._
import play.api.libs.json.Json

case class UkCompanyModel(companyName: CompanyNameModel,
                          utr: UTRModel,
                          consenting: Boolean,
                          netTaxInterestExpense: BigDecimal,
                          netTaxInterestIncome: BigDecimal,
                          taxEBITDA: BigDecimal,
                          allocatedRestrictions: Option[AllocatedRestrictionsModel],
                          allocatedReactivations: Option[AllocatedReactivationsModel]
                         )

object UkCompanyModel {

  implicit val writes = Json.writes[UkCompanyModel]

}
