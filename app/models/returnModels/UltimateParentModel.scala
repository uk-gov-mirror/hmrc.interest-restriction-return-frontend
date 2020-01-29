package models.returnModels

import play.api.libs.json.Json

case class UltimateParentModel(isUk: Boolean,
                               companyName: CompanyNameModel,
                               ctutr: Option[UTRModel],
                               sautr: Option[UTRModel],
                               crn: Option[CRNModel],
                               knownAs: Option[String],
                               countryOfIncorporation: Option[CountryCodeModel],
                               nonUkCrn: Option[String]
                              )

object UltimateParentModel {

  implicit val writes = Json.writes[UltimateParentModel]
}