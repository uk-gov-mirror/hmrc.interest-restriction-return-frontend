package models.returnModels

import play.api.libs.json.Json

case class ParentCompanyModel(ultimateParent: Option[UltimateParentModel],
                              deemedParent: Option[Seq[DeemedParentModel]])

object ParentCompanyModel {

  implicit val writes = Json.writes[ParentCompanyModel]
}