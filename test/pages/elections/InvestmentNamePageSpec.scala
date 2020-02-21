package pages.elections

import pages.behaviours.PageBehaviours

class InvestmentNamePageSpec extends PageBehaviours {

  "InvestmentNamePage" must {

    beRetrievable[String](InvestmentNamePage)

    beSettable[String](InvestmentNamePage)

    beRemovable[String](InvestmentNamePage)
  }
}
