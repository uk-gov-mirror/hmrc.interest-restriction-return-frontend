package pages

import pages.behaviours.PageBehaviours

class AgentActingOnBehalfOfCompanyPageSpec extends PageBehaviours {

  "AgentActingOnBehalfOfCompanyPage" must {

    beRetrievable[Boolean](AgentActingOnBehalfOfCompanyPage)

    beSettable[Boolean](AgentActingOnBehalfOfCompanyPage)

    beRemovable[Boolean](AgentActingOnBehalfOfCompanyPage)
  }
}
