package models.returnModels.abbreviatedReturnModel

import models.returnModels._
import play.api.libs.json.Json

case class AbbreviatedReturnModel(agentDetails: AgentDetailsModel,
                                  reportingCompany: ReportingCompanyModel,
                                  parentCompany: Option[ParentCompanyModel],
                                  publicInfrastructure: Boolean,
                                  groupCompanyDetails: GroupCompanyDetailsModel,
                                  submissionType: SubmissionType,
                                  revisedReturnDetails: Option[String],
                                  groupLevelElections: Option[GroupLevelElectionsModel],
                                  ukCompanies: Seq[UkCompanyModel])

object AbbreviatedReturnModel{

  implicit val writes = Json.writes[AbbreviatedReturnModel]
}