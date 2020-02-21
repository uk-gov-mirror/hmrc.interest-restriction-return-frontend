package models.ukCompanies

import models.returnModels.fullReturn.{AllocatedReactivationsModel, AllocatedRestrictionsModel}
import models.returnModels.{CompanyNameModel, UTRModel}
import play.api.libs.json.Json

case class UkCompanyModel(companyName: CompanyNameModel,
                          ctutr: UTRModel,
                          consenting: Option[Boolean],
                          netTaxInterestExpense: Option[BigDecimal],
                          netTaxInterestIncome: Option[BigDecimal],
                          taxEBITDA: Option[BigDecimal],
                          allocatedRestrictions: Option[AllocatedRestrictionsModel],
                          allocatedReactivations: Option[AllocatedReactivationsModel]
                          )

object UkCompanyModel {

  implicit val writes = Json.writes[UkCompanyModel]
}



