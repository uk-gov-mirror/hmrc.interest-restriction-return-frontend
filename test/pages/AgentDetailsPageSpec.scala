package pages

import pages.behaviours.PageBehaviours

class AgentDetailsPageSpec extends PageBehaviours {

  "AgentDetailsPage" must {

    beRetrievable[Boolean](AgentDetailsPage)

    beSettable[Boolean](AgentDetailsPage)

    beRemovable[Boolean](AgentDetailsPage)
  }
}
