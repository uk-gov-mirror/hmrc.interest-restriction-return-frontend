package models.returnModels

import play.api.libs.json.Json

case class IdentityOfCompanySubmittingModel(companyName: CompanyNameModel,
                                            ctutr: Option[UTRModel],
                                            crn: Option[CRNModel],
                                            countryOfIncorporation: Option[CountryCodeModel],
                                            nonUkCrn: Option[String])

object IdentityOfCompanySubmittingModel {

  implicit val writes = Json.writes[IdentityOfCompanySubmittingModel]
}