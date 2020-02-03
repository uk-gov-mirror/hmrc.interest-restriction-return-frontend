package pages

import pages.behaviours.PageBehaviours

class mikeyPageSpec extends PageBehaviours {

  "mikeyPage" must {

    beRetrievable[Boolean](mikeyPage)

    beSettable[Boolean](mikeyPage)

    beRemovable[Boolean](mikeyPage)
  }
}
