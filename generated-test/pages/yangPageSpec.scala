package pages

import pages.behaviours.PageBehaviours

class yangPageSpec extends PageBehaviours {

  "yangPage" must {

    beRetrievable[Boolean](yangPage)

    beSettable[Boolean](yangPage)

    beRemovable[Boolean](yangPage)
  }
}
