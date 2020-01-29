package models.returnModels

import play.api.libs.json.Json

case class DeemedParentModel(isUk: Boolean,
                             companyName: CompanyNameModel,
                             knownAs: Option[String],
                             ctutr: Option[UTRModel],
                             sautr: Option[UTRModel],
                             crn: Option[CRNModel],
                             countryOfIncorporation: Option[CountryCodeModel],
                             nonUkCrn: Option[String])

object DeemedParentModel {

  implicit val writes = Json.writes[DeemedParentModel]
}
