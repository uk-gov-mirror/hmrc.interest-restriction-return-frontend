package models.returnModels

import play.api.libs.json.Json

case class AgentDetailsModel(agentActingOnBehalfOfCompany: Boolean,
                             agentName: Option[String])

object AgentDetailsModel {

  implicit val writes = Json.writes[AgentDetailsModel]

}
